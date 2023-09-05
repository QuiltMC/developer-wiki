---
title: Adding a Simple Block
index: 1
---
# Adding a Simple Block

Creating a block is quite similar to [creating an item](../items/first-item), but now leveraging both the block *and* item registries, as well as a more in-depth process for creating a model.

## Creating and Registering the Block

First we create the block and store it in a field:

```java
public final Block EXAMPLE_BLOCK = new Block(new QuiltBlockSettings())
```

Then we register it in the `onInitialize()` function:

```java
Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "example_block"), EXAMPLE_BLOCK);
```

Replace `example_block` with your block's name. Write everything in lowercase and separate words using underscores.

## Adding an Item for the Block

Having done that, we can place the block using the `setblock` command but, opening the creative menu, we won't be able to find an item version of it. To fix this, we register a `BlockItem` for the block and add it to an item group, in our case `BUILDING_BLOCKS`:

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_block"), new BlockItem(EXAMPLE_BLOCK, new QuiltItemSettings()))

ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
	entries.addItem(EXAMPLE_BLOCK.asItem());
});
```

The item's name should be the same as your block's name.

For more information on what this does, see the [Creating your First Item](../items/first-item#registering-the-item) Article.

## Adding a Model for the Block

First, we need to create the `blockstates` file for your block. There is one block state for each of the different forms that your block can take: For example, each stage of growth in a crop block is a different block state. The `blockstates` file simply links all those different block states to their respective models. In this case, the block has only one state, so the block state file is pretty simple. For an example on using a more complicated block state setup, see [Adding Redstone functionality to your Block](redstone-interaction). In this case though can use this simple JSON:

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

Replace `simple_block_mod` and `example_block` with your mod id and block name. Be sure to replace them in both the file/folder names and the JSON file.

---

Now we can put the model at the path specified by the identifier (in this case `assets/simple_block_mod/models/block/example_block.json`).

```json
{
	"parent": "minecraft:block/cube_all",
	"textures": {
		"all": "simple_block_mod:block/example_block"
	}
}
```

Replace `simple_block_mod` and `example_block` as before.
This will use the texture located at `assets/simple_block_mod/textures/block/example_block.png` for all sides of the block.

---

Our block item needs a model too, but instead of simply using a texture as we did in the [item tutorial](../items/first-item), we'll declare the block model as a parent in the item model. This will render the block item identically to how the block would look in-game.

`assets/simple_block_mod/models/item/example_block.json`

```json
{
	"parent": "simple_block_mod:block/example_block"
}
```

Replace `simple_block_mod` and `example_block` as before.

## Adding a translation for the Block

Last but not least we need to add a translation for the block. This will automatically apply to the block item, too.

`assets/simple_block_mod/lang/en_us.json`:

```json
{
	"block.simple_block_mod.example_block": "Example Block"
}
```

Replace `simple_block_mod` and `example_block` as before.

## What's next?

Now that you've added a block to Minecraft, you can continue by [Adding an oriented Block](oriented-block), [Adding Redstone functionality to your Block](redstone-interaction), or adding advanced items such as [armor](../items/armor), [food](../items/food) or [tools](../items/tools)
