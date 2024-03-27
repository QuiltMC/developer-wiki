# Creating your First Item

<!-- This is migrated from the old wiki and modified to match 1.20, with some additions -->

Items are crucial to Minecraft, and almost any mod will make use of them. This tutorial will go through the basic steps for creating an item.

## Registering the Item

The first thing we need to do is register the item so that the game knows to add it in. [Registries](../concepts/registries) are an integral part to Minecraft (and modding it). Blocks, entities, items, sounds, particles... all those different aspects are using a registry.

---

First, we need to set up a place for our mod's items. Make a new class called `ExampleModItems`, replacing `ExampleMod` with your mod's name, in the same package as your main class. This will help as stay organised if our mod expands beyond just one item.

Next, we'll declare an instance of `net.minecraft.item.Item` with the parameters for our item.

In theory, we could skip this step and declare the item as we register it, but having a separate variable allows us to reference it elsewhere for other purposes.

`src/main/com/example/example_mod/ExampleModItems`:

```java
public class ExampleModItems {
    public static final Item EXAMPLE_ITEM = new Item(new QuiltItemSettings());
}
```

Here, the `public static final` ensures that we can access the item elsewhere but can't reassign the variable itself, making sure that we don't accidentally alter it somewhere else.

Our new instance of `Item` takes in an instance of `QuiltItemSettings` as an argument. This is where we declare all the settings for our item. There are a variety of settings, such as durability and stack size, but in this case we can just use the default settings.

---

Now that we've declared the item, we need to register it in order to put it into the game. We're going to set up a new method in your items class to hold the registrations for all your items. Note that we take the mod's `ModContainer` as a parameter, so that we can get the mod ID from it.

`src/main/com/example/example_mod/ExampleModItems`:

```java
public class ExampleModItems {
    // ...
    public static void register(ModContainer mod) {
        Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item"), EXAMPLE_ITEM);
    }
}
```

`Registry.register()` takes three parameters:

- The `Registry` we want to add to. For items this is always `Registries.ITEM`.
- The `Identifier` used for the item. This must be unique. The first part is the namespace (which should be the mod id) and the item name itself. Only lowercase letters, numbers, underscores, dashes, periods, and slashes are allowed. To convert the item's name into this format, write everything in lowercase and separate words using underscores.
- The `Item` to register. Here, we pass in the item we declared earlier.

Finally, we need to make sure that the `register()` method is called as the game boots up.
We do so by putting the method call into the mod's `ModInitializer` ([read more about mod initializers here!](../concepts/sideness#on-mod-initializers)) in the `onInitialize` method:

`src/main/com/example/example_mod/ExampleMod`:

```java
public class ExampleMod implements ModInitializer {
    // ...
    @Override
    public void onInitialize(ModContainer mod) {
        // ...
        ExampleModItems.register(mod);
    }
}
```

Having done all of this, if we run the game we can see that we can give the item using the give command: `/give @s simple_item_mod:example_item`! But it doesn't appear in the creative menu, nor does it have a texture, and its name isn't translated properly. How do we fix this?

## Adding the Item to a Group

`ItemGroup`s represent the tabs in the creative menu.
Because of a change in 1.19.3, you can't add items to item groups using only [Quilt Standard Libraries](../concepts/qsl-qfapi#quilt-standard-libraries) (From now on QSL). However, [Fabric API](../concepts/qsl-qfapi#fabric-api) has an API for it. Thanks to [Quilted Fabric API](../concepts/qsl-qfapi#quilted-fabric-api), which the template mod includes and users download with QSL, we can use it on Quilt, too:

`src/main/com/example/example_mod/ExampleModItems`:

```java
public class ExampleModItems {
    // ...
    public static void register() {
        //...
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addItem(EXAMPLE_ITEM);
        });
    }
}
```

Here we are using the `ItemGroupEvents` API. We get the [event](../concepts/events) for modifying the `INGREDIENTS` item group and register a new listener. [Events](../concepts/events) have two main use cases. On the one side you can use them to do things when something happens, for example when block gets broken or even simply at each tick. On the other side they can be used for modifying things like item groups, where ordering is important. In this case, however, we are doing nothing complicated and simply adding the item to the end of the ingredients item group. This will also add them to the creative menu search.

## Textures

First we need to declare the model for the item. This tells the game how to render the item.

`assets/simple_item_mod/models/item/example_item.json`:

```json
{
	"parent": "item/generated",
	"textures": {
		"layer0": "simple_item_mod:item/example_item"
	}
}
```

For most items, all you need to do here is replace `simple_item_mod` with your mod ID and `example_item` with the item name you set earlier. Be sure to replace them in both the file/folder names and the JSON file.
The texture file, as shown in the model, should match the path specified in the `Identifier`, so in our case `assets/simple_item_mod/textures/item/example_item.png`

## Language Translation

Finally, we need to add a translation. Put this in `assets/simple_item_mod/lang/en_us.json`, replacing the mod id and item name as before:

`assets/simple_item_mod/lang/en_us.json`:

```json
{
	"item.simple_item_mod.example_item": "Example Item"
}
```

Replace the mod id and item name as before.

And that's it! Your item should be fully working.

## What's next?

This tutorial only covers the most basic of items. Check the other item tutorials for more advanced items or try [Adding a simple block](../blocks/first-block).

If you want your item to have a recipe, generate one from [destruc7i0n's crafting recipe generator](https://crafting.thedestruc7i0n.ca/) (you can add your mod's items using the "Add Item" button above the ingredient list) and then put it in a JSON file under `resources/data/simple_item_mod/recipes/` (replacing `simple_item_mod` with your mod id). Further details on item recipes can be found on [the dedicated recipe page](../data/adding-recipes).
