# Расширенная настройка

Простые значения — это хорошо, но если их становится слишком много, всё может быстро превратиться в кашу. В этом руководстве мы рассмотрим, как структурировать конфигурацию и использовать процессоры, чтобы выжать из неё максимум.

## Использование секций

Когда файл с десятками значений становится плоским и громоздким, навигация по нему становится трудной. К счастью, с Quilt Config мы можем организовать его в секции! Это очень просто реализовать.

С помощью секций можно использовать отступы для визуального разграничения частей конфигурационного файла, что удобно для читающих его пользователей. Вот пример секции в формате TOML:

```toml
# ...

# На самом деле это не используется модом, но у меня закончились идеи, что добавить.
typesOfSoup = ["tomato", "borscht", "chicken noodle", "ramen", "STEW", "mushroom"]

# Расширенные настройки для продвинутых пользователей.
[advanced_settings]
    # Автоматически добавлять перевод строки в конец каждого сообщения.
    # default: true
    printNewlines = true
```

Чтобы это реализовать в коде, создадим секцию:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("Расширенные настройки для продвинутых пользователей.")
    @SerializedName("advanced_settings")
    public final AdvancedSettings advancedSettings = new AdvancedSettings();

    public static class AdvancedSettings extends Section {
        @Comment("Автоматически добавлять перевод строки в конец каждого сообщения.")
        @SerializedName("print_newlines")
        public final TrackedValue<Boolean> printNewlines = this.value(true);
    }
}
```

Мы создаём вложенный класс внутри нашего конфигурационного класса, который расширяет `ReflectiveConfig.Section`. Затем создаём объект этой секции в основном классе конфигурации. Обрати внимание: экземпляр секции **не** хранится в `TrackedValue`, как остальные значения — вместо этого сама секция содержит `TrackedValue`.

Теперь в секцию можно добавлять сколько угодно значений. Но что если нам нужно сохранить что-то более сложное, чем простые типы, карты или списки? Давай посмотрим, как сериализовать собственный объект.

## Сериализация пользовательских объектов

В Java можно печатать в разные потоки вывода в консоли. Эти потоки не являются сериализуемыми объектами, как `String` или числа. Вот тут и приходит на помощь интерфейс `ConfigSerializableObject<T>`.

Этот интерфейс позволяет сериализовать и десериализовать нестандартные типы. Нужно реализовать три метода:

- `T getRepresentation()` — преобразует объект в сериализуемую форму.
- `YourSerializableClass convertFrom(T)` — восстанавливает оригинальный объект из сериализованной формы.
- `YourSerializableClass copy()` — создаёт копию объекта (используется Quilt Config'ом внутренне).

Вот пример:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public static class AdvancedSettings extends Section {
        // ...
        @Comment("Куда печатать сообщения.")
        @SerializedName("print_stream")
        public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);

        @SuppressWarnings("unused") // IDE не поймёт, что все значения enum можно использовать в конфиге
        public enum PrintStreamOption implements ConfigSerializableObject<String> {
            SYSTEM_OUT(System.out),
            SYSTEM_ERROR(System.err);

            private final PrintStream printStream;

            PrintStreamOption(PrintStream stream) {
                this.printStream = stream;
            }

            public PrintStream getStream() {
                return this.printStream;
            }

            @Override
            public PrintStreamOption convertFrom(String representation) {
                return valueOf(representation);
            }

            @Override
            public String getRepresentation() {
                return this.name();
            }

            @Override
            public PrintStreamOption copy() {
                return this;
            }
        }
    }
}
```

Мы использовали `Enum` для удобства — он автоматически подсказывает возможные значения в конфиге. В методах сериализации всё просто: `name()` и `valueOf()` делают всю работу. Если бы это был обычный класс, возможностей было бы ещё больше.

А теперь пример класса, представляющего точку в 3D-пространстве:

```java
public class Vec3i implements ConfigSerializableObject<ValueMap<Integer>> {
    public final int x;
    public final int y;
    public final int z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vec3i copy() {
        return this;
    }

    @Override
    public Vec3i convertFrom(ValueMap<Integer> representation) {
        return new Vec3i(representation.get("x"), representation.get("y"), representation.get("z"));
    }

    @Override
    public ValueMap<Integer> getRepresentation() {
        return ValueMap.builder(0)
                .put("x", this.x)
                .put("y", this.y)
                .put("z", this.z)
                .build();
    }
}
```

Здесь используется `ValueMap` для сериализации сложной структуры. Такой подход подходит для всех классов с несколькими полями.

## Использование процессоров

Теперь, когда мы знаем всё о значениях, пора заняться «тёмной магией»: знакомься, `@Processor`.

Этот аннотационный интерфейс позволяет выполнять код при загрузке конфигурации или при изменении значений. Сначала создадим простой процессор, который напечатает сообщение при загрузке:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Загрузка конфигурации!");
    }

    // ...
}
```

Метод `processConfig` вызывается при сборке конфигурации. Аргумент зависит от того, к чему применяется аннотация:

- Для `TrackedValue`: `TrackedValue.Builder`
- Для секций: `SectionBuilder`
- Для всей конфигурации: `Config.Builder`

Также можно указать *callback*, который выполнится при изменении значения:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Загрузка конфигурации!");
        builder.callback(config -> System.out.println("Обновление конфигурации!"));
    }

    // ...
}
```

Можно использовать это для синхронизации значений. Например, сделаем переменную `activeStream`, которая всегда совпадает с выбранным потоком `printStream`, чтобы не лезть за ним глубоко:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public static class AdvancedSettings extends Section {
        // ...
        @Processor("processPrintStream")
        public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);
        public transient PrintStream activeStream = printStream.value().getStream();

        public void processPrintStream(TrackedValue.Builder<PrintStreamOption> builder) {
            builder.callback(val -> activeStream = val.getStream());
        }
    }
}
```

Теперь `activeStream` будет автоматически обновляться, если пользователь изменит значение в конфиге.
