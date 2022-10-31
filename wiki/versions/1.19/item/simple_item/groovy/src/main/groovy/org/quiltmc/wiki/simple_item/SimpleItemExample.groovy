package org.quiltmc.wiki.simple_item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

// @start Declaration
final Item EXAMPLE_ITEM = new Item(new QuiltItemSettings().group(ItemGroup.MISC))
// @end Declaration

// @start Registration
// arg0 is the ModContainer
Registry.ITEM[new Identifier(arg0.metadata().id(), 'example_item')] = EXAMPLE_ITEM
// @end Registration
