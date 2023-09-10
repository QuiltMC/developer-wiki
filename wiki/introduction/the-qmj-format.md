---
title: The quilt.mod.json Format
---

# The `quilt.mod.json` Format
The `quilt.mod.json` (also referred to as the "QMJ") is the basic manifest of all Quilt mods. It describes metadata (like mod name, description, and icon), dependencies, ID, and a few more as shown in the [Fields](#fields) section. This article is derived from the [`quilt.mod.json` specification](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md).

## Fields

* [schema_version](#schemaversion)
* [quilt_loader](#quiltloader) — Information related to loading the mod
  * [group](#group) — The Maven group ID
  * [id](#id) — The mod ID
  * [version](#version) — The mod version
  * [entrypoints](#entrypoints) — Collection of entrypoints
  * [plugins](#plugins) — Collection of plugins
  * [jars](#jars) — Array of nested JARs to be loaded
  * [language_adapters](#languageadapters) — Array of language adapters
  * [depends](#depends) — Collection of mod dependencies
  * [breaks](#breaks) — Collection of mods that this mod is incompatible with
  * [load_type](#load-type) — How eagerly to load this mod
  * [repositories](#repositories) — Array of maven repositories
  * [intermediate_mappings](#intermediatemappings) — The intermediate mappings used
  * [metadata](#metadata) — Extra information about the mod
    * [name](#name) — Human-readable mod name
    * [description](#description) — Human-readable mod description
    * [contributors](#contributors) — A list of contributors to this mod
    * [contact](#contact) — List of contact information
    * [license](#license) — One or more licenses this project is under
    * [icon](#icon) — The mod icon
* [mixin](#mixin) — Path(s) to the mixin file(s)
* [access_widener](#accesswidener) — Path(s) to the access widener file(s)
* [minecraft](#minecraft) — Minecraft-related options
  * [environment](#environment) — What game environment this mod will run in

### `schema_version`
| Type   | Required |
|--------|----------|
| Number | True     |
The schema version for the `quilt.mod.json`. This is essentially the version of the format and describes how it will be parsed. The current schema version is `1`.

### `quilt_loader`
| Type   | Required |
|--------|----------|
| Object | True     |
Information that is necessary for loading the mod.

#### `group`
| Type   | Required |
|--------|----------|
| String | True     |
A unique identifier for the organization or developers behind the mod. This must match the `^[a-zA-Z0-9-_.]+$` regular expression and must not begin with the reserved namespace `loader.plugin.`. It is recommended to follow Maven's [guide to naming conventions](https://maven.apache.org/guides/mini/guide-naming-conventions.html).

#### `id`
| Type   | Required |
|--------|----------|
| String | True     |
A unique identifier for the mod. This must match the `^[a-z][a-z0-9-_]{1,63}$` regular expression. It is best practice to use `snake_case` for the mod ID; however, `kebab-case` is also accepted.

#### `provides`
| Type  | Required |
|-------|----------|
| Array | False    |
An array of [ProvidesObject](#providesobject)s describing other mods that this mod provides.

---

#### ProvidesObject
Defines the identifier and a version range that this mod provides. This can be represented as either an object or as a string in the format of [the `id` field](#id-1).

##### `id`
| Type   | Required |
|--------|----------|
| String | True     |
A mod ID in the form of either `mavenGroup:modId` or `modId`.

##### `version`
| Type   | Required |
|--------|----------|
| String | False    |
A valid mod version. When omitted, this defaults to the version of the providing mod.

---

