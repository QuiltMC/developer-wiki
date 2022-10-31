package org.quiltmc.wiki.simple_item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

class SimpleItemExample implements ModInitializer {

    // @start Declaration
    static final Item EXAMPLE_ITEM = new Item(new QuiltItemSettings().group(ItemGroup.MISC))
    // @end Declaration

    @Override
    void onInitialize(ModContainer mod) {
        // @start Registration
        Registry.ITEM[new Identifier(mod.metadata().id(), 'example_item')] = EXAMPLE_ITEM
        // @end Registration
    }
}
