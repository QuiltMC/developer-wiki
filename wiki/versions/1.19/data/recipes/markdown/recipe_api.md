# Recipe API

A Quilt Standard Libraries module that aims to help to manipulate recipes.

It has three parts, `QuiltRecipeSerializer` (Which was briefly covered in the [Recipe Type Tutorial](./recipe_type.md)),
recipe manager manipulation, and utilities.

## QuiltRecipeSerializer

An interface that extends `RecipeSerializer` to add to-JSON serialization.

Its primary use is the recipe dump, which allows for dumping of JSON representations of recipes as files into the game
directory (`./debug/quilt/recipe/...`) - It supports any recipes whose serializer implements `QuiltRecipeSerializer`,
vanilla recipes are supported by default.

Dumping can be enabled with the `quilt.recipe.dump` system property.

## Manipulation

The recipe manager manipulation is a grouping of systems that can alter the state of the recipe manager and its recipes,
they can be accessed using the `RecipeManagerHelper` class.

The events contained in this system have a set order, which is addition, modification, and removal respectively.

### Static Recipes

Static recipes are a kind of recipe that is always kept present during the whole game lifecycle.
They're only applied after data pack reloads, that is, when starting a world/server or after using the `/reload` command.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Static-Recipe
```

### Addition

Mods might want to add recipes that have runtime components - Something that cannot be known at build time - for example,
recipes that depend on other mods. Dynamic recipe providers allow for that.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Adding
```

### Modification

Mods might want to modify values or replace recipes entirely while they're being built, this event allows for that.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Modification
```

### Removal

Mods might want to remove a recipe completely from the game, this event allows for that. Be aware that removing recipes
might not be entirely compatible with what other mods expect.

```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Removal
```
