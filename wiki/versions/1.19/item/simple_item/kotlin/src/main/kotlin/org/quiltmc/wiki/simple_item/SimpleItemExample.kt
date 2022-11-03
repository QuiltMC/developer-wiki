package org.quiltmc.wiki.simple_item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.wrapper.minecraft.registry.registryScope
import org.quiltmc.qkl.wrapper.qsl.items.itemSettingsOf
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

// @start Declaration
val EXAMPLE_ITEM: Item = Item(itemSettingsOf(group = ItemGroup.MISC))
// @end Declaration

object SimpleItemExample: ModInitializer {

    override fun onInitialize(mod: ModContainer) {
        // @start Registration
        registryScope(mod.metadata().id()) {
            EXAMPLE_ITEM withPath "example_item" toRegistry Registry.ITEM
        }
        // @end Registration
    }
}
