package org.quiltmc.wiki.simple_item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class SimpleItemExample implements ModInitializer {

	// @start Declaration
	public static final Item EXAMPLE_ITEM = new Item(new QuiltItemSettings().tab(CreativeModeTab.TAB_MISC));
	// @end Declaration

	@Override
	public void onInitialize(ModContainer mod) {
		// @start Registration
		Registry.register(Registry.ITEM, new ResourceLocation(mod.metadata().id(), "example_item"), EXAMPLE_ITEM);
		// @end Registration
	}
}
