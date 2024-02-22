# Customizing the Mappings in Your Development Environment

## What are mappings?

Before a new Minecraft version jar is published to Mojang's servers, it goes through a process called *obfuscation*, where the human-readable class, field and method names are simplified to just a few letters, mainly to optimize the file size, and in other contexts to prevent people from easily understanding your code. Obfuscation makes code very difficult to understand, because most names are just converted to random letters, so this is where mappings come into play.

A mapping is just a change from one name to another, in most cases an obfuscated name to a human readable one. Each mapping may also have extra metadata, like documentation. A set of mappings is called a "mapping set" or more often just "mappings". You can think of a mapping set as a translation dictionary, where each single mapping would be a translation of a word to another language. Here, the obfuscated names would be the language the computer uses, and the dictionary would help us translate to plain english.

While a simple obuscated-to-human-readable mapping set would be enough for one minecraft version, the obfuscated names aren't constant between different minecraft versions: `DirtBlock` could be obfuscated as `abc` in 1.19 but in 1.20 it could be `def`. From this arises the need to keep the obfuscation changes between versions minimal, and the problem can be solved with intermediary mappings, that convert those obfuscated names to names that won't change between versions, but still aren't readable in english. Quilt uses Hashed Mojmap, in which every class, field, and method is prefixed by `C_`, `f_` and `m_` respectively, followed by an 8-letter hash. While developing mods, most of the you'll see Fabric's intermediary instead, which uses `class_`, `field_` and `method_`, followed by a number. Mojang publishes official mappings for every version 1.14, but they don't use intermediaries, so you have to do some processing to use them where you'd use other mapping sets.

There a few different formats to store mappings, and as of the time this article was written, we are developing a new one with a bunch of improvements. In our ecosystem, the most used format is Tiny V2, which uses a single `.tiny` file to store mappings, and places fields and methods as "children" of their parent class. Another format we use is the Enigma format, which uses a directory tree with a file for each top-level class, and structure the entries in a tree-like structure, placing each class, field and method as a child of another class or a top level one.

## Editing your mappings

Mapping sets don't have to contain a mapping for every class, field or method in a jar to be valid, and in fact most mapping sets aren't complete. For instance, Quilt Mappings (QM for short) has reached 99% completion at most. Unmapped things in a development environment will use their intermediary name, instead of the obfuscated one, so if you have ever browsed Minecraft's code with QM or Fabric's Yarn applied, you have probably seen quite a few intermediary names. It gets worse with parameters, since they are really prone to incompatible changes between versions, and they don't have intermediary names, so the names you see in the code depend on what tooling you used to decompile the game.

If you want to add or change a wrong or bad name, or add documentation to the code, you can start by picking what mapping set you'll work on top of. In this article, we'll use QM, though the process for Yarn is almost identical. If you want to work on top of Mojang's mappings, you'll have to do some extra work which we won't cover here. If you are going to work with QM we highly suggest looking at its [CONTRIBUTING.md](https://github.com/QuiltMC/quilt-mappings/blob/HEAD/CONTRIBUTING.md) and contributing your changes to the repository.

To get started, clone or download [Quilt Mappings](https://github.com/QuiltMC/quilt-mappings), and run `./gradlew mappings` in your command prompt or terminal. This will launch [Enigma](https://github.com/QuiltMC/enigma), our tool to edit and write mappings. Rai wrote a really [great guide on how to edit mappings in Enigma](https://github.com/QuiltMC/quilt-mappings/blob/HEAD/GUIDE.md), so you can take a look and start mapping!

## Using the edited mappings

Once you have finished and saved your changes, you can run `./gradlew publishToMavenLocal` to make the required files available to other projects in your computer. You can now head to the project where you want to apply these mappings, and edit the `build.gradle` file to add `mavenLocal()` to the `repositories` block, if it isn't already there.

```gradle
repositories {
    // ...
    mavenLocal()
}
```

Once you have `mavenLocal()` in your repositories, you can edit the `libs.versions.toml` file in the `gradle/` directory, to change the version of the mappings you are using to the one you just edited. In the case of QM, you can change the `+build.*` suffix to `+local`; other projects may use a different versioning format, so you have to check their documentation or code to verify the version you want.

```diff
 minecraft = "1.20.4"
-quilt_mappings = "1.20.4+build.1"
+quilt_mappings = "1.20.4+local"
 quilt_loader = "0.23.1"
```

That's it! You can now reload gradle through your IDE to apply these changes, and use your new mappings when reading Minecraft's code.
