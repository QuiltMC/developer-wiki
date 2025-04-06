# Aperçu des QSL et de la QFAPI

Cet article explique les différentes bibliothèques standards que vous pourriez rencontrer en moddant avec Quilt.

## Qu'est-ce qu'une API ?

Une API (Application Programming Interface) fournit un moyen pour plusieurs programmes d'intéragir les uns avec les autres.
Dans le contexte du moddage de Minecraft, les APIs fournissent en général des outils pour une meilleure compatibilité entre les mods ou d'autres extensions utiles.
Par exemple, nombreux des gros mods que vous connaissez ont une API qui permet à d'autres mods d'améliorer leur compatibilité avec eux.

En plus de ce genre d'API, il y a les bibliothèques. Les bibliothèques en elles-même n'ajoutent aucun contenu au jeu,
mais elles implémentent des API pour rendre l'implémentation de certaines fonctionnalités plus simple pour d'autres dévelopeur·euse·s.
La Fabric API et les Quilt Standard Libraries sont des exemples de bibliothèques officielles publiées par un loader de mod particulier.

Il est imporant de noter qu'une API ne définie que la _façon_ d'interagir avec un programme spécifique et son _comportement correspondant_,
_pas son implémentation_. **Les implémentations peuvent changer à tout moment et ne devraient donc pas être utilisées.**
Les implémentations on tendance à contenir `impl` dans le nom du paquet ou de la classe,
tandis que les APIs sont souvent dans un paquet `api`.

## La Fabric API

La Fabric API (FAPI pour faire court) est l'API de Fabric et fournit plusieurs APIs utiles que les Quilt Standard Libraries ne fournissent pas,
telles que la majorité des APIs de rendu, l'API de groupe d'items et l'API de key bind.
Pour Quilt, la [Quilted Fabric API](#la-quilted-fabric-api) est une implémentation alternative, qui utilise les Quilt Standard Libraries dès que possible.

## Les Quilt Standard Libraries

Les Quilt Standard Libraries (QSL pour faire court) fournissent beaucoup de fonctionnalités essentielles pour les mods, ainsi que quelques APIs utiles,
telles que le [Registry Entry Attributes](../data/rea).

Quelques fonctionnalités fournies par les QSL sont :

- Les [entrypoints principaux](sideness#les-mod-initializers) pour les mods.
- Charger les dossiers `assets` et `data` de votre mod avec le [chargeur de ressources](../data/resource-loader).
- De nombreux [événements](events) tel que l'événement de tick du monde.
- Des classes et interfaces d'extension pour de nombreux blocs, items, entités et plus.

Bien que vous pouvez ne faire votre mod qu'avec des [mixin](mixins), les QSL font une grosse partie du travai
et s'assure que des fonctionnalités communes de mods ne causent pas de conflits entre eux.
Les QSL essayent également de garder leurs APIs plutôt stable entre les versions de Minecraft,
de façon à ce que vous ayez moins de travail à faire en mettant votre mod à jour.

Cependant, les APIs des QSL ne sont pas assez complètes pour fournir toutes les fonctionnalités de la Fabric API
(de la même façon, la FAPI ne fournit pas tout ce que les QSL fournissent),
c'est pourquoi vous utiliserez rapidement la Quilted Fabric API dans vos projets.

## La Quilted Fabric API

La Quilted Fabric API (QFAPI pour faire court) fournit l'API de Fabric, mais implémente cette API en utilisant les QSL dès que possible.
Les APIs de la FAPI avec une véritable alternative QSL sont dépréciées dans la QFAPI,
alors pensez à utiliser les APIs des QSL quand certaines méthodes de la Fabric API sont dépréciées.

La Quilted Fabric API a deux utilisations principales :

- Elle fournit un niveau de compatibilité pour que les mods Fabric puissent être chargées avec Quilt.
- Et elle permet d'utiliser les API de Fabric quand les QSL ne fournit pas encore d'APIs correspondantes.
  C'est le cas par exemple pour les [groupes d'items](../items/first-item#ajouter-litem-à-un-groupe).

Comme la QFAPI dépend des QSL et a besoin de connaitre l'implémentation de Fabric,
elle sera toujours mise à jour après que la Fabric API et les QSL aient été mises à jour.

La QFAPI inclut aussi les QSL, ce qui signifie que vous voudrez généralement n'utiliser que la QFAPI.
Les téléchargements sur [Modrinth](https://modrinth.com/mod/qsl) et [Curseforge](https://www.curseforge.com/minecraft/mc-mods/qsl)
sont également la QFAPI et les QSL emballées ensemble.
