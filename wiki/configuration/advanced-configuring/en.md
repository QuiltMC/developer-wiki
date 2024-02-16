---
title: Advanced Configuring
index: 2
---

# Advanced configuring

Simple values are nice and all, but if you have a lot of them it can begin to get unwieldy. In this tutorial, we'll discuss how to organize your config and use processors to get the most out of it.

## Using sections

A flat file of dozens of values can get hard to navigate fast, not to mention confusing. Luckily we can organize it into sections using Quilt Config! This is super simple to get up and running.

Via sections, you can use indentation to visually differentiate parts of the config file for users reading. We're going to add an example section that looks like this in TOML:

```toml
# ...

# This isn't actually used by the mod, but I was completely out of ideas for things to add.
typesOfSoup = ["tomato", "borscht", "chicken noodle", "ramen", "STEW", "mushroom"]

# Advanced settings for advanced users.
[advanced_settings]
    # Whether to automatically append newlines to every message printed.
    # default: true
    printNewlines = true
```

To do that, we'll create a section inside our code:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("Advanced settings for advanced users.")
    @SerializedName("advanced_settings")
    public final AdvancedSettings advancedSettings = new AdvancedSettings();

    public static class AdvancedSettings extends Section {
        @Comment("Whether to automatically append newlines to every message printed.")
        @SerializedName("print_newlines")
        public final TrackedValue<Boolean> printNewlines = this.value(true);
    }
}
```

We simply create a new class, inside our config class, that extends `ReflectiveConfig.Section`. Then we have to create another object inside the main config to add the section. Note that this instance of our section is **not** stored inside a `TrackedValue` like everything else, instead it stores `TrackedValue`s inside itself! Now that we have a section, we can add as many values as we want inside. But what if we want to store something more interesting than a basic type, map, or list? Let's serialize a custom object.

## Serializing custom values

In Java, you can print to different output streams in the console. These aren't basic integer or float objects, so we can't just save them in our config! This is where the `ConfigSerializableObject<T>` interface comes in. By implementing its three methods, we can set up any class to be usable as a config object.

The interface works via generics, just like `TrackedValue`. The `<T>` in `ConfigSerializableObject<T>` can be swapped out with any serializable class (remember, by default that's primitive types, `String`, `ValueList`, and `ValueMap`), and the value of your object will be translated into that type to be saved to disk, and then converted back into your custom object when read. To do that translating, we need to implement three methods:
- `T getRepresentation()`: here, your value is converted to the serializable class that you specified in the generics (represented by `T`) so that it can be saved.
- `YourSerializableClass convertFrom(T)`: this one is called when reading the config file, and converts the representation created by `getRepresentation` back to its original type.
- `YourSerializableClass copy()`: makes a copy of the value, which Quilt Config uses internally.

Enough with the explanations: let's see an example!

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public static class AdvancedSettings extends Section {
        // ...
        @Comment("What stream to print the message to.")
        @SerializedName("print_stream")
        public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);

        @SuppressWarnings("unused") // IDEs won't understand that all options in this enum can be used via the config
        public enum PrintStreamOption implements ConfigSerializableObject<String> {
            SYSTEM_OUT(System.out),
            SYSTEM_ERROR(System.err);

            private final PrintStream printStream;

            PrintStreamOption(PrintStream stream) {
                this.printStream = stream;
            }

            public PrintStream getStream() {
                return this.printStream;
            }

            @Override
            public PrintStreamOption convertFrom(String representation) {
                return valueOf(representation);
            }

            @Override
            public String getRepresentation() {
                return this.name();
            }

            @Override
            public PrintStreamOption copy() {
                // enum values cannot be duplicated
                return this;
            }
        }
    }
}
```

This may look like a lot of code, but we've already covered most of it! We're using an `Enum` here, which allows us to clearly define a set of options for our value. If you wanted, you could use a regular class and have an infinite number of possibilities for your config field! An additional benefit of using an `Enum` is that Quilt Config will automagically generate a comment with the possible values in the serialized config file.

The implementation of `ConfigSerializableObject<T>`'s methods is incredibly simple here: since the values in our enum already have names, we just use the methods `name` and `valueOf(String)` to serialize and deserialize respectively. Cute! Let's look at another example of a custom object that can't be represented in an enum.

Let's say we want to store a point on a 3d grid in the config. We would need to store an `x` coordinate, a `y` coordinate, and a `z` coordinate for that value. Let's do that! This is simply an example, and won't be used in our mod.

```java
public class Vec3i implements ConfigSerializableObject<ValueMap<Integer>> {
    public final int x;
    public final int y;
    public final int z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vec3i copy() {
        return this;
    }

    @Override
    public Vec3i convertFrom(ValueMap<Integer> representation) {
        return new Vec3i(representation.get("x"), representation.get("y"), representation.get("z"));
    }

    @Override
    public ValueMap<Integer> getRepresentation() {
        return ValueMap.builder(0)
                .put("x", this.x)
                .put("y", this.y)
                .put("z", this.z)
                .build();
    }
}
```

Here we leverage a `ValueMap` instead of a `String` as the serialized type. This allows us to easily distinguish between the `x`, `y`, and `z` fields, and is the data type you'll nearly always use when serializing complex objects like this. We're not going to go in depth here, as the code is fairly self-explanatory now that we understand the `ConfigSerializableObject<T>` interface.

## Using processors

Now that we've learned all about values, let's learn how to do evil things: introducing `Processor`s.
This devious annotation allows you to configure your configs and their values, as well as add modification callbacks, which allow you to run code when the value is changed.
The annotation works via allowing you to point to code that will be called as the config is built.
First we'll set up a simple processor that prints to the console when the config starts to be loaded.

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Loading config!");
    }

    // ...
}
```

With that, our config will print "Loading config!" before any of its values are deserialized. Note despite the method name passed to `@Processor` not coming with any parameter information, we still had to put a `Config.Builder` on our method: what's up with that?
Processors can be attached to three different types: tracked values, config sections, and config classes. For each, the parameter will be different, as documented in `Processor`'s Javadoc:
- When used on a tracked value, the processor method will take a `TrackedValue.Builder` as its parameter.
- When used on a section, the processor method will take a `SectionBuilder` as its parameter.
- When used on a config class, the processor method will take a `Config.Builder` as its parameter.

But there's more that we can do with processors than just printing nonsense to the command line! Let's see what we can do with that `Builder` object we're being offered.
On both tracked values and config classes, we're able to leverage a method called `callback` to set up code that runs when the config is changed!

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Loading config!");
        builder.callback(config -> System.out.println("Updated!"));
    }

    // ...
}
```

With that line, we've expanded our logging to now tell us whenever a config value is updated! Neat, but what else can we do with callbacks?

One example of a callback usage is syncing a value between your config field and another. This could be needed for many reasons: your config value is complicated, and you want to make it easier to access, or maybe you need to update the configuration of one of the libraries you depend on when the value is changed ([enigma](<https://github.com/QuiltMC/enigma>) does just that!).
We're going to set up a shortcut to accessing the print stream made available in `printStream`, that doesn't force you to go through two separate getters to use. To do that, we can use a processor applied to the field!

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public static class AdvancedSettings extends Section {
        // ...
        @Processor("processPrintStream")
        public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);
        public transient PrintStream activeStream = printStream.value().getStream();

        public void processPrintStream(TrackedValue.Builder<PrintStreamOption> builder) {
            builder.callback(value -> activeStream = printStream.value().getStream());
        }
		
        // ...
    }
}
```

Using our callback, we update the `activeStream` variable each time that the print stream is changed. This keeps it perfectly in sync with the `printStream` field at all times! Note that we mark it as `transient`, a keyword which tells Java (and subsequently Quilt Config!) not to serialize the value.
Now instead of dealing with `ExampleModConfig.INSTANCE.advancedSettings.printStream.value().getStream()` we can simply do `ExampleModConfig.INSTANCE.advancedSettings.activeStream`, simplifying our lives a little when interacting with the config. The power of processors, in action.

## Changing the config format

Let's get into how you choose a file format to save to. Quilt Config currently only provides two serializers: `json5`, an extension of the JSON format to allow cleaner syntax and comments, and `toml`, with the default being `toml`. If you want to switch to `json5`, we can do that using a `Processor`!
We'll need to apply this processor globally to our config, since the way we'll be changing the format is via the `Config.Builder` object a config class processor will provide.

This processor needs to run before the config is read, so we're going to place it directly on the class:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
	public void processConfig(Config.Builder builder) {
		// ...
		builder.format("json5");
	}

	// ...
}
```

With our knowledge of processors, this is simple! You can also use the config builder to add new fields, new sections, and update metadata (TODO LINK TO METADATA PAGE), on top of changing the format and using callbacks as we've already covered.

## Adding multiple files

For massive mods, a single config file can become unwieldy, even when organized into sections.
Luckily, Quilt Config is designed to easily support adding multiple config files!
To add a second config file, we must make another config class: let's call this one `ExampleModConfig2`.
We'll also have to update the name of our original config file to be more specific:

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    public static final ExampleModConfig INSTANCE = QuiltConfig.create(ExampleMod.MOD_ID, "main", ExampleModConfig.class);
}
```

Instead of using the mod ID as the name of our class, we call our original config `main` instead.
Now let's create a second config:

`src/main/com/example/example_mod/ExampleModConfig2`:

```java
public class ExampleModConfig2 extends ReflectiveConfig {
    public static final ExampleModConfig2 INSTANCE = QuiltConfig.create(ExampleMod.MOD_ID, "secondary", ExampleModConfig2.class);
}
```

Just the same thing as our original, but using the `ExampleModConfig2` class instead of `ExampleModConfig` everywhere. We also name it `secondary`, to go along with the `main` name of our original config.
Now you can add whatever fields you'd like! With Quilt Config, you can repeat this process as much as you like, as long as no configs have duplicate names.
