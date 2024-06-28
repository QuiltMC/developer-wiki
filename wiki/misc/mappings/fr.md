# Personnaliser les Mappings dans votre Environnment de Développement

## C'est quoi les mappings ?

Avant que le jar d'une nouvelle version de Minecraft ne soit publiée sur les serveurs de Mojang,
il passe par un procédé appelé l'_obfuscation_, qui simplifie les noms des classes, champs et méthodes à quelques lettres,
surtout pour optimiser la taille du fichier. L'obfuscation rend le code très difficile à comprendre
car ces noms simplifés ne sont pas de simples abreviations des vrais noms, ce sont des lettres complétement aléatoires.
C'est là que les mappings interviennent.

Un mapping est simplement le changement d'un nom à un autre, dans la plupart des cas d'un nom "obfusqué" vers un nom compréhensible.
Chaque mapping peut aussi avoir des metadonnées supplémentaires, comme de la documentation.
Un ensemble de mappings est souvent appelé juste des "mappings".
Vous pouvez voir un ensemble de mappings comme un dictionnaire de traduction,
dans lequel chaque mapping est une traduction d'un mot dans une autre langue.
Ici les noms "obfusqués" serait le langage utilisé par l'ordinateur
et le dictionnaire nous aide à traduire ça dans un langage qu'on peut comprendre.

Bien qu'un simple ensemble de mappings d'"obfusqué" à compréhensible serait suffisant pour une version de Minecraft,
les noms "obfusqués" ne sont pas constants d'une version de Minecraft à une autre : `DirtBlock` pourrait être "obfusqué"
en `abc` en 1.19 mais en 1.20 ça pourrait être `def`.
Il faudait donc garantir que les changement d'obfuscation entres deux versions reste minimal,
un problème qui peut être résolu avec les mappings intermédiaires qui convertissent ces noms "obfusqués"
en noms qui ne changeront pas d'une version à une autre mais qui ne sont quand même pas faits pour être compréhensibles.
Quilt utilise Hashed Mojmap, dans lequel chaque classe, champ et méthode est préfixé par
`C_`, `f_` et `m_` respectivement, suivi par un hash de 8 lettres.
En développant des mods, vous rencontrerez surtout les mappings intermédiaires de Fabric,
qui utilisent `class_`, `field_` et `method_`, suivit d'un nombre.
Mojang publie également des mappings officiel, souvent appelés Mojmap (abreviation pour Moj(ang)-map(pings)),
pour chaque version après la 1.14.
Comme ils n'ont pas de mappings intermédiaires vous devez faire un peu de travail supplémentaire
pour les utiliser à la place d'autres mappings.
Heureusement, Loom fait ce travail pour vous donc vous n'avez pas à vous inquietez pour ça
et vous pouvez facilement remplacé les mappings utilisés dans votre mod par les mappings officiel de Mojang.

Il y a plusieurs formats différents pour stocker des mappings et, au moment où cet article a été écrit,
nous travaillons sur un nouveau format avec beaucoup d'améliorations.
Dans notre écosystème, le format le plus populaire est [Tiny v2], qui utilise un unique fichier `.tiny` pour stocker
les mappings, et place les champs et méthodes comme "enfants" de leur classe.
Un autre format que l'on utilise souvent est le format Enigma, qui utilise une arborescence de fichiers avec un fichier pour
chaque classe de haut niveau et organise les entrées dans une hiérarchie avec chaque champ, méthode et classe comme enfant
d'une autre classe ou d'une classe de haut niveau.

## Modifier vos Mappings

Les ensembles de mappings n'ont pas besoin de contenir des mappings pour toutes les classes, champs ou méthodes pour être
valides, en fait la majorité des mappings ne sont pas complets.
Par exemple, les Quilt Mappings (ou QM) atteignent au mieux 99% de complétion.
Dans un environnement de développement, les choses non-mappées utiliseront leur nom intermédiaire
au lieu de leur nom "obfusqué", donc si vous avez déjà traversé le code de Minecraft avec les QM
ou Yarn (les mappings de Fabric) appliqué, il est très probable que vous ayez vu un bon nombre de noms intermédiaires.
C'est encore pire pour les paramètres de méthodes : comme ils sont à haut risque d'incompatibilité entre deux versions,
ils n'ont (en général) pas de noms intermédiaires, ainsi les noms que vous voyez dans le code dépend des outils
que vous avez utilisé pour décompiler le jeu.

Si vous voulez ajouter un nom ou un changer un qui est faux ou juste mauvais,
vous pouvez commencer par choisir un mapping que vous utiliserez comme base.
Dans cet article, nous utiliserons les QM mais le procédé pour Yarn est presque identique.
Si vous voulez utiliser les mappings de Mojang comme base,
vous devrez accomplir un peu de travail supplémentaire qui ne sera pas expliqué ici.
Si vous décidez d'utiliser les QM, nous vous recommandons fortement de lire
sa [documentation de contribution][QM CONTRIBUTING.md] et de contribuer vos modification au dépot.
Vous aurez besoin de connaissances très basique de Git mais cela devrait être assez facile à faire
si vous avez déjà travaillé avec Git auparavant.

Pour commencer, vous devez acquérir votre propre copie du code des [Quilt Mappings] en clonant ou téléchargeant le dépot.
Si vous voulez contribuer vos modifications à un moment, télécharger directement le code ne fonctionnera pas,
il faudra [fork le dépot][fork qm] et cloné votre fork à la place.
Une fois que vous avez le code, lancez `./gradlew mappings` dans votre terminal pour lancer [Enigma],
notre outil préféré pour modifier et écrire des mappings.
Rai a écrit un [super guide (en anglais) qui explique comment modifier des mappings avec Enigma][Enigma guide],
donc vous pouvez y jetter un œil et commencer à mapper!
Une fois que vous aurez fini de modifier les mappings,
n'oubliez pas de sauvegarder vos modifications avant de fermer Enigma.

### Contribuer les modifications à Quilt

Pour contribuer vos modifications, vous devez ajouter et commiter vos modifications,
puis push les modifications vers votre fork des QM.
C'est très facile à faire avec une IDE, comme expliqué dans l'[article de Mise en Place](../introduction/setting-up),
mais vous pouvez aussi le faire depuis votre terminale avec ces commandes si vous le voulez.

```bash
git add . # dit à git de suivre toutes les modifications dans le dossier actuel

git commit -m "Blabla" # ajoutes ces modifications dans un nouveau commit (remplacez "blabla" par une courte description de vos modifications)

git push # upload vos commits vers votre fork des QM. Vous aurez peut-être besoin d'ajouter `origin <version de minecraft>` à la fin si git se plaint de ne pas avoir de branche upstream
```

Après avoir push vos modification vers votre fork, allez dans [l'onglet des Pull Request des QM][QM PRs]
et cliquez sur le boutton "Compare & Pull Request" dans la note sur vos modifications récentes.
Remplissez le titre et la description de votre PR, validez là,
puis attendez que vos modifications soient vérifiées et acceptées.
N'oubliez pas que vous pouvez trouver une expication plus approfondie (en anglais) du procédé de PR dans
la [documentation de contribution][QM CONTRIBUTING.md].

### Utiliser les mappings modifiés

Si vous ne voulez pas contribuer vos modifications à Quilt,
ou si vous voulez les tester dans un environnement de développement,
vous pouvez lancer `./gradlew publishToMavenLocal` pour rendre les fichiers nécessaires disponibles
aux autres projets dans votre ordinateur.
Vous pouvez maintenant aller dans le projet ou vous voulez appliquer ces mappings, et éditer le fichier `build.gradle`
pour ajouter `mavenLocal()` au block `repositories`, si il n'y est pas déjà.

```gradle
repositories {
    // ...
    mavenLocal()
}
```

Une fois que vous avez `mavenLocal()` dans les repositories, vous pouvez modifier le fichier `libs.versions.toml`,
dans le dossier `gradle/`, pour changer la version des mappings que vous utilisez pour celle que vous venez de modifier.
Dans le cas des QM, vous pouvez changer le suffix `+build.*` en `+local`, d'autres projets peuvent utiliser d'autres
format de versioning, donc vous devrez vérifier leur documentation ou code pour vérifier la version que vous voulez.

```diff
 minecraft = "1.20.4"
-quilt_mappings = "1.20.4+build.1"
+quilt_mappings = "1.20.4+local"
 quilt_loader = "0.23.1"
```

Et voilà ! Vous pouvez maintenant recharger gradle via votre IDE pour appliquer les modifications
et utiliser vos nouveaux mappings en lisant le code de Minecraft.

<!-- Links -->

[Quilt Mappings]: https://github.com/QuiltMC/quilt-mappings
[QM CONTRIBUTING.md]: https://github.com/QuiltMC/quilt-mappings/blob/HEAD/CONTRIBUTING.md
[Fork QM]: https://github.com/QuiltMC/quilt-mappings/fork
[QM PRs]: https://github.com/QuiltMC/quilt-mappings/pulls
[Enigma]: https://github.com/QuiltMC/enigma
[Enigma guide]: https://github.com/QuiltMC/quilt-mappings/blob/HEAD/GUIDE.md
[Tiny V2]: https://fabricmc.net/wiki/documentation:tiny2
