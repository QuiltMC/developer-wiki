# Полный перевод Quilt Config

## Введение

Quilt Config — это библиотека для конфигурационных файлов в Minecraft, предназначенная для простоты использования. Она помогает создавать и управлять конфигурациями, а также предоставляет удобные методы для сохранения и загрузки настроек. В этом руководстве мы рассмотрим, как использовать Quilt Config для создания конфигурационных классов и работы с различными типами данных.

### Зависимости

Чтобы начать использовать Quilt Config, добавь зависимость в `build.gradle`:

```gradle
dependencies {
    modCompile "org.quiltmc.config:quilt-config-api:3.0.0"
}
```

## Создание конфигурационного класса

Для начала создадим класс конфигурации, который будет хранить данные, например, сообщение и настройки:

```java
import org.quiltmc.config.api.Config;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.SerializedName;
import org.quiltmc.config.api.value.TrackedValue;
import org.quiltmc.config.api.value.ValueList;
import org.quiltmc.config.api.value.ValueMap;

public class ExampleModConfig extends Config {
    // Поле для строки
    @Comment("Сообщение для отображения.")
    @SerializedName("message")
    public final TrackedValue<String> message = this.value("Привет, мир!");

    // Поле для целого числа
    @Comment("Число попыток.")
    public final TrackedValue<Integer> attempts = this.value(3);

    // Поле для списка строк
    @Comment("Список игнорируемых слов.")
    public final TrackedValue<ValueList<String>> ignoredWords = this.list().add("a").add("the").add("and").build();

    // Поле для карты строк
    @Comment("Реплейсменты для текста.")
    public final TrackedValue<ValueMap<String>> replacements = this.map().put("собака", "пес").build();
}
```

### Описание аннотаций

- `@Comment` — аннотация для добавления комментариев в конфигурационный файл.
- `@SerializedName` — аннотация для задания другого имени поля в конфиге (например, если в конфиге нужно использовать другое имя, отличное от названия поля в классе).

### Типы данных

- `TrackedValue<String>` — строка, которая отслеживает изменения.
- `TrackedValue<Integer>` — целое число, отслеживающее изменения.
- `TrackedValue<ValueList<String>>` — список строк, отслеживающий изменения.
- `TrackedValue<ValueMap<String>>` — карта строк, отслеживающая изменения.

## Доступ к значениям и их изменению

Теперь, когда у нас есть конфигурационный класс, мы можем получить доступ к значениям и изменять их. Это можно сделать через поля класса:

```java
ExampleModConfig config = ExampleModConfig.INSTANCE;

String message = config.message.value();  // Получаем текущее значение сообщения
config.message.set("Новое сообщение!");   // Устанавливаем новое значение
```

Метод `value()` позволяет получить текущее значение поля, а метод `set()` позволяет изменить его. Изменения автоматически сохранятся при вызове `save()`.

## Работа с типами данных

### Списки (ValueList)

Списки позволяют хранить несколько значений. Пример использования:

```java
public final TrackedValue<ValueList<String>> ignoredWords = this.list()
    .add("a")
    .add("the")
    .add("and")
    .build();
```

Здесь создается список, состоящий из нескольких слов, которые будут игнорироваться.

### Карты (ValueMap)

Карты позволяют хранить пары ключ-значение. Пример:

```java
public final TrackedValue<ValueMap<String>> replacements = this.map()
    .put("собака", "пес")
    .put("кошка", "кот")
    .build();
```

Здесь создается карта, в которой заменяется слово "собака" на "пес", а "кошка" на "кот".

## Сохранение и загрузка конфигурации

Quilt Config автоматически сохраняет изменения в файл конфигурации. Чтобы сохранить или загрузить конфигурацию вручную, можно использовать следующие методы:

```java
ExampleModConfig.INSTANCE.save();   // Сохранить текущую конфигурацию
ExampleModConfig.INSTANCE.load();   // Загрузить конфигурацию
```

## Рекомендуемые практики

- Используйте комментарии (`@Comment`) для описания полей конфигурации. Это поможет пользователям понять, что делает каждое поле.
- Названия полей в классе и имена в конфигурационном файле могут отличаться. Используйте аннотацию `@SerializedName`, чтобы задать имя в конфиге, если оно должно быть другим.
- Храните данные в типах `TrackedValue`, чтобы отслеживать изменения в реальном времени.

## Заключение

Quilt Config — это мощная библиотека для работы с конфигурационными файлами в Minecraft. С помощью аннотаций и удобных типов данных можно легко управлять настройками мода и создавать конфиги, которые удобно редактировать и обновлять.
