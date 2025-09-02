# Настройка среды разработки

Перед началом тебе понадобится несколько вещей:

- **JDK для Java 17** (рекомендуется) или новее.  
  Рекомендуем использовать Temurin JDK от Adoptium:  
  Ссылка: <https://adoptium.net/releases.html>

- **Любая среда разработки для Java**, например [IntelliJ IDEA](https://www.jetbrains.com/idea/) или [Eclipse](https://www.eclipse.org/ide/).  
  Также можно использовать [Visual Studio Code](https://code.visualstudio.com/), но его нужно дополнительно настраивать.  
  - **Рекомендуем IntelliJ IDEA**, так как у неё лучшая интеграция и самый простой старт.

Далее нужно выбрать: [скачать шаблон мода в виде ZIP](#template-mod-download-zip-file) или [использовать шаблон с GitHub](#template-mod-download-github-template).  
Если ты не знаешь, как пользоваться git — выбирай первый вариант. Но всё же лучше зарегистрироваться на GitHub и освоить основы.

---

## Скачивание шаблона мода (ZIP-архив)

Шаблон мода можно скачать с репозитория [quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod).

Открой [репозиторий на GitHub](https://github.com/QuiltMC/quilt-template-mod),  
нажми кнопку `< > Code`, затем — `Download ZIP`.

Распакуй содержимое ZIP-архива в любую удобную папку на компьютере.

---

## Скачивание шаблона мода (GitHub Template)

Чтобы использовать [шаблон с GitHub](https://github.com/QuiltMC/quilt-template-mod), перейди по ссылке и нажми `Use this template`.  
Создай свой репозиторий и клонируй его с помощью git. После этого можешь продолжить работу.

---

## Настройка IntelliJ IDEA

Открой IntelliJ IDEA и пройди первичную настройку. Затем открой проект:

- Если ты скачал ZIP и распаковал его — нажми `Open` и выбери папку проекта.
- Если создал репозиторий на GitHub — используй `Get from VCS`.

Если появится окно с вопросом, доверяешь ли ты проекту — нажми **`Trust Project`**.

**Рекомендуется установить плагин Minecraft Development:**
<https://plugins.jetbrains.com/plugin/8327-minecraft-development>

---

## Обзор интерфейса IntelliJ IDEA

После открытия проекта ты увидишь несколько панелей:

- **Слева вверху** — кнопка с папкой открывает `Project`, где находятся файлы.
- **Слева внизу** — терминал (`Terminal`) и система контроля версий (`Version Control System`).
- **Справа** — уведомления, `Gradle`, `Run`, `Build`.

Файлы открываются по центру. Чтобы переименовать файл или переменную — кликни правой кнопкой и выбери `Refactor > Rename`.

---

## Сделай мод своим

1. Придумай название мода, например: `Bingus Mod`.
2. Придумай ID мода: только строчные буквы и подчёркивания. Например: `bingus_mod`.
3. Укажи **maven_group** — это имя разработчика. Если у тебя есть GitHub — можно использовать, например:  
   `io.github.твой_ник`, заменив спецсимволы на подчёркивания.

### Пример `gradle.properties`:

```gradle
# Свойства Gradle
org.gradle.jvmargs = -Xmx1G
org.gradle.parallel = true

# Свойства мода
version = 1.0.0
maven_group = io.github.bingus
archives_base_name = bingus-mod
```

> В `archives_base_name` нужно использовать **дефисы (-)**, а не подчёркивания.

---

### Измени `quilt.mod.json`

Файл находится в `src/main/resources`. Пример:

```json
{
	"schema_version": 1,
	"quilt_loader": {
		"group": "io.github.bingus",
		"id": "bingus_mod",
		"version": "${version}",
		"metadata": {
			"name": "Bingus Mod",
			"description": "Этот мод добавляет Бингуса в Minecraft!",
			"contributors": {
				"Bingus": "Разработчик"
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
