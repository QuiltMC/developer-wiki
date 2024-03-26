# Débuter avec Quilt Config

[Quilt Config](https://github.com/QuiltMC/quilt-config) est la puissante API de configuration de Quilt. Elle permet aux developpeur·euse·s (c'est vous !) de facilement mettre en place des valeurs qui peuvent être modifiées par l'utilisateur·ice pour changer le comportement de votre mod. Elle est inclue dans le [Quilt Loader](https://github.com/QuiltMC/quilt-loader) pour permettre à n'importe quel mod Quilt de l'utiliser sans importer de nouvelles dépendences ou lancer de setup.

## Mettre en place votre config

Dans ce tutorial, nous allons constuire un mod simple qui écrit une ligne de texte dans la console quand il est initilisé. N'oubliez pas que c'est un exemple et votre mod peut utiliser les valeurs de la config n'importe où ! Vous pouvez voir le code source complété de ce tutoriel [sur GitHub](https://github.com/ix0rai/quilt-config-example-mod).

<!-- TODO: Add code project into wiki -->

Pour débuter avec Quilt Config, vous aurez besoin d'une classe dédiée pour contenir votre config. Pour un mod extrémement configurable, vous pourriez vouloir séparer votre config dans plusieurs fichiers dans un package `config`, mais pour ce tutoriel et la majorité des mods nous allons tout mettre dans un seul fichier.

Créons un nouveau fichier pour notre config, dans le même dossier que notre `ModInitializer`. Nous l'appelerons `ExampleModConfig`, mais vous pouvez l'appeler comme vous voulez. Cette nouvelle classe de configuration hérite de l'API `ReflectiveConfig` qui fournit tout ce dont nous avons besoin pour faire fonctionner la config.

Nous commencerons avec ce code :

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    public static final ExampleModConfig INSTANCE = QuiltConfig.create(ExampleMod.MOD_ID, ExampleMod.MOD_ID, ExampleModConfig.class);
}
```

Cette grosse ligne au milieu peut sembler intimidante alors on va l'expliquer morceau par morceau.

- D'abord, `public static final` signifie que la valeur ne peut jamais changer (bien que les données _contenues_ dans la config le peuvent) et `instance` est un mot utilisé par les programmeur·euse·s pour désigner l'objet d'une classe. On créé cet object de cette manière car c'est la seule instance de la config que nous ferons.
- Ensuite, nous appelons la méthode `QuiltConfig.create(String family, String id, Class<C> configCreatorClass)`. Comme vous pouvez le voir elle prend trois paramètres :
  - Le string `family`, qui indique le dossier dans lequel votre config sera stockée, par rapport au dossier de configuration global (en général `<your instance directory>/config`). Nous utiliserons l'ID de notre mod comme dossier et c'est une bonne pratique.
  - Un deuxième string `id`, qui sera le nom du fichier de configuration (avant que l'extension de fichier soit ajouté, ce qui change en fonction du format que vous choisissez). Ici nous utilisons à nouveau l'ID de notre mod mais si vous avez une config compliqué avec plusieurs fichiers vous voudrez utiliser différents noms.
  - Enfin, quelque-chose de bizarre : c'est quoi un `Class<C> configCreatorClass` ? On ne va pas trop s'occuper des détails : passez simplement le nom de votre classe de configuration avec `.class` ajouté à la fin, comme on l'a fait ici avec `ExampleModConfig.class`.

## Ajouter des valeurs

Voilà ! Nous avons maintenant une config à laquelle nous pouvons accéder partout dans notre projet. Le problème étant, on n'a aucune raison d'y accéder parce qu'il n'y a rien. Réglons ce problème en ajoutant une valeur ! Notre mod d'exemple écrit une ligne de texte dans la console au démarage : pourquoi pas permettre à l'utilisateur·ice de choisir quelle est cette ligne. Ajoutons un deuxième champ dans notre config :

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public final TrackedValue<String> message = this.value("rai minecraft :thumbsup:");
}
```

On présente encore plein de choses avec cette seule ligne ! Regardons tout ça :

- Cette valeur est `public final`, ce qui signifie qu'au lieu d'être accessible par n'importe quoi il faut d'abord une instance de `ExampleModConfig`. Notez qu'il _faut_ utiliser `public final` quand vous créer une valeur de config en utilisant Quilt Config. Vu qu'on a déjà défini notre champ `INSTANCE`, nous pourrons cette valeur n'importe où via `ExampleModConfig.INSTANCE.message`.
- Le type est `TrackedValue<String`. Les chevrons (`<>`) nous permettent d'utiliser des _generics_, une méthode d'adapter une classe à votre besoin spécifique en Java. Ici, la classe `TrackedValue` nous permet d'adapter le type d'objet qu'elle contient, donc nous l'utilisons pour contenir un `String`. Grâce aux generics, nous pourrions mettre un autre type dans ces chevrons plus tard pour contenir un autre type de valeur ! C'est du foreshadowing. Nous allons stocker d'autres valeurs. Soyez prêt·e·s.
- Nous appelons la méthode `value`, qui vient de la classe `ReflectiveConfig` dont on hérite. Cette méthode prend un paramètre, qui sera la valeur par défaut d'une valeur de configuration. Ici, l'autrice originale de ce tutoriel l'utilise pour pour faire sa promo.

Ouf. On s'en est sorti ! C'était le dernier gros afflux d'informations de ce tutoriel et on peut commencer à s'amuser un peu. Maintenant qu'on a un champ dans notre config, on peut retourner à notre `ModInitializer` et commencer à l'utiliser !

`src/main/com/example/example_mod/ExampleMod`:

```java
public class ExampleMod implements ModInitializer {
    // ...

    @Override
    public void onInitialize(ModContainer mod) {
        LOGGER.info(ExampleModConfig.message.value());
    }
}
```

Comme le champ `message` est une `TrackedValue<String>` et pas un simple `String`, nous devons appeler sa méthode `value` pour récupérer sa valeur et pouvoir l'écrire dasn la console. Nous pourrons aussi utiliser la méthode `setValue` pour changer la valeur depuis le code.

Maintenant notre mod écrit un message personnalisé dans la console au démarage ! Bien que cela nous permettrait facilement d'obtenir 1 million de téléchargements, Quilt Config nous permet de faire beaucoup plus que ça.

## Utiliser des annotations

Ajoutons un deuxième champ. Ici nous allons faire quelque chose d'un peu plus recherché en utilisant à la fois un nouveau type de données et une des nombreuses annotations de Quilt Config.

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @IntegerRange(min = 0, max = 10)
    public final TrackedValue<Integer> timesToPrint = this.value(1);
}
```

Nous n'allons pas montrer les changements dans le code pour utiliser chacune de ces valeurs mais n'oubliez pas que vous pouvez voir comment elles sont utilisées dans le [code terminé](https://github.com/ix0rai/quilt-config-example-mod). Revenons à nos listes à puces pour expliquer cet extrait !

- `IntegerRange` est une annotation : elle ajoute un nouvelle fonctionnalité à notre champ, au délà de la déclaration normale. `IntegerRange` nous permet de limiter les valeurs possibles de notre champ `Integer`, pour qu'elles soient contenues entre `min` et `max` (de manière inclusive).
- Nous avons changé le type de `String` à `Integer`. Pourquoi `Integer` au lieu de `int` comme nous l'utiliserions pour un champ numérique normal ? `int` est un _type primitif_, ce qui signifie que ce n'est pas une classe ! Comme les generics ne peuvent prendre que des classes, Java fournit des classes pour chaque primitive. `boolean` devient `Boolean`, `float` devient `Float`, `double` devient `Double`, `char` devient `Character`, etc, etc.

Quelque chose de primordial à ne pas oublier est que vous ne pouvez pas simplement mettre n'importe quelle classe dans les generics de `TrackedValue` : Quilt Config doit savoir comment le sérialiser. Par défaut, tous les [types primitifs](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) sont sérialisables ainsi que la classe `String`. Nous expliquerons comment dire à Quilt Config comment sérialiser une classe plus tard mais d'abord : plus de fun avec des annotations !

Nouvelle valeur !

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("Whether to print the message at all.")
    public final TrackedValue<Boolean> print = this.value(true);
}
```

Ici on utilise l'annotation `Comment` pour ajouter un commentaire expliquant exactement ce que fait le champs de la configuration. Ça sera sauvegarder par Quilt Config dans le fichier de configuration, tant que le format que vous utilisez permet les commentaires. Pratique !

Enfin, nous allons utiliser une autre annotation pour conclure. Par défaut, Quilt Config serialise en [TOML](https://toml.io/en/) qui a quelques standards que l'on ne suit pas. En Java, les champs `public final` sont nommés en utilisant la `lowerCamelCase`, comme notre champ `timesToPrint`. Mais, dans des fichiers TOML, les noms devrait utiliser la `snake_case`, signifiant que nous devrions nommer le champ `times_to_print`. Pour suivre les deux conventions, nous pouvons utiliser l'annotation `SerializedName` !

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @IntegerRange(min = 0, max = 10)
    @SerializedName("times_to_print")
    public final TrackedValue<Integer> timesToPrint = this.value(1);
    // ...
}
```

Problème résolu ! Maintenant notre champ suit la convention de Java dans le code Java et la convention de TOML dans le fichier TOML. Nous pouvons utiliser l'annotation `SerializedName` pour tous les champs de config avec des noms à plusieurs mots pour résoudre notre problème partout.

## Maps and lists

Deux des structures de données les plus communes utilisées en Java sont les `Map` et les `List`. Quilt Config fournit des versions pratiques et sérialisables de ces structures de données avec `ValueMap` et `ValueList` !

En commençant avec les maps, `ReflectiveConfig` nous fournit une méthode `map` pour nous aider à créer une valeur par défaut facilement.

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("When a key in this map shows up in the message, it'll be replaced with the corresponding value.")
    public final TrackedValue<ValueMap<String>> replacements = this.map("")
        .put("oro armor", "rai minecraft")
        .put("lambda aurora", "rai minecraft")
        .put("tib s", "rai minecraft")
        .build();
}
```

Il y a quelques éléments intéressants ici :

- Nous devons passer un string vide (`""`) à la méthode `map` pour qu'elle comprenne que la map stocke des valeurs de type `String`. Cette valeur n'est pas utilisée pour autre chose que vérifier les types donc vous pouvez mettre n'importe quoi ici.
- `ValueMap` utilise toujours `String` comme type pour ses clefs, au lieu de vous permettre de choisir comme avec la plupart des implémentations de `Map` en Java.
- On peut mettre autant de valeur que l'ont veut dans la map par défaut. Si l'autrice voulait remplacer tous les développeur·euse·s Quilt avec elle-même au lieu de se limiter aux admins, ça serait possible !

Maintenant que l'on sait comment utiliser des maps, continuons avec les listes !

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("This isn't actually used by the mod, but I was completely out of ideas for things to add.")
    @SerializedName("types_of_soup")
    public final TrackedValue<ValueList<String>> typesOfSoup = this.list("", "tomato", "borscht", "chicken noodle", "ramen", "STEW", "mushroom");
}
```

C'est assez similaire à la création d'une map. Ici, au lieu de d'enchainer des appels à `puts` sur un builder, on ajoute simplement autant de valeur que l'on veut directement dans le constructeur. Encore une fois, le premier argument n'est pas utilisé et est ici pour aider Quilt Config à déduire le type. Ceci étant fait, nous avons fait une excellente petite config pour notre mod ! Si vous voulez en apprendre plus, continuons avec le tutoriel de [Configuration Avancée](../configuration/advanced-configuring).
