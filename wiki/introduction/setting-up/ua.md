# Налаштування середовища розробки

Перед тим як почати, тобі знадобляться кілька речей.

- **JDK для Java 17** (рекомендовано) або новіше.  
  Темурін Adoptium JDK легко доступний і рекомендований.  
  Завантажити можна тут: <https://adoptium.net/releases.html>

- **Будь-яке середовище розробки Java**, наприклад [IntelliJ IDEA](https://www.jetbrains.com/idea/) або [Eclipse](https://www.eclipse.org/ide/).  
  Також можна використовувати [Visual Studio Code](https://code.visualstudio.com/), але його потрібно додатково налаштувати.  
  - Ми рекомендуємо **IntelliJ IDEA**, оскільки воно має найбільшу інтеграцію та найпростіше у використанні.

Далі потрібно вирішити, чи ти хочеш [завантажити шаблон моду у вигляді ZIP-файлу](#template-mod-download-zip-file), чи [використати шаблон з GitHub](#template-mod-download-github-template). Якщо ти не знаєш, як користуватись git, обери перший варіант. Але все ж рекомендовано створити акаунт на GitHub і швидко ознайомитися з git.

---

## Завантаження шаблону моду (ZIP-файл)

Можна завантажити шаблон моду з репозиторію [quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod).

Для цього відкрий [репозиторій на GitHub](https://github.com/QuiltMC/quilt-template-mod),  
натисни кнопку `< > Code`, а потім — `Download ZIP`.

Розпакуй вміст ZIP-файлу в будь-яку теку на своєму комп’ютері.

---

## Завантаження шаблону моду (GitHub Template)

Щоб використати [шаблон з GitHub](https://github.com/QuiltMC/quilt-template-mod), відвідай репозиторій і натисни кнопку `Use this template`. Далі виконай вказівки та клонуй репозиторій за допомогою git. Потім можна продовжувати роботу.

---

## Налаштування IntelliJ IDEA

Відкрий IntelliJ IDEA та виконай початкове налаштування. Потім відкрий свій проєкт:

- Якщо ти завантажив ZIP-файл та розпакував його, натисни кнопку `Open` у списку проєктів і вибери теку з модом.
- Якщо ти створив репозиторій на GitHub, скористайся кнопкою `Get from VCS`.

Якщо з’явиться вікно з питанням, чи довіряєш ти проєкту — натисни **`Trust Project`**.

**Рекомендується встановити плагін для підтримки Minecraft-модів:**
<https://plugins.jetbrains.com/plugin/8327-minecraft-development>

---

## Огляд IntelliJ IDEA

Після відкриття проєкту ти побачиш вікно з кількома панелями:

- **Зліва вгорі** — іконка папки відкриває `Project`, де знаходяться файли проєкту.
- **Зліва внизу** — кнопки `Terminal` і `Version Control System`.
- **Справа** — кнопки `Notifications`, `Gradle`, `Run` і `Build`.

Файли відкриваються в центральній частині вікна. Якщо хочеш перейменувати файл або змінну, клацни правою кнопкою і вибери `Refactor > Rename`.

---

## Зроби мод своїм

1. Придумай назву моду, наприклад: `Bingus Mod`.
2. Створи ID моду: лише малі літери та підкреслення. Наприклад: `bingus_mod`.
3. Вкажи **Maven-групу**: вона ідентифікує автора. Якщо маєш GitHub — можна використовувати:  
   `io.github.твій_нік_на_github`, замінюючи спецсимволи на підкреслення.

### Приклад `gradle.properties`:

```gradle
# Gradle Properties
org.gradle.jvmargs = -Xmx1G
org.gradle.parallel = true

# Mod Properties
version = 1.0.0
maven_group = io.github.bingus
archives_base_name = bingus-mod
```

> У назві `archives_base_name` потрібно використовувати **дефіси (-)** замість підкреслень.

---

### Онови `quilt.mod.json`

Файл знаходиться в `src/main/resources`. Зміні на своє:

```json
{
	"schema_version": 1,
	"quilt_loader": {
		"group": "io.github.bingus",
		"id": "bingus_mod",
		"version": "${version}",
		"metadata": {
			"name": "Bingus Mod",
			"description": "Цей мод додає Bingus-а до Minecraft!",
			"contributors": {
				"Bingus": "Розробник"
			},
			"contact": {
				"homepage": "https://example.org"
			},
			"icon": "assets/bingus_mod/icon.png"
		},
		"intermediate_mappings": "net.fabricmc:intermediary",
		"entrypoints": {
			"init": "io.github.bingus.bingus_mod.BingusMod"
		},
		"depends": [
			// ...
		]
	}
}
```
