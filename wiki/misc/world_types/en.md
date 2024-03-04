---
title: Adding a Custom World Preset
index: 0
---

# Adding custom world presets
If your mod completely overhauls Minecraft's worldgen, you'll likely want to create a custom worldtype to offer users the option to use your worldgen or MC's vanilla worldgen.
Now, what is a custom world type you may ask? *World types*, also called *world presets* define world generation options for Minecraft worlds. They let you change the make-up of your Minecraft worlds by configuring how world generation behaves.

Some examples of world types already built into MC are "Amplified", "Super Flat", "Single Biome", and "Large Biomes".

## Specifying which dimensions your worldtype applies to

The following block shows how to specify which dimensions your world preset will apply to. In `"dimension"`, a `"minecraft:overworld"` object is required.
The dimension `type` can be a built-in preset or a custom dimension preset you implement in your mod.


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

## Configuring how the worldgen works in your worldtype

`generator` is where you specify how the worldgen in your world preset behaves. The `type` is a "generator ID" and the following are valid generator IDs:
 - `noise`

In `biome_source`, you specify how biomes generate. The `type` parameter here is what kind of biome generation will be used.
Valid generation types:
 - `multi_noise` (Similar to overworld generation)
 - `fixed` (Like selecting "Single Biome" in world creation)
 - `the_end` (This is the generation the end dimension uses, not really applicable to what we're doing here)
 - `checkerboard` (Places biomes in a checkerboard style)

If the biome source `type` happens to be `minecraft:multi_noise`, Minecraft will use 3D biomes in the overworld and the nether.
The `preset` can be `minecraft:overworld` or `minecraft:nether`. More info about this can be found [here](https://minecraft.wiki/w/Custom_dimension#Multi-noise_biome_source_parameter_list).

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
This will generate a world relatively similar to that of MC's default overworld generation.

## Making your worldtype accessible

In order for your world preset to show in the "create a new world" screen, you will need to make a file called `normal.json` in `resources/data/minecraft/tags/worldgen/world_preset` and populate it with the following.

`src/main/resources/data/minecraft/tags/worldgen/world_preset/normal.json`:
```json
{
	"values": [
		"example_mod:example_mod_preset"
	]
}
```
This will make your world preset show up in the world creation screen.

Alternatively, if you want your world preset to only show as an option while the user is holding down ALT, make a file called `extended.json` in the same directory as your `normal.json` file.
