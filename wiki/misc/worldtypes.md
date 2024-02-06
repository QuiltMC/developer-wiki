```json
"dimensions": {
  "minecraft:overworld": {
    "type": "minecraft:overworld"
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
