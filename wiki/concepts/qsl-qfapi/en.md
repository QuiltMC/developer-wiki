# QSL and QFAPI Overview

This article explains the different standard libraries you might encounter when modding with Quilt.

## What is an API?

An API (Application Programming Interface) provides an interface for programs to interact with other programs. In the context of Minecraft modding, APIs tend to provide tools for better compatibility between mods or provide other useful extensions. For example, many of the bigger mods you might know of have an API which allows other mods to improve compatibility with them.

In addition to that kind of API, there are libraries. Libraries themselves do not add any content to the game, and instead implement an API to make certain features simpler to implement for other developers. Fabric API and Quilt Standard Libraries are examples of official libraries published by the specific loader.

It is important to note that an API only defines the _means_ to interact with a specific program and the _corresponding behavior_, _not its actual implementation_. **The implementations may change at any time and thus shouldn't be used.** Implementations tend to contain `impl` in the package or class name, while APIs sometimes are in an `api` package.

## Fabric API

Fabric API (FAPI for short) is Fabric's API and provides some useful APIs that Quilt Standard Libraries does not provide, such as a majority of rendering APIs, the item group API and a key binds API. For Quilt, [Quilted Fabric API](#quilted-fabric-api) is an alternate implementation, that uses Quilt Standard Libraries where possible.

## Quilt Standard Libraries

The Quilt Standard Libraries (QSL for short) provide lots of core functionality for mods, as well as many useful APIs, such as [Registry Entry Attributes](../data/rea).

Some features QSL provides are:

- The [main entrypoints](sideness#on-mod-initializers) for mods.
- Loading the `assets` and `data` directory of your mod through the [resource loader](../data/resource-loader).
- Many [events](events) such as world tick events.
- Extension classes and interfaces for many blocks, items, entities, and more.

While you can make your mod work with just [mixin](mixins), QSL does a lot of the heavy lifting and makes sure that common features mods have won't cause conflicts between them. QSL will also try to keep its API fairly stable between Minecraft versions, so that you have to do less work when updating your mod.

However, QSL's API is not complete enough to provide all functionality that would be provided by Fabric API (Likewise, FAPI does not provide everything QSL provides), which is why you'll find yourself using Quilted Fabric API in projects quickly.

## Quilted Fabric API

Quilted Fabric API (QFAPI for short) provides Fabric API, but implements the API using QSL where possible. APIs from FAPI with a proper QSL alternative are deprecated in QFAPI, so consider using QSL APIs when some Fabric API methods are deprecated.

Quilted Fabric API has two main use cases:

- It provides a compatibility layer so that Fabric mods can be loaded on Quilt.
- And it allows using Fabric's APIs when QSL does not yet provide relevant APIs. This is for example the case for [item groups](../items/first-item#adding-the-item-to-a-group).

Because QFAPI depends on QSL and needs to know Fabric's implementation, it will always update after Fabric API and QSL have been updated.

QFAPI also includes QSL, meaning that you'll usually want to only use QFAPI. The downloads on [Modrinth](https://modrinth.com/mod/qsl) and [Curseforge](https://www.curseforge.com/minecraft/mc-mods/qsl) also are QFAPI and QSL packaged together.
