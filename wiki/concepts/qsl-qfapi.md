---
title: QSL and QFAPI Overview
---

# QSL and QFAPI Overview

This article explains the different standard libraries you might encounter when modding with Quilt.

## What is an API?

An API (Application Programming Interface) provides an interface for programs to interact with other programs. In the context of Minecraft modding, APIs tend to provide tools for better compatibility between mods or provide other useful extensions. For example, many of the bigger mods you might know of have an API which allows other mods to improve compatibility with them.

In addition to that kind of API, there are libraries. Libraries themselves do not add any content to the game, and instead implement an API to make certain features simpler to implement for other developers. Fabric API, QSL and QFAPI are all official libraries published by the specific loader.

It is important to note that an API only defines the *means* to interact with a specific program and the *corresponding behavior*, *not its actual implementation*. **The implementations may change at any time and thus shouldn't be used.** Implementations tend to contain `impl` in the package or class name, while APIs sometimes are in an `api` package.

## Fabric API

Fabric API (short FAPI) is fabric's API and provides some useful APIs that QSL does not provide. For Quilt, [Quilted Fabric API](#quilted-fabric-api) is an alternate implementation, that uses QSL where possible.

## Quilt Standard Libraries

Quilt Standard Libraries (short QSL) provide lots of core functionality for mods, as well as many useful APIs, such as [REA](../data/rea).

The [main entrypoints](sideness#on-mod-initializers) are provided by QSL, and QSL loads the `assets` directory of your mod. This means that unless you use [mixin](mixins), basically nothing will work without QSL.

However, QSL's API is not complete enough to provide all functionality that would be provided by Fabric API (Likewise, FAPI does not provide everything QSL provides), which is why you'll find yourself using QFAPI in projects quickly.

## Quilted Fabric API

Quilted Fabric API (short QFAPI) provides Fabric API, but implements the API using QSL where possible. APIs from FAPI with a proper QSL alternative are deprecated in QFAPI, so consider using QSL APIs when some Fabric API methods are deprecated.

Quilted Fabric API has two main use cases:

- It provides a compatibility layer so that Fabric mods can be loaded on Quilt.
- And it allows using fabric's APIs when QSL does not yet provide relevant APIs. This is for example the case for [item groups](../items/first-item#adding-the-item-to-a-group).

Because QFAPI depends on QSL and needs to know fabric's implementation, it will always update after Fabric API and QSL released.

QFAPI also includes QSL, meaning that you'll usually want to only use QFAPI. The downloads on [Modrinth](https://modrinth.com/mod/qsl) and [Curseforge](https://www.curseforge.com/minecraft/mc-mods/qsl) also are QFAPI and QSL packaged together.
