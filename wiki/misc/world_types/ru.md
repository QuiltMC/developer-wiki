# Добавление пользовательских типов мира

Если твой мод полностью перерабатывает генерацию мира Minecraft, тебе, скорее всего, стоит создать собственный тип мира, чтобы дать пользователям выбор — использовать твою генерацию или стандартную.

Что же такое _тип мира_? Типы мира (или _пресеты мира_) определяют, как будет происходить генерация мира. С их помощью можно полностью изменить структуру мира.

Примеры встроенных типов: "Amplified", "Super Flat", "Single Biome", "Large Biomes".

## Указание, для каких измерений действует тип мира

В блоке ниже показано, как указать, на какие измерения действует тип мира. В `dimensions` обязательно должен быть объект `minecraft:overworld`.

Тип измерения `type` может быть встроенным или кастомным (твоим).

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

## Настройка генерации мира

`generator` определяет, как будет работать генерация мира в твоем типе. Доступные типы генераторов:

- `noise`
- `flat`: создаёт суперплоский мир
- `debug`: отладочный мир

В `biome_source` указывается, как будут генерироваться биомы. Типы:

- `multi_noise`: как в стандартном оверворлде
- `fixed`: один выбранный биом
- `the_end`: как в Энде (не актуально)
- `checkerboard`: биомы размещаются в стиле шахматной доски

Если `type` в `biome_source` равен `minecraft:multi_noise`, Minecraft будет использовать 3D-биомы.

`preset` может быть `minecraft:overworld` или `minecraft:nether`. Подробнее см. [в Minecraft Wiki](https://minecraft.wiki/w/Custom_dimension#Multi-noise_biome_source_parameter_list).

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

Этот пресет создаст мир, похожий на стандартный Overworld.

На сервере открой файл `server.properties` и измени значение поля `level-type` с `minecraft:normal` на ID твоего пресета (`example_mod:example_mod_preset`).

## Отображение пресета в интерфейсе

Чтобы пресет отображался в меню создания мира, создай файл `normal.json` в папке `resources/data/minecraft/tags/worldgen/world_preset` и добавь:

```json
{
	"values": ["example_mod:example_mod_preset"]
}
```

Этот файл делает твой тип мира доступным в интерфейсе при создании мира.

Если хочешь, чтобы он появлялся только при зажатой клавише ALT — создай файл `extended.json` в той же папке.
