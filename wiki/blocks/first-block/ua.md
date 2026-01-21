# Додавання простого блоку

Створення блоку дуже схоже на [створення предмета](../items/first-item), але тепер ми працюємо як з реєстрацією блоку, так і з реєстрацією предмета, а також детальніше розглядаємо процес створення моделі.

## Створення та реєстрація блоку

Спочатку створимо блок і збережемо його в полі:

```java
public static final Block EXAMPLE_BLOCK = new Block(QuiltBlockSettings.create());
```

Потім реєструємо його у функції `onInitialize()`:

```java
Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "example_block"), EXAMPLE_BLOCK);
```

Замініть `example_block` на назву вашого блоку. Усі літери мають бути малими, слова розділяйте підкресленнями.

## Додавання предмета для блоку

Після цього ми зможемо розміщувати блок за допомогою команди `setblock`, але в творчому режимі предмет для нього не з’явиться. Щоб це виправити, потрібно зареєструвати `BlockItem` для блоку й додати його до групи предметів, наприклад `BUILDING_BLOCKS`:

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_block"), new BlockItem(EXAMPLE_BLOCK, new QuiltItemSettings()));

ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
	entries.addItem(EXAMPLE_BLOCK.asItem());
});
```

Назва предмета має бути така ж, як і назва блоку.

Докладніше про це можна прочитати в статті [Створення вашого першого предмета](../items/first-item#registering-the-item).

## Додавання моделі для блоку

Спочатку потрібно створити файл `blockstates` для вашого блоку. Для кожної варіації блоку існує окрема **стадія блоку** (block state). Наприклад, кожен етап росту рослини — це окрема стадія. Файл `blockstates` пов’язує ці стадії з відповідними моделями. У нашому випадку блок має лише одну стадію, тому файл досить простий.

Для прикладу складнішої конфігурації дивіться статтю [Додавання взаємодії з редстоуном](redstone-interaction). А поки що можна використати цей простий JSON:

`assets/simple_block_mod/blockstates/example_block.json`:

```json
{
	"variants": {
		"": {
			"model": "simple_block_mod:block/example_block"
		}
	}
}
```

Замініть `simple_block_mod` і `example_block` на ваш ID мода і назву блоку. Не забудьте оновити імена файлів/тек.

---

Тепер можна створити модель блоку за вказаним шляхом (`assets/simple_block_mod/models/block/example_block.json`):

```json
{
	"parent": "minecraft:block/cube_all",
	"textures": {
		"all": "simple_block_mod:block/example_block"
	}
}
```

Замініть `simple_block_mod` і `example_block` як раніше.

Це використовуватиме текстуру, що розташована за шляхом `assets/simple_block_mod/textures/block/example_block.png`, для всіх сторін блоку.

---

Наш предмет-блок також потребує моделі, але замість використання окремої текстури, як у [уроці про предмети](../items/first-item), ми вкажемо модель блоку як батьківську модель. Так блок у інвентарі виглядатиме так само, як і в світі гри.

`assets/simple_block_mod/models/item/example_block.json`:

```json
{
	"parent": "simple_block_mod:block/example_block"
}
```

Знову ж, замініть `simple_block_mod` і `example_block` на ваші значення.

## Додавання перекладу назви блоку

Нарешті, потрібно додати переклад для назви блоку. Він автоматично застосується і до предмета-блоку.

`assets/simple_block_mod/lang/en_us.json`:

```json
{
	"block.simple_block_mod.example_block": "Example Block"
}
```

Замініть `simple_block_mod` і `example_block` відповідно.

## Що далі?

Тепер, коли ви додали блок до Minecraft, ви можете продовжити з [Додаванням орієнтованого блоку](oriented-block), [Додаванням взаємодії з редстоуном](redstone-interaction) або створенням складніших предметів: [броні](../items/armor), [їжі](../items/food) або [інструментів](../items/tools).
