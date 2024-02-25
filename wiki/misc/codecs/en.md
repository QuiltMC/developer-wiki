# Codecs

WARNING: This tutorial expects a strong understanding of both Java basics and generics.

The `Codec` class from [DataFixerUpper](https://github.com/Mojang/DataFixerUpper) is the backbone of content serialization and deserialization.
It provides an abstraction layer between Java Objects and serialization types, such as `json`, `nbt`, and more.
Each `Codec` is made of a `Encoder` and a `Decoder`, but you rarely need to create a raw `Codec` from scratch.
Let's start off with the primative `Codec`s.

<!-- TODO: Is this div needed? The basic example is getting pushed to the bottom of the page for some reason -->
<div>
## Primative Codecs

Mojang thankfully builds in many default `Codec` implementations, making our lives easier as most objects are composed of these few types.
We will cover building `Codec`s composed of other `Codec`s later on.
It'll be important to understand the basics.

A non-exhaustive list of `Codec`s:
- `Codec.BOOL`: A `boolean` codec.
- `Codec.BYTE`: A `byte` codec.
- `Codec.SHORT`: A `short` codec.
- `Codec.INT`: An `int` codec.
    - `Codec<Integer> Codec.intRange(int min, int maxInc)`: An `int` codec with an inclusive range.
- `Codec.LONG`: A `long` codec.
- `Codec.FLOAT`: A `float` codec.
    - `Codec<Float> Codec.floatRange(float min, float maxInc)`: A `float` codec with an inclusive range.
- `Codec.DOUBLE`: A `double` codec.
    - `Codec<Double> Codec.doubleRange(double min, double maxInc)`: An `double` codec with an inclusive range.
- `Codec.STRING`: A `string` codec.

"Ok", you tell me, "Thats cool. But... I still don't know what codecs are for or how to use them".

### Basic Codec Example
Let's go over a very basic example:

```java
boolean bool = 
        Codec.BOOL.decode(
            JsonOps.INSTANCE,
            new JsonPrimitive(true)
        )
        .result()
        .get()
        .getFirst();

assert bool;
```

WOAH! That doesn't look simple *at all*. What happened?

Well, using `Codec`s is fairly verbose, but that means you get a lot of useful information to help with errors and such, which is important for Mojang to provide in their library since we want to know why Minecraft failed to load something, not just that it failed.

Now, lets break this down into a couple sections.

First off:
```java
Codec.BOOL.decode(
    JsonOps.INSTANCE, // DynamicOps<T> ops
    new JsonPrimitive(true) // T input
) // DataResult<Pair<Boolean, JsonElement>>
...
```
The `decode` method on a codec takes two values, an `ops` and an `input`.
As shown in the comments above, the type of the input and a generic parameter on `ops` must match.
This is because the `ops` needs to know about how the `input` functions.
In this example, we use `com.mojang.serialization.JsonOps.INSTANCE`, which operates on json elements from `gson`.
We then pass in a `JsonPrimative` with a value of `true` for this example.

Finally, the `com.mojang.serialization.DataResult<Pair<A, T>>` type allows us to encode more information than just the result.
First off, the `A` type is the output of the `Codec`, which is `Boolean` in this case, and the `T` is the same as the input.

Let's look more into the `DataResult`:
```java
...
.result() // Option<Pair<Boolean, JsonElement>>
...
```

Ok, this starts to make more sense. 
`DataResult` has a lot of associated methods on it, but for now let's only cover two: `result` and `error`.
`error` returns a `PartialResult`, which allows you to both recover a decode, and to get the error message for why the decode failed. Right now, the `result` method is more important to us.
`result` returns an `Option<Pair<A, T>>`, which makes sure that we know for sure if we have a result, otherwise we could just get null.

Finally, we get to the last two lines:
```java
...
.get() // Pair<Boolean, JsonElement>
.getFirst(); // Boolean
```

We use `get` to unbox the `Option`. Generally this is unsafe to do, an IntelliJ even gives a warning.
In this case we know that it is safe due to the simplicity of the example.
Then finally, we call `getFirst` on `com.mojang.datafixers.util.Pair` to get the first half of the pair

Wow. That sure was a lot.
Now, I know this may seem like the `Codec` system is complicated right now, but unfortunately we have only scratched the surface.
Yep. That's right, it gets so much worse.
Let's step back and look at some more `Codec` types.
</div>

## Collection Codecs

While the primative `Codec`s are the most basic building blocks for `Codec`s, we need to we able to put them together to be able to fully represent serializable objects.
These collection `Codec`s are fairly straight forward, and each has a constructor which takes a `Codec` parameter for each associated type with the collection.

<!-- TODO: Use the static methods instead of the classes -->

- `ListCodec<T>`: A codec for `List<T>`. You can also make a list by calling `listOf` on a `Codec`.
- `SimpleMapCodec<K, V>`: A codec for `Map<K, V>` with a known set of `K`s. This known set is an additional parameter.
- `UnboundedMapCodec<K, V>`: A codec for `Map<K, V>`.
- `PairCodec<F, S>`: A codec of a `Pair<F, S>`.
- `EitherCodec<L, R>`: A codec of `Either<L, R>`.

## The `RecordCodecBuilder`
Oh no. There is an explicit type name in a header. This is going to get crazy.

Let's start it off simple: a `RecordCodecBuilder` creates a `Codec` that can directly serialize and deserialize a Java object.
While it has the name `Record` in it, this isnt specific to the `record`s in Java, but it's often a good idea to use `record`s.
Going over a basic example will probably be the clearest here.

```java
record Foo(int bar, List<Boolean> baz, String qux) {
    public static final Codec<Foo> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance.group(
                    Codec.INT.fieldOf("bar").forGetter(Foo::bar),
                    Codec.BOOL.listOf().fieldOf("baz").forGetter(Foo::baz),
                    Codec.STRING.optionalFieldOf("qux", "default string").forGetter(Foo::qux)
                ).apply(instance, Foo::new)
        );
}
```

Ok, thats not too bad. 
One nice thing about this is that Mojang did a lot of magic behind the scene to make this feel nice.
Trust me, I (OroArmor) once wrote a similar library and partially gave up on doing the right thing.

Now, `RecordBuilder.create` takes a lambda, providing an `instance` parameter.
The main bulk of this lambda is the `group` method.
You can pass up to 16 different `Codec`s turned into fields through this method.

Turning a `Codec` into a field follows a fairly simple pattern.

First, you start with the `Codec` (`Codec.INT`, `Codec.BOOL.listOf()`, and `Codec.STRING`).

Then, you can call one of two methods:
 - `fieldOf`, which takes a string parameter for the serialized field name.
 - `optionalFieldOf`, also takes the same name parameter. 
    By default this represents an `Optional<T>`, with `T` being the `Codec` type. 
    There is a method overload, like used in the example, which allows you to provide a default value and not have to store an `Optional<T>`.

Finally, you call `forGetter`, which takes a `Function<O, T>`, with `O` being the object you are trying to serialize, and `T` being the type of the field on the object.

Now, let's see a serialized `new Foo(8, List.of(true, false, true), "string")` in json:
```json
{
    "bar": 8,
    "baz": [true, false, true],
    "qux": "string"
}
```

Now, since we had an optional field, let's see what this json looks like:
```json
{
    "bar": -2,
    "baz": []
}
```
Once deserialized, we get an object equal to `new Foo(-2, List.of(), "default string")`