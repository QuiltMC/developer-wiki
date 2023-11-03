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
  <img alt="La fenêtre de projet d'IntelliJ IDEA" src="/introduction/setting-up-2-light.png">
</picture>

Si vous avez une fenêtre vous demandant `Trust and Open project ?`, cliquez sur `Trust Project`.

Il y a un plugin qui ajoute du support additionnel pour les projets de mods
pour Minecraft qui est hautement recommandé. Vous pouvez l'obtenir ici :
<https://plugins.jetbrains.com/plugin/8327-minecraft-development>

## Un Apperçu d'IDEA

Après avoir ouvert le projet, vous devriez voir une fenêtre qui ressemblre vaguement à ça :

TODO: Embed screenshot

Vous pouvez voir des barres latérales de chaque côté de la fenêtre avec des icônes pour accéder à différents outils.
Voici quelques'uns qui vous seront utils :
D'abord, l'icône de dossier en haut à gauche permet d'activer/désactiver l'outil `Project`
qui vous donne accès à l'arborescence de fichiers de votre projet.
En bas à gauche, vous pouvez entre autre trouver les boutons pour le `Terminal`,
qui vous donne accès à une interface de lignes de commandes,
et le `Version Control System`.
Après avoir lancé votre mod une première fois vous pourrez aussi y trouver les boutons `Run` et `Build`.
Dans la barre latérale de droite, vous pouvez trouvez les boutons pour les `Notifications` et l'outil `Gradle`.

Ouvrir un fichier devrait ouvrir un nouvel onglet dans l'éditeur au milieu de la fenêtre.
De plus, il y a des choses que vous pourriez avoir du mal à trouver :
Par exemple pour renomer quelque-chose, que ce soit un fichier, une variable ou une fonction,
vous pouvez le faire en faisant un click droit sur l'élément que vous voulez renommer
plus en cliquant sur `Refactor > Rename` dans le menu contextuel.

## Vous Approprier le Mod

D'abord vous allez devoir trouver un nom pour votre mod. Pour ce tutoriel, nous utiliserons le nom `Bingus Mod`.

Il vous faut également un ID pour le mod. Il ne devrait contenir que des lettres minuscules et des underscores (ou tirets du 8).
En général l'ID d'un mod est son nom avec des unserscores à la place des espaces, des espaces et autres caractères spéciaux.
De plus, votre ID de mod ne devrait pas déjà être utilisé par un autre mod.
Pour notre mod `Bingus Mod`, nous utiliserons l'ID `bingus_mod`.

Enfin, il faut que vous vous trouviez un groupe maven. Il sert à identifier l'auteur d'un mod d'une manière compréhensible par une machine et devrait être unique.
Il devrait correspondre à un domaine qui vous appartient à l'envers. Par exemple si le domain `bingus.example.com` vous appartient,
votre groupe serait `com.example.bingus`. Si vous n'avez pas de domaine (ou que vous ne le connaissez pas), mais que vous avez un compte Github,
vous pouvez utiliser `io.github.votre_pseudo_github`, en prenant soin de remplacer tous les caractères spéciaux par des underscores encore une fois.

---

Après avoir décider de ces éléments, vous pouvez mettre à jour les métadonnées de votre mod :

D'abord, modifier le fichier `gradle.properties` dans le dossier racine de votre mod
afin qu'il utilise votre groupe maven et l'ID de votre mod.

Modifiez la ligne commençant par `maven_group =` pour qu'elle utilise votre group maven à la place de `com.example`.
Changer la valeur de `archives_base_name` avec l'ID de votre mod et ignorez les autres propriétés pour l'instant.
Voici un example de ce à quoi pourrait ressembler le résultat :

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

Ensuite, mettez à jour le fichier `quilt.mod.json` dans le dossier `src/main/resources`.
Ce fichier définie les métadonnées de votre mod, comme son nom, saon auteur.ice, sa description, son site web,
mais aussi des métadonnées qui concernent plus directement le développement du mod
tel que les dépendances, la version, l'ID du mod et les '[mod initializers](../concepts/sideness#les-mod-initializers)'.

Vous aurez besoin de modifier quelques éléments du champs `"quilt_loader"`,
vous pouvez trouver un exemple finalisé ci-dessous :

1. `"group"` devrait correspondre au groupe maven spécifié dans le fichier `gradle.properties`.
2. `"id"` devrait correspondre à l'ID de votre mod.
   Cela devrait correspondre au nom de votre mod en minuscule avec des underscores à la place des espaces.
3. `"metadata"`:
   1. `"name"` devrait correspondre au nom de votre mod.
      Pas besoin de format spécifique, contrairement à l'ID de votre mod.
      . `"description"` devrait correspondre à une courte description de votre mod.
   2. `"contributors"` devrait contenir une liste des perssones ayant partcipé à la création du mod, accompagnées de leur rôle.
      Ce champs n'a pas de structure spécifique, vous pouvez y mettre ce que vous voulez.
      Si vous ne savez pas quoi mettre vous pouvez simplement mettre votre nom comme clé
      avec la valeur `"Owner"`.
   3. `"contact"` n'a pas de structure spécifique, comme `"contributors"`.
      Cependant les gens y mettent en général des champs `"homepage"`, `"sources"` et
      `"issues"` avec comme valeur des URL vers les pages correspondantes.
   4. Remplacez `example_mod` avec l'ID de votre mod dans `"icon"`.
4. Dans `"entrypoints"`, remplacez `com.example.example_mod` par votre groupe maven suivit de l'ID de votre mod,
   et le `ExampleMod` à la fin devrait correspondre au nom de la classe Java pour votre Mod.
   Le nom de la classe Java correspond généralement au nom du mod, écrit en `UpperCamelCase`
   (sans espaces entre les mots et chaque mot commence avec une Majuscule).
   Par exemple dans notre cas on utiliserait `io.github.bingus.bingus_mod.BingusMod`.
5. Dans `"mixin"`, remplacez `example_mod` dans le nom du fichier par l'ID de votre mod.

Votre fichier `quilt.mod.json` ne devrait plus contenir la moindre trace de "example" maintenant.

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

Créé un fichier `LICENSE` pour votre mod. Si vous ne savez pas quelle license choisir,
vous pouvez vous aider de cet outil : <https://choosealicense.com/>.
Notez que les licenses GPL-3.0 et AGPL-3.0 ne sont pas compatible avec Minecraft, donc ne les utilisez pas.
Après avoir choisi votre license, copiez son texte dans votre fichier `LICENSE`.

Vous pouvez maintenant supprimer le fichier `LICENSE-TEMPLATE.md`.

---

Changez le nom des dossiers dans le dossier `src/main/java` afin qu'ils correspondent à votre mod.
Par exemple, si votre groupe maven est `io.github.bingus` et que l'ID de votre mod est `bingus_mod`,
vous devriez avoir 4 dossiers en tout. IntelliJ IDEA devrait rendre cette étape plus facile pour vous,
en renommant les dossier, changez simplement toute la ligne pour correspondre à votre groupe et ID.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-3-dark.png">
  <img alt="Le dialogue de renomage de paquet d'IntelliJ IDEA" src="/introduction/setting-up-3-light.png">
</picture>

Supprimez les dossiers `com.example`.

Renommez `ExampleMod` pour correspondre au nom de classe Java que vous avez utilisé
dans votre fichier `quilt.mod.json` plsu tôt. Vérifiez que la déclaration `package`
correspond bien au dossier actuel de la classe !

Dans le dossier `resources`, changez `example_mod.mixins.json` au nom de fichier que vous
avez utilisé dans le fichiers `quilt.mod.json` pour le champs `"mixin"`.
Changez le nom du dossier dans `assets` pour qu'il corresponde à l'ID de votre mod.

Les nom de vos fichiers et dossiers ne devraient plus contenir la moindre trace de "example" maintenant.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-4-dark.png">
  <img alt="Structure de dossiers" src="/introduction/setting-up-4-light.png">
</picture>

---

Si les versions dans `gradle/libs.versions.toml` sont dépassées, remplacez les par des versions
plus récentes que vous pouvez trouver ici : <https://lambdaurora.dev/tools/import_quilt.html>

---

On en a presque fini ! La dernière étape est de générer les fichiers source de Minecraft avec Vineflower.
Cela vous permettra de consulter le code de Minecraft grace au decompiler de Quilt.
Dans le menu Gradle, allez à la categorie `fabric` puis lancez la tâche `genSourcesWithVineflower`.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-5-dark.png">
  <img alt="La fenêtre Gradle avec genSourcesWithVineflower surligné" src="/introduction/setting-up-5-light.png">
</picture>

Enfin, rechargez le projet Gradle en appuyant sur ce bouton dans le menu Gradle :

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/introduction/setting-up-6-dark.png">
  <img alt="La fenêtre Gradle avec le bouton 'reload project' surligné" src="/introduction/setting-up-6-light.png">
</picture>

Après avoir complété toutes ces étapes, vous pouvez maintenant travaillez sur votre mod !
Vous pouvez commencer par [Créer votre Premier Objet](../items/first-item).
