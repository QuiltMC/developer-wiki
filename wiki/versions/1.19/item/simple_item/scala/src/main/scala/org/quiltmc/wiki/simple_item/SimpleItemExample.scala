package org.quiltmc.wiki.simple_item

import net.minecraft.item.{Item, ItemGroup}
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

// Can be made an object with a language adapter for scala
class SimpleItemExample extends ModInitializer {

    override def onInitialize(mod: ModContainer): Unit = {
        // @start Registration
        Registry.register(Registry.ITEM, Identifier(mod.metadata.id, "example_item"), SimpleItemExample.EXAMPLE_ITEM)
        // @end Registration
    }
}
object SimpleItemExample {

    // @start Declaration
    final val EXAMPLE_ITEM: Item = Item(QuiltItemSettings().group(ItemGroup.MISC))
    // @end Declaration
}
