# The `quilt.mod.json` Format
The `quilt.mod.json` (also referred to as the "QMJ") is the basic manifest of all Quilt mods. It describes metadata (like mod name, description, and icon), dependencies, ID, and a few more as shown in the [Fields](#fields) section. A "package", as mentioned in this article, refers to the `.jar` that the `quilt.mod.json` is in. This article is derived from the [`quilt.mod.json` specification](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md).

# Fields

## `schema_version`
| Type   | Required |
|--------|----------|
| Number | True     |
The schema version for the `quilt.mod.json`. This is essentially the version of the format and describes how it will be parsed. The current schema version is `1`.

---

## `quilt_loader`
| Type   | Required |
|--------|----------|
| Object | True     |
Information that is necessary for loading the package.

---

### `group`
| Type   | Required |
|--------|----------|
| String | True     |
A unique identifier for the organization or developers behind the mod. This must match the `^[a-zA-Z0-9-_.]+$` regular expression and must not begin with the reserved namespace `loader.plugin.`. It is recommended to follow Maven's [guide to naming conventions](https://maven.apache.org/guides/mini/guide-naming-conventions.html).

---

### `id`
| Type   | Required |
|--------|----------|
| String | True     |
A unique identifier for the package. This must match the `^[a-z][a-z0-9-_]{1,63}$` regular expression. It is best practice to use `snake_case` for the mod ID; however, `kebab-case` is also accepted.

---

### `provides`
| Type  | Required |
|-------|----------|
| Array | False    |
An array of [`ProvidesObject`](#providesobject)s describing other packages that this package provides.

---

### ProvidesObject
Defines the identifier and a version range that this mod provides.

### `id` (ProvidesObject)
| Type   | Required |
|--------|----------|
| String | True     |
A package ID in the form of either `mavenGroup:modId` or `modId`.

### `version` (ProvidesObject)
| Type   | Required |
|--------|----------|
| String | False    |
A valid package version. When omitted, this defaults to the version of the providing package.

### Alternative Form (ProvidesObject)
The `ProvidesObject` may also be represented as a `String` in the format of [the `id` field](#id-providesobject).

---

### `version`
| Type   | Required |
|--------|----------|
| String | True     |
A package version. Versions must conform to the [Semantic Versioning 2.0.0 specification](https://semver.org/). In a development environment, `${version}` (or any valid gradle placeholder) may be used in the value to be replaced by gradle upon build.

---

### `entrypoints`
| Type   | Required |
|--------|----------|
| Object | False    |
A collection of keys mapped to [`EntrypointObject`](#entrypointobject)s where the keys represent the type of entrypoint.

---

### EntrypointObject
Defines an entrypoint.

### `adapter` (EntrypointObject)
| Type   | Required |
|--------|----------|
| String | False    |
The language adapter to use for this entry. By default, this is `default` which tells the loader to parse this entry using the [JVM entrypoint notation](#jvm-entrypoint-notation-entrypointobject).

### `value` (EntrypointObject)
| Type   | Required |
|--------|----------|
| String | True     |
Points to an implementation of the entrypoint.

### Alternative Form (EntrypointObject)
If an entrypoint does not need to specify a language adapter, the `EntrypointObject` may be a `String` in the format of [the `value` field](#value-entrypointobject).

### JVM Entrypoint Notation (EntrypointObject)
When referring to a class, the [binary name](https://docs.oracle.com/javase/specs/jls/se8/html/jls-13.html#jls-13.1) is used. An example of a binary name is `example.mod.ExampleClass$Inner`.

One of the following `value` notations may be used in JVM notation:
* Implementation onto a class
  * The value must contain the fully-qualified binary name of the class.
  * The implementing class must extend or implement the entrypoint interface (e.g. `ModInitializer`).
  * The class must have a no-argument public constructor (in Java, there will be one by default if none is explicitly written).
  * Example: `example.mod.ExampleMain`
* A field in a class
  * The value must contain a fully-qualified binary name of the class followed by `::` and the field's name.
  * The field must be static.
  * The field must have the type of the class or a subclass (where `ExampleClass::THE_FIELD instanceof ExampleClass` returns true).
  * If the field shares its name with a method, an exception will be thrown.
  * Example: `example.mod.ExampleMain::INSTANCE`
* A method in a class
  * The value must contain a fully-qualified binary name of the class followed by `::` and the method's name.
  * The method must be an implementation of the entrypoint type. This usually means it implements a functional interface.
  * Whether a public no-argument constructor is required is based on if the method is an instance method or is static.
    * When the method is static, no constructor is necessary.
    * When it's an instance method, it requires a public no-argument constructor.
  * If the method shares its name with a field, an exception will be thrown.
  * Example: `example.mod.ExampleMain::init`

### Other Notations (EntrypointObject)
Language providers may extend the capabilities of [JVM Entrypoint Notation](#jvm-entrypoint-notation-entrypointobject) or provide additional notations. For notation rules for other language adapters, consult the language adapter's documentation.

---

### `plugins`
| Type  | Required |
|-------|----------|
| Array | False    |
An array of [`LoaderPluginObject`](#loaderpluginobject)s.

---

### LoaderPluginObject
An `Object` representing a loader plugin.

### `adapter` (LoaderPluginObject)
| Type   | Required |
|--------|----------|
| String | False    |
The language adapter to use for this plugin.

### `value` (LoaderPluginObject)
| Type   | Required |
|--------|----------|
| String | True     |
Points to an implementation of the `LoaderPlugin` interface. This can be in either of the following forms:
- `example.plugin.ExamplePlugin` — A subclass of `LoaderPlugin` that will be instantiated.
- `example.plugin.ExamplePlugin::PLUGIN` — A static field set to an instance of `LoaderPlugin`.

### Alternative Form (LoaderPluginObject)
If a plugin does not need to specify a language adapter, the `LoaderPluginObject` may be represented as [the `value` field](#value-loaderpluginobject).

---

### `jars`
| Type  | Required |
|-------|----------|
| Array | False    |
A list of `String` paths to nested `.jar` files to load relative to the root directory of the package's `.jar`.

### Example (`jars`)
```json
{
	"quilt_loader": {
		"jars": [
			"/resources/example.jar"
		]
	}
}
```

---

### `language_adapters`
| Type   | Required |
|--------|----------|
| Object | False    |
An `Object` in which each key is the namespace of the language adapter and each value is a reference in [JVM Entrypoint Notation](#jvm-entrypoint-notation-entrypointobject) of an implementation of `LanguageAdapter`.

---
