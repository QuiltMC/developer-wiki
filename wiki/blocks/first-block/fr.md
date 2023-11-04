# Ajouter un Bloc Simple

Créer un bloc est assez similaire à [créer un objet](../items/first-item), mais en utilisant maintenant le registre des blocs _et_ des objets,
ainsi qu'un procecédé plus complexe pour la création d'un modèle.

## Créer et Enregistrer un Bloc

Nous commençons par créer le bloc et le stocker dans une variable :

```java
public static final Block EXAMPLE_BLOCK = new Block(QuiltBlockSettings.create());
```

Ensuite on l'enregistre dans la fonction `onInitialize()` :

```java
Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "example_block"), EXAMPLE_BLOCK);
```

Remplacez `example_block` avec le nom de votre bloc. Ecrivez tout en minuscule et séparez les mots avec des underscores (tirets du 8).

## Ajouter un Objet pour le Bloc

Après avoir fait ça, on peut placer le bloc dans Minecraft avec la command `setblock` mais, en ouvrant le menu du mode créatif,
on ne trouvera pas d'objet correspondant au bloc. Pour réparer ça, il faut enregistrer un `BlockItem` pour le bloc et l'ajouter à un groupe d'objets,
`BUILDING_BLOCKS` dans notre cas :

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_block"), new BlockItem(EXAMPLE_BLOCK, new QuiltItemSettings()))

ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
	entries.addItem(EXAMPLE_BLOCK.asItem());
});
```

Le nom de l'objet devrait être le même que celui du bloc.

Pour plus d'information sur ce que ça fait, voir l'article [Créez votre Premier Objet](../items/first-item#registering-the-item).

## Ajouter un Modèle pour le Bloc

D'abord il faut créer le fichier `blockstates` du bloc. Il y a un 'blockstate' (ou littéralement 'état de bloc') pour chaque forme que votre bloc peut prendre :
Par exemple, chaque état de croissance d'un bloc de plante est un 'blockstate' différent.
Le fichier `blockstate` lie simplement tous les différents 'blocstates' à leur modèle respectif.
Dans notre cas, le bloc a seulement un état, donc le fichier `blocstate` est assez simple.
Pour un exemple de 'blockstate' plus complexe, vous pouvez consulter [Ajouter des fonctionnalités Redstone à votre Bloc](redstone-interaction).
Dans notre cas vous pouvez simplement utiliser ce JSON :

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

Remplacez `simple_block_mod` et `example_block` avec l'ID de votre mod et le nom du bloc.
Assurez vous de les remplacez dans les noms des fichiers/dossier et dans le fichier JSON.

---

Maintenant on peut mettre le modèle au chemin spécifié par l'identifiant (dans notre cas `assets/simple_block_mod/models/block/example_block.json`).

```json
{
	"parent": "minecraft:block/cube_all",
	"textures": {
		"all": "simple_block_mod:block/example_block"
	}
}
```

Remplacez `simple_block_mod` et `example_block` comme à l'étape précédente.
Cela utilisera la texture située à l'adresse `assets/simple_block_mod/textures/block/example_block.png` pour tous les côtés du bloc.

---

L'objet correspondant à notre bloc a également besoin d'un modèle,
mais au lieu d'utiliser simplement une texture comme l'a fait pour le [tutoriel d'ajout d'objet](../items/first-item),
nous allons déclarer le modèle d'un bloc comme le parent du modèle de notre objet.
Cela donnera à l'objet une apparence identique à celle du bloc dans le jeu.

`assets/simple_block_mod/models/item/example_block.json`

```json
{
	"parent": "simple_block_mod:block/example_block"
}
```

Remplacez `simple_block_mod` et `example_block` comme à l'étape précédente.

## Ajouter une Traduction pour le Bloc

Dernier mais pas des moindres, il faut ajouter une traduction pour le bloc.
Cela sera aussi automatiquement utilisé pour l'objet du bloc.

`assets/simple_block_mod/lang/en_us.json`:

```json
{
	"block.simple_block_mod.example_block": "Example Block"
}
```

Remplacez `simple_block_mod` et `example_block` comme à l'étape précédente.

## Et Après ?

Maintenant que vous avez ajouté un bloc à Minecraft, vous pouvez continuer par [Ajouter un Bloc avec Orientation](oriented-block),
[Ajouter des fonctionnalités Redstone à votre Bloc](redstone-interaction) ou ajouter des objets avancés tels que
[une armure](../items/armor), [de la nourriture](../items/food) ou [des outils](../items/tools).
