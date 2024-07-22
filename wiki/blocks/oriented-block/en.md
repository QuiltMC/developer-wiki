---
title: Creating an Oriented Block
index: 0
---

# Creating an Oriented Block
Blocks such as Oak, Spruce and Birch logs are an example of an oriented block. The following example will show you how to make your own oriented block
with "Glowing Blackwood Log" being the example block.


First off, you will want to make two block model json like the following:
The X, Y, and Z are your blockstate's axis.

`src/main/resources/assets/yeefpineapple/blockstates/glowing_blackwood.json`:
```json
{
    "variants": {
        "axis=x": {
            "model": "yeefpineapple:block/glowing_blackwood",
            "x": 90,
            "y": 90
        },
        "axis=y": {
            "model": "yeefpineapple:block/glowing_blackwood"
        },
        "axis=z": {
            "model": "yeefpineapple:block/glowing_blackwood",
            "x": 90
        }
    }
}
```

Then you will need to create a class file for the block you're adding. In this example, the file will be called "GlowingBlackwood.java".

`src/main/com/example/yeefpineapple/blocks/GlowingBlackwood.java`:
```java
public class BeamBlock extends PillarBlock {
	// The following deals with block rotation
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return changeRotation(state, rotation);
	}
	
	public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
		return switch (rotation) {
			case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(AXIS)) {
				case X -> state.with(AXIS, Direction.Axis.Z);
				case Z -> state.with(AXIS, Direction.Axis.X);
				default -> state;
			};
			default -> state;
		};
	}
	
	// Deals with placing the block properly in accordance to direction.
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(AXIS, context.getSide().getAxis());
	}
}
```
