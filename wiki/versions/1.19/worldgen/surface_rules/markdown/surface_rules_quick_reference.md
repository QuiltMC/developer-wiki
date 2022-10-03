# Surface Rules Quick Reference

This is a listing of all the vanilla surface rules and associated classes, listed in all 3 of the major mapping sets, along with a quick description of them.

For a more in depth guide to how they work, check out [The Forsaken Furby's Gist on Surface Rules](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#links), or [The Minecraft Wiki](https://minecraft.fandom.com/wiki/Custom_world_generation/noise_settings)!

### Base Classes

These are the base classes involved in surface rules. If you want to create custom conditions or other rules, look into these classes.

|            MojMap            |               QM               |               Yarn                | Datapack Identifier (prepended with `minecraft:` | Description                                                                                                                                                                                                                                                                                                                                                  |
|:----------------------------:|:------------------------------:|:---------------------------------:|:------------------------------------------------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|         SurfaceRules         |          SurfaceRules          |           MaterialRules           |                        -                         | The class that holds all the methods and classes found below.                                                                                                                                                                                                                                                                                                |
|    SurfaceRules.Condition    |     SurfaceRules.Condition     |   MaterialRules.BooleanSupplier   |                        -                         | An interface that provides one method - `test`. This method takes in a `Context`, and outputs a boolean - it is used for deciding if a SurfaceRule should be applied.                                                                                                                                                                                        |
|     SurfaceRules.Context     |      SurfaceRules.Context      | MaterialRules.MaterialRuleContext |                        -                         | A class used to provide block coordinates and similar information to certain inheritors of `Condition`. Those specific inheritors are inner classes of this class.                                                                                                                                                                                           |
| SurfaceRules.ConditionSource | SurfaceRules.MaterialCondition |  MaterialRules.MaterialCondition  |                        -                         | A Functional Interface that takes in a `Context` and outputs a `Condition`. Those `Condition`s are created in the `apply` method that `ConditionSource`s override. `ConditionSource` is used to give proper context, such as `NoiseCondtiionSource` making `NoiseCondition` have the correct noise and bounds in its `test` method.                          |
|   SurfaceRules.RuleSource    |    SurfaceRule.MaterialRule    |    MaterialRules.MaterialRule     |                        -                         | A Functional Interface that takes in a `Context` and outputs a `SurfaceRule`. Those `SurfaceRule`s are created in the `apply` that `MaterialRule`'s override. Similar to the `ConditionSource`, a `MaterialRule` can provide context/preprocessing, such as how the `SequenceMaterialRule` transforms each of the given `MaterialRule`s into `SurfaceRule`s. |
|   SurfaceRules.SurfaceRule   |    SurfaceRules.SurfaceRule    |   MaterialRules.BlockStateRule    |                        -                         | The base class for the actual rules - these all override the `tryApply` method (from `SurfaceRule`) that takes in block coordinates, and returns what blockstate should be placed, if any. It should be noted that this does not actually place blocks.                                                                                                      |

### Condition-Related Classes

These are the default classes for various types of `Condition`s. For brevity, only `ConditionSource`s will be mentioned - to see the associated `Condition`, look in the `apply` method of a given `ConditionSource`.

| MojMap (base class SurfaceRules) |       QM (base class SurfaceRules)       |  Yarn (base class MaterialRules)  | Datapack Identifier (prepended with `mineraft:` | Description |
|:--------------------------------:|:----------------------------------------:|:---------------------------------:|:-----------------------------------------------:|:-----------:|
|     AbovePreliminarySurface      | AbovePreliminarySurfaceMaterialCondition |     SurfaceMaterialCondition      |           `above_preliminary_surface`           |             |
|       BiomeConditionSource       |          BiomeMaterialCondition          |      BiomeMaterialCondition       |                     `biome`                     |             |
|               Hole               |          HoleMaterialCondition           |       HoleMaterialCondition       |                     `hole`                      |             |
|  NoiseThresholdConditionSource   |     NoiseThresholdMaterialCondition      |  NoiseThresholdMaterialCondition  |                `noise_threshold`                |             |
|        NotConditionSource        |           NotMaterialCondition           |       NotMaterialCondition        |                      `not`                      |             |
|              Steep               |          SteepMaterialCondition          |      SteepMaterialCondition       |                     `steep`                     |             |
|         StoneDepthCheck          |       StoneDepthMaterialCondition        |    StoneDepthMaterialCondition    |                  `stone_depth`                  |             |
|           Temperature            |       TemperatureMaterialCondition       |   TemperatureMaterialCondition    |                  `temperature`                  |             |
| VerticalGradientConditionSource  |    VerticalGradientMaterialCondition     | VerticalGradientMaterialCondition |               `vertical_gradient`               |             |
|       WaterConditionSource       |          WaterMaterialCondition          |      WaterMaterialCondition       |                     `water`                     |             |
|         YConditionSource         |         AboveYMaterialCondition          |      AboveYMaterialCondition      |                    `y_above`                    |             |


