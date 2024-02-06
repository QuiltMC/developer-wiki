---
title: Adding a Custom World Preset
---

# Adding custom world presets
If your mod completely overhauls Minecraft's worldgen, you'll want to create a custom worldtype so you give people the option to use your worldgen or MC's vanilla worldgen.
Now, what is a custom worldtype you may ask? World types define world generation options for Minecraft worlds. They let you change the make-up of your Minecraft worlds by configuring how world generation behaves.

```json
"dimensions": {
  "minecraft:overworld": {
    "type": "minecraft:overworld"
    }
  }
```

```json
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
```

```json
"dimensions": {
  "minecraft:overworld": {
    "type": "minecraft:overworld",
    "generator": {
      "type": "minecraft:noise",
      "biome_source": {
        "type": "minecraft:multi_noise",
        "preset": "minecraft:overworld"
      },
      "settings": "example_mod:example_mod_noise"
    }
  }
}
```
