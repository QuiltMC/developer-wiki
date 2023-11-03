# Mettre en place un Environement de Développement

Vous aurez besoin de deux choses avant de commencer.

- Un Kit de Développement Java (JDK) pour Java 17 (recommandé) ou suppérieur.
  Les Kits de Développement Temurin Adoptium sont facilement disponibles et recommandés.
  Vous pouvez les télécharger ici : <https://adoptium.net/releases.html>
- Une IDE pour Java, comme [IntelliJ Idea](https://www.jetbrains.com/idea/) ou [Eclipse](https://www.eclipse.org/ide/).
  - Nous recommendons d'utiliser IntelliJ IDEA car il a le plus d'intégrations et est plus facile à utiliser.

Ensuite, vous devez décider si vous voulez [télécharger le zip du mod patron](#télécharger-le-mod-patron-fichier-zip) ou
[utiliser le patron Github](#télécharger-le-mod-patron-github-template). Si vous ne savez pas utiliser git, utiliser la première méthode.
Cependant, il est recommandé d'au moins avoir un compte Github pour débuter et vous familiariser rapidement avec git.

## Télécharger le Mod Patron (Fichier ZIP)

Vous pouvez télécharger le mod patron depuis le dépôt [quilt-template-mod](https://github.com/QuiltMC/quilt-template-mod).

Pour télécharger le fichier ZIP du mod patron, ouvrez le [dépôt Github](https://github.com/QuiltMC/quilt-template-mod),
puis cliquer sur le boutton `< > Code` puis `Download ZIP` comme montré dans l'image ci-dessous:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-1-dark.png">
  <img alt="Le dépôt Github avec un flèche qui pointe vers le boutton 'Code' et une autre qui pointe vers le bouton 'Download ZIP' dans la popup ouverte" src="/introduction/setting-up-1-light.png">
</picture>

Extrayez le contenu du fichier ZIP dans un dossier de votre choix.

## Télécharger le Mod Patron (Github Template)

Pour utiliser le [patron Github](https://github.com/QuiltMC/quilt-template-mod), ouvrez le [dépôt Github](https://github.com/QuiltMC/quilt-template-mod)
puis cliquer sur le boutton `Use this template`. Suivez les instructions puis clonez le nouveau dépôt en utilisant git. Vous pouvez ensuite continuer.

## Mise en place avec IntelliJ IDEA

Maintenant il faut que vous mettiez en place votre environnement de développement.
Ouvrez IntelliJ IDEA et suivez les instructions de mise en place initiale. Vous pouvez ensuite ouvrir le projet :

Si vous avez téléchargé le mod patron et l'avez extrait dans un dossier,
importez le projet en cliquant sur le boutton `Open` dans la liste de projets.

Si vous avez créé un dépôt Github avec le patron, cliquez sur le boutton `Get from VCS`.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-2-dark.png">
  <img alt="IntelliJ IDEA projects window" src="/introduction/setting-up-2-light.png">
</picture>

Si vous avez une fenêtre vous demandant `Trust and Open project ?`, cliquez sur `Trust Project`.

Il y a un plugin qui ajoute du support additionnel pour les projets de mods
pour Minecraft qui est hautement recommandé. Vous pouvez l'obtenir ici :
<https://plugins.jetbrains.com/plugin/8327-minecraft-development>

## Un Apperçu d'IDEA

Après avoir ouvert le projet, vous devriez voir une fenêtre qui ressemblre vaguement à ça :

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
