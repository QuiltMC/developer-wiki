# 建立开发环境

在开始之前，你需要准备以下东西。

- Java 17（推荐）或更新版本的Java开发工具包（JDK）。
  Temurin Adoptium JDK 很容易获得并推荐使用。
  你可以在这里下载它：<https://adoptium.net/releases.html>
- 任何Java IDE，如[IntelliJ Idea](https://www.jetbrains.com/zh-cn/idea/)和[Eclipse](https://www.eclipse.org/ide/).
  [Visual Studio Code](https://code.visualstudio.com/)可以工作, 但需要额外的操作来设置。
- 我们建议使用IntelliJ IDEA，因为它集成的功能多且容易使用。

接下来，你需要决定是[下载模板模组压缩包](#模板模组下载压缩包)还是[使用GitHub模板](#模板模组下载GitHub模板)。如果你不知道如何使用Git，请使用第一种方法。但还是建议你拥有一个GitHub帐户，以便快速入门并熟悉Git。

## 模板模组下载（压缩包）

你可以从[quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod)仓库中下载模板模组。  

要下载模板mod的ZIP文件，请打开[GitHub 仓库](https://github.com/QuiltMC/quilt-template-mod),
然后点击到`< > Code`按钮并选择`Download ZIP`，如下图所示：

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-1-dark.png">
  <img alt="The GitHub repository with an arrow pointing to the code button, and another to the Download ZIP button inside the opened popup" src="/introduction/setting-up-1-light.png">
</picture>

然后，提取ZIP文件的内容到你选择的文件夹中。

## 模板模组下载（GitHub模板）

要使用[GitHub模板](https://github.com/QuiltMC/quilt-template-mod)，请访问[GitHub仓库](https://github.com/QuiltMC/quilt-template-mod)，然后点击`Use this template`按钮。按照对话框进行操作，然后使用Git克隆该存储库。做完这一切后，你可以继续了。  

## 使用IntelliJ IDEA建立开发环境

现在你需要设置开发环境。打开IntelliJ IDEA，按照初始设置对话框进行操作。
然后，你需要打开项目：

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

When you open a file, it should open a new tab in the editor in the middle.
Additionally, there are some things you might find not straight away: If you want to rename something, be it a file or a name of a variable or function, you can do so in the context menu under `Refactor > Rename`

## 让模组成为你的

首先，你需要为你的模组取一个名字。在本教程中，我们将使用`Bingus Mod`这个模组名。

根据名字，你还需要一个MODID，它由小写字母和下划线组成。通常情况下，你的MODID应该是你的模组名字，但用下划线代替空格、连接字符或其他特殊字符。此外，此MODID不能与其他模组重复。比如名为`Bingus Mod`的模组，它的MODID应是`bingus_mod`。

最后，你需要决定你的maven group。它用于在机器可读的方案中识别mod的开发者，且被设计为唯一的。它应该是你拥有的域名颠倒过来。因此，如果你拥有`bingus.example.com`，你的maven group将是`com.example.bingus`。如果你没有域名（或者不知道它是什么），但有一个GitHub帐户，你可以使用`io.github.your_github_username`，再用下划线替换所有特殊字符。

---

现在你已经决定了这些事情，你可以更新你的模组元数据：

首先，直接更新在项目文件夹中的`gradle.properties`中的Maven group和MODID。

将以`maven_group =`开头的行更改为使用你模组的maven group，而不是`com.example`。
类似地，将`archives_base_name`设置为你的MODID，暂时忽略所有其他属性。这里是结果的示例：

```gradle
# Gradle属性
org.gradle.jwmargs = -Xmx1G
org.gradle.parallel = true

# 模组属性
version = 1.0.0
maven_group = io.github.bingus
archives_base_name = bingus-mod
```

---

接下来，更新在`src/main/resources`文件夹中的`quilt.mod.json`文件。The `quilt.mod.json` defines your mod's metadata, like mod name, author, description, website, but also more development focused metadata such as dependencies, version, mod ID and [mod initializers](../concepts/sideness#on-mod-initializers).

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
   // ……
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
