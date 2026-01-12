# Créez votre Premier Item

<!-- This is migrated from the old wiki and modified to match 1.20, with some additions -->

Les items sont essentiels dans Minecraft, et presque tous les mods vont en utiliser.
Ce tutoriel va expliquer les étapes de base de création d'un item.

## Enregistrer l'Item

La première chose à faire est d'enregistrer l'item pour que le jeu sache qu'il faut l'ajouter.
Les [Registres](../concepts/registries) sont une partie très importante de Minecraft (et de son moddage).
Les blocs, les entités, les items, les sons, les particules... tous ces différents éléments passent par des registres.

---

Premièrement, il faut que nous mettions en place un endroit pour les items de notre mod. Créez une nouvelle classe nommée `ExampleModItems`, en renplaçant `ExampleMod` par le nom de votre mod, dans le même package que votre classe principale.
Cela nous aidera à rester organiser si jamais notre mod s'aggrandit et ajoute plus d'un item.

Ensuite, on va déclarer une instance de `net.minecraft.item.Item` avec les paramètres pour notre item.

En théorie, nous pourrions passer cette étape et déclarer l'item en même temps qu'on lenregistre,
mais l'avoir dans une variable séparée nous permet d'y faire référence à d'autres endroit pour d'autres usages.

`src/main/com/example/example_mod/ExampleModItems`:

```java
public class ExampleModItems {
	public static final Item EXAMPLE_ITEM = new Item(new QuiltItemSettings());
}
```

Ici, le `public static final` permet d'acceder à l'item ailleurs sans que l'on puisse changer la valeur de la variable en elle même,
ce qui permet d'être sûr qu'on ne la modifie pas accidentellement ailleurs.

Notre nouvelle instance d'`Item` prend une nouvelle instance de `QuiltItemSettings` comme argument.
Celle là que l'on déclare tous les paramètres de notre item.
Il y a toute une variété de paramètres, tels que la durabilité ou la taille des stacks,
mais dans notre cas nous utilisons simplement les paramètres par défaut.

---

Après avoir déclaré l'item, nous devons l'enregistrer pour l'inclure dans le jeu.
Nous allons mettre en place une méthode dans votre classe pour items responsable de l'enregistrement de tous vos items.
Vous pouvez voir que l'on prend le `ModContainer` du mod comme paramètre pour que l'on puisse l'utiliser pour récupérer l'ID du mod.

`src/main/com/example/example_mod/ExampleModItems`:

```java
public class ExampleModItems {
	// ...
	public static void register(ModContainer mod) {
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item"), EXAMPLE_ITEM);
	}
}
```

`Registry.register()` prend trois arguments :

- Le `Registry` dans lequel on veut ajouter un élément. Pour les items cela sera toujours `Registries.ITEM`.
- L'`Identifier` utilisé pour cet item. Il doit être unique. La première partie est le 'namespace' (qui devrait correspondre à l'identifiant du mod)
  suivi du nom de l'item en lui même. Seules les lettres minuscules, les chiffres, les underscores, les tirets, les points et les slashs sont autorisés.
- L'`Item` à enregistrer. Ici, on passe l'item déclaré plus tôt.

Enfin, nous devons nous assurer que la méthode `register()` est bien appelée pendant le démarrage du jeu.
On peut accomplir cela en appelant la méthode dans le `ModInitializer` du mod ([plus d'informations sur les mod initializers ici!](../concepts/sideness#les-mod-initializers)) dans la méthode `onInitialize`:

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

Une fois que vous avez fait tout ça, si on lance le jeu on peut voir que l'on peut se donner l'item avec la commande give : `/give @s simple_item_mod:example_item` !
Mais il n'apparait pas dans le menu du mode créatif, et il n'a pas de texture, et son nom n'a pas de traduction.
Comment réparer ça ?

## Ajouter l'Item à un Groupe

Les `ItemGroup`s, ou groupes d'items, représentent les onglets du menu du mode créatif.
À cause de changements faits en 1.19.3, vous ne pouvez plus ajouter d'items à des groupes d'items
en utilisant seulement les [Quilt Standard Libraries](../concepts/qsl-qfapi#les-quilt-standard-libraries)
(qu'on appelera désromais QSL). Cependant, la [Fabric API](../concepts/qsl-qfapi#la-fabric-api) a une API pour ça.
Grâce à la [Quilted Fabric API](../concepts/qsl-qfapi#la-quilted-fabric-api), qui est inclue dans le mod patron
et est téléchargée par les utilisateurs avec la QSL, on peut aussi l'utiliser avec Quilt :

`src/main/com/example/example_mod/ExampleModItems`:

```java
public class ExampleModItems {
	// ...
	public static void register(ModContainer mod) {
		// ...
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register (entries -> {
			entries.addItem(EXAMPLE_ITEM);
		});
	}
}
```

Ici on utilise l'API `ItemGroupEvents`. On récupère l'[événement](../concepts/events)
pour modifier le groupe d'item `INGREDIENTS` et on lui ajoute un nouveau 'listener'.
Les [événements](../concepts/events) ont deux utilitées principales.
On peut les utiliser pour déclencher du code quand quelque chose se passe, par exemple quand un bloc est cassé
ou même simplement à chaque tick.
D'un autre côté on peut aussi les utiliser pour modifier des choses comme les groupes d'items, quand l'ordre est important.
Dans notre cas cependant, nous ne faisons rien de compliqué et ajoutons simplement l'item à la fin du goupe d'items des ingrédients.
Cela l'ajoutera aussi à la recherhe du menu du mode créatif.

## Les Textures

Nous devons d'abord déclarer le modèle de l'item.
Cela indique au jeu comment faire le rendu de l'item.

`assets/simple_item_mod/models/item/example_item.json`:

```json
{
	"parent": "item/generated",
	"textures": {
		"layer0": "simple_item_mod:item/example_item"
	}
}
```

Pour la plupart des items, tout ce que vous avez besoin de faire ici est de remplacer `simple_item_mod`
par l'identifiant de votre mod et `example_item` par le nom de l'item que vous avez choisi plus tôt.
Assurez vous de les remplacer à la fois dans les noms de fichiers/dossiers et dans le fichier JSON.
Le fichier de texture, comme montré dans le modèle, devrait correspondre au chemin spécifié dans l'`Identifier`,
donc dans notre cas : `assets/simple_item_mod/textures/item/example_item.png`.

## Les Traductions

Enfin, on a besoin d'ajouter une traduction.
Mettez ceci dans `assets/simple_item_mod/lang/en_us.json`, en remplaçant l'identifiant du mod et le nom de l'item comme avant :

`assets/simple_item_mod/lang/en_us.json`;

```json
{
	"item.simple_item_mod.example_item": "Example Item"
}
```

Et voilà ! L'item devrait complètement fonctionner.

## Et Après ?

Ce tutorial ne couvre que le plus basique des items. Suivez les autres tutoriels pour des items plus avancés
ou essayez d'[Ajouter un Bloc Simple](../blocks/first-block).

Si vous voulez que votre item ait une recette, générez en une avec [le générateur de recette de destruc7i0n](https://crafting.thedestruc7i0n.ca/)
(vous pouvez ajouter les items de votre mod en cliquant sur le bouton "Add Item" au dessus de la liste d'ingrédients)
puis mettez le fichier JSON dans le dossier `resources/data/simple_item_mod/recipes/`
(remplacez `simple_item_mod` avec l'identifiant de votre mod).
Plus de détails sur les recettes peuvent être trouvés dans la [page dédiée aux recettes](../data/adding-recipes).
