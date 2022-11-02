package org.quiltmc.wiki.simple_item

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

// @start Declaration
final Item EXAMPLE_ITEM = new Item(new QuiltItemSettings().group(CreativeModeTab.TAB_MISC))
// @end Declaration

// @start Registration
// arg0 is the ModContainer
Registry.ITEM[new ResourceLocation(arg0.metadata().id(), 'example_item')] = EXAMPLE_ITEM
// @end Registration
