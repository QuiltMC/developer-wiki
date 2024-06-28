# Customizing the Mappings in Your Development Environment

## What are mappings?

Before a new Minecraft version jar is published to Mojang's servers, it goes through a process called _obfuscation_,
where the human-readable class, field and method names are simplified to just a few letters, mainly to optimize the file
size. In addition, obfuscation makes code very difficult to understand, because those simplified names aren't just
contractions of the real names, they're completely random letters. This is where mappings come into play.

A mapping is just a change from one name to another, in most cases an obfuscated name to a human readable one. Each
mapping may also have extra metadata, like documentation. A set of mappings is called a "mapping set" or more often
just "mappings". You can think of a mapping set as a translation dictionary, where each single mapping would be a
translation of a word to another language. Here, the obfuscated names would be the language the computer uses, and
the dictionary would help us translate that to plain english.

While a simple obuscated-to-human-readable mapping set would be enough for one minecraft version, the obfuscated names
aren't constant between different minecraft versions: `DirtBlock` could be obfuscated as `abc` in 1.19 but in 1.20 it
could be `def`. From this arises the need to keep the obfuscation changes between versions minimal, a problem that can
be solved with intermediate mappings, that convert those obfuscated names to names that won't change between versions,
but still aren't readable in english. Quilt uses Hashed Mojmap, in which every class, field, and method is prefixed by
`C_`, `f_`, and `m_` respectively, followed by an 8-letter hash.
While developing mods, most of the time you'll see Fabric's intermediary instead, which uses `class_`, `field_`, and
`method_`, followed by a number. Mojang also publishes official mappings, often called Mojmap (short for
Moj(ang)-map(pings)), for every version after 1.14. Since they don't have an intermediate mapping set, you have to do
some extra processing to use them where you'd use other mappings. Luckily, Loom does this process for you, so you don't
have to worry about that and can easily replace the mappings used in your mod for Mojang's official ones.

There are a few different formats to store mappings, and, as of the time this article was written, we are developing a
new one with a bunch of improvements. In our ecosystem, the most used format is [Tiny V2], which uses a single `.tiny`
file to store mappings, and places fields & methods as "children" of their parent class.
Another format we often use is the Enigma format, which uses a directory tree with a file for each top-level class, and
organizes the entries in a tree-like structure, placing each field, method, and class as a child of another class or a
top level one.

## Editing your mappings

Mapping sets don't have to contain a mapping for every class, field or method in a jar to be valid; in fact, most mapping
sets aren't complete. For instance, Quilt Mappings (QM for short) has reached 99% completion at most. In a development
environment, unmapped things will use their intermediary name instead of the obfuscated one, so if you have ever browsed
Minecraft's code with QM or Fabric's Yarn applied, it's very likely you have seen quite a few intermediary names.
It gets worse when it comes to method parameters: since they are really prone to incompatible changes between versions,
they (usually) don't have intermediate names, thus the names you see in the code depend on the tooling that you used to
decompile the game.

If you want to add a name or change a wrong or bad one, or add documentation to the code, you can start by picking which
mapping set you'll work on top of. In this article, we'll use QM, though the process for Yarn is almost identical.
If you want to work on top of Mojang's mappings, you'll have to do some extra work which we won't cover here. If you
are going to work on QM, we highly suggest taking a look at its [contributing documentation][QM CONTRIBUTING.md] and
contributing your changes to the repository. You'll need some very basic Git knowledge, but it should be fairly easy
to do if you've ever worked with Git before.

To get started, first get your own copy of [Quilt Mappings]' code by cloning or downloading the repo. If you want to
contribute your changes at some point, directly downloading the code won't work; you'll have to [fork the repo][fork qm]
and clone said fork instead.
Once you have the code, run `./gradlew mappings` in your command prompt or terminal to launch [Enigma], our favorite
tool to edit and write mappings. Rai wrote a really [great guide on how to edit mappings in Enigma][Enigma guide],
so you can take a look and start mapping! Once you have finished editing, don't forget to save your changes before
closing Enigma.

### Contributing the changes back to Quilt

To contribute your changes, you have to add and commit your changes, then push the changes to your fork of QM. This is
really easy to do with an IDE, as described in [the Setting Up article](../introduction/setting-up), but if you want you can also do it
from the command prompt or terminal with these commands.

```bash
git add . # tell git to track all your changes in the current directory

git commit -m "Blabla" # add those changes into a new commit (replace "blabla" with a short description of your changes)

git push # upload your commits to your fork of QM. You might need to add `origin <minecraft version>` at the end if git complains about a missing upstream branch
```

Once you pushed your changes to your fork, go to [QM's Pull Requests tab][QM PRs] and click the "Compare & Pull
Request" button in the note about your recent changes. Fill in the title and description of your PR, submit it, and wait
for your changes to be reviewed and accepted. Again, there's a more in-depth explanation of the PR process in
[the contributing documentation][QM CONTRIBUTING.md].

### Using the edited mappings

If you don't want to contribute your changes back to Quilt, or want to try them out in a development environment, you
can run `./gradlew publishToMavenLocal` to make the required files available to other projects in your computer. You can
now head to the project where you want to apply these mappings, and edit the `build.gradle` file to add `mavenLocal()`
to the `repositories` block, if it isn't already there.

```gradle
repositories {
    // ...
    mavenLocal()
}
```

Once you have `mavenLocal()` in your repositories, you can edit the `libs.versions.toml` file, in the `gradle/`
directory, to change the version of the mappings you are using to the one you just edited. In the case of QM, you can
change the `+build.*` suffix to `+local`; other projects may use a different versioning format, so you have to check
their documentation or code to verify the version you want.

```diff
 minecraft = "1.20.4"
-quilt_mappings = "1.20.4+build.1"
+quilt_mappings = "1.20.4+local"
 quilt_loader = "0.23.1"
```

That's it! You can now reload gradle through your IDE to apply these changes, and use your new mappings when reading
Minecraft's code.

<!-- Links -->

[Quilt Mappings]: https://github.com/QuiltMC/quilt-mappings
[QM CONTRIBUTING.md]: https://github.com/QuiltMC/quilt-mappings/blob/HEAD/CONTRIBUTING.md
[Fork QM]: https://github.com/QuiltMC/quilt-mappings/fork
[QM PRs]: https://github.com/QuiltMC/quilt-mappings/pulls
[Enigma]: https://github.com/QuiltMC/enigma
[Enigma guide]: https://github.com/QuiltMC/quilt-mappings/blob/HEAD/GUIDE.md
[Tiny V2]: https://fabricmc.net/wiki/documentation:tiny2
