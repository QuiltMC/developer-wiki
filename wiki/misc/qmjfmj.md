---
title: Differences between the QMJ and the FMJ
---

The `quilt.mod.json` (QMJ) has quite a few differences compared to the fabric.mod.json (FMJ). Lets go through them all.

Here; you have a `"quilt_loader"` key whereas in the FMJ, its straight to the modid.
```json
{
	"schema_version": 1,
	"quilt_loader": {
		"group": "com.example",
		"id": "example_mod",
		"version": "${version}",
		"metadata": {
			"name": "Mod Name",
			"description": "A short description of your mod.",
			"contributors": {
				"Your name here": "Owner"
			}
		}
	}
}
```

```json
{
	"schemaVersion": 1,
	"id": "modid",
	"version": "${version}",
	"name": "Example mod",
	"description": "This is an example description! Tell everyone what your mod is about!",
	"authors": [
		"Me!"
	]
}
```

The `"contact"` section is fairly similar but in the QMJ, you have an `"issues"` key.
```json
"contact": {
		"homepage": "https://example.com/",
		"issues": "https://github.com/QuiltMC/quilt-template-mod/issues",
		"sources": "https://github.com/QuiltMC/quilt-template-mod"
	}
```

```json
"contact": {
		"homepage": "https://fabricmc.net/",
		"sources": "https://github.com/FabricMC/fabric-example-mod"
	}
```

The `"license"` key is not present in the QMJ.
```json
"icon": "assets/example_mod/icon.png"
```

```json
"license": "CC0-1.0",
"icon": "assets/modid/icon.png",
```

The `"intermediate_mappings"` key is present in the QMJ but not in the FMJ.

Entrypoints are fairly similar but there are some differences.

```json
"entrypoints": {
			"init": "com.example.example_mod.ExampleMod"
		}
```

```json
"entrypoints": {
		"main": [
			"com.example.ExampleMod"
		],
		"client": [
			"com.example.ExampleModClient"
		]
	}
```

In the FMJ, `"mixins"` shows up before `"depends"`.

```json
"mixins": [
		"modid.mixins.json",
		{
			"config": "modid.client.mixins.json",
			"environment": "client"
		}
	],
	"depends": {
		"fabricloader": ">=0.15.0",
		"minecraft": "~1.20.4",
		"java": ">=17",
		"fabric-api": "*"
	},
	"suggests": {
		"another-mod": "*"
	}
```

...whereas in the QMJ, `"mixins"` are after `"depends"`.

```json
{
"depends": [
			{
				"id": "quilt_loader",
				"versions": ">=0.19.1"
			},
			{
				"id": "quilted_fabric_api",
				"versions": ">=7.0.2"
			},
			{
				"id": "minecraft",
				"versions": ">=1.20"
			}
		]
	},
	"mixin": "example_mod.mixins.json"
```
