---
title: Creating an Oriented Block
index: 0
---

# Creating an Oriented Block
Blocks such as Oak, Spruce and Birch logs are an example of an oriented block. The following example will show you how to make your own oriented block
with "Glowing Blackwood Log" being the example block.


First off, you will want to make a blockstate json like the following:
The X, Y, and Z are your blockstate's axis.

`src/main/resources/assets/yeefpineapple/blockstates/glowing_blackwood.json`:
```json
{
    "variants": {
        "axis=x": {
            "model": "yeefpineapple:block/glowing_blackwood_horizontal",
            "x": 90,
            "y": 90
        },
        "axis=y": {
            "model": "yeefpineapple:block/glowing_blackwood"
        },
        "axis=z": {
            "model": "yeefpineapple:block/glowing_blackwood_horizontal",
            "x": 90
        }
    }
}
```

Then you will need to create a class file for the block you're adding. In this example, the file will be called "GlowingBlackwood.java".

`src/main/com/example/yeefpineapple/blocks/YeefPineapple.java`:
```java
    public static final PillarBlock GLOWING_BLACKWOOD = new PillarBlock(QuiltBlockSettings.create());
```
