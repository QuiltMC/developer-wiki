# Adding custom world types

If your mod completely overhauls Minecraft's worldgen, you'll likely want to create a custom worldtype to offer users the option to use your worldgen or vanilla Minecraft's world generation.
Now, what is a custom world type you may ask? _World types_, also called _world presets_, define world generation options for Minecraft worlds. They let you change the make-up of your Minecraft worlds by configuring how world generation behaves.

Some examples of world types already built into Minecraft are "Amplified", "Super Flat", "Single Biome", and "Large Biomes".

## Specifying which dimensions your world type applies to

The following block shows how to specify which dimensions your world type will apply to. In `dimensions`, a `minecraft:overworld` object is required.
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

`generator` is where you specify how the worldgen in your world type behaves. The `type` is a "generator ID" and the following are valid generator IDs:

- `noise`
- `flat`: Creates a superflat world (not really applicable here)
- `debug`: Creates a debug world (not really applicable here)

In `biome_source`, you specify how biomes generate. The `type` parameter here is what kind of biome generation will be used.
Valid generation types:

- `multi_noise`: Similar to overworld generation
- `fixed`: Like selecting "Single Biome" in world creation
- `the_end`: This is the generation the end dimension uses (not really applicable here)
- `checkerboard`: Places biomes in a checkerboard style

If the biome source `type` is `minecraft:multi_noise`, Minecraft will use 3D biomes in the overworld and the nether.
The `preset` can be `minecraft:overworld` or `minecraft:nether`. More info about this can be found [in the article about custom dimensions in the Minecraft wiki](https://minecraft.wiki/w/Custom_dimension#Multi-noise_biome_source_parameter_list).

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

This will generate a world relatively similar to that of 's default overworld generation.

On a dedicated server, you navigate to your `server.properties` file and find the `level-type` field. By default, the value is `minecraft:normal`. Changing this to the ID for your world type (`example_mod:example_mod_preset`) will use that instead.

## Making your worldtype accessible

In order for your world type to show in the "create a new world" screen, you will need to make a file called `normal.json` in `resources/data/minecraft/tags/worldgen/world_preset` and populate it with the following.

`src/main/resources/data/minecraft/tags/worldgen/world_preset/normal.json`:

```json
{
	"values": ["example_mod:example_mod_preset"]
}
```

This will make your world type show up in the world creation screen.

Alternatively, if you want your world type to only show as an option while the user is holding down ALT, make a file called `extended.json` in the same directory as your `normal.json` file.
