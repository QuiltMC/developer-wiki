# Recipes

Recipes are a data-driven Minecraft system that allows for matching between inventories and item inputs, resulting in outputs.

## Concepts

- `Recipe` - Specifies the behavior and logic of a recipe - its inputs, outputs and matching.
- `RecipeType` - Represents a group of recipes.
- `RecipeSerializer` - Deserialization of JSON files to a recipe object, and serialization and deserialization of a recipe to a packet.

## Creating a simple recipe

To start to create a recipe, create a class that implements the `Recipe` interface, the generic type of it represents
the kind of `Inventory` where this recipe will work.
<!--- TODO: Refer to Inventory/BlockEntity tutorial -->

The conventional way of specifying inputs and output, is to utilize `Ingredient` and `ItemStack` respectively -
`Ingredient` is a generalized object that can match against stacks being backed by either a stack or tags. <!--- TODO: Link to Tags page -->

You should also store the recipe identifier for identification and filling values.

```file:src/main/java/org/quiltmc/wiki/recipes/MyRecipe.java@Starting
```

### Matching

The method `matches` returns whether this current recipe should be matched when retrieved via the `RecipeManager` -
Match the inventory contents against the inputs here.

```file:src/main/java/org/quiltmc/wiki/recipes/MyRecipe.java@Match
```

### Outputs

Outputting has two parts, `getOutput` and `craft`, the first being a view of the output and the latter being a copy of
the output that may be modified.

```file:src/main/java/org/quiltmc/wiki/recipes/MyRecipe.java@Output
```

### Serializer

To start to create a serializer, create a class that implements `QuiltRecipeSerializer` (*An extended version of
`RecipeSerializer` that allows for serialization of recipe objects into JSON*).

#### JSON

In `read` you must read the JSON object and convert it into a new recipe object of your type.

In `toJson` you must convert the recipe object into a valid equivalent JSON object - You can check if it's being
serialized correctly via dumping the recipes with the `quilt.recipe.dump` property (`-Dquilt.recipe.dump=true` in the VM options)
and checking the outputted recipes.

```file:src/main/java/org/quiltmc/wiki/recipes/MyRecipe.java@Serializer-JSON
```

#### Packet

You must create and read recipe packets, so the server is able to send the recipes to the clients, and be converted correctly,
failing to implement the packet reading and writing might result in desyncs.

It is imperative that you read values in the same order that you wrote them.

```file:src/main/java/org/quiltmc/wiki/recipes/MyRecipe.java@Serializer-Packet
```

### Recipe Type

Creating a recipe type is as simple as instantiating `RecipeType`, unfortunately it is an interface, so you must subclass it.
```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@RecipeType-Instance
```

### Registration

You must register your serializer and your type, so Minecraft knows about them.
```file:src/main/java/org/quiltmc/wiki/recipes/Recipes.java@Registration
```

Additionally, you also have to specify them in your recipe class.
```file:src/main/java/org/quiltmc/wiki/recipes/MyRecipe.java@Serializer/Type
```

### Using your recipe

#### Creating recipe JSON files

Now that you have created and registered your recipe, it's time to make a few recipes.

Recipes should be located inside `resources/data/[namespace]/recipes`; its id will be composed of the namespace to which it was added,
and the file path with its file name.

All recipes have different structures, the only common denominator between all of them is the field `"type"`, which
specifies the identifier of the recipe type - so in our case, `"example:my_recipe"`; The remaining values
are dependent on the serializer.

For instance, a recipe of ours that inputs an iron ingot and outputs two apples would be:
```file:src/main/resources/data/recipes/recipes/fun.json
```

#### Retrieving your recipes

Recipes must be retrieved from the `RecipeManager`, which can be fetched from either a `World` or the `MinecraftServer`.
The recipe manager has a few methods for recipe retrieving, such as:

- `getFirstMatch` - Gets the first recipe of a type that returned `true` for `matches`.
- `listAllOfType` - Gets all recipes from a type.
- `getAllMatches` - Gets all recipes of a type that returned `true` for `matches`.

The methods that get matching recipes require you to pass an inventory that matches the specified in the recipe generic.
