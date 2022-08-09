# Migrating From Fabric

While Quilt supports loading Fabric mods, you might want to experience some additional features that Quilt might
provide.

To set up a Quilt mod workspace, check the [`Setup` guide](./setup).

## Notable Differences

Although many things are very similar, there are a few remarkable divergences between Quilt and Fabric mods.

### Mod Metadata

Quilt utilizes a `quilt.mod.json` which is largely different from the `fabric.mod.json`.
More information can be found at the [quilt.mod.json specification RFC](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md).

- The naming convention that official fields naming use is `snake_case`, instead of `camelCase`.
- The structure contains many differences.
    - Most of the fields have been reorganized into objects, such as `quilt_loader` and `minecraft`.
    - Information that is meant to be read and shown to humans is now contained under a `metadata` block.
- Custom values are not defined in a `custom` object, instead they are declared in the top-level object.
- The dependency notation now is able to be represented as an object.
    - This allows for extended behavior which was not possible before, check the [dependency object section in the RFC](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md#dependency-objects).

### Built-in Entrypoints

- The game initializers (i.e. `ModInitializer`, `ClientModInitializer`, and `DedicatedServerModInitializer`) have been moved from loader to the base module of Quilt Standard Libraries.
- Their keys have been altered.
    - `ModInitializer`: `init`
    - `ClientModInitializer`: `client_init`
    - `DedicatedServerModInitializer`: `server_init`
    - `PreLaunchEntrypoint`: `pre_launch`
- Their main methods now contain a `ModContainer` parameter that represents the mod that declared them.
- Entrypoints can be declared with a single key-value in the `quilt.mod.json` instead of an array, if there is only one.

### Base API

While not all of Fabric API has been matched by Quilt Standard Libraries, the parts that have been improved, besides
all the new APIs that were added.

A comparison chart that shows features that are present or absent, between QSL and Fabric API is available at the [Quilt
Standard Libraries README](https://github.com/QuiltMC/quilt-standard-libraries/blob/1.19/README.md#features).

In case there is the need of usage of an API which is present in Fabric API, but not in QSL, it is possible to pull and
use [Quilted Fabric API](https://github.com/QuiltMC/quilted-fabric-api), which is acts as a bridge between both.

In case something is deprecated in Quilted Fabric API while not in Fabric API, it is very likely that
an alternative in Quilt Standard Libraries exists and should be used instead.
