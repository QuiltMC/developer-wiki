# Додавання сета броні

Цей посібник припускає, що ви вже розумієте, як створювати свій перший предмет ([Creating your First Item](first-item)).

## Створення матеріалу броні

Щоб додати комплект броні, потрібно почати з створення матеріалу броні. Це визначає основні властивості вашої броні, такі як міцність, рівень захисту або витривалість.
Для цього потрібно створити новий клас матеріалу броні:

`src/main/com/example/example_mod/ExampleArmorMaterial`:

```java
public class ExampleArmorMaterial implements ArmorMaterial {
    //...
```

Замініть шлях на відповідні значення для вашого мода, а назву матеріалу броні виберіть згідно з основним ресурсом. Наприклад, матеріал броні, який базується на смарагдах, буде називатися `EmeraldArmorMaterial`. Ви також можете використовувати `enum`, якщо у вас є кілька різних типів броні, так само як це робить Minecraft.

---

Тепер давайте додамо значення міцності та захисту:

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

Тут ми використовуємо зручний трюк: `slot.getEquipmentSlot().getEntitySlotId()` повертає значення між 0 та 3 в залежності від частини броні, тому ми можемо використовувати ці значення як індекс для масиву.
Захист відповідає значенню броні.

---

Далі ми визначимо [Enchantability](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability). Це визначає, наскільки добре чарівності, отримані в столику для чарування, працюють. Наприклад, броня з нетеріту та золота має високу [Enchantability](https://minecraft.wiki/w/Enchanting_mechanics#Enchantability).

```java
@Override
public int getEnchantability() {
	return 10;
}
```

---

Тепер час визначити звук одягання. Якщо ви хочете додати свій власний звук, подивіться на [Додавання звуків](../misc/sounds). Якщо ви хочете використовувати існуючий звук, ви можете використати `SoundEvents.ITEM_ARMOR_EQUIP_GENERIC`, як показано в прикладі.

```java
@Override
public SoundEvent getEquipSound() {
	return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
}
```

---

Тепер потрібно вказати інгредієнти для ремонту, в даному випадку використаємо прикладний предмет з [Creating your First Item](first-item):

```java
@Override
public Ingredient getRepairIngredient() {
	return Ingredient.ofItems(ExampleMod.EXAMPLE_ITEM);
}
```

---

Нарешті, потрібно вказати ім'я матеріалу броні, його витривалість та опір до відштовхування.
Ім'я матеріалу повинно зазвичай відповідати назві основного предмета, з якого виготовлена ваша броня. Витривалість допомагає зменшувати значний шкоди та відштовхування, а опір до відштовхування працює лише для броні з нетеріту у ванільному Minecraft, але він був доданий для інших броньових матеріалів завдяки [QSL](../concepts/qsl-qfapi).

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

## Додавання самих предметів

Тепер, коли ми оголосили матеріал броні, давайте додамо самі предмети броні:

```java
public static final ArmorMaterial EXAMPLE_ARMOR_MATERIAL = new ExampleArmorMaterial();
public static final Item EXAMPLE_HELMET = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings());
public static final Item EXAMPLE_CHESTPLATE = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings());
public static final Item EXAMPLE_LEGGINGS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings());
public static final Item EXAMPLE_BOOTS = new ArmorItem(EXAMPLE_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings());
```

Спочатку ми ініціалізуємо матеріал броні та зберігаємо його в фінальній статичній змінній. Потім ми створюємо самі предмети, щоб зареєструвати їх пізніше. Зазначте, що ми створюємо `ArmorItem` у всіх випадках і просто передаємо `ArmorItem.ArmorSlot`, щоб вказати, який слот використовувати. В кінці передаємо налаштування, оскільки розмір стеку для броні автоматично встановлюється на 1, тому ми передаємо стандартні налаштування.

---

Тепер реєструємо броню в ініціалізаторі мода та додаємо її до групи бою. Тут є два моменти, на які потрібно звернути увагу:

1. Ми називаємо предмети броні як ім'я матеріалу + його варіант.
2. Ми додаємо їх до групи предметів, використовуючи метод `addAfter`. Оскільки це броня, ми хочемо, щоб вона відображалась поруч з усією ванільною бронею, тому використовуємо цей метод, який дозволяє додавати кілька предметів після вже наявного предмета.

```java
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_helmet"), EXAMPLE_HELMET);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_chestplate"), EXAMPLE_CHESTPLATE);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_leggings"), EXAMPLE_LEGGINGS);
Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "example_item_boots"), EXAMPLE_BOOTS);
ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
	entries.addAfter(Items.NETHERITE_BOOTS, EXAMPLE_HELMET, EXAMPLE_CHESTPLATE, EXAMPLE_LEGGINGS, EXAMPLE_BOOTS);
});
```

## Додавання текстур для броні

Тепер, коли броня зареєстрована, залишилося тільки додати текстури та переклади.
Для броні потрібно надати дві текстури: одну для самих предметів і одну для броні, яка буде відображатися на моделі гравця.

---

Додавання текстури до предмета таке ж, як і для звичайного предмета: вам потрібно додати модель і текстуру. Ось модельний файл для прикладу шолома:

```json
{
	"parent": "item/generated",
	"textures": {
		"layer0": "example_mod:item/example_item_helmet"
	}
}
```

Повторіть це для всіх ваших предметів броні, замінивши `example_mod` та `example_item_helmet` на ваш мод і назву предмета, а також додайте відповідні текстури.

---

Тепер броня повинна правильно відображатися в інвентарі, але коли ви її одягнете, вона все одно буде показувати відсутню текстуру. Для того, щоб це працювало, потрібно також додати модель броні. Модель броні зазвичай складається з двох текстур, організованих, як шкіра гравця. Однак різні частини броні малюють різні частини цієї текстури.
Черевики, шолом і нагрудник використовують текстуру `layer_1` (і мають більшу відстань від шкіри гравця), а штани використовують текстуру `layer_2`.
Якщо хочете, ви можете використовувати цей шаблон текстури. Різні частини броні використовують різні тони, щоб допомогти відрізняти їх.

`assets/minecraft/models/armor/example_item_layer_1.png`:  
![Текстура броні для layer 1](path_to_example_item_layer_1.png)

`assets/minecraft/models/armor/example_item_layer_2.png`:  
![Текстура броні для layer 2](path_to_example_item_layer_2.png)

Замість `example_item` використовуйте назву вашого матеріалу броні в шляху. Важливо,

 щоб шляхи відповідали вказаним моделям.
