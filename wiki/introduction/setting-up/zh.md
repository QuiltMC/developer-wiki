# 建立开发环境

在开始之前，你需要准备以下东西。

- Java 17（推荐）或更新版本的Java开发工具包（JDK）。
  Temurin Adoptium JDK 易于使用。
  你可以在这里下载它：<https://adoptium.net/releases.html>
- 任何Java IDE，如 [IntelliJ Idea](https://www.jetbrains.com/zh-cn/idea/) 和 [Eclipse](https://www.eclipse.org/ide/)。
   [Visual Studio Code](https://code.visualstudio.com/) 可以工作, 但需要设置的操作更繁琐。
- 我们建议使用IntelliJ IDEA，因为它集成的功能多且容易使用。

接下来，你需要[下载模组模板压缩包](#模组模板下载压缩包)或者[使用GitHub模板](#模组模板下载GitHub模板)。如果你不知道如何使用Git，请使用第一种方法。但还是建议你拥有一个GitHub帐户，以便快速入门并熟悉Git。

## 模组模板下载（压缩包）

你可以从[quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod)仓库中下载模组模板。  

请打开[GitHub 仓库](https://github.com/QuiltMC/quilt-template-mod)下载模组模板的压缩包,
然后点击到`< > Code`按钮并选择`Download ZIP`，如下图所示：

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-1-dark.png">
  <img alt="The GitHub repository with an arrow pointing to the code button, and another to the Download ZIP button inside the opened popup" src="/introduction/setting-up-1-light.png">
</picture>

然后，解压压缩包到你选择的文件夹中。

## 模组模板下载（GitHub模板）

要使用[GitHub模板](https://github.com/QuiltMC/quilt-template-mod)，请访问[GitHub仓库](https://github.com/QuiltMC/quilt-template-mod)，然后点击`Use this template`按钮。按照对话框进行操作，然后使用Git克隆该存储库。做完这一切后，你可以继续了。  

## 使用IntelliJ IDEA建立开发环境

现在你需要设置开发环境。打开IntelliJ IDEA，按照初始设置对话框进行操作。
然后，你需要打开项目：

如果你下载了模板模块并将其解压到一个文件夹中，  
可以通过点击项目列表中的 `打开`按钮来导入项目。

如果你使用模板创建了一个 GitHub 仓库，请使用 `克隆存储库`按钮。

<picture>  
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-2-dark.png">  
  <img alt="IntelliJ IDEA 项目窗口" src="/introduction/setting-up-2-light.png">  
</picture>

如果你看到一个询问是否信任该文件夹的窗口，请点击 `Trust Project`（信任项目）。

有一个插件可以为 Minecraft 模组开发项目提供额外的支持，强烈推荐安装。  
你可以通过以下链接获取：  
<https://plugins.jetbrains.com/plugin/8327-minecraft-development>

## An Overview Over IDEA

After you opened the project, you should see a window looking roughly like this:

TODO: Embed screenshot

On the right and left side there are sidebars with icons to toggle different "Tool Windows". There are a few that you should know about: First, the folder icon in the top left toggles your `Project` tool window. With it, you can select the different files in your project. In the bottom left, there are the buttons for the `Terminal` and `Version Control System`. Additionally, after you tested your mod, there should also be a `Run` and a `Build` button. To the right are the `Notifications` and `Gradle` tool windows.

When you open a file, it should open a new tab in the editor in the middle.
Additionally, there are some things you might find not straight away: If you want to rename something, be it a file or a name of a variable or function, you can do so in the context menu under `Refactor > Rename`

## IDEA 概览

当你打开项目后，你应该会看到一个大致如下的窗口：

待办：嵌入截图

在窗口的左右两侧有侧边栏，上面有图标用于切换不同的“工具窗口”。其中有几个是你应该了解的：首先，左上角的文件夹图标用于切换你的 `项目`工具窗口。你可以通过它选择项目中的不同文件。在左下角，有 `终端`和 `Version Control System`（版本控制系统）的按钮。此在你测试完你的模块后，还会出现 `Run`（运行）和 `Build`（构建）按钮。在右侧是 `通知`和 `Gradle` 工具窗口。

当你打开一个文件时，它会在中间的编辑器中打开一个新标签页。
此外，还有一些你可能不会立刻发现的功能：如果你想重命名某些内容，无论是文件、变量名还是函数名，你都可以在右键菜单中的 `重构 > 重命名`进行操作。

## 填写模组信息

首先，你需要为你的模组取一个名字。在本教程中，我们将使用`Bingus Mod`这个模组名。

根据名字，你还需要一个MODID，它由小写字母和下划线组成。通常情况下，你的MODID应该是你的模组名字，但用下划线代替空格、连接字符或其他特殊字符。此外，此MODID不能与其他模组重复。比如名为`Bingus Mod`的模组，它的MODID应是`bingus_mod`。

最后，你需要决定你的maven group。它用于在机器可读的方案中识别mod的开发者，且被设计为唯一的。它应该是你拥有的域名颠倒过来。因此，如果你拥有`bingus.example.com`，你的maven group将是`com.example.bingus`。如果你没有域名（或者不知道它是什么），但有一个GitHub帐户，你可以使用`io.github.your_github_username`，再用下划线替换所有特殊字符。

---

现在你已经决定了这些事情，你可以更新你的模组元数据：

首先，直接更新在项目文件夹中的`gradle.properties`中的Maven group和MODID。

将以`maven_group =`开头的行更改为使用你模组的maven group，而不是`com.example`。
类似地，将`archives_base_name`设置为你的MODID，暂时忽略所有其他属性。这里是结果的示例：

```gradle
# Gradle 属性
org.gradle.jwmargs = -Xmx1G
org.gradle.parallel = true

# 模组属性
version = 1.0.0
maven_group = io.github.bingus
archives_base_name = bingus-mod
```

---

接下来，更新在`src/main/resources`文件夹中的`quilt.mod.json`文件。
`quilt.mod.json` 定义了你的模组的元数据，如模组名称、模组作者、模组描述和官方网站。还定义了开发相关的元数据，如依赖项、版本、MODID和[mod initializers](../concepts/sideness#on-mod-initializers)。

你需要在 `"quilt_loader"` 下更新一些内容：

1. `"group"` 应设置为你在 `gradle.properties` 中指定的 Maven group。
2. `"id"` 应设置为你的MODID。这里应该是模组名称的小写形式，且用下划线代替空格。
3. `"metadata"`：
   1. `"name"` 应为你模组的名称。与MODID不同，这里不需要特殊格式。
   2. `"description"` 应为你模组的简短描述。
   3. `"contributors"` 可以包含任何条目。如果不知道要放什么，可以将你的名字作为键，值设为 `"Developer"`。
   4. `"contact"` 可以包含与 `"contributors"` 类似的条目，通常包括 `"homepage"`、`"sources"` 和 `"issues"` 条目，其中的值为有效的URL。
   5. 在 `"icon"` 中用你的MODID替换 `example_mod`。
4. 在 `"entrypoints"` 中，用你的 Maven group 和MODID替换 `com.example.example_mod`，末尾的 `ExampleMod` 应为你模组的Java类名。Java类名通常使用 `UpperCamelCase`，例如 `io.github.bingus.bingus_mod.BingusMod`。
5. 在 `"mixin"` 中，用你的MODID替换文件名中的 `example_mod`。

修改后你的 `quilt.mod.json` 中不应出现 “example” 这个单词，详见下面的最终示例：

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

为你的模组创建一个 `LICENSE` 文件。如果你不确定使用哪个开源协议，可在此链接查看主流开源协议的详细介绍：<https://choosealicense.com/>。请注意，GPL-3.0 和 AGPL-3.0 与 Minecraft 不兼容，不要使用这两种开源协议。选择好后，需要该开源协议的纯文本内容放入名为 `LICENSE` 的文件中。

删除 `LICENSE-TEMPLATE.md` 文件。因为这是模板的开源协议，而不是你模组的开源协议。

---

将 `src/main/java` 文件夹中的目录名称更改的与你的模组相符。例如，如果你的 Maven Gruop 是 `io.github.bingus` 且 MODID 是 `bingus_mod`，那么你应该总共有四个目录。IntelliJ IDEA 在重命名这些目录时会更简单：将整行名称更改为与你的 Maven Gruop 和 MODID 匹配的名称即可。

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
