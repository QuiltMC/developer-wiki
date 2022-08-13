# Provided JavaDocs

Mods might want to document some javadoc comments to Minecraft fields, methods, or classes and expose them to dependents,
this feature allows for that.

This can be particularly useful for libraries.

## Format

The format used is Tiny Mappings v2 (`.tiny`), whose complete, but subject to change, spec can be found in https://github.com/FabricMC/tiny-remapper/issues/9.

The complete format is not necessary, only the `intermediary` namespace, as it is not meant to remap, only add comments.

```file:src/main/resources/my_javadoc.tiny
```

## Setup

Create a key-value field in the top-level object of the `quilt.mod.json`, named `loom:provided_javadoc`. The value
should be the path and name to the Tiny file.

```json
{
  //...
  "loom:provided_javadoc": "my_javadoc.tiny"
}
```

Then refresh gradle, and regenerate sources.
