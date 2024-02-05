# Getting started with Quilt Config

[Quilt Config](https://github.com/QuiltMC/quilt-config) is Quilt's advanced configuration API. It allows developers (that's you!) to easily set up values that can be changed by the user in order to change the behaviour of your mod. It's built into [Quilt Loader](https://github.com/QuiltMC/quilt-loader) to allow any Quilt mod to leverage it without having to pull in new dependencies or run any setup.

## Setting up your config

In this tutorial, we're going to be building a simple mod that prints a line of text to the console when it's initialized. Keep in mind that this is an example, and your mod can use config values anywhere you want! You can view of the final source code of this tutorial [on GitHub](https://github.com/ix0rai/quilt-config-example-mod).
<!-- TODO: Add code project into wiki -->
To begin with Quilt Config, you'll need a dedicated class for holding your config. For an extremely configurable mod, you may want to break up your config into multiple files inside a `config` package, but for this tutorial and most mods we're going to put everything in one file.

Let's create a new file for our config, in the same directory as our mod initializer. We'll call it `ExampleModConfig`, but you can call it whatever you want. This new config class will extend the `ReflectiveConfig` API, which will provide everything we need to get the config working.

We'll start with this code:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    public static final ExampleModConfig INSTANCE = QuiltConfig.create(ExampleMod.MOD_ID, ExampleMod.MOD_ID, ExampleModConfig.class);
}
```

Now that big line in the middle may seem intimidating, but we're going to break it down.
- First, `public static final` means that the value never changes (though data *stored* in the config can), and `instance` is a fancy programmer word that just means it's an object of a class.
- Second, we're calling the method `QuiltConfig.create(String family, String id, Class<C> configCreatorClass)`. As you can see, it takes three parameters:
  - The string `family`, which indicates the directory that your config file will be stored in, relative to the global config directory (usually `<your instance directory>/config`). We're using our mod ID as the directory, and that's the best practice.
  - A second string, `id`, which will be the name of the configuration file (before the file extension is added, which changes depending on the format you choose). Here we use our mod ID once again, but if you have a complicated config with multiple files you'll want to use a different name.
  - Finally, something confusing: what is a `Class<C> configCreatorClass`? We're not going to worry too much about the details: just pass in the name of your config class with `.class` appended, as we've done here with `ExampleModConfig.class`.

## Adding values

That's it! We now have a config that we can access anywhere in our project. Problem is, there's no reason to access it because there's nothing there. Let's fix that by adding a value! Our example mod prints a line of text to the console when started up: why don't we allow the user to decide what that line is. Let's add a second field to our config:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public final TrackedValue<String> message = this.value("rai minecraft :thumbsup:");
}
```

We're introducing a bunch of things with one line again. Lovely! Let's dig into it:
- This value is `public final`, which means that instead of being accessible by anything you need an instance of `ExampleModConfig` first. Since we defined our `INSTANCE` field already, we'll be able to access this from anywhere via `ExampleModConfig.INSTANCE.message`.
- The type is `TrackedValue<String>`. The angle brackets (`<>`) allow us to use what's called *generics*, which in Java are a way to adapt a class to your specific use case. Here, the `TrackedValue` class allows us to adapt the type of object it holds, so we use it to store a `String`. Thanks to the generics, we could put another type inside those brackets later to store a different kind of value! This is foreshadowing. We're going to store some different values. Get ready.
- We call the `value` method, which comes from the `ReflectiveConfig` class we're extending. This method takes one parameter, which will be the default value of that config field. Here, the author of this tutorial is using it to self-advertise.

Phew. We got through it! That'll be the final big information dump of this tutorial, and we can begin having some fun. Now that we have a field in our config, we can pop back over to our mod initializer and start using it!

`src/main/com/example/example_mod/ExampleMod`:

```java
public class ExampleMod implements ModInitializer {
    // ...

    @Override
    public void onInitialize(ModContainer mod) {
        LOGGER.info(ExampleModConfig.message.value());
    }
}
```

Since the `message` field is a `TrackedValue<String>` instead of a simple `String`, we need to call the `value` method on it to get its value to print. We'll also be able to use the `setValue` method to change the value from code.

Now our mod prints a custom message to the console on startup! While this would already get us an easy 1 million downloads, Quilt Config allows us to do so much more.

## Using annotations

Let's add a second field. Here we're going to get fancy with it, using both a new data type and one of Quilt Config's many fancy annotations.

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @IntegerRange(min = 0, max = 10)
    public final TrackedValue<Integer> timesToPrint = this.value(1);
}
```

We're not going to show the changes to the code to use each one of these values, but remember you can see how they're used in the [final code](https://github.com/ix0rai/quilt-config-example-mod). Let's get back on our bullet points to explain this snippet!
- `IntegerRange` is an annotation: it adds new functionality to our field, outside the normal declaration. `IntegerRange` allows us to limit the allowed values of our `Integer` field, to between `min` and `max` (inclusively).
- We've changed the type from `String` to `Integer`. Now, why `Integer` instead of `int` like we would use for a normal number field? `int` is a *primitive type*, which means that it isn't actually a class! Since generics can only take classes, Java provides class versions of each primitive. `boolean` becomes `Boolean`, `float` becomes `Float`, `double` becomes `Double`, `char` becomes `Character`, etc etc.

Something incredibly important to remember is that you can't just send any class into `TrackedValue`'s generics: Quilt Config has to know how to serialize it. By default, all [primitive types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) are serializable, as well as the `String` class. We'll cover how to explain to Quilt Config how to serialize a class later, but first: more fun with annotations!

Anyway. New value!

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("Whether to print the message at all.")
    public final TrackedValue<Boolean> print = this.value(true);
}
```

Here we simply leverage the `Comment` annotation to add a comment explaining exactly what the config field does. This will be saved by Quilt Config in the config file, as long as the format you use supports comments. Neat!

Finally, we're going to use one more annotation to tie it all together. By default, Quilt Config serializes to [TOML](https://toml.io/en/) which has a couple standards we're not following. In Java, `public final` fields are named using `lowerCamelCase`, like our `timesToPrint` field. But, in a TOML file, names should use `snake_case`, meaning we should have named the field `times_to_print`. To match both conventions, we can use the `SerializedName` annotation!

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @IntegerRange(min = 0, max = 10)
    @SerializedName("times_to_print")
    public final TrackedValue<Integer> timesToPrint = this.value(1);
    // ...
}
```

Problem solved! Now it'll follow the Java conventions in the Java code, and the TOML conventions in the saved TOML file. We can apply `SerializedName` annotations to every multi-word config field to solve our problems everywhere.

## Maps and lists

Two of the most common data structures used in Java programming are `Map` and `List`. Quilt Config provides convenient serializable versions of both of these in the for of `ValueMap` and `ValueList`!

Starting with maps, `ReflectiveConfig` provides us a handy `map` method to help us easily make a default value.

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("When a key in this map shows up in the message, it'll be replaced with the corresponding value.")
    public final TrackedValue<ValueMap<String>> replacements = this.map("")
        .put("oro armor", "rai minecraft")
        .put("lambda aurora", "rai minecraft")
        .put("tib s", "rai minecraft")
        .build();
}
```

There are a few things of note here:
- We have to pass an empty string (`""`) to the `map` method, in order for it to understand that the map stores values of the type `String`. This value is not used beyond checking the type, so you can put whatever you'd like there.
- `ValueMap` always uses `String` as the type for its keys, instead of having you pick like most Java `Map` implementations.
- We can put as many values in the default map as we'd like. If the author wanted to replace every single Quilt developer with herself instead of just the admins, that would be possible!

Now that we know how to use maps, onward to lists!

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("This isn't actually used by the mod, but I was completely out of ideas for things to add.")
    @SerializedName("types_of_soup")
    public final TrackedValue<ValueList<String>> typesOfSoup = this.list("", "tomato", "borscht", "chicken noodle", "ramen", "STEW", "mushroom");
}
```

This is pretty similar to building a list. Instead of chaining `put` calls on a builder, we simply add as many values as we want directly in the constructor. Again, the first argument is unused and is to help Quilt Config infer the type. With that, we've made an excellent little config for our mod! If you want to know more, let's move on to the [Advanced Configuring tutorial](TODO LINK).
