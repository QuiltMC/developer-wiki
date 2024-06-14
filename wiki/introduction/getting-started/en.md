# Getting Started with Quilt

## Basic Requirements

In order to start modding Minecraft, you'll need to know a couple of things.

- Basic Java knowledge. This includes understanding its syntax, object-oriented
  programming, and some essential design patterns.
- Basic [Git](https://git-scm.com/) knowledge. While technically not necessary, it'll
  make your life a lot easier. This includes understanding how to clone repositories
  and make commits. Some great resources include the [Git docs](https://git-scm.com/doc) and the [GitHub docs](https://docs.github.com/en/get-started).

<!-- TODO: Is there anything more to be described here? -->

## How to Use this Wiki

<!-- TODO: Create example code repository and put it here ([Issue #68](https://github.com/QuiltMC/developer-wiki/issues/68)) -->

This wiki contains pages that are best read in order. Many of the articles, especially at the beginning, suggest other articles sensible to continue with. Most pages will contain code snippets, with full examples planned to be provided in example mods. (This is not done yet)

In the next article, you will set up your first mod to get started with mod development.

After that, it is recommended to look into [Creating your First Item](../items/first-item). Then, you are ready for [adding blocks](../blocks/first-block), or creating advanced items, like [food](../items/food), [tools](../items/tools) and [armor](../items/armor).

For more general explanations, you can look into the Concepts category:

- In [QSL and QFAPI Overview](../concepts/qsl-qfapi) you get an overview on Quilt Standard Libraries and Quilted Fabric API, Quilt's official APIs.

Many more articles are planned, but not done yet ([Issue #69](https://github.com/QuiltMC/developer-wiki/issues/69)). This section will be updated later.

<!-- TODO: Give an outline of all of the wiki articles once they're ready. -->

## How to Learn Modding, for Beginners

Minecraft modding can get complicated. We aim to provide a resource to help you learn
fundamental modding concepts. However, there are some things that we can't do and
expect you to learn on your own.

Asking questions properly, whether it is on our forum or on Discord, is an essential
skill to have. Never be afraid to ask questions, we're always willing to help, and we
don't judge.

When asking questions, try to include the necessary context in your first message, and [do not ask to ask](https://dontasktoask.com/). State your problem, what you have already tried to solve it, and how those attempts failed. Make sure to avoid the [XY problem](https://xyproblem.info/) by not only asking for help for one specific part of a solution to your problem, but also stating the actual problem you are trying to solve. When debugging, include your `latest.log` and when your game crashes, a crash log. Additionally, full access to your source code can be really helpful, ideally uploaded to a website such as GitHub.

You can ask development related questions in our [Forum](https://forum.quiltmc.org/), as well as in the [mod-dev-help](https://discord.com/channels/817576132726620200/1047429688521396325) forum channel in our [Discord](https://discord.quiltmc.org/)

Many problems have been solved in modding before, and are publicly available for you
to see and use as most mods for Quilt are open source. We recommend that you look at
examples of how to do things in open source repositories and sometimes "steal" code.
Open source repositories are there for you to learn from and use, don't be afraid of
them!

There are many topics in modding where you quickly will find yourself at a point where there are no tutorials to help. In those cases you'll usually either have to understand the relevant Minecraft code, or look at other mods and see how they got similar things done.
If you want to look into the Minecraft source code, run the `genSourcesWithVineflower` Gradle task in the `fabric` category or open any Minecraft source file and click the download sources button in IntelliJ IDEA.

<!-- TODO: Is this todo fixed?: Levi write your thing about stealing code here -->
