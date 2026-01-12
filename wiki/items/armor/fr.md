# Ajouter une Armure

Ce tutoriel part du principe que vous savez déjà [Créer votre Premier Item](first-item).

## Créer un Materiaux d'Armure

Pour ajouter une armure, il faut commencer par créer un matériau d'armure.
Il défine les propriétés de base de votre armure, tel que sa durabilité,
sa valeur d'armure, sa robustesse...
Pour cela vous devez créer une nouvelle classe de matériau d'armure :

`src/main/com/example/example_mod/ExampleArmorMaterial`:

```java
public class ExampleArmorMaterial implements ArmorMaterial {
    //...
```

Changez le chemin avec les valeurs adaptées à votre mod, et nommez le matériau d'armure en fonction de sa ressource de base.
Par exemple, un matériau d'armure d'émeraudes s'appelerait `EmeraldArmorMaterial`.
Vous pouvez aussi utiliser un `enum` à la place d'une `class` si vous avez plusieurs matériaux d'armure, comme le fait Minecraft.

---

Continuons maintenant en ajoutant la durabilité et la valeur d'armure :

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

Ici, on utilise une astuce pratique : `slot.getEquipmentSlot().getEntitySlotId()` renvoie une valeur
comprise entre 0 et 3 en fonction de la partie de l'armure,
on peut donc simplement utiliser cela comme index pour notre array.

---

Ensuite nous définirons l'[enchentabilité](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability).
Cela définit la qualité des enchantements que cette armure pourra avoir dans une table d'enchantement.
Par exemple, les armures en netherite et en or ont une haute enchantabilité.

```java
@Override
public int getEnchantability() {
	return 10;
}
```

---

Nous allons maintenant définir le son d'équipement de l'armure.
Si vous voulez ajouter un son personnalisé, référez vous à [Ajouter des Sons](../misc/sounds).
Si vous voulez utiliser un son existant, vous pouvez utiliser `SoundEvents.ITEM_ARMOR_EQUIP_GENERIC`,
comme dans l'exemple ci-dessous.

```java
@Override
public SoundEvent getEquipSound() {
	return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
}
```

---

Ensuite vous devez spécifier l'ingrédient à utiliser pour réparer cette armure,
dans notre cas nous utiliserons l'item d'exemple créé dans le tutoriel [Créez votre Premier Item](first-item).

```java
@Override
public Ingredient getRepairIngredient() {
	return Ingredient.ofItems(ExampleMod.EXAMPLE_ITEM);
}
```

---

Enfin, nous devons spécifier le nom du matériau d'armure, sa robustesse et sa resistance au knockback.
Le nom du matériau devrait en général correspondre au nom de l'élément principal dont est constituée l'armure.
La robustesse protège particulièrement contre dégats élevés
et la resistance au knockback ne fonctionne que sur l'armure en netherite en vanilla,
mais est patchée pour fonctionner avec d'autres type d'armures par la [QSL](../concepts/qsl-qfapi).

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

## Ajouter les Items en eux-mêmes

Maintenant que nous avons déclaré le matériau d'armure, ajoutons les items de l'armure en eux-mêmes :

<!-- TODO: Note that there will be a link here in the MVP so a path is not specified currently -->

```java
public static final ArmorMaterial EXAMPLE_ARMOR_MATERIAL = new ExampleArmorMaterial();
public static final Item EXAMPLE_HELMET = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings());
public static final Item EXAMPLE_CHESTPLATE = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings());
public static final Item EXAMPLE_LEGGINGS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings());
public static final Item EXAMPLE_BOOTS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings());
```

D'abord on instancie le matériau d'armure et on le stocke dans une variable final static.
Ensuite on instancie les items pour pouvoir les enregistrer plus tard.
Notez que l'on créé un `ArmorItem` dans tous les cas et qu'ensuite on fournit simplement un `ArmorItem.ArmorSlot`
pour préciser l'emplacement d'armure à utiliser.
Enfin on passe les paramètres, la taille du stack devrait automatiquement être 1 pour les armures,
donc on passe les paramètres par défaut.

---

Ensuite, on enregistre l'armure dans notre `ModInitializer` et on l'ajoute dans le groupe d'item de combat.
Il y a deux choses à noter par rapport à ça :

1. On nomme les items d'armure par le nom du matériau avec leur emplacement d'armure.
2. On les ajoute au groupe d'items avec la méthode `addAfter`.
   Comme il s'agit d'une armure, on veut qu'elle apparaisse à côté des armures vanilla.
   Nous devons donc utiliser cette méthode qui permet d'ajouter un nombre variable d'items après un autre item spécifique déjà présent dans le groupe d'items.

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_helmet"), EXAMPLE_HELMET);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_chestplate"), EXAMPLE_CHESTPLATE);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_leggings"), EXAMPLE_LEGGINGS);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_boots"), EXAMPLE_BOOTS);
ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
	entries.addAfter(Items.NETHERITE_BOOTS, EXAMPLE_HELMET, EXAMPLE_CHESTPLATE, EXAMPLE_LEGGINGS, EXAMPLE_BOOTS);
});
```

## Ajouter des Textures à l'Armure

Maintenant que l'armure est enregistrée, les seules choses manquantes sont les textures et les traductions.
Pour les armures, vous devez fournir deux textures, Une pour les items en eux-mêmes et une pour le rendu de l'armure sur le modèle de lae joueur·euse.

<!-- TODO: the desectionization fails if nested like this. See svelte.config.json -->
<!-- ### Adding the texture to the Item-->

---

Pour ajouter la texture aux items, c'est la même chose que pour n'importe quel item :
Vous devez ajouter un modèle et une texture.
Par exemple, voici le fichier de modèle pour un casque :

```json
{
	"parent": "item/generated",
	"textures": {
		"layer0": "example_mod:item/example_item_helmet"
	}
}
```

Répétez cela pour tous les items de l'amure, en remplaçant `example_mod` et `example_item_helmet` avec l'identifiant de votre mod et le nom de l'item,
et ajouter les textures correspondantes.

<!-- TODO: the desectionization fails if nested like this. See svelte.config.json -->
<!-- ### Adding the texture for the player model -->

---

Maintenant l'armure devrait avoir l'air normale dans l'inventaire, mais quand vous la mettrez, vous vous rendrez compte qu'il manque les textures de l'armure quand on la porte.
Pour que cela marche il faut également ajouter le modèle d'armure.
Le modèle d'amure contient globalement deux textures organisées comme un skin de joueur·euse.
Cependant, différentes parties de l'armure n'utilisent que certaines parties de cette texture.
Les bottes, le casque et le plastron utilisent la texture `layer_1` (et ont beaucoup plus de distance entre eux et le skin de lae joueur·euse)
et le pantalon utilise la texture `layer_2`.
Si vous voulez, vous pouvez utiliser cette texture d'exemple comme un patron.
Les différentes parties d'armure utilisent différentes couleurs pour vous aider à les différencier.

`assets/minecraft/models/armor/example_item_layer_1.png`:  
![Texture layer 1 d'une armure d'exemple](/items/example_item_layer_1.png)<br><a href="/items/example_item_layer_1.png" target="_blank">example_item_layer_1.png</a>

`assets/minecraft/models/armor/example_item_layer_2.png`:  
![Texture layer 2 d'une armure d'exemple](/items/example_item_layer_2.png)<br><a href="/items/example_item_layer_2.png" target="_blank">example_item_layer_2.png</a>

Remplacez `example_item` par le nom de votre matériau dans le chemin du fichier.
Veuillez à ce que les modèles soient bien dans le namespace `minecraft`.

Après avoir ajouté celà, relancez le jeu et vous devriez pouvoir voir votre magnifique armure !

## Dernières Étapes

La dernière chose à faire est d'[ajouter la traduction](../items/first-item#les-traductions) pour les items de votre armure.
Après avoir fait ça, vous pouvez aussi vouloir [ajouter des recettes pour votre armure](../data/adding-recipes).
