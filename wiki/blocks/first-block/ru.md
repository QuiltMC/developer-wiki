# Добавление простого блока

Создание блока во многом похоже на [создание предмета](../items/first-item), но теперь мы используем как реестр блоков, так и реестр предметов, а также более подробно разбираемся с созданием модели.

## Создание и регистрация блока

Сначала создаём блок и сохраняем его в поле:

```java
public static final Block EXAMPLE_BLOCK = new Block(QuiltBlockSettings.create());
```

Затем регистрируем его в методе `onInitialize()`:

```java
Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "example_block"), EXAMPLE_BLOCK);
```

Замените `example_block` на имя вашего блока. Имя должно быть написано строчными буквами, слова разделяйте нижними подчёркиваниями.

## Добавление предмета для блока

После этого мы сможем разместить блок с помощью команды `setblock`, но предмета в творческом инвентаре всё ещё не будет. Чтобы это исправить, зарегистрируем `BlockItem` для нашего блока и добавим его в группу предметов, например `BUILDING_BLOCKS`:

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_block"), new BlockItem(EXAMPLE_BLOCK, new QuiltItemSettings()));

ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
	entries.addItem(EXAMPLE_BLOCK.asItem());
});
```

Имя предмета должно совпадать с именем блока.

Подробнее о регистрации предметов читайте в [руководстве по созданию первого предмета](../items/first-item#registering-the-item).

## Добавление модели для блока

Сначала создадим файл `blockstates` для блока. Для каждой формы блока существует своё **состояние блока**. Например, каждая стадия роста растения — это отдельное состояние. Файл `blockstates` связывает состояния блока с их соответствующими моделями. В нашем случае у блока только одно состояние, поэтому JSON очень простой.

Пример более сложной настройки смотрите в [добавлении редстоун-функционала](redstone-interaction). Сейчас же можно использовать такой файл:

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

Замените `simple_block_mod` и `example_block` на ID вашего мода и имя блока — в названии файла, папок и внутри JSON.

---

Теперь создадим модель блока по указанному пути (`assets/simple_block_mod/models/block/example_block.json`):

```json
{
	"parent": "minecraft:block/cube_all",
	"textures": {
		"all": "simple_block_mod:block/example_block"
	}
}
```

Как и прежде, замените `simple_block_mod` и `example_block`.

Эта модель будет использовать текстуру по пути `assets/simple_block_mod/textures/block/example_block.png` для всех сторон блока.

---

Нам также нужна модель для предмета-блока. Вместо указания отдельной текстуры, как в [уроке про предметы](../items/first-item), мы укажем модель блока как родительскую. Тогда предмет будет отображаться точно так же, как и сам блок в мире.

`assets/simple_block_mod/models/item/example_block.json`:

```json
{
	"parent": "simple_block_mod:block/example_block"
}
```

И снова — не забудьте заменить `simple_block_mod` и `example_block`.

## Добавление перевода названия блока

И напоследок добавим перевод для блока. Он автоматически будет применён и к предмету-блоку.

`assets/simple_block_mod/lang/en_us.json`:

```json
{
	"block.simple_block_mod.example_block": "Example Block"
}
```

Замените `simple_block_mod` и `example_block` соответственно.

## Что дальше?

Теперь, когда ты добавил блок в Minecraft, можешь перейти к [добавлению направленного блока](oriented-block), [добавлению редстоун-функционала](redstone-interaction) или к созданию более продвинутых предметов, таких как [броня](../items/armor), [еда](../items/food) или [инструменты](../items/tools).
