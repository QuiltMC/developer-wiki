# Configuration Avancée

Les valeurs simples c'est sympa mais si vous en avez beaucoup ça peut rapidement devenir compliqué à gérer. Dans ce tutoriel, nous allons voir comment organiser votre config et utiliser des processeurs pour en tirer le maximum.

## Utiliser des sections

Un simple fichier avec dizaines de valeurs à la suite peut rapidement devenir complexe à comprendre et à utiliser. Heureusement on peut l'organiser en plusieurs sections en utilisant Quilt Config ! C'est très simple à réaliser.

Avec des sections, vous pouvez utiliser de l'indentation pour differencier visuellement les parties de votre fichiers de configuration pour faciliter la lecture des utilisateur·ice·s. Nous allons ajouter une section d'exemple qui ressemblera à ça dans le fichier TOML :

```toml
# ...

# This isn't actually used by the mod, but I was completely out of ideas for things to add.
typesOfSoup = ["tomato", "borscht", "chicken noodle", "ramen", "STEW", "mushroom"]

# Advanced settings for advanced users.
[advanced_settings]
    # Whether to automatically append newlines to every message printed.
    # default: true
    printNewlines = true
```

Pour faire cela, nous allons créer une section dans notre code :

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    @Comment("Advanced settings for advanced users.")
    @SerializedName("advanced_settings")
    public final AdvancedSettings advancedSettings = new AdvancedSettings();

    public static class AdvancedSettings extends Section {
        @Comment("Whether to automatically append newlines to every message printed.")
        @SerializedName("print_newlines")
        public final TrackedValue<Boolean> printNewlines = this.value(true);
    }
}
```

Nous ajoutons simplement une nouvelle classe, dans notre classe de configuration, qui hérite de `ReflectiveConfig.Section`. Ensuite nous devons créer un autre objet dans notre config principale pour ajouter cette section. Notez que cette instance de notre section n'est **pas** contenue dans une `TrackedValue` comme le reste, en fait elle contient elle-même des `TrackedValue` ! Maintenant que l'on a une section, on peut ajouter autant de valeurs que l'on veut dedans. Et si on veut contenir quelque-chose de plus intéressant que des types basiques, des maps ou des listes ? Serialisons un objet personnalisé.

## Sérialiser des valeurs personnalisées

En Java, vous pouvez écrire dans plusieurs flux de sortie dans la console. Ce ne sont pas des objets serialisables basiques comme des entiers ou des `String` donc on ne peut pas simplement les contenir dans notre config ! C'est là que l'interface `ConfigSerializableObject<T>` intervient. En implémentant ses trois méthodes, on peut mettre en place n'importe quelle classe pour pouvoir les utiliser comme objet de configuration.

L'interface fonctionne avec des generics, comme `TrackedValue`. Le `<T>` dans `ConfigSerializableObject<T>` peut être remplacé par n'importe quelle classe sérialisable (par défaut ce sont les types primitifs, `String`, `ValueList` et `ValueMap`) et la valeur de votre objet sera traduit en ce type pour être sauvegardé en dur puis sera reconverti en votre objet personnalisé en étant lu. Pour faire cette traduction, nous devons implémenter trois méthodes :

- `T getRepresentation()` : ici, votre valeur est converti en la classe sérialisable que vous avez spécifié dans les generics (représenté par `T`) pour qu'elle puisse être sauvegardée.
- `YourSerializableClass convertFrom(T)` : celle-là est appelée en lisant le fichier de configuration et reconverti la représentation créée par `getRepresentation` en son type original.
- `YourSerializableClass copy()` : réalise une copie de la valeur que Quilt Config utilise internalement.

Assez avec les explications : voyons un exemple !

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public static class AdvancedSettings extends Section {
        // ...
        @Comment("What stream to print the message to.")
        @SerializedName("print_stream")
        public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);

        @SuppressWarnings("unused") // les IDEs ne comprendront pas que toutes les options de cet enum peuvent être utilisées via la config
        public enum PrintStreamOption implements ConfigSerializableObject<String> {
            SYSTEM_OUT(System.out),
            SYSTEM_ERROR(System.err);

            private final PrintStream printStream;

            PrintStreamOption(PrintStream stream) {
                this.printStream = stream;
            }

            public PrintStream getStream() {
                return this.printStream;
            }

            @Override
            public PrintStreamOption convertFrom(String representation) {
                return valueOf(representation);
            }

            @Override
            public String getRepresentation() {
                return this.name();
            }

            @Override
            public PrintStreamOption copy() {
                // les valeurs d'enum ne peuvent pas être dupliquées
                return this;
            }
        }
    }
}
```

Cela peut avoir l'air d'être beaucoup de code mais on en a déjà couvert la majorité ! Ici on utilise un `Enum` qui nous permet de définir clairement une liste d'options pour notre valeur. Si vous le vouliez, pour pourriez utiliser une classe normale et avoir un nombre infinie de possibilités pour votre champ de configuration ! Un autre bonus avec l'utilisation d'un `Enum` est que Quilt Config va automagiquement générer un commentaire avec les valeurs possibles dans le fichier de configuration sérialisé.

L'implémentation des méthodes de `ConfigSerializableObject<T>` est incroyablement simple ici : comme les valeurs de notre enum ont déjà des noms, on utilisé simplement les méthodes `name` et `valueOf(String)` pour sérialiser et désérialiser respectivement. Pratique ! Regardons un autre exemple d'un objet personnalisé qui ne peut pas être représenté comme un enum.

Disons que l'on veut stocker un point sur une grille 3d dans la config. Nous devrions stocker des coordonées `x`, `y` et `z` pour cette valeur. Faisons cela ! C'est un simple exemple et ne sera pas utilisé dans notre mod.

```java
public class Vec3i implements ConfigSerializableObject<ValueMap<Integer>> {
    public final int x;
    public final int y;
    public final int z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vec3i copy() {
        return this;
    }

    @Override
    public Vec3i convertFrom(ValueMap<Integer> representation) {
        return new Vec3i(representation.get("x"), representation.get("y"), representation.get("z"));
    }

    @Override
    public ValueMap<Integer> getRepresentation() {
        return ValueMap.builder(0)
                .put("x", this.x)
                .put("y", this.y)
                .put("z", this.z)
                .build();
    }
}
```

Ici on utilise une `ValueMap` au lieu d'un `String` comme type sérialisé. Cela nous permet de facilement distinguer les champs `x`, `y` et `z` et c'est le type que vous utiliserez presque à chaque fois pour sérialiser des objets complexes comme celui-ci. On ne va pas aller trop en profondeur ici, le code est assez parlant maintenant que l'on comprend l'interface `ConfigSerializableObject<T>`.

## Utiliser des processeurs

Maintenant que nous avons tout apprit sur les valeurs, apprenons à faire des choses malveillantes : voici les `Processeur`s.
Cette annotation sournoise vous permet de configurer votre config et ses valeur, ainsi que d'ajouter des callbacks de modifications, qui vous permettent d'appeler du code quand une valeur est modifiée.
L'annotation fonctionne en vous permettant de désigner du code qui sera appelé à la construction de la config.
D'abord, mettons en place un simple processeur qui écrit dans la console quand la config commence à être chargée.

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Loading config!");
    }

    // ...
}
```

Avec ça, notre config écrira "Loading config!" dans la console avant qu'aucune de ses valeurs n'ai été désérialisé. Notez que bien que le nom de la méthode soit passée à `@Processor` sans information sur des paramètres, nous devons quand même mettre un `Config.Builder` dans notre méthode : mais pourquoi ?
Les processeurs peuvent être attachés à trois différents types : des valeurs de la configuration, des sections de la configuration ou des classes de configuration. Dans chacun des cas, le paramètre sera différent comme expliqué dans la Javadoc de `Processor` :

- Quand utilisé avec une valeur, la méthode du processeur prendra un `TrackedValue.Builder` comme paramètre.
- Quand utilisé avec une section, la méthode du processeur prendra un `SectionBuilder` comme paramètre.
- Quand utilisé avec une classe de configuration, la méthode du processeur prendra un `Config.Builder` comme paramètre.

Mais on peut faire plus avec les processeur qu'écrire n'importe quoi dans la console ! Voyons ce que l'on peut faire avec cet object `Builder` qui nous a été donné.
Sur les valeur et les classes de configurations, nous pouvons utiliser une méthode appelée `callback` pour mettre en place du code qui est appelé quand la config est modifiée !

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    public void processConfig(Config.Builder builder) {
        System.out.println("Loading config!");
        builder.callback(config -> System.out.println("Updated!"));
    }

    // ...
}
```

Avec cette ligne, on a développé notre logging pour nous dire quand une valeur de la config est modifiée ! Sympa, mais que peut-on faire d'autres avec ces callbacks ?

Un exemple d'utilisation de callback est de synchroniser entre deux champs de la configuration. Cela pourrait être nécessaire pour plusieurs raisons : la valeur de votre config est compliquée et vous voullez la rendre plus accessible, ou peut-être que vous devez modifier la configuration d'une des librairies dont vous dépendez quand une valeur est modifiée (c'est ce que fait [enigma](https://github.com/QuiltMC/enigma) !).
Nous allons mettre en place un racourcit pour accéder au flux de sortie rendu disponible par `printStream`, cela ne vous force pas à utiliser deux getters pour l'utiliser. Pour faire cela, on va utiliser un processeur appliqué au champ !

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
    // ...
    public static class AdvancedSettings extends Section {
        // ...
        @Processor("processPrintStream")
        public final TrackedValue<PrintStreamOption> printStream = this.value(PrintStreamOption.SYSTEM_OUT);
        public transient PrintStream activeStream = printStream.value().getStream();

        public void processPrintStream(TrackedValue.Builder<PrintStreamOption> builder) {
            builder.callback(value -> activeStream = printStream.value().getStream());
        }

        // ...
    }
}
```

En utilisant notre callback, nous mettons à jour la variable `activeStream` à chaque fois que le flux de sortie est changé. Cela le garde parfaitement synchronisé avec le champ `printStream` en tout temps ! Notez que l'on marque cette variable avec le mot clef `transient` qui dit à Java (et donc aussi à Quilt Config !) de ne pas sérialiser la valeur.
Maintenant au lieu de se casser la tête avec `ExampleModConfig.INSTANCE.advancedSettings.printStream.value().getStream()` on peut simplement faire `ExampleModConfig.INSTANCE.advancedSettings.activeStream`, simplifiant nos vie un petit peu quand on utilise la config. Le pouvoir des processeurs en action.

## Changer le format de la config

Voyons comment choisir un format de fichier pour sauvegarder votre config. Quilt Config ne fournit pour l'instant que deux sérialisateurs : `json5`, une extension du format JSON qui permet une syntax plus propre et des commentaires, et `toml`, qui est la valeur par défaut. Si vous voulez utiliser `json5`, nous pouvons le faire en utilisant un `Processor` !
Nous aurons besoin d'appliquer ce processeur globalement à notre configuration comme nous allons changer le format via l'objet `Config.Builder` fournit par un processeur de classe de configuration.

Ce processeur a besoin d'être appelé avant que la config soit lue donc nous allons le mettre directement sur la classe :

`src/main/com/example/example_mod/ExampleModConfig`:

```java
@Processor("processConfig")
public class ExampleModConfig extends ReflectiveConfig {
	public void processConfig(Config.Builder builder) {
		// ...
		builder.format("json5");
	}

	// ...
}
```

Avec notre connaissance des processeurs, c'est très simple ! Vous pouvez également utiliser le config builder pour ajouter des nouveaux champs, nouvelles sections et mettre à jour les [métadonnées](../configuration/metadata) en plus de changer le format et utiliser des callback comme nous l'avons déjà montré.

## Ajouter plusieurs fichiers

Pour des très gros mods, un seul fichier de configuration peut vite devenir difficile à géré, même découpé en sections.
Heureusement, Quilt Config est designé pour permettre d'ajouter facilement plusieurs fichiers de configuration : par défaut, vos fichiers de configuration sont tous placés dans leurs propres dossiers.
Pour ajouter un deuxième fichier dans ce dossier, nous devons créer une autre classe de configuration : appelons là `ExampleModConfig2`.
Nous devrons aussi changer le nom de notre fichier de config originale pour être plus spécifique :

`src/main/com/example/example_mod/ExampleModConfig`:

```java
public class ExampleModConfig extends ReflectiveConfig {
    public static final ExampleModConfig INSTANCE = QuiltConfig.create(ExampleMod.MOD_ID, "main", ExampleModConfig.class);
}
```

Au lieu d'utiliser l'ID du mod comme nom de notre classe, nous appelons notre config originale `main`.
Maintenant créons une deuxième config :

`src/main/com/example/example_mod/ExampleModConfig2`:

```java
public class ExampleModConfig2 extends ReflectiveConfig {
    public static final ExampleModConfig2 INSTANCE = QuiltConfig.create(ExampleMod.MOD_ID, "secondary", ExampleModConfig2.class);
}
```

La même chose qu'avec notre originale mais en utilisant la classe `ExampleModConfig2` à la place de `ExampleModConfig` partout. Nous l'appelons aussi `secondary` pour aller avec le nom `main` de notre config originale.
Maintenant vous pouvez ajouter les champs que vous voulez ! Avec Quilt Config, vous pouvez répéter ce procédé autant que vous le voulez, tant qu'aucunes des configs n'ont des noms en doublons.
