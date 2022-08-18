# Your First Item
Items are crucial to Minecraft, and almost any mod will make use of them. This tutorial will go through the basic steps for creating an item.

## Registering the Item
The first thing we need to do is register the item so that the game knows to add it in. First, we need to declare an instance of `net.minecraft.item.Item` with the parameters for our item.

In theory, we could do this directly in the registration line but having a separate variable allows us to reference it elsewhere for other purposes.

```file:src/main/java/org/quiltmc/wiki/tutorial/SimpleItemExample.java@Declaration
```

Here, the `public static final` ensures that we can access the item elsewhere but not change it, making sure that we don't accidentally alter it somewhere else. 

Our new instance of `Item` takes in an instance of `QuiltItemSettings` as an argument. This is where we declare all of the settings for our item. There are a variety of these, but for now we only need `.group()`, which sets which creative tab the item will be found in. You can create your own, but here we use the Miscellaneous tab.

Now that we've declared the item, we need to tell the game registry to put it into the game. We do so by putting this into the mod's `onInitialize` method:

```file:src/main/java/org/quiltmc/wiki/tutorial/SimpleItemExample.java@Registration
```

`Registry.register()` takes three parameters:
- The `Registry` we want to add to. For items this is always `Registry.ITEM`.
- The `Identifier` used for the item. This must be unique. The first part is the namespace (which should be the mod ID, but here it is `tutorial`) and the item name itself. Only lowercase letters, numbers and underscores should be used. 
- The `Item` to register. Here, we pass in the item we declared earlier.

Having done all of this, if we run the game we can see that our item appears in the Miscellaneous tab! But it doesn't have a texture, and it's name isn't translated properly. How do we fix this?

## Assets
First we need to declare the model for the item. This tells the game how to render the item.

```file:src/main/resources/assets/tutorial/models/item/example_item.json
```

For most items, all you need to do here is replace `tutorial` with your mod ID and `example_item` with the item name you said earlier. This file should go to your assets folder under `/models/item`.

The texture file, as shown in the model, should be at `textures/item/example_item.png`.

Finally, we need to add a translation. Put this in `lang/en_us.json` in your assets folder, replacing the same values as before:

```file:src/main/resources/assets/tutorial/lang/en_us.json
```

And that's it! Your item should be working fully.

## What's next?
This tutorial only covers the most basic of items. Check the other item tutorials for more advanced items.