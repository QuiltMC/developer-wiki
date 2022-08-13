# Access Widener

Access Wideners are a system that allows developers to change the access modifiers of methods, fields, and classes from
Minecraft.

## Header

Every access widener must have a header which specifies the version of the format, and the target namespace.

```
accessWidener v1 named
```

## Access

Access wideners have 3 different access modifiers:

| Target |                                `accessible`                                |                 `extendable`                 |    `mutable`     |
|:------:|:--------------------------------------------------------------------------:|:--------------------------------------------:|:----------------:|
| Field  |                          Makes the field public.                           |                     N/A                      | Removes `final`. |
| Method | Makes the method public, and if it was private previously, makes it final. |   Removes `final` and turns it protected.    |       N/A        |
| Class  |                          Makes the class public.                           | Makes the class public, and removes `final`. |       N/A        |

## Format

- Class: `<modifier> class <class name>`
- Method: `<modifier> method <class name> <method name> <method descriptor>`
- Field: `<modifier> field <class name> <field name> <field descriptor>`

## Transitivity

Access Wideners usually cannot be seen by dependents, making them transitive allows for dependents to see in source the
changes made.

Marking some entry to be transitive is as simple as prefixing the modifier with `transitive-`.

This can be really useful for libraries to expose some detail without having to wrap around it.

## Setting Up

### `quilt.mod.json`

Set the `access_widener` field in the top-level object to the file path and name.

```json
{
  // ...
  "access_widener": "aw.accesswidener"
}
```

### Gradle

Set the `accessWidenerPath` property of the Loom extension.

```groovy
loom {
    accessWidenerPath = file("src/main/resources/my/path/to/aw.accesswidener")
}
```

## Example

```
accessWidener v2 named
# v2 added transitive entries

# Makes the BeeFollowTargetGoal inner class public.
accessible class net/minecraft/entity/passible/BeeEntity$BeeFollowTargetGoal

# Removes final from runDirectory
mutable field net/minecraft/client/MinecraftClient runDirectory Ljava/io/File;

# Removes final from getRecipeRemainder
extendable method net/minecraft/item/Item getRecipeRemainder ()Lnet/minecraft/item/Item;

# Makes the BeeLookControl inner class public for you and possible dependents
transitive-accessible class net/minecraft/entity/passible/BeeEntity$BeeLookControl
```
