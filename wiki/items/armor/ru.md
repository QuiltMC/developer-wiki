# Добавление сета брони

Этот гайд предполагает, что вы уже знакомы с созданием своего первого предмета ([Создание первого предмета](first-item)).

## Создание материала брони

Чтобы добавить комплект брони, нужно начать с создания материала брони. Это определяет основные характеристики вашей брони, такие как прочность, уровень защиты или стойкость.
Для этого нужно создать новый класс материала брони:

`src/main/com/example/example_mod/ExampleArmorMaterial`:

```java
public class ExampleArmorMaterial implements ArmorMaterial {
    //...
```

Замените путь на соответствующие значения для вашего мода, а название материала брони выберите согласно основному ресурсу. Например, материал брони, основанный на изумрудах, будет называться `EmeraldArmorMaterial`. Вы также можете использовать `enum`, если у вас есть несколько различных типов брони, так же как это делает Minecraft.

---

Теперь давайте добавим значения прочности и защиты:

```java
private static final int[] DURABILITY = new int[]{100,160,100,100};
private static final int[] PROTECTION = new int[]{2,8,4,2};
@Override
public int getDurability(ArmorItem.ArmorSlot slot) {
    return DURABILITY[slot.getEquipmentSlot().getEntitySlotId()];
}

@Override
public int getProtection(ArmorItem.ArmorSlot slot) {
    return PROTECTION[slot.getEquipmentSlot().getEntitySlotId()];
}
```

Здесь мы используем удобный трюк: `slot.getEquipmentSlot().getEntitySlotId()` возвращает значение от 0 до 3 в зависимости от части брони, так что мы можем использовать эти значения как индекс для массива.
Защита соответствует значению брони.

---

Далее мы определим [Enchantability](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability). Это определяет, насколько хорошо чарования, полученные на столе для зачаровывания, работают. Например, броня из нетерита и золота имеет высокий [Enchantability](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability).

```java
@Override
public int getEnchantability() {
    return 10;
}
```

---

Теперь время указать звук надевания. Если вы хотите добавить свой собственный звук, посмотрите на [Добавление звуков](../misc/sounds). Если хотите использовать уже существующий, можете использовать `SoundEvents.ITEM_ARMOR_EQUIP_GENERIC`, как показано в примере.

```java
@Override
public SoundEvent getEquipSound() {
    return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
}
```

---

Теперь нужно указать ингредиенты для ремонта, в данном случае используем предмет, созданный в [Создании первого предмета](first-item):

```java
@Override
public Ingredient getRepairIngredient() {
    return Ingredient.ofItems(ExampleMod.EXAMPLE_ITEM);
}
```

---

Наконец, нужно указать имя материала брони, его стойкость и сопротивление отталкиванию.
Имя материала должно обычно соответствовать названию основного предмета, из которого изготовлена ваша броня. Стойкость помогает уменьшать получаемый урон и отталкивание, а сопротивление отталкиванию работает только для брони из нетерита в ванильном Minecraft, но было добавлено для других броневых материалов через [QSL](../concepts/qsl-qfapi).

```java
@Override
public String getName() {
    return "example_item";
}
```

```java
@Override
public float getToughness() {
    return 2;
}
```

```java
@Override
public float getKnockbackResistance() {
    return 0.2f;
}
```

## Добавление самих предметов

Теперь, когда мы объявили материал брони, давайте добавим сами предметы брони:

```java
public static final ArmorMaterial EXAMPLE_ARMOR_MATERIAL = new ExampleArmorMaterial();
public static final Item EXAMPLE_HELMET = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings());
public static final Item EXAMPLE_CHESTPLATE = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings());
public static final Item EXAMPLE_LEGGINGS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings());
public static final Item EXAMPLE_BOOTS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings());
```

Сначала мы инициализируем материал брони и сохраняем его в финальной статической переменной. Затем мы создаем сами предметы, чтобы зарегистрировать их позже. Обратите внимание, что мы создаем `ArmorItem` в каждом случае и просто передаем `ArmorItem.ArmorSlot`, чтобы указать, какой слот использовать. В конце передаем настройки, так как размер стека для брони автоматически устанавливается на 1, поэтому мы передаем стандартные настройки.

---

Теперь регистрируем броню в инициализаторе мода и добавляем её в группу боевых предметов. Здесь есть два момента, на которые нужно обратить внимание:

1. Мы называем предметы брони как имя материала + его вариант.
2. Мы добавляем их в группу предметов, используя метод `addAfter`. Так как это броня, мы хотим, чтобы она отображалась рядом с уже существующей броней, поэтому используем этот метод, который позволяет добавлять несколько предметов после уже имеющегося предмета.

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_helmet"), EXAMPLE_HELMET);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_chestplate"), EXAMPLE_CHESTPLATE);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_leggings"), EXAMPLE_LEGGINGS);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_boots"), EXAMPLE_BOOTS);
ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
    entries.addAfter(Items.NETHERITE_BOOTS, EXAMPLE_HELMET, EXAMPLE_CHESTPLATE, EXAMPLE_LEGGINGS, EXAMPLE_BOOTS);
});
```

## Добавление текстур для брони

Теперь, когда броня зарегистрирована, осталось только добавить текстуры и переводы.
Для брони нужно предоставить две текстуры: одну для самих предметов и одну для брони, которая будет отображаться на модели игрока.

---

Добавление текстуры для предмета такое же, как и для обычного предмета: вам нужно добавить модель и текстуру. Вот пример модели для шлема:

```json
{
    "parent": "item/generated",
    "textures": {
        "layer0": "example_mod:item/example_item_helmet"
    }
}
```

Повторите это для всех ваших предметов брони, заменив `example_mod` и `example_item_helmet` на ваш мод и название предмета, а также добавьте соответствующие текстуры.

---

Теперь броня должна правильно отображаться в инвентаре, но когда вы её оденете, она всё равно будет показывать отсутствующую текстуру. Для того, чтобы это работало, нужно также добавить модель брони. Модель брони обычно состоит из двух текстур, организованных, как кожа игрока. Однако разные части брони рисуют разные части этой текстуры.
Ботинки, шлем и нагрудник используют текстуру `layer_1` (и имеют большее расстояние от кожи игрока), а штаны используют текстуру `layer_2`.
Если хотите, вы можете использовать этот шаблон текстуры. Разные части брони используют разные оттенки, чтобы помочь отличать их.

`assets/minecraft/models/armor/example_item_layer_1.png`:  
![Текстура брони для layer 1](path_to_example_item_layer_1.png)

`assets/minecraft/models/armor/example_item_layer_2.png`:  
![Текстура брони для layer 2](path_to_example_item_layer_2.png)

Замените `example_item` на название вашего материала брони в пути. Важно, чтобы пути соответствовали указанным моделям.
