---
title: Adding an Armor Set
index: 3
---
# Adding an Armor Set

This tutorial will assume that you already understand [Creating your First Item](first-item).

## Creating an Armor Material

To add an armor set, you start by creating an armor material. It defines the basic properties of your armor, such as the durability, the armor value, or the toughness.
For this you create a new armor material class:

`src/main/com/example/example_mod/ExampleArmorMaterial`:

```java
public class ExampleArmorMaterial implements ArmorMaterial {
    //...
```

Replace the path with the appropriate values from your mod, and name the armor material according to your base resource. For example, an armor material based of emeralds would be called `EmeraldArmorMaterial`. You can also use an `enum` instead of a simple class if you have multiple different armor types, just like Minecraft does.

---

Now let's continue by adding the durability and armor values:

```java
private static final int[] DURABILITY = new int[]{100,160,100,100};
private static final int[] PROTECTION = new int[]{2,8,4,2};
@Override
public int getDurability(ArmorItem.ArmorSlot slot) {
	return DURABILITY[slot.getEquipmentSlot().getEntitySlotId()];
}

@Override
public int getProtection(ArmorItem.ArmorSlot slot) {
	return PROTECTION[slot.getEquipmentSlot().getEntitySlotId()];
}
```

Here, we use a neat trick: `slot.getEquipmentSlot().getEntitySlotId()` will return a value between 0 and 3 based on the piece, so we can simply take those as an index for an array.
The protection corresponds to the Armor's armor value.

---

Next we'll define the [Enchantability](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability). This defines how good the enchantments in an enchantment table will get. For example netherite armor and gold armor have a high [Enchantability](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability).

```java
@Override
public int getEnchantability() {
	return 10;
}
```

---

Now it's time to define an equipment sound. If you want to add your custom sound, see [Adding Sounds](../misc/sounds). If you want to use an existing sound, you can use `SoundEvents.ITEM_ARMOR_EQUIP_GENERIC`, just like in the example.

```java
@Override
public SoundEvent getEquipSound() {
	return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
}
```

---

Next you need to specify the repair ingredients you use, in this case the example item from the [Creating your First Item](first-item) tutorial.

```java
@Override
public Ingredient getRepairIngredient() {
	return Ingredient.ofItems(ExampleMod.EXAMPLE_ITEM);
}
```

---

Finally, we need to specify the armor material's name, its toughness and its knockback resistance.
The material's name should usually be the name of the main item you armor is made of. Toughness helps to reduce especially high damage and knockback resistance works only on netherite armor in vanilla, but is patched to work for other armors by [QSL](../concepts/qsl-qfapi).

```java
@Override
public String getName() {
	return "example_item";
}
```

```java
@Override
public float getToughness() {
	return 2;
}
```

```java
@Override
public float getKnockbackResistance() {
	return 0.2f;
}
```

## Adding the Actual Items

Now that we have declared the armor material, let's add the actual items of the armor:
<!-- TODO: Note that there will be a link here in the MVP so a path is not specified currently -->
```java
public static final ArmorMaterial EXAMPLE_ARMOR_MATERIAL = new ExampleArmorMaterial();
public static final Item EXAMPLE_HELMET = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings());
public static final Item EXAMPLE_CHESTPLATE = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings());
public static final Item EXAMPLE_LEGGINGS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings());
public static final Item EXAMPLE_BOOTS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings());
```

First we instantiate the armor material and store it in a final static variable. Then we instantiate the items to register them later. Note that we create an `ArmorItem` in all cases and then simply supply a `ArmorItem.ArmorSlot` to specify the slot to use. Lastly we pass the settings, the stack size should automatically be 1 for armor, so we pass the default settings.

---

Next, we register the armor in our mod initializer and add it to the combat item group. There are two things to notice about this:

1. We name the armor items the material name combined with their variant.
2. We add them to the item group using the `addAfter` method. Because it is an armor, we want to have it appear next to all the vanilla armors. So we use that method, which can add a variable amount of items after another specific, already present item.

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_helmet"), EXAMPLE_HELMET);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_chestplate"), EXAMPLE_CHESTPLATE);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_leggings"), EXAMPLE_LEGGINGS);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_boots"), EXAMPLE_BOOTS);
ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
	entries.addAfter(Items.NETHERITE_BOOTS, EXAMPLE_HELMET, EXAMPLE_CHESTPLATE, EXAMPLE_LEGGINGS, EXAMPLE_BOOTS);
});
```

## Adding Textures for the Armor

Now that we have the armor registered, the only things missing are the textures and translations.
For armors, you have to provide two textures. One for the items themselves, and one for the armor that is rendered on the player model.

<!-- TODO: the desectionization fails if nested like this. See svelte.config.json -->
<!-- ### Adding the texture to the Item-->

---

Adding the texture to the item is the same as any normal item: You need to add a model and a texture. For example, here is the model file for an example helmet:

```json
{
	"parent": "item/generated",
	"textures": {
		"layer0": "example_mod:item/example_item_helmet"
	}
}
```

Repeat this for all your armor's items, replacing `example_mod` and `example_item_helmet` with your mod id and item name, and add the corresponding textures.

<!-- TODO: the desectionization fails if nested like this. See svelte.config.json -->
<!-- ### Adding the texture for the player model -->

---

Now the armor should look right in the inventory, but when you put it on, it will still show the missing texture. For that one to work we also need to add the armor model. The armor model basically consists of two textures that are organized like a player skin. However, different armor parts only draw different parts of that texture.
Shoes, Helmet, and Chestplate use the `layer_1` texture (and have a lot more distance from the player skin itself) and Leggings use the `layer_2` texture.
If you want, you can use this example texture as a template. The different armor parts use different tones to try help differentiating them.

`assets/minecraft/models/armor/example_item_layer_1.png`:  
![An example armor layer 1 texture](example_item_layer_1.png)<br><a href="example_item_layer_1.png" target="_blank">example_item_layer_1.png</a>

`assets/minecraft/models/armor/example_item_layer_2.png`:  
![An example armor layer 2 texture](example_item_layer_2.png)<br><a href="example_item_layer_2.png" target="_blank">example_item_layer_2.png</a>

Replace use your armor material's name for instead of `example_item` in the path. It is important that the models are located in the `minecraft` namespace.

Now that you have added these, and reloaded your game, you should be able to view your fancy armor!

## Finishing It Up

The final step is to [add translation keys](../items/first-item#language-translation) for your armor's items. Once you've done that, you might also want to [add recipes for your armor](../data/adding-recipes).
