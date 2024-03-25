# Référence pour les Annotations et les Métadonnées

Quilt Config est plein d'annotations qui vous permettent toutes d'étendre les fonctionnalités de votre config ! Cette page sert de référence, donnant un apperçu des fonctionnalités de chaque annotation. Des informations plus en profondeurs peuvent être trouvées dans la Javadoc de chaque annotation.

## `@Comment`

- Utilisable sur les champs et les sections de configuration
- Peut être répété à l'infini sur un élément

Vous permet d'attacher des commentaires à vos champs qui sont sauvegardés comme métadonnées. Par défaut ils seront sérialisés dans le fichier de configuration dans les formats `toml` et `json5`. Comme ils sont sauvegardés comme métadonnée, vous pouvez y accéder n'importe où où vous avez acès aux `TrackedValue` pour les champs de votre config, signifiant que vous pouvez les afficher dans des écrans de configuration ou autre.

## `@FloatRange` et `@IntegerRange`

- Utilisables sur les champs de configuration de type `Float` et `Integer` respectivement
- Ne peut être utilisée qu'une fois par élément

Au démarage et quand les champs avec ces annotations sont modifiés, vérifie si les valeurs des champs sont bien contenues entre les `min` et `max` fournient, de manière inclusive. Si ce n'est pas le cas, lance une erreur `TrackedValueException`.

## `@Matches`

- Utilisable sur les champs de configuration de type `String`
- Ne peut être utilisée qu'une fois par élément

Au démarage et quand les champs avec cette annotations sont modifiés, vérifie si les valeurs de ces champs correspondent à l'[expression régulière](https://regexr.com/) fournie. Si ce n'est pas le cas, lance une erreur `TrackedValueException`.

## `@Processor`

- Utilisable sur les champs, les sections et les classes de configuration
- Ne peut être utilisée qu'une fois par élément

Permet de mettre en place des méthodes qui seront appelées avant que la config ne soit initialisée. Prend un paramètre `String` qui correspond le nom d'une méthode dans votre classe : cette méthode sera appelée avant l'initialisation de la config. Vous devez une paramètre à la méthode correspondant au type avec l'annotation `@Processor` :

- Quand utilisé avec une valeur, la méthode du processeur prendra un `TrackedValue.Builder` comme paramètre.
- Quand utilisé avec une section, la méthode du processeur prendra un `SectionBuilder` comme paramètre.
- Quand utilisé avec une classe de configuration, la méthode du processeur prendra un `Config.Builder` comme paramètre.

Les processeurs vous permettent de faire une grande variété de choses, comme par exemple :

- Appeler du code personnalisé quand des valeurs sont modifiées
- Ajouter des nouvelles valeurs et sections via du code
- Changer dynamiquement les métadonnées

Vous pouvez voir des processeurs d'exemple et une apperçu plus en profondeur de leurs usages dans le [tutoriel de configuration avancée](../configuration/advanced-configuring#utiliser-des-processeurs).

## `@SerializedName`

- Utilisable sur les champs et section de configuration
- Ne peut être utilisée qu'une fois par élément

Ajoute une métadonnée `String` utilisée comme le nom qui sera sérialisé dans le fichier de configuration. Le nom par défaut reste en fallback : si le nom sérialisé n'est pas présent dans le fichier pendant la désérialisation, le nom par défaut sera utilisé. Le nom par défaut sera toujours utilisé pour écrire dans le fichier.

## `@SerializedNameConvention`

- Utilisable sur les champs, les section et les classes de configuration
- Ne peut être utilisée qu'une fois par élément

Ajoute des métadonnées sur la convention de nommage pour chaque champ dans la classe de configuration qui dictent quel convention utiliser pour sauvegarder dans la config. Cela transformera le nom par défaut du champ pour appliquer la convention contenue dans les métadonnées. Si le nom a une annotation `@SerializedName` elle prendra la priorité sur celle-ci. Les options de conventions de nommages sont documentées dans `NamingSchemes`. Quand cette annotation est utilisée sur un champs ou une section dans un parent qui a aussi cette annotation, remplace la valeur du parent pour cet élément seulement (par exemple, si une classe a cette annotation avec une certaine convention et un champs dans cette classe a aussi cette annotation avec une autre convention, le champ prendra la convention de son annotation à lui).

## `@Alias`

- Utilisable sur les champs et les section de configuration
- Peut être répété à l'infini sur un élément

Ajoute des métadonnées précissant d'autres noms pour cet élément. Ces noms seront pris en compte pendant la désérialisation mais ne seront jamais écrit dans le fichier de configuration. Vous pouvez par exemple utiliser cette annotation avec d'anciens noms utilisés pour un champs afin de faciliter la mise à jour des configurations !
