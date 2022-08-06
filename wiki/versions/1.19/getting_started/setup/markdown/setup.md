# Setup

Quilt is a mod-loader toolchain project for Minecraft; it allows you to modify and expand upon the game.

It is recommended that you have knowledge of Java, because it is the language which Minecraft is written in. <!--- TODO: Perhaps link to some Java resources and courses. -->

## Setting Up your Workspace

While you could create your own template, cloning the [official template mod](https://github.com/QuiltMC/quilt-template-mod)
might be a bit simpler.

You can either clone the template with the [git command `clone`](https://git-scm.com/docs/git-clone), or download the
ZIP file in the GitHub interface, and then unzipping it.

### IDEs?

Using an Integrated Development Environment is going to make your life easier while coding Minecraft mods, they provide
several utilities and integrations for some tools, like Git, or Gradle.

#### [IntelliJ IDEA](https://www.jetbrains.com/idea)

Importing the mod with IntelliJ IDEA is as simple as selecting the folder/`build.gradle` file and opening it as a project.

#### [Eclipse](https://www.eclipse.org)

Open the project, and run the `eclipse` Gradle task.

#### [Visual Studio Code](https://code.visualstudio.com)

Visual Studio Code does not support Java by default, you must install an extension to be able to code in Java inside
VSCode.

- [Language Support for Java(TM) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java)
- [Debugger for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)

Then open the folder as a project and run the `vscode` Gradle task.

### Customizing your Project

Even though you have a ready workspace, it still has all the default values from the template, which is no good.
You should replace them to match the details of your mod.

- `build.gradle`

    The Gradle buildscript, it specifies dependencies, packaging, compiling, and publishing behavior.

- `gradle.properties`

    Properties that can be used in the buildscript.
    The default values included in it, that you should modify first are: `version`, the current version of the mod;
`maven_group`, the group ID for your mod, preferable it should follow Java's package convention, that is, the reverse
of a domain you own (e.g. `mydomain.com` -> `com.mydomain`), alphanumeric lowercase with underscores replacing dashes;
`archives_base_name`, the name of the built JAR file.

- `libs.versions.toml`

    Specifies the dependency notation of certain dependencies of your mod, by default Minecraft, Quilt Mappings, Quilted
Fabric API, and Quilt Loader.

    They might not be always up-to-date, if that is the case, check the [Quilt Import Utility Page](https://lambdaurora.dev/tools/import_quilt.html).

    In case of adding new dependencies, don't forget to also add them and their repositories into your buildscript with a
configuration befit for them.

- `LICENSE`

    A default `LICENSE` file is not included by itself in the template mod, only a template, choose a license that matches
your own priorities and thoughts, and then create a file that has the legal text for it.

    You also should set the `license` field in the `quilt.mod.json` to the [SPDX Identifier](https://spdx.org/licenses) of the chosen license.

- `quilt.mod.json`

    The Quilt mod metadata file.
    Contains a variety of information relating to your mod.

    A complete specification of all the fields and format can be accessed in [the RFC](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md).

- `File Structure`

    The packages of source should match the group inserted into the `gradle.properties`.

### Generating the Minecraft Source

While Minecraft is not open-source nor readable in normal circumstances, people have found ways to decompile, unobfuscate
and remap the game.

Although not strictly necessary, generating sources allow for facility in navigating through the code and for a more
pleasant code viewing experience.

The default of Quilt Loom - The Gradle plugin that the template mod uses - has three decompilers for choosing, Quiltflower
(Notably, a Quilt project), CFR, and Fernflower.

Generating the source is a simple as running the `genSources` (Append `with[Decompiler Name]` in case you want a decompiler
other than Quiltflower) Gradle task.

- Unix-like and Windows Powershell: `./gradlew genSources`
- Windows Command Prompt: `gradlew genSources`

In case your sources weren't attached, attach them manually, the file is located in
`./.gradle/quilt-loom-cache/[Minecraft Version]/[Mappings Folder]`, the sources jar being the one ending with `-sources`.

### Other Mappings

Mappings are a part of toolchain that gives Minecraft code human-readable names and documentation, mappings choice is
purely personal and depends on your own priorities.

The template mod provides [Quilt Mappings](https://github.com/quiltMC/quilt-mappings) by default, but you might also want
to use other mapping sets, like:
- **Mojang's Official Mappings**: Also known as "Mojmap", does not provide documentation nor parameter names - Can be used with the `loom.officialMojangMappings()` method.
- [**Parchment**](https://parchmentmc.org/about.html): Expands upon the Mojang's Official Mappings, providing documentation and parameter names.
- [**Yarn**](https://github.com/FabricMC/Yarn): FabricMC mappings.

Just replace the `mappings` dependency declaration in your buildscript, with the respective mapping.

#### Migrating to Another Mapping Set

While you could rewrite the names manually when switching to other mappings, that becomes unpractical once you have a
large codebase.

The `migrateMappings` task allows for remapping of your whole project; bear in mind that it is not a perfect tool and
some inconsistencies might occur, and you may have to fix them manually.

Before switching mappings in your buildscript, run the task with a `mappings` argument specifying the groupId, module name,
and version in a dependency notation way.

- Unix-like and Windows Powershell: `./gradlew migrateMappings --mappings=com.somedom:some-mappings:1.0.1`
- Windows Command Prompt: `gradlew migrateMappings --mappings=com.somedom:some-mappings:1.0.1`

The remapped source should be in a folder called `remappedSrc` inside your project.
