---
title: Adding food
index: 2
---
# Adding food
This tutorial will assume that You have understood [Creating your first item](first-item)

An item can be made food by adding a `FoodComponent` to the Item Settings which you can create by using the Builder. Try reading the javadocs of the builderto understand whats going on here or continue reading for an explanation:

```java
public static final Item EXAMPLE_FOOD = new Item(new QuiltItemSettings().food(new FoodComponent.Builder()
    .hunger(2)
	.saturationModifier(1)
	.snack()
    .alwaysEdible()
    .meat()
	.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 10), 0.8f)
    .build()));
```
Now that you tried using the javadocs *cough, cough*, here is an alternate explanation:
- First, we instanciate a new builder. Using this, we can configure the `FoodComponent` before finally turning it into a proper `FoodComponent` using the build method. Note that all of the following values are by default 0, false or empty.
- Then we set the hunger to 2, which corresponds to one of the total 10 drumsticks in minecraft.
- Then we set the saturation modifier, which, multiplied by the hunger value and 2, is added to the saturation.
- Next we make it a snack, which means that it gets eaten quickly, just like dried kelp.
- After that we make it always edible, just like suspicious stew.
- Next we mark it as meat, so that wolves can eat it.
- Lastly we add a Night Vision status effect which will be applied for 10 seconds with a 80% chance.

## What's next?
If you want to continue adding items, add [armor](armor) or [tools](tools)