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

```tabbed-files
java:java/src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Static-Recipe
kotlin:kotlin/src/main/kotlin/org/quiltmc/wiki/recipes/Recipes.kt@Static-Recipe
scala:scala/src/main/scala/org/quiltmc/wiki/recipes/Recipes.scala@Static-Recipe
groovy:groovy/src/main/groovy/org/quiltmc/wiki/recipes/Recipes.groovy@Static-Recipe
```

### Addition

Mods might want to add recipes that have runtime components - Something that cannot be known at build time - for example,
recipes that depend on other mods. Dynamic recipe providers allow for that.

```tabbed-files
java:java/src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Adding
kotlin:kotlin/src/main/kotlin/org/quiltmc/wiki/recipes/Recipes.kt@Adding
scala:scala/src/main/scala/org/quiltmc/wiki/recipes/Recipes.scala@Adding
groovy:groovy/src/main/groovy/org/quiltmc/wiki/recipes/Recipes.groovy@Adding
```

### Modification

Mods might want to modify values or replace recipes entirely while they're being built, this event allows for that.

```tabbed-files
java:java/src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Modification
kotlin:kotlin/src/main/kotlin/org/quiltmc/wiki/recipes/Recipes.kt@Modification
scala:scala/src/main/scala/org/quiltmc/wiki/recipes/Recipes.scala@Modification
groovy:groovy/src/main/groovy/org/quiltmc/wiki/recipes/Recipes.groovy@Modification
```

### Removal

Mods might want to remove a recipe completely from the game, this event allows for that. Be aware that removing recipes
might not be entirely compatible with what other mods expect.

```tabbed-files
java:java/src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Removal
kotlin:kotlin/src/main/kotlin/org/quiltmc/wiki/recipes/Recipes.kt@Removal
scala:scala/src/main/scala/org/quiltmc/wiki/recipes/Recipes.scala@Removal
groovy:groovy/src/main/groovy/org/quiltmc/wiki/recipes/Recipes.groovy@Removal
```
