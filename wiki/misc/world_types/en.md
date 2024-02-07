---
title: Adding a Custom World Preset
index: 0
---

# Adding custom world presets
If your mod completely overhauls Minecraft's worldgen, you'll likely want to create a custom worldtype to offer users the option to use your worldgen or MC's vanilla worldgen.
Now, what is a custom worldtype you may ask? World types define world generation options for Minecraft worlds. They let you change the make-up of your Minecraft worlds by configuring how world generation behaves.

<br/>
<br/>

Here is where you specify which dimensions your world preset will apply to. It does require a `minecraft:overworld` dimension or your world preset will not work.
Valid dimensions are `minecraft:overworld`, `minecraft:nether` and `minecraft:the_end`.


`src/main/resources/data/minecraft/example_mod/worldgen/world_preset`
```json
{
	"dimensions": {
		"minecraft:overworld": {
			"type": "minecraft:overworld"
		}
	}
}
```

<br/>
<br/>

`generator` is where you specify how the worldgen in your world preset behaves. The `type` is a "generator ID" and the following are valid generator IDs:
```
noise
```

`biome_source` is where you specify how biomes generate. The `type` parameter here is what kind of biome generation will be used.
Valid generation types:
```
multi_noise
```

If the biome source `type` happens to be `minecraft:multi_noise`, Minecraft will use 3D biomes in the overworld and the nether.
The `preset` can be `minecraft:overworld` or `minecraft:nether`. More info about this can be found [here](https://minecraft.wiki/w/Custom_dimension#Multi-noise_biome_source_parameter_list).

`src/main/resources/data/minecraft/example_mod/worldgen/world_preset/example_mod_preset.json`
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

<br/>
<br/>

In order for your world preset to show in the "create a new world" screen, you will need to make a file called `normal.json` in `resources/data/minecraft/tags/worldgen/world_preset` and populate it with the following.

`src/main/resources/data/minecraft/tags/worldgen/world_preset/normal.json`
```json
{
	"values": [
		"example_mod:example_mod_preset"
	]
}
```

Alternatively, if you want your world preset to only show as an option while the user is holding down ALT, make a file called `extended.json` in the same directory as your `normal.json` file.
