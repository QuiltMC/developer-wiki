# Entity Attributes

Attributes describe the preset yet fluctuating properties of an entity. Every living entity shares a few attributes, such as movement speed, maximum health, and attack damage, and some entities have attributes specific to their type, like a player entity's attack speed.

In this guide, we will create a Jump Boost attribute that controls the jump height of an entity.

## Creating an Attribute

To create an attribute, we'll construct a `ClampedEntityAttribute`. This extends the base `EntityAttribute` and allows min and max values for its modifiers, which is useful if this a certain range of values that make sense. All vanilla attributes are clamped.

Additionally, all numerical values in the constructor are doubles. This doesn't make sense for some attributes, but as long as you have a whole number, it will display as a regular integer; for example, in an item's tooltip.

The constructor takes:
- A translation key
- A 'fallback' value: the base value before any external attribute modifiers are applied
- A min and max value, clamping the **end result** of the modifier calculations (after all modifiers are applied)

```java
public static final EntityAttribute GENERIC_JUMP_BOOST = new ClampedEntityAttribute("attribute.name.generic_jump_boost", 1.0, 0.0, 2.0).setTracked(true);
```

The `generic` preceding the attribute name is a convention meaning 'applying to multiple living entities (and players).' Specific attributes get their own prefixes, such as the case for `horse` in `horse_jump_strength`.

You might notice the `setTracked` call; this ensures that the value of this attribute will be synchronized between the server and client. Since we're dealing with player movement, it's important to have this set to `true`, which is what you'll want in most cases regardless.

At this point, we might want to add a language entry:

```json
{
  "attribute.name.generic_jump_boost": "Jump Boost"
}
```

With our `EntityAttribute` instance created, we can go ahead and register it:

```java
Registry.register(Registry.ATTRIBUTE, new Identifier("example", "generic.jump_boost"), GENERIC_JUMP_BOOST);
```

This identifier doesn't follow normal conventions, but it's what the vanilla attributes use.

## Registering Attributes on Entities

The next step is letting certain entities know that they accept our new attribute. Let's walk through how entities do this in the first place.

### How Entities Know Their Attributes

1. Each entity class has some sort of static attribute creation method returning a `DefaultAttributeContainer.Builder`. Here's what the one in `LivingEntity` looks like:

```java
public static DefaultAttributeContainer.Builder createLivingAttributes() {
    return DefaultAttributeContainer.builder()
        .add(EntityAttributes.GENERIC_MAX_HEALTH)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        .add(EntityAttributes.GENERIC_ARMOR)
        .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
}
```

This builder is still mutable at this point, so other entities call `LivingEntity.createLivingAttributes` and build off of it when defining their own accepted attributes.

2. This method gets called in the `DefaultAttributeRegistry` class when creating its namesake map (`DEFAULT_ATTRIBUTE_REGISTRY`). This maps entity types to their corresponding `DefaultAttributeContainers`, now built and immutable. This map is immutable *and* private, but the class has wrappers for `get` and `containsKey`.

> When creating your own entity classes, you can use `FabricDefaultAttributeRegistry.register` to take care of this without your own mixins.

3. During play, the `LivingEntity` constructor initializes the `attributes` field as a new `AttributeContainer` instance based on the result of the getter in `DefaultAttributeRegistry` mentioned prior. This is the value returned in the `LivingEntity#getAttributes` getter.

4. When *using* an attribute's value stored in an entity, you need to retrieve its corresponding `EntityAttributeInstance`; we'll go over this in more detail later. For now, all you should know is that **if the entity doesn't have an attribute in its `attributes` field, it won't have that attribute instance**, and you won't be able to apply a modifier.

### Mixing Into Attribute Creation

Each entity that does something different with attributes has their own attribute creation method, which you can find in `DefaultAttributeRegistry`. Since we want our attribute on all entities, we'll mix into the `LivingEntity.createLivingAttributes` method shown in the previous section.

Remember, at this point, the builder is still immutable - and a reference - so our mixin is very straightforward:

```java

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "createLivingAttributes", at = @At(value = "RETURN"))
    private static void addCustomAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(ExampleMod.GENERIC_JUMP_BOOST);
    }
}
```

## Using Attributes

We've registered our attribute and let all living entities know that they have it. The next step is to actually use it somewhere: in our case, to modify jump height.

`LivingEntity#getAttributes` gets the *possible* attributes that the entity knows it has, but not the actual values. Each attribute has its own 'instance' on an entity known as an `EntityAttributeInstance`, which has all the logic for controlling attribute values. You can use the `LivingEntity#getAttributeInstance` method for getting an instance.

We'll need another mixin into `LivingEntity`, this time for the `getJumpVelocity` method. Since our initial fallback value is `1.0`, we can feel free to just multiply the end result by the attribute value. Here's what that looks like:

```java
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    // ...

    @Shadow public @Nullable abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Inject(method = "getJumpVelocity", at = @At(value = "RETURN"), cancellable = true)
    private void applyJumpBoost(CallbackInfoReturnable<Float> cir) {
        EntityAttributeInstance instance = getAttributeInstance(ExampleMod.GENERIC_JUMP_BOOST);
        cir.setReturnValue(cir.getReturnValue() * (float) instance.getValue());
    }
}
```

## Attribute Modifiers

Now that we've given our attribute functionality, it's time to modify its value in some way. The next section will discuss three methods you might use to do so, but they all have one thing in common: `EntityAttributeModifier`.

Attribute modifiers are completely disconnected from any particular attribute and are only concerned with numbers, operations, and identities. The constructor takes:
- A UUID to uniquely identify this modifier... yes, not even an `Identifier`, a UUID that you need to hard-code or generate randomly if you don't care about identifying the modifier afterwards
  - Modifiers with the same UUIDs will simply overwrite each other on the same attribute instance. You can see how this is all handled in `LivingEntity#getEquipment`.
- A string representing the modifier's 'name' - only used for NBT and when `StatusEffect`s link to attributes, notably showing up in potion tooltips
- A double value and an `EntityAttributeModifier.OPERATION` that specifies how the modifier should be applied and by what magnitude

Here's an example:

```java
public static UUID SOME_MODIFIER_ID = UUID.fromString("de17612a-adec-11ec-b909-0242ac120002");

public static EntityAttributeModifier SOME_MODIFIER = new EntityAttributeModifier(
    SOME_MODIFIER_ID,
    "Jump stick modifier",
    0.5,
    EntityAttributeModifier.ADDITION
);
```

It's important to note that all attribute modifiers are calculated in order based on type, with the order being `ADDITION`, `MULTIPLY_BASE`, then `MULTIPLY_TOTAL`. You can see the exact behavior in `EntityAttributeInstance#computeValue`.

## Applying Modifiers

The final step is to apply this modifier. These examples use the `SOME_MODIFIER` field created in the previous section.

### Via Code

It's as simple as getting the attribute instance and calling `addTemporaryModifier` (or `addPersistentModifier` if you want it to be serialized in the instance's NBT).

```java
EntityAttributeInstance instance = entity.getAttributeInstance(ExampleMod.GENERIC_JUMP_BOOST);
instance.addTemporaryModifier(SOME_MODIFIER);
```

This is also the most appropriate case where you would remove the modifier at some other point in time. You can either use the UUID associated with the modifier or the modifier itself.

```java
// Either of these would work:
instance.removeModifier(SOME_MODIFIER_ID);
instance.removeModifier(SOME_MODIFIER);
```

### Via Items

Upon equipping an item, the game checks if it has any attribute modifiers marked in the slot it was equipped in. If so, it applies those modifiers to the entity. There are two ways items can achieve this: through `ItemStack` NBT or custom `Item` instance behavior.

#### NBT

You can use the `ItemStack#addAttributeModifier` method to apply a modifier via NBT.

```java
ItemStack stack = new ItemStack(Items.STICK);
stack.addAttributeModifier(
    ExampleMod.GENERIC_JUMP_BOOST,
    SOME_MODIFIER,
    EquipmentSlot.MAINHAND
);
```

#### `getAttributeModifiers`

The `Item` class contains a `getAttributeModifiers` method which returns a multimap of modifiers to apply when the item is equipped in the specified slot. We can override this in our own item class like so:

```java
public class JumpStick extends Item {

    public JumpStick(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        // If held in offhand, return our modifier, otherwise nothing
        if (slot == EquipmentSlot.MAINHAND) {
            return ImmutableMultimap.builder()
                .put(ExampleMod.GENERIC_JUMP_BOOST, SOME_MODIFIER)
                .build();
        }
        return super.getAttributeModifiers(slot);
    }
}
```

#### Conflicts

By default, NBT and hard-coded modifiers don't mix. If you've ever applied a modifier via NBT on a sword, for example, you might have noticed that your modifier gets applied, but the item suddenly doesn't have any attack damage or speed. You can see why in `ItemStack#getAttributeModifiers`: it straight up ignores any hard-coded ones like in the previous example if the stack has any modifiers in its NBT.

[Here's a mod that fixes this](https://modrinth.com/mod/cmods). It's basically a one-line mixin, but, hey, someone had to do it.