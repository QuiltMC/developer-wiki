# Додавання власних типів світу

Якщо твій мод повністю змінює генерацію світу Minecraft, тобі, ймовірно, знадобиться створити власний тип світу, щоб дати користувачам вибір: використовувати твою генерацію світу або стандартну Minecraft.

Що ж таке "тип світу", можеш запитати? _Типи світу_, також відомі як _пресети світу_, визначають параметри генерації світу в Minecraft. Вони дозволяють змінити вигляд світу, налаштовуючи поведінку генерації.

Деякі приклади типів світу, які вже є у грі: "Amplified", "Super Flat", "Single Biome", "Large Biomes".

## Вказання, до яких вимірів застосовується тип світу

Наступний блок показує, як вказати, до яких вимірів буде застосовуватись тип світу. У полі `dimensions` обов’язково повинен бути об’єкт `minecraft:overworld`.

Тип виміру `type` може бути стандартним або створеним у твоєму моді.

`src/main/resources/data/minecraft/example_mod/worldgen/world_preset`:

```json
{
	"dimensions": {
		"minecraft:overworld": {
			"type": "example_mod:example_mod_dimension_type"
		}
	}
}
```

## Налаштування генерації у власному типі світу

Поле `generator` визначає, як буде працювати генерація світу у твоєму типі. `type` — це ID генератора. Дійсні значення:

- `noise`
- `flat`: створює суперплоский світ
- `debug`: створює світ для налагодження

У `biome_source` задається спосіб генерації біомів. Типи генерації:

- `multi_noise`: як у стандартному Overworld
- `fixed`: один обраний біом
- `the_end`: як у Енді (не дуже підходить сюди)
- `checkerboard`: біоми чергуються, як на шаховій дошці

Якщо тип `biome_source` — `minecraft:multi_noise`, Minecraft буде використовувати 3D-біоми, як в Overworld і Nether.

`preset` може бути `minecraft:overworld` або `minecraft:nether`. Більше інформації в [Вікі Minecraft](https://minecraft.wiki/w/Custom_dimension#Multi-noise_biome_source_parameter_list).

`src/main/resources/data/minecraft/example_mod/worldgen/world_preset/example_mod_preset.json`:

```json
{
	"dimensions": {
		"minecraft:overworld": {
			"type": "minecraft:overworld",
			"generator": {
				"type": "minecraft:noise",
				"biome_source": {
					"type": "minecraft:multi_noise",
					"preset": "minecraft:overworld"
				}
			}
		}
	}
}
```

Це згенерує світ, подібний до стандартного Overworld.

На сервері потрібно змінити `level-type` у `server.properties`. Стандартне значення — `minecraft:normal`. Заміни його на ID твого типу світу (`example_mod:example_mod_preset`).

## Робимо тип світу доступним

Щоб тип світу з'явився у вікні створення нового світу, створи файл `normal.json` у `resources/data/minecraft/tags/worldgen/world_preset` і встав туди:

```json
{
	"values": ["example_mod:example_mod_preset"]
}
```

Це дозволить вибрати твій тип світу при створенні нового світу.

Альтернативно, якщо хочеш, щоб він з’являвся лише при натиснутій клавіші ALT, створи файл `extended.json` у тій же папці.
			
