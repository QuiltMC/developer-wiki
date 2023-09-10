---
title: The quilt.mod.json Format
---

# The `quilt.mod.json` Format
The `quilt.mod.json` (also referred to as the "QMJ") is the basic manifest of all Quilt mods. It describes metadata (like mod name, description, and icon), dependencies, ID, and a few more as shown in the [Fields](#fields) section. This article is derived from the [`quilt.mod.json` specification](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md).

## Fields

* [schema_version](#schema-version)
* [quilt_loader](#quilt-loader) — Information related to loading the mod
  * [group](#group) — The Maven group ID
  * [id](#id) — The mod ID
  * [version](#version) — The mod version
  * [entrypoints](#entrypoints) — Collection of entrypoints
  * [plugins](#plugins) — Collection of plugins
  * [jars](#jars) — Array of nested JARs to be loaded
  * [language_adapters](#language-adapters) — Array of language adapters
  * [depends](#depends) — Collection of mod dependencies
  * [breaks](#breaks) — Collection of mods that this mod is incompatible with
  * [load_type](#load-type) — How eagerly to load this mod
  * [repositories](#repositories) — Array of maven repositories
  * [intermediate_mappings](#intermediate-mappings) — The intermediate mappings used
  * [metadata](#metadata) — Extra information about the mod
    * [name](#name) — Human-readable mod name
    * [description](#description) — Human-readable mod description
    * [contributors](#contributors) — A list of contributors to this mod
    * [contact](#contact) — List of contact information
    * [license](#license) — One or more licenses this project is under
    * [icon](#icon) — The mod icon
* [mixin](#mixin) — Path(s) to the mixin file(s)
* [access_widener](#access-widener) — Path(s) to the access widener file(s)
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


