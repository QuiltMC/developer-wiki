---
title: Setting Up a Development Environment
index: 2
---

# Setting up a Development Environment

You'll need a couple of things before you can get started.

- A Java Development Kit (JDK) for Java 17 (recommended) or newer.
  Temurin Adoptium JDKs are easily available and recommended.
  You can download them here: <https://adoptium.net/releases.html>
- Any Java IDE, like [IntelliJ Idea](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/ide/).
  [Visual Studio Code](https://www.eclipse.org/ide/) can work, but it takes extra work to get set up.
  - We recommend using IntelliJ IDEA as it has the most integrations and is the easiest to use.

## Template Mod Download (Java)

You can download the template mod from the [quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod)
repository or use GitHub's template feature to generate a mod that you can use
straight away.

To download the ZIP file for the template mod, open the [GitHub repository](https://github.com/QuiltMC/quilt-template-mod),
then go to the `< >` button and press `Download ZIP`, or refer to the following image:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-1-dark.png">
  <img alt="The GitHub repository with an arrow pointing to the code button, and another to the Download ZIP button inside the opened popup" src="/introduction/setting-up-1-light.png">
</picture>

Extract the ZIP file's contents into a folder of your choosing. Alternatively, if you
created a repository through GitHub, clone that repository using Git.

## Setting up with IntelliJ IDEA

If you downloaded the template mod and extracted it into a folder,
import the project by pressing the `Open` button in the project listing.
If you made a GitHub repository with the template, use the `Get from VCS` button.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-2-dark.png">
  <img alt="IntelliJ IDEA projects window" src="/introduction/setting-up-2-light.png">
</picture>

If you get a window asking if you trust the folder, press `Trust Project`.

There is a plugin that adds additional support for Minecraft modding projects
that is highly recommended. You can get it here:
<https://plugins.jetbrains.com/plugin/8327-minecraft-development>

## Making the Mod Yours

First, update `gradle.properties` to use your Maven group and mod ID.
If you don't know which Maven group to use, and you are planning to host the mod's
source code on GitHub, use `io.github.<Your_Username_Here>`. Set `archives_base_name`
to your mod's ID.

```gradle
# Gradle Properties
org.gradle.jwmargs = -Xmx1G
org.gradle.parallel = true

# Mod Properties
version = 1.0.0
maven_group = io.github.bingus
archives_base_name = bingus-mod
```

---

Next, update the `quilt.mod.json` file in the `src/main/resources` folder.
You'll need to update a few things under `"quilt_loader"`:

1. `"group"` should be set to the Maven group you specified in your `gradle.properties`.
2. `"id"` should be set to your mod's ID. This should be your mod's name in all lowercase
   with underscores instead of spaces.
3. `"metadata"`:
   1. `"name"` should be your mod's name. This does not require any special formatting,
      unlike your mod's ID.
   2. `"description"` should be a short description of your mod.
   3. `"contributors"` can contain entries for anything. Use your name as the key
      and `"Developer"` as the value if you don't know what to put here.
   4. `"contact"` can contain entries for anything similar to `""contributors"`,
      but typically people put a `"homepage"` and a `"sources"` entry with a valid URL here.
   5. Replace `example_mod` with your mod's ID in `"icon"`.
4. In `"entrypoints"`, replace `com.example.example_mod` with your Maven group and mod ID,
   and the `ExampleMod` at the end should be the Java class name for your mod.
   For example, `io.github.bingus.bingus_mod.BingusMod`.
5. In `"mixin"`, replace `example_mod` in the file name to your mod's ID.

Your `quilt.mod.json` should not have any traces of "example" in them anymore.

```json
{
   "schema_version": 1,
   "quilt_loader": {
      "group": "io.github.bingus",
      "id": "bingus_mod",
      "version": "${version}",
      "metadata": {
         "name": "Bingus Mod",
         "description": "This mod adds Bingus to Minecraft!",
         "contributors": {
            "Bingus": "Developer"
         },
         "contact": {
            "homepage": "https://example.org"
         },
         "icon": "assets/bingus_mod/icon.png"
      },
      "intermediate_mappings": "net.fabricmc:intermediary",
      "entrypoints": {
         "init": "io.github.bingus.bingus_mod.BingusMod"
      },
      "depends": [
         // ...
      ]
   },
   "mixin": "bingus_mod.mixins.json"
}
```

---

Create a `LICENSE` for your mod. If you don't know which license to use, check out this
link: <https://choosealicense.com/>. Note that GPL-3.0 and AGPL-3.0 are both incompatible
with Minecraft, so don't use them. Once you've chosen a license, put the license's plain text
in a file named exactly `LICENSE`.

Delete the `LICENSE-TEMPLATE.md` file. It's not necessary to have since it's the license for
the template, not your mod.

---

Change the name of the directories in the `src/main/java` folder to reflect your mod.
For example, if your Maven group is `io.github.bingus` and your mod's ID is `bingus_mod`,
you should have four directories in total. IntelliJ IDEA should make this step easier for
you when you rename the directories; just change the whole line to match your group and ID.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-3-dark.png">
  <img alt="IntelliJ IDEA rename packages dialog" src="/introduction/setting-up-3-light.png">
</picture>

Delete the `com.example` directories.

Rename `ExampleMod` to your mod's name using Java class naming conventions. Make sure the
`package` declaration at the top is correct!

In the `resources` folder, change `example_mod.mixins.json` to the file name you set in
`quilt.mod.json`. Change the name of the folder inside `assets` to be your mod's ID.

Your directories and file names should not have any traces of "example" in them anymore.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-4-dark.png">
  <img alt="Directory structure" src="/introduction/setting-up-4-light.png">
</picture>

---

If the versions in `gradle/libs.versions.toml` are out of date, replace the old version
number with the new ones obtained from this link: <https://lambdaurora.dev/tools/import_quilt.html>

---

We're almost done! The last step is to generate the Minecraft sources with Vineflower.
This is so you can view Minecraft's code with the power of Quilt's decompiler.
In the Gradle menu, go to the `fabric` category and run the `genSourcesWithVineflower`
task.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-5-dark.png">
  <img alt="Gradle window with genSourcesWithVineflower highlighted" src="/introduction/setting-up-5-light.png">
</picture>

Finally, reload the Gradle project by pressing this button in the Gradle menu:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-6-dark.png">
  <img alt="Gradle window with the 'reload project' button highlighted" src="/introduction/setting-up-6-light.png">
</picture>

Once you're done with all of these steps, your mod is ready to be worked on!
You can start by [Creating your First Item](../items/first-item)
