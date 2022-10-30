package org.quiltmc.wiki.simple_item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

class SimpleItemExample : ModInitializer {
    override fun onInitialize(mod: ModContainer) {
        // @start Registration
        Registry.register(Registry.ITEM, Identifier("simple_item", "example_item"), EXAMPLE_ITEM)
        // @end Registration
    }

    // @start Declaration
    companion object {
        val EXAMPLE_ITEM = Item(QuiltItemSettings().group(ItemGroup.MISC))
    }
    // @end Declaration
}
