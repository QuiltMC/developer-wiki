---
title: Mod integration
---

# Mod integration

One of the main advantages of creating mods with Quilt is the ability to provide unique functionality depending on if another mod is installed.

Some mods explicitly provide an API (Application Programming Interface) which can be included in your own project within the `build.gradle` file. This acts like a bridge between 2 mods that allows you to utalize features of that mod in your own project, without including the entire mods codebase which is likely to constantly be changing.

A mod does not have to include an API for you to interact with it however. Blocks, Items, Entities etc... are all registered in the same place and so should be accesible from any mod. Resources can share commmon id's allowing duplicates and common functionality. Mixins can also be used to directly modify the behaviour of other mods. Although doing so should be exercised with caution.

## Licensing restrictions

Before deciding to add any kind of integration with other mods please check the existing `LICENSE` restrictions.

Most Minecraft mods are now open source and free to modify without permission but this is not always the case. Depending on the license some may request attribution is given or there code is not modified in a certain way. Some mods may also have an `All rights reserved` license meaning no kind of modification is allowed without asking the developers for explicit permission first.

It is of course ok to check if a mod is installed and adjust your own mod accordingly.

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

## Mixin Injection

Lucky Ducks will again be used to demonstrate some example code and as the victim for a Mixin injection. Applying a Mixin to another mod can be considered invasive depending on what is being done. If a mod is hosted using git it is generally a better idea to post a pull request with the functionality you need to the mod developer. Nevertheless inter-mod Mixins can be an options for a quick and(or) temporary fix.

## Example Files

- **<a href="/integration/Mods.java" target="_blank">Mods.java</a>** - An example of an easy way to store multiple mod id's in an enum class and reference them directly when needed.