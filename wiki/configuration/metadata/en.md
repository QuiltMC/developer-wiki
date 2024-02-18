# Annotations and Metadata Reference

Quilt Config is absolutely packed full of annotations, with each one allowing you to expand on the functionality of your config! This page serves as a reference, giving an overview of the functionality of each annotation. More in-depth information can be found in the annotations' Javadoc.

## `@Comment`

- Usable on config fields and sections
- Can be repeated infinitely on either object

Allows you to attach comments to your fields, which are saved as metadata. By default, they will be serialized to config files in both the `toml` and `json5` formats. Since they're saved as metadata, they will be accessible anywhere you have the `TrackedValue` for your config field, meaning you can display them in config screens and otherwise.

## `@FloatRange` and `@IntegerRange`

- Usable on config fields of type `Float` and type `Integer` respectively
- Can only be applied once per field

On startup and when the annotated field is changed, checks if the number value of the field is in between the provided `min` and `max`, inclusively. If not, errors with a `TrackedValueException`.

## `@Matches`

- Usable on config fields of type `String`
- Can only be applied once per field

On startup and when the annotated field is changed, checks if the `String` value of the field matches the provided [regular expression](https://regexr.com/). If not, errors with a `TrackedValueException`.

## `@Processor`

- Usable on config classes, fields, and sections
- Can only be applied once per object

Allows you to set up methods that will run before the config is initialized. Takes a `String` parameter that matches the method name of a method inside your class: this method will be run before config initialization. You must give the method one parameter corresponding to the type annotated with `@Processor`:
- When used on a tracked value, the processor method will take a `TrackedValue.Builder` as its parameter.
- When used on a section, the processor method will take a `SectionBuilder` as its parameter.
- When used on a config class, the processor method will take a `Config.Builder` as its parameter.

Processors allow you to do a large variety of things, including:
- Running custom code when values are edited
- Adding new values and sections programmatically
- Dynamically updating metadata

You can see example processors and a more in-depth look at their usage in the [advanced configuring tutorial](TODO LINK).

## `@SerializedName`

- Usable on config fields and sections
- Can only be applied once per class

Adds a piece of `String` metadata that's used as the name that will be serialized in the config file. The default name remains a fallback: if the serial name is not present in the file during deserialization, the default name will be used. The serial name will always be used when writing to the file.

## `@SerializedNameConvention`

- Usable on config classes, sections and fields
- Can only be applied once per class

Adds name convention metadata to each field inside the config class, which dictates a casing to be used when saving to the config. This will transform the default name of the field to apply the casing stored in the metadata. If the field has a serial name, that name will take priority over the transformed name from this annotation. Casing options are provided and documented in `NamingSchemes`. When used under a parent that also has this annotation, overwrites for that element only (for example, if a class is annotated with a certain scheme and a field inside the class is annotated with a different scheme, the field will take the scheme it's annotated with).

## `@Alias`

- Usable on config fields and sections
- Can be repeated infinitely on either object

Adds a piece of metadata dictating past names of this field. These names will be considered during deserialization, but will never be saved to the config file. This is useful for migrating configs to new names after changing things!