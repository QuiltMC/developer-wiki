# Setup

Quilt is a mod-loader toolchain project for Minecraft; it allows you to modify and expand upon the game.

It is recommended that you have knowledge of Java, because it is the language which Minecraft is written in. <!--- TODO: Perhaps link to some Java resources and courses. -->

## Setting Up your Workspace

While you could create your own template, using the [official template mod](https://github.com/QuiltMC/quilt-template-mod)
might be a bit simpler.

You can either clone the template with the [git command `clone`](https://git-scm.com/docs/git-clone), or download the
ZIP file in the GitHub interface, and then unzipping it.

### IDEs?

Using an Integrated Development Environment is going to make your life easier while coding Minecraft mods, they provide
several utilities and integrations for some tools, like Git, or Gradle.

#### [IntelliJ IDEA](https://www.jetbrains.com/idea)

Importing the mod with IntelliJ IDEA is as simple as selecting the folder/`build.gradle` file and opening it as a project.

#### [Eclipse](https://www.eclipse.org)

Open the project as a Gradle Project, then run the `eclipse` Gradle task, which should be available under the Gradle Tasks
view tab, alternatively, it's also possible to use the command-line to run Gradle tasks with the Wrapper script (`gradlew`):
- Unix-like and Windows Powershell: `./gradlew eclipse`
- Windows Command Prompt: `gradlew eclipse`

#### [Visual Studio Code](https://code.visualstudio.com)

Visual Studio Code does not support Java by default, you must install an extension to be able to code in Java inside
it.

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack): A package of Java-related
extensions for VSCode.

After that, open the folder as a project, then run the `vscode` Gradle task: Do note that without the [Gradle for Java extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-gradle),
which might have some flaws and bugs, it is not possible to run tasks within VSCode as an integrated convenience. If that is the case
you must use the command-line (<code>Ctrl + \`</code> by default) with the Gradle wrapper script (`gradlew`):
- Unix-like and Windows Powershell: `./gradlew vscode`
- Windows Command Prompt: `gradlew vscode`

### Customizing your Project

Even though you have a ready workspace, it still has all the default values from the template, which is no good.
You should replace them to match the details of your mod.

- `build.gradle`

    The Gradle buildscript, it specifies dependencies, packaging, compiling, and publishing behavior.

- `gradle.properties`

    Properties that can be used in the buildscript.
    The default values included in the template file, that perhaps should be modified are: `version`, the current version of the mod;
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
your own priorities and thoughts, and then create a file that has the legal text for it. In the event that you are unsure
which license to choose, Modrinth has a very straightforward [blog post](https://blog.modrinth.com/p/licensing-guide) on the subject which may help.

    You also should set the `license` field in the `quilt.mod.json` to the [SPDX Identifier](https://spdx.org/licenses) of the chosen license.

- `quilt.mod.json`

    The Quilt mod metadata file.
    Contains a variety of information relating to your mod.

    The complete specification of all the fields and format can be accessed in [the RFC](https://github.com/QuiltMC/rfcs/blob/main/specification/0002-quilt.mod.json.md)
and in [the official JSON Schema.](https://raw.githubusercontent.com/QuiltMC/quilt-json-schemas/main/quilt.mod.json/schemas/main.json).

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
