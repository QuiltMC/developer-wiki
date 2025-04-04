# Розширене налаштування

Прості значення — це добре, але коли їх багато, вони стають важкими для організації. У цьому уроці ми розглянемо, як структурувати конфігурацію та використовувати обробники (processors), щоб отримати від неї максимум користі.

## Використання секцій

Файл з десятками значень швидко стає важким для навігації та розуміння. На щастя, з Quilt Config ми можемо організувати його на секції! Це дуже просто реалізувати.

Секції дозволяють використовувати відступи для візуального розділення частин конфігурації. Давайте додамо приклад секції, яка у форматі TOML виглядає так:

```toml
# ...

# Це насправді не використовується модом, просто ідей більше не було.
typesOfSoup = ["tomato", "borscht", "chicken noodle", "ramen", "STEW", "mushroom"]

# Розширені налаштування для досвідчених користувачів.
[advanced_settings]
    # Чи додавати новий рядок після кожного повідомлення.
    # default: true
    printNewlines = true
```

Щоб зробити це в коді:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("Розширені налаштування для досвідчених користувачів.")
    @SerializedName("advanced_settings")
    public final AdvancedSettings advancedSettings = new AdvancedSettings();

    public static class AdvancedSettings extends Section {
        @Comment("Чи додавати новий рядок після кожного повідомлення.")
        @SerializedName("print_newlines")
        public final TrackedValue<Boolean> printNewlines = this.value(true);
    }
}
```

Ми просто створюємо новий клас усередині основного конфігураційного класу, який наслідує `ReflectiveConfig.Section`. Потім створюємо об'єкт цього класу в головному конфіг-файлі. Зверніть увагу: екземпляр секції **не** зберігається у `TrackedValue`, натомість всередині секції вже зберігаються `TrackedValue`.

Тепер ми можемо додавати будь-яку кількість значень в секцію. Але що, якщо ми хочемо зберегти не просто тип, а об'єкт? Для цього нам знадобиться серіалізація власних типів.

## Серіалізація власних значень

В Java ви можете виводити повідомлення у різні потоки виводу. Вони не є серіалізованими за замовчуванням, як `int` чи `String`. Тут і стає в нагоді інтерфейс `ConfigSerializableObject<T>`.

Він працює через дженерики: `<T>` — це тип, в який ваш об’єкт буде серіалізовано для збереження у файлі, а потім буде відновлено назад. Щоб це реалізувати, потрібно визначити три методи:

- `T getRepresentation()`: повертає серіалізований варіант вашого об'єкта.
- `YourSerializableClass convertFrom(T)`: перетворює серіалізований варіант назад у ваш клас.
- `YourSerializableClass copy()`: створює копію об'єкта (використовується всередині Quilt Config).

### Приклад з Enum:

```java
public static class AdvancedSettings extends Section {
    @Comment("До якого потоку виводити повідомлення.")
    @SerializedName("print_stream")
    public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);

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
```

Enum дозволяє нам задати обмежений набір значень, а також Quilt Config самостійно згенерує коментар з можливими варіантами.

### Приклад з об’єктом координат:

```java
public class Vec3i implements ConfigSerializableObject<ValueMap<Integer>> {
    public final int x, y, z;

    public Vec3i(int x, int y, int z) {
        this.x = x; this.y = y; this.z = z;
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
                .put("x", x)
                .put("y", y)
                .put("z", z)
                .build();
    }
}
```

Тут ми використовуємо `ValueMap`, щоб зручно представити складний об’єкт з кількома полями.

## Використання процесорів

А тепер — трохи зла 😈. `@Processor` дозволяє змінювати налаштування конфігурації, а також виконувати код при зміні значень.

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Завантаження конфігурації!");
    }
}
```

Метод `processConfig` буде викликано при створенні конфігу. Крім того, ми можемо додати зворотний виклик (callback), який виконується при зміні конфігурації:

```java
builder.callback(config -> System.out.println("Конфігурацію оновлено!"));
```

Можна також використовувати процесори для окремих полів. Наприклад:

```java
@Processor("processPrintStream")
public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);
public transient PrintStream activeStream = printStream.value().getStream();

public void processPrintStream(TrackedValue.Builder<PrintStreamOption> builder) {
    builder.callback(val -> activeStream = val.getStream());
}
```

Це дозволяє автоматично оновлювати змінну `activeStream`, коли змінюється поле `printStream`.
