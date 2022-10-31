# Your First Item
Items are crucial to Minecraft, and almost any mod will make use of them. This tutorial will go through the basic steps for creating an item.

## Registering the Item
The first thing we need to do is register the item so that the game knows to add it in. First, we need to declare an instance of `net.minecraft.item.Item` with the parameters for our item.

In theory, we could do this directly in the registration line but having a separate variable allows us to reference it elsewhere for other purposes.

```tabbed-files
java:java/src/main/java/org/quiltmc/wiki/simple_item/SimpleItemExample.java@Declaration
kotlin:kotlin/src/main/kotlin/org/quiltmc/wiki/simple_item/SimpleItemExample.kt@Declaration
scala:scala/src/main/scala/org/quiltmc/wiki/simple_item/SimpleItemExample.scala@Declaration
groovy:groovy/src/main/groovy/org/quiltmc/wiki/simple_item/SimpleItemExample.groovy@Declaration
```

Here, the `public static final` ensures that we can access the item elsewhere but not change the contents of the variable itself, making sure that we don't accidentally alter it somewhere else.

Our new instance of `Item` takes in an instance of `QuiltItemSettings` as an argument. This is where we declare all of the settings for our item. There are a variety of these, but for now we only need `.group()`, which sets which creative tab the item will be found in. You can create your own, but here we use the Miscellaneous tab.

Now that we've declared the item, we need to tell the game registry to put it into the game. We do so by putting this into the mod's `onInitialize` method:

```tabbed-files
java:java/src/main/java/org/quiltmc/wiki/simple_item/SimpleItemExample.java@Registration
kotlin:kotlin/src/main/kotlin/org/quiltmc/wiki/simple_item/SimpleItemExample.kt@Registration
scala:scala/src/main/scala/org/quiltmc/wiki/simple_item/SimpleItemExample.scala@Registration
groovy:groovy/src/main/groovy/org/quiltmc/wiki/simple_item/SimpleItemExample.groovy@Registration
```

`Registry.register()` takes three parameters:

- The `Registry` we want to add to. For items this is always `Registry.ITEM`.
- The `Identifier` used for the item. This must be unique. The first part is the namespace (which should be the mod id, but here it is `simple_item`) and the item name itself. Only lowercase letters, numbers, underscores, dashes, periods, and slashes are allowed.
- The `Item` to register. Here, we pass in the item we declared earlier.

Having done all of this, if we run the game we can see that our item appears in the Miscellaneous tab! But it doesn't have a texture, and its name isn't translated properly. How do we fix this?

## Textures
First we need to declare the model for the item. This tells the game how to render the item.

```json:java/src/main/resources/assets/simple_item/models/item/example_item.json
```

For most items, all you need to do here is replace `simple_item` with your mod ID and `example_item` with the item name you set earlier. This file should go to your assets folder under `/models/item`.

The texture file, as shown in the model, should match the path specified in the `Identifier`, so in our case `textures/item/example_item.png`

## Language Translation

Finally, we need to add a translation. Put this in `lang/en_us.json` in your assets folder, replacing the same values as before:

```json:java/src/main/resources/assets/simple_item/lang/en_us.json
```

And that's it! Your item should be fully working.


## What's next?
This tutorial only covers the most basic of items. Check the other item tutorials for more advanced items.

If you want your item to have a recipe, generate one from [destruc7i0n's crafting recipe generator](https://crafting.thedestruc7i0n.ca/) (you may want to use a placeholder for the `output` item and then replace it with e.g. `simple_item:example_item`) and then put it in a JSON file under `src/main/resources/data/simple_item/recipes/` (replacing `simple_item` with your mod ID). Further details on item recipes can be found <abbr title="This documentation is not done yet, but it will be soon!">here</abbr>.
