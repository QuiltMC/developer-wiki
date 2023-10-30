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

Next, you need to decide on whether you want to [download the template mod zip](#template-mod-download-zip-file) or [use the GitHub template](#template-mod-download-github-template). If you don't know how to use git, use the first method. However, it is recommended for you to have at least a GitHub account to get started and familiarize yourself with git quickly.

## Template Mod Download (ZIP file)

You can download the template mod from the [quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod)
repository.

To download the ZIP file for the template mod, open the [GitHub repository](https://github.com/QuiltMC/quilt-template-mod),
then go to the `< > Code` button and press `Download ZIP` as shown in the following image:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-1-dark.png">
  <img alt="The GitHub repository with an arrow pointing to the code button, and another to the Download ZIP button inside the opened popup" src="/introduction/setting-up-1-light.png">
</picture>

Extract the ZIP file's contents into a folder of your choosing.

## Template Mod Download (GitHub Template)

To use the [GitHub template](https://github.com/QuiltMC/quilt-template-mod), visit the [GitHub repository](https://github.com/QuiltMC/quilt-template-mod) and click the `Use this template` button. Follow through the dialog and clone that repository using git. Then you can continue.

## Setting up with IntelliJ IDEA

Now you need to set up your development environment. Open IntelliJ IDEA and follow through the initial setup dialog. Then, you need to open the project:
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

## An Overview Over IDEA

After you opened the project, you should see a window looking roughly like this:

TODO: Embed screenshot

On the right and left side there are sidebars with icons to toggle different "Tool Windows". There are a few that you should know about: First, the folder icon in the top left toggles your `Project` tool window. With it, you can select the different files in your project. In the bottom left, there are the buttons for the `Terminal` and `Version Control System`. Additionally, after you tested your mod, there should also be a `Run` and a `Build` button. To the right are the `Notifications` and `Gradle` tool windows.

When you open a file, it should open a new tab in the editor in the middle. ^
Additionally, there are some things you might find not straight away: If you want to rename something, be it a file or a name of a variable or function, you can do so in the context menu under `Refactor > Rename`

## Making the Mod Yours

First you'll need to make up a name for your mod. For this tutorial, we will use the mod name `Bingus Mod`.

Based on the name, you also need a mod id. It should be composed of lowercase characters from the alphabet and underlines. Usually, you mod id should be your mod name, but with underscores instead of spaces, hyphens or other special characters. Additionally, there shouldn't be a mod already using that id. For a mod named `Bingus Mod`, it would be `bingus_mod`.

Lastly you need to decide on a maven group. It is used to identify the developer of the mod in a machine-readable scheme and is designed to be unique. It should be a domain you own in reversed. So if you own `bingus.example.com`, your maven group would be `com.example.bingus`. If you don't have a domain (or don't know what it is), but have a GitHub account, you can use `io.github.your_github_username`, replace all special characters with underscores again.

---

Now that you have decided on these things, you can update your mod's metadata:

First, update the `gradle.properties` file directly in your project folder to use your Maven group and mod ID.

Change the line beginning with `maven_group =` to use your mod maven group instead of `com.example`
Set `archives_base_name` to your mod's ID similarly and ignore all other properties for now. Here is an example how the result might look:

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

Next, update the `quilt.mod.json` file in the `src/main/resources` folder. The `quilt.mod.json` defines your mod's metadata, like mod name, author, description, website, but also more development focused metadata such as dependencies, version, mod ID and [mod initializers](../concepts/sideness#on-mod-initializers).

You'll need to update a few things under `"quilt_loader"`, see below for a finalized example:

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
      but typically people put a `"homepage"`, a `"sources"`, and a `"issues"` entry with a valid URL here.
   5. Replace `example_mod` with your mod's ID in `"icon"`.
4. In `"entrypoints"`, replace `com.example.example_mod` with your Maven group and mod ID,
   and the `ExampleMod` at the end should be the Java class name for your mod. The Java class name usually is your mod's name, written in `UpperCamelCase` (So new words simply start by using a uppercase letter and the initial latter is also upper case)
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

Rename `ExampleMod` to your mod's name using Java class naming conventions. Check that the
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
