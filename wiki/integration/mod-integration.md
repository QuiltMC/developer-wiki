---
title: Mod integration
---

# Mod integration

One of the main advantages of creating mods with Quilt is the ability to provide unique functionality depending on if another mod is installed.

Some mods explicitly provide an API (Application Programming Interface) which can be included in your own project as a dependency. This acts like a bridge between 2 mods that allows you to utalize features of that mod in your own project, without including the entire mods codebase, which is likely to constantly be changing.

A mod does not have to include an API for you to interact with it however. You can choose to execute sections of code depending on if a mod is loaded and if the mod is declared as a dependency you can use content from it.
  
Other ways of achieving mod integration can include loading objects from a common registry such as a specific Item, Block or Entity; Creating common tags for resources to prevent duplicates / allow shared behaviour and using Mixins to modify the behaviour of another mod.

## Licensing restrictions

Before deciding to add any kind of integration with other mods please check the existing `LICENSE` restrictions.

Most Minecraft mods are now open source and free to modify without permission but this is not always the case. Depending on the license some may request attribution is given or there code is not modified in a certain way. Some mods may also have an `All rights reserved` license meaning no kind of modification is allowed without asking the developers for explicit permission first.

It is of course ok to check if a mod is installed and adjust your own mod accordingly.

## Including other mods & libraries

Other mods can be included within the `dependencies` section of the `build.gradle` file. The `.jar` file for mod dependencies will be located within a specific `repository` which also must be included. 

If you are confused about the `libs.example.mod` notation you may want to read through [LINK]() first.

```gradle
repositories {
	// Import the maven repository that stores the files we need
	maven {
		name = 'Example Mod API'
		url = 'https://maven.examplemod.org/releases'
	}
}

dependencies {
	// Remap from the maven repository and apply different types of dependency configurations
	modImplementation libs.example.mod.implemented
	modImplementation (include (libs.example.mod.included))
	modCompileOnly libs.example.mod.compiled
}

```
As we can see there are various different ways mods can be included in our `build.gradle` file. These can achieve different purposes depending on what you need.

### Gradle dependency configurations

A table is included below showing a couple different mod dependency configurations. This is not a full list but should get you started.

| Configuration | Explanation | Example Usage |
| --- | --- | --- |
| modCompileOnly | Included at compile time within the development environment only. There is no guarentee this mod will be included at runtime within an instance of Modded Minecraft | An optional dependency which is not required for a mod to run but can provide additional functionality |
| modImplementation | Included as a dependency. Must be included both at `compile` time and `runtime` within your development environment and in a Modded Minecraft instance | Relying on another library. This library must be manually included by a player in the `mods` folder| 
| include | `Shades` a dependency. This means the `.jar` file is included in the final build without another user having to include it themselves | If you want to bundle one mod alongside another `*` |

`*` <small> Be careful with this configuration as within a modpack multiple versions of the same mod can be included causing conflicts. It can also make it difficult for players to debug a mod if they see another mod included that they don't recognise. </small>

### Using a library / API

Normally developers who want you to implement certain functionality from there mod will include information on how to do so. This will include lines to add to the `build.gradle` file, excluding the exact version which you must fill in. 
  
Most developers also include a wiki describing how to use the mod but this is not always the case. If you are having difficulties it can be helpful to see if other mods have used a specific library / API and learn by example or ask the mod developer(s) on what to do next. You can of course experiment if all else fails.

If a mod is hosted using the `git` version control system you can also import the mod using [Jitpack](https://jitpack.io).

## Conditional execution

You may decide that it is worth executing a certain piece of code depending on if a mod is loaded or not. 
To do this you want to check if the mod you want to add compatibility for is loaded using `QuiltLoader.isModLoaded(id)` where `id` is the unique identifier for the target mod.

This identifier is kept in the `quilt.mod.json` file for Quilt mods and the `fabric.mod.json` file for Fabric mods within the `/resources` folder. 

The following gives a basic example of conditional code execution depending on if the mod `example-mod` is installed. 

```java
public static void conditionalExecution() {
	// Only execute the codeblock within the lambda expression if a mod with the id: example-mod is loaded
    executeIfInstalled("example-mod", () -> () -> {
        // Code to execute...
    });
}

public static void executeIfInstalled(String modId, Supplier<Runnable> toExecute) {
	// Check if a mod is loaded and if so execute a runnable code-block if so
	if(QuiltLoader.isModLoaded(modId)) { 
		toExecute.get().run();
	}
}
```
<small>Code segment acquired and modified from <a href="https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/compat/Mods.java">Create (Forge) - Mods.java</a></small>

## Registry objects

In the same way objects such as Items, Blocks and Entities can be registered within a mod they can also be retrieved from any other mod by matching the mod id and the path to the object.

```java
Item exampleItem = Registries.ITEM.get(new Identifier("example-mod", "example_item"));
```

### Practical Example

The following describes a practical example of how to obtain an item from another mod. In this case a `pink duck` from [Lucky Ducks](https://github.com/UltrusBot/Lucky-Ducks) when a user starts sleeping.

```java
    @Override
	public void onInitialize(ModContainer mod) {
		EntitySleepEvents.START_SLEEPING.register(ExampleMod::onStartSleeping); // (1)
	}

	private static void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
		Item duckItem = getRegisteredObject(Registries.ITEM, "luckyducks", "rubber_duck").orElse(Items.SPONGE); // (2)
		ItemStack duckItemStack = duckItem.getDefaultStack();

		duckItemStack.getOrCreateSubNbt("duckEntity").putString("type", "luckyducks:pink"); // (4)

		if(entity instanceof PlayerEntity playerEntity) { // (5)
			playerEntity.giveItemStack(duckItemStack);
		}
	}

	public static <T> Optional<T> getRegisteredObject(Registry<T> registry, String modID, String path) {
		return registry.getOrEmpty(new Identifier(modID, path)); // (3)
	}
```

1. Trigger an event when any living entity starts sleeping
2. Obtain a registry object from a certain mod. In this case the `rubber_duck` item from the `luckyducks` mod. A fallback is vital if nothing is found. In this case if the mod is not present a `sponge` is given instead.
3. Get the associated registry object depending on the mods id and path to the object. For those not familiar the `T` generic type means the method can accept any registry type it wants. For example using `Registries.BLOCK` is just as valid.
4. We have the default item stack but now we need to associate NBT data with the duck item, in this case we specify that we want the duck to be pink.
5. Finally check if the sleeping entity is a player. If they are a pink duck will be gifted!

## Metadata

All Quilt mods come with additional descriptive data called metadata.
  
This data can be used to extract additional context from other Quilt mods installed alongside your own, such as a mods contributors, description and licences. 

Better compatibility can also be established by checking a mods version and whether it depends on or breaks other mods. 


## Mod Containers

A Mod Container holds a mods associated metadata alongside other important information such as the type of mod (Quilt, Fabric, Built-in etc...), the location of it's root directory and sub-directories.

## Example Files

- **<a href="/integration/Mods.java" target="_blank">Mods.java</a>** - An example of an easy way to store multiple mod id's in an enum class and reference them directly when needed.