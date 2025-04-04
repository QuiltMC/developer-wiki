# Повний переклад Quilt Config

## Вступ

Quilt Config — це бібліотека для конфігураційних файлів у Minecraft, яка покликана полегшити їх використання. Вона дозволяє створювати та управляти конфігураціями, а також надає зручні методи для збереження та завантаження налаштувань. У цьому посібнику ми розглянемо, як використовувати Quilt Config для створення конфігураційних класів та роботи з різними типами даних.

### Залежності

Щоб почати використовувати Quilt Config, додайте залежність у `build.gradle`:

```gradle
dependencies {
    modCompile "org.quiltmc.config:quilt-config-api:3.0.0"
}
```

## Створення конфігураційного класу

Для початку створимо клас конфігурації, який буде зберігати дані, наприклад, повідомлення та налаштування:

```java
import org.quiltmc.config.api.Config;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.SerializedName;
import org.quiltmc.config.api.value.TrackedValue;
import org.quiltmc.config.api.value.ValueList;
import org.quiltmc.config.api.value.ValueMap;

public class ExampleModConfig extends Config {
    // Поле для рядка
    @Comment("Повідомлення для відображення.")
    @SerializedName("message")
    public final TrackedValue<String> message = this.value("Привіт, світ!");

    // Поле для цілого числа
    @Comment("Кількість спроб.")
    public final TrackedValue<Integer> attempts = this.value(3);

    // Поле для списку рядків
    @Comment("Список ігнорованих слів.")
    public final TrackedValue<ValueList<String>> ignoredWords = this.list().add("a").add("the").add("and").build();

    // Поле для карти рядків
    @Comment("Заміни для тексту.")
    public final TrackedValue<ValueMap<String>> replacements = this.map().put("собака", "пес").build();
}
```

### Опис анотацій

- `@Comment` — анотація для додавання коментарів у конфігураційний файл.
- `@SerializedName` — анотація для вказівки іншого імені поля в конфігурації (наприклад, якщо в конфігурації потрібно використовувати інше ім’я, відмінне від назви поля в класі).

### Типи даних

- `TrackedValue<String>` — рядок, що відстежує зміни.
- `TrackedValue<Integer>` — ціле число, що відстежує зміни.
- `TrackedValue<ValueList<String>>` — список рядків, що відстежує зміни.
- `TrackedValue<ValueMap<String>>` — карта рядків, що відстежує зміни.

## Доступ до значень та їх зміна

Тепер, коли ми маємо конфігураційний клас, ми можемо отримувати доступ до значень і змінювати їх. Це можна зробити через поля класу:

```java
ExampleModConfig config = ExampleModConfig.INSTANCE;

String message = config.message.value();  // Отримуємо поточне значення повідомлення
config.message.set("Нове повідомлення!");   // Встановлюємо нове значення
```

Метод `value()` дозволяє отримати поточне значення поля, а метод `set()` дозволяє змінити його. Зміни автоматично збережуться при виклику `save()`.

## Робота з типами даних

### Списки (ValueList)

Списки дозволяють зберігати кілька значень. Приклад використання:

```java
public final TrackedValue<ValueList<String>> ignoredWords = this.list()
    .add("a")
    .add("the")
    .add("and")
    .build();
```

Тут створюється список, що складається з кількох слів, які будуть ігноруватися.

### Карти (ValueMap)

Карти дозволяють зберігати пари ключ-значення. Приклад:

```java
public final TrackedValue<ValueMap<String>> replacements = this.map()
    .put("собака", "пес")
    .put("кіт", "кот")
    .build();
```

Тут створюється карта, в якій слово "собака" замінюється на "пес", а "кіт" на "кот".

## Збереження та завантаження конфігурації

Quilt Config автоматично зберігає зміни в конфігураційний файл. Щоб зберегти або завантажити конфігурацію вручну, можна використовувати наступні методи:

```java
ExampleModConfig.INSTANCE.save();   // Зберегти поточну конфігурацію
ExampleModConfig.INSTANCE.load();   // Завантажити конфігурацію
```

## Рекомендовані практики

- Використовуйте коментарі (`@Comment`) для опису полів конфігурації. Це допоможе користувачам зрозуміти, що робить кожне поле.
- Назви полів у класі та імена в конфігураційному файлі можуть відрізнятися. Використовуйте анотацію `@SerializedName`, щоб вказати ім’я в конфігурації, якщо воно має бути іншим.
- Зберігайте дані в типах `TrackedValue`, щоб відстежувати зміни в реальному часі.

## Висновок

Quilt Config — це потужна бібліотека для роботи з конфігураційними файлами в Minecraft. За допомогою анотацій і зручних типів даних можна легко керувати налаштуваннями мода та створювати конфіги, які зручно редагувати та оновлювати.
