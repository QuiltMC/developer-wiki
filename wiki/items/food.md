---
title: Adding food
index: 2
---
# Adding food
This tutorial will assume that you already understand [Creating your first item](first-item)

Adding a `FoodComponent` to an item's settings makes that item food. A `FoodComponent` can be created using a `FoodComponent.Bulder`. Try reading the inline documentation (javadocs) of the builder to understand whats going on here, or continue reading for an explanation:

```java
public static final Item EXAMPLE_FOOD = new Item(new QuiltItemSettings().food(
	new FoodComponent.Builder()
		.hunger(2)
		.saturationModifier(1)
		.snack()
    	.alwaysEdible()
    	.meat()
		.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 10), 0.8f)
    .build()));
```
Now that you tried using the javadocs *cough, cough*, here is an alternate explanation:
1. We instantiate a new builder. Using this, we can configure the `FoodComponent` before finally turning it into a proper `FoodComponent`. Note that all of the following values are by default 0, false or empty.
2. We set the hunger to 2, which corresponds to one of the total 10 drumsticks in minecraft.
3. We set the saturation modifier, which, multiplied by the hunger value and 2, is added to the saturation.
4. We make it a snack, which means that it gets eaten quickly, just like dried kelp.
5. We make it always edible, just like suspicious stew.
6. We mark it as meat, so that wolves can eat it.
7. We add a Night Vision status effect which will be applied for 10 seconds with a 80% chance.
7. finally, we create a proper `FoodComponent` from the builder using the `build` method.

## What's next?
If you want to continue adding items, see [Armor](armor) or [Tools](tools).