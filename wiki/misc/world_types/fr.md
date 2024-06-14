# Ajouter des types de monde personnalisés

Si votre mod change complétement la génération de monde de Minecraft, vous voudrez surement créer un type de monde
personnalisé pour offrir à vos utilisateurs l'option d'utiliser votre worldgen ou la génération
de monde de Minecraft vanilla.
Mais, c'est quoi un type de monde personnalisé ? _Les types de monde_, ou _presets de monde_, définissent les options
de génération des monde Minecraft. Ils vous laisse changer la composition de vos mondes Minecraft
en configurant comment se comporte la génération de monde.

Des exemples de type de monde inclut dans Minecraft sont "Amplified", "Super Flat", "Single Biome" et "Large Biomes".

## Spécifier à quelles dimensions votre type de monde s'applique

Le block qui suit montre comment spécifier à quelles dimensions votre type de monde s'applique.
Dans `dimensions`, un objet `minecraft:overworld` est nécessaire.
Le `type` de dimension peut être un preset built-in ou une preset de dimension personnalisé
que vous définissez dans votre mod.

`src/main/resources/data/minecraft/example_mod/worldgen/world_preset`:

```json
{
	"dimensions": {
		"minecraft:overworld": {
			"type": "example_mod:example_mod_dimension_type"
		}
	}
}
```

## Configurer comment le génération fonction dans votre type de monde

`generator` est l'endroit où vous spécifiez comment la génération se comporte dans votre monde.
Le `type` est un "generator ID", dont voici une liste :

- `noise`
- `flat`: Créer un monde superflat (ne s'applique pas vraiment ici)
- `debug`: Créer une monde de debug (ne s'applique pas vraiment ici)

Dans `biome_source`, vous spécifiez comment les biomes se générents.
Le paramètre `type` ici correspond à quel type de génération de biome sera utilisé.
Liste des types de génération :

- `multi_noise`: Similaire à la génération de l'overworld
- `fixed`: Comme le world type "Single Biome"
- `the_end`: La génération de la dimension de l'end (ne s'applique pas vraiment ici)
- `checkerboard`: Place les biomes en damier

Si le `type` du biome source est `minecraft:multi_noise`, Minecraft utilisera des biomes 3D dans l'overworld et le nether.
Le `preset` peut être `minecraft:overworld` ou `minecraft:nether`.
Plus d'infos là dessus peut être trouvée dans [l'article sur les dimensions personnalisés dans le wiki de Minecraft](https://minecraft.wiki/w/Custom_dimension#Multi-noise_biome_source_parameter_list) (pas disponible en français à l'écriture de cet article).

`src/main/resources/data/minecraft/example_mod/worldgen/world_preset/example_mod_preset.json`:

```json
{
	"dimensions": {
		"minecraft:overworld": {
			"type": "minecraft:overworld",
			"generator": {
				"type": "minecraft:noise",
				"biome_source": {
					"type": "minecraft:multi_noise",
					"preset": "minecraft:overworld"
				}
			}
		}
	}
}
```

Cela générera un monde relativement similaire à la génération par défaut de l'overworld.

Sur un serveur dédié, vous pouvez ouvrir votre fichier `server.properties` et trouver le champ `level-type`.
Par défault, la valeur est `minecraft:normal`. Remplacer ça par l'ID de votre type de monde
(`example_mod:example_mod_preset`) permettra d'utiliser votre génération à la place.

## Rendre votre type de monde accessible

Pour que votre type de monde soit visible dans l'écran de création de monde, vous devrez créer un fichier
nommé `normal.json` dans `resources/data/minecraft/tags/worldgen/world_preset` et le remplir avec ce qui suit.

`src/main/resources/data/minecraft/tags/worldgen/world_preset/normal.json`:

```json
{
	"values": ["example_mod:example_mod_preset"]
}
```

Cela rendra votre type de monde disponible dans l'écran de création de monde.

Sinon, si vous voulez que votre type de monde ne soit disponible que quand l'utilisateur maintient la touche ALT appuyée,
créez un fichier `extended.json` dans le même dossier que votre fichier `normal.json`.
