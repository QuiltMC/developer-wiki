---
title: Advanced Configuring
index: 2
---

# Advanced configuring - WORK IN PROGRESS

Simple values are nice and all, but if you have a lot of them it can begin to get unwieldy. In this tutorial, we'll discuss how to organise your config and use processors to get the most out of it.

## Using sections

A flat file of dozens of values can get hard to navigate fast, and not to mention confusion. Luckily we can organise it into sections using Quilt Config! This is super simple to get up and running:

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

Let's say we want to store a point on a 3d grid in the config. We would need to store both an `x` coordinate, a `y` coordinate, and a `z` coordinate for that value. Let's do that! This is simply an example, and won't be used in our mod.

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

## Adding multiple files

g

## Changing the config format

Let's get into how you choose a file format to save to. Quilt Config currently only provides two serializers: `json5`, an extension of the JSON format to allow cleaner syntax and comments, and `toml`, which is the default format it serializes to. If you want to switch to `json5`, we can do that using a `Processor`!

This processor needs to run before the config is read, so we're going to place it directly on the class:

https://github.com/hibiii/BlindMe/blob/main/src/main/java/hibi/blind_me/Config.java use processor to run builder.format
