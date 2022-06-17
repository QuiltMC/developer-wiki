# Recipe API

A Quilt Standard Libraries module that aims to help to manipulate recipes.

It has three parts, `QuiltRecipeSerializer` (Which was briefly covered in the [Recipe Creation Tutorial](./recipes.md)),
recipe manager manipulation (Addition, Removal and Modification of recipes) and utilities.

## QuiltRecipeSerializer

An interface that extends `RecipeSerializer` to add JSON serialization.

Its primary use is the recipe dump, which allows for dumping of JSON representations of recipes as files into the game
directory (`./debug/quilt/recipe/...`) - It supports any recipes whose serializer implements `QuiltRecipeSerializer`,
vanilla recipes are supported by default.

Dumping can be enabled by setting the `quilt.recipe.dump` system property.

## Manipulation

### Addition

#### Static Recipes

Static recipes are a kind of recipe that is always kept present during the whole game lifecycle.
They're only applied after data pack reloads, that is, when starting a world/server or after using the `/reload` command.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Static-Recipe
```

#### Dynamic Recipes

Dynamic recipe providers can be used to register recipes that might have a runtime component - Something that cannot be
known at build time - for example, recipes that depend on other mods.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Adding
```

### Modification

Mods might want to modify values or replace recipes entirely while they're being built, this event allows for that.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Modification
```

### Removal

Mods might want to remove a recipe completely from the game; this event allows for that - Proceed with caution, this might
cause incompatibilities.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Removal
```
