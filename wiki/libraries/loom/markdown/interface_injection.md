# Interface Injection

Interface Injection is a compile-time feature that makes Minecraft classes implement specified interfaces.

This can be useful to avoid excessive casting for interfaces that are implemented at runtime.

## The Interface

While not necessarily required, all the interface methods should be marked as `default`, with a default implementation;
that is needed for the case that some inheritor for that class exists, having a default implementation allows them to
compile without overriding those methods.

A reminder that the interface must be implemented at runtime, for example with a Mixin, to avoid failing in production.

## Setting Up

Create an object inside the `quilt_loom` object of the `quilt.mod.json`, named`injected_interfaces`, this object
must contain key-array values, the key being the injected class, and the array all the interfaces to inject.

```json
{
  //...
  "quilt_loom": {
    "injected_interfaces": {
      "some/minecraft/Class": [
        "my/interface/ToInject",
        "my/interface/ToInject2"
      ]
    }
  }
}
```
<!--- Link to the guide that talks about genSource (i.e. setup) -->
Then refresh gradle, and regenerate sources.

## Usage

Now it is possible to use methods of the injected interface when using an instance of the class.

