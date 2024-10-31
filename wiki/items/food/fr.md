# Ajouter de la Nourriture

Ce tutoriel part du principe que vous savez déjà [Créer votre Premier Item](first-item).

Ajouter un `FoodComponent` aux paramètres d'un item fait de cet item de la nourriture.
Un `FoodComponent` peut être créé en utilisant un `FoodComponent.Builder`.
Essayez d'utiliser la documentation intégrée (Javadoc) du builder pour comprendre comment il fonctionne,
ou continuez à lire pour une explication :

```java
public static final Item EXAMPLE_FOOD = new Item(new QuiltItemSettings().food(
	new FoodComponent.Builder()
		.hunger(2)
		.saturationModifier(1)
		.snack()
		.alwaysEdible()
		.meat()
		.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 10), 0.8f)
		.build()
));
```

Maintenant que vous avez bien lu la Javadoc _tousse, tousse_, voici une explication alternative :

1. On instancie un nouveau builder. Grâce à cela, on peut configurer le `FoodComponent` avant de vraiment l'instancier.
   Notez que toutes les valeurs suivantes sont par défaut 0, `false` ou `null`.
2. On passe sa valeur de faim à 2, ce qui correspond à un des 10 indicateurs de faim dans minecraft.
3. On donne une valeur au modificateur de saturation, qui, multiplié par la valeur de hunger et 2, est ajouté à la saturation.
4. On en fait un snack, ce qui signifie qu'il pourra être mangé rapidement, comme les algues séchées.
5. On le rend toujours mangeable (même quand la barre de faim est pleine), comme la soupe suspecte.
6. On le note comme étant de la viande, pour que les loups puisse le manger.
7. On lui ajoute un effet de Vision Nocture qui sera appliqué pendant 10 secondes avec une chance de 80%.
8. Enfin, on instancie un `FoodComponent` à partir du builder en utilisant la méthode `build`.

## Et Après ?

Si vous voulez continuer à ajouter des items, voyez [Armure](armor) ou [Outils](tools).
