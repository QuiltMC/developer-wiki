---
title: Adding your first Block
index: 1
---
# Adding your first Block
Creating a Block is pretty similar to creating an Item, [read the corresponding article](../items/first-item) for more elaborate explanations.

## Creating and registering the Block

First we create the Block and store it in a field:

```java
public final Block EXAMPLE_BLOCK = new Block(new QuiltBlockSettings())
```

Then we register it in the `onInitialize()` function:

```java
Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "example_block"), EXAMPLE_BLOCK);
```
## Adding a BlockItem

Having done that, we can place the block using the `setblock` command, but there isn't an item to place it. So we additionially register a `BlockItem` for the block and add it to the `BUILDING_BLOCKS` item group:

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata.id(), "example_block"), new BlockItem(EXAMPLE_BLOCK, new QuiltItemSettings()))

ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
	entries.addStack(EXAMPLE_BLOCK.asItem().getDefaultStack());
});
```

For more information on what this does, see the [Creating your first Item](../items/first-item) Article.

## Adding a Model for the Block

First, we need to create the BlockStates file for your Block. This tells the game which BlockStates use which models. For an example on using a more complicated BlockState setup, see [Adding redstone functionality to your Block](TODO). In this case, we only have one BlockState and can use this simple json:
```json
{
    "variants": {
        "": {
            "model": "simple_block:block/example_block"
        }
    }
}
```

Use this for your `example_block.json` in `blockstates` in your assets folder and replace `simple_block` and `example_block` with your mod id and block name.

Now we can put the model at the path specified by the identifier (in this case `models/block/example_block.json`).

```json
{
	"parent": "minecraft:block/cube_all",
	"textures": {
		"all": "simple_block:block/example_block"
	}
}
```

This will use the texture located at `textures/block/example_block` in your assets folder for all sides of the block.


To use this model for the item variant, too, declare the Block model as a parent in the item model which is located at `models/item/example_block.json` in your assets folder:
```json
{
	"parent": "simple_block:block/example_block"
}
```

## Adding a translation for the Block

Last but not least we need to add a translation for the block. This will automatically apply to the BlockItem, too.
```json
{
    "block.simple_block.example_block": "Example Block"
}
```

## What's next?
Now that you've added your first Block to Minecraft, you can continue by [Adding a oriented Block](oriented-block) or [Adding redstone functionality to your Block](redstone-interaction) or adding advanced items such as [Armor](../items/armor), [Food](../items/food) or [Tools](../items/tools)