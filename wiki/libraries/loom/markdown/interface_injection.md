# Interface Injection

Interface Injection is a compile-time feature that makes Minecraft classes implement specified interfaces.

This can be useful to avoid excessive casting for interfaces that are implemented at runtime.

## The Interface

While not necessarily required, all the interface methods should be marked as `default`, with a default implementation;
that is needed for the case that some inheritor for that class exists, having a default implementation allows them to
compile without overriding those methods.

A reminder that the interface must be implemented at runtime, for example with a Mixin, to avoid failing in production.

## Setting Up

Create an object in the top-level object of the `quilt.mod.json`, named `loom:injected_interfaces`. This
object must contain a key-array, the key being the injected class, and the array of all the interfaces to inject.

```json
{
  //...
  "loom:injected_interfaces": {
    "some/minecraft/Class": [
      "my/interface/ToInject",
      "my/interface/ToInject2"
    ]
  }
}
```
<!--- Link to the guide that talks about genSource (i.e. setup) -->
Then refresh gradle, and regenerate sources.

## Usage

Now it is possible to use methods of the injected interface when using an instance of the class.

