# Débuter avec Quilt

## Nécessités de base

Pour commencer à modder Minecraft, vous aurez besoins de deux choses.

- Des connaissances de base en Java. Cela inclut comprendre sa syntaxe,
  la programmation orientée objet et quelques patrons de conception essentiels.
- Des connaissances de base sur [Git](https://git-scm.com). Bien que techniquement non essentiel,
  c'est un outil qui vous facilitera grandement la vie. Cela inclut de savoir comment cloner des
  dépôts et comment faire des commits. De bonnes ressources pour débuter sont la [documentation de Git](https://git-scm.com/doc) et la [documentation de GitHub](https://docs.github.com/en/get-started).

<!-- TODO: Is there anything more to be described here? -->

## Comment Utiliser ce Wiki

TODO: Create example code repository and put it here ([Issue #68](https://github.com/QuiltMC/developer-wiki/issues/68))

Ce wiki contient des pages qu'il vaut mieux lire dans l'ordre. Nombre de ces articles, surtout ceux au début, suggèrent d'autres articles avec lesquels continuer.
La plupart des pages contiennent des extraits de code, et des exemples complets de mods sont prévus. (ça n'est pas encore fait)

Vous pourrez rencontrer des lignes qui commencent avec `TODO: `. Ce sont des notes pour les rédacteur·ice·s du wiki sur le travail qu'il reste à faire.
Vous pouvez les ignorer pour l'instant, sachez simplement que plus de contenu sera inséré à leur place plus tard.

Dans le prochain article, vous mettrez en place votre premier mod pour débuter avec le développement de mod.

Une fois cela fais, il est recommandé d'apprendre à [Créer votre Premier Item](../items/first-item). Puis, si vous êtes près, vous pouvez apprendre à [ajoutez des blocs](../blocks/first-block), ou à créer des items plus avancés, tels que de la [nourriture](../items/food), des [outils](../items/tools) et des [armures](../items/armor).

Pour des ecplications plus générales, vous pouvez regarder dans la catégorie "Concepts" :

- Dans [Aperçu des QSL et de la QFAPI](../concepts/qsl-qfapi) vous avez un apperçu des Quilt Standard Libraries et de la Quilted Fabric API, les APIs officielles de Quilt.

De nombreux autres articles sont prévus mais pas encore réalisés ([Issue #69](https://github.com/QuiltMC/developer-wiki/issues/69)). Cette section sera mise à jour plus tard.

<!-- TODO: Give an outline of all of the wiki articles once they're ready. -->

## Comment apprendre à Modder, pour les Débutants

Le moddage de Minecraft peut être un peu compliqué. Notre but est de fournir une ressource
pour vous aider à apprendre les concepts de base du moddage. Cependant, il y a des choses
que l'on ne peut pas faire et nous nous attendons donc à ce que vous les appreniez de vous même.

Poser des questions correctement (et en anglais), que ce soit sur notre forum ou sur Discord, est
une compétence essentielle à avoir. N'ayez jamais peur de poser des questions, nous sommes toujours
prêts à aider, et on ne juge pas.

Quand vous posez des questions, essayez d'inclure le contexte nécessaire dans le premier message et [ne demandez pas si vous pouvez poser une question](https://dontasktoask.com/). Décrivez votre problème, ce que vous avez déjà essayé pour le résoudre, et comment ces tentatives ont échouées. Assurez vous de ne pas poser de questions sur une partie spécifique d'une solution à votre problème mais de bien expliquer le problème en lui même (voir [XY problem](https://xyproblem.info/)). Quand vous déboguez, incluez votre `latest.log` et quand votre jeu crash, un crash log. De plus, un accès complet à votre code source peut vraiment aider, idéalement disponible en ligne sur un site tel que GitHub.

Vous pouvez demander des questions liées au développement dans notre [Forum](https://forum.quiltmc.org/), ainsi que sur le salon de forum [mod-dev-help](https://discord.com/channels/817576132726620200/1047429688521396325) de notre [Discord](https://discord.quiltmc.org/).

De nombreux problèmes liés au moddage ont déjà été résolus et sont disponibles publiquement
afin que vous puissiez les voir et les réutiliser car la plupart des mods pour Quilt
sont open source. Nous vous recommandons de regarder des exemples de comment faire les choses
dans des dépôts open source et parfois de "voler" du code. Les dépôts open source
sont là pour que vous puissiez en apprendre, n'ayez pas peur de les utiliser !

Il y a de nombreux sujets dans le modding pour lesquels vous ne trouverez pas de tutoriels. Dans ces cas là vous devrez soit comprendre le code de Minecraft impliqué, soit regarder le code d'autres mods pour comprendre comment ils accomplissent des choses similaires à ce que vous essayez de faire. Si vous voulez regarder le code source de Minecraft, lancez la tâche Gradle `genSourcesWithVineFlower` dans la catégorie `fabric` ou ouvez n'importe quel fichier source de Minecraft dans IntelliJ IDEA et cliquez sur le bouton "download sources".

<!-- TODO: Is this todo fixed?: Levi write your thing about stealing code here -->

## Les différences entre Fabric et Quilt

Quilt est basé sur Fabric. Cependant, il y a des différences clés entre les deux.

TODO: Levi write your thing here
