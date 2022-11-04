package org.quiltmc.wiki.entity_attributes;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.UUID;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.CreativeModeTab;

public class AttributesExample implements ModInitializer {

	// @start Attribute-Instance
	public static final Attribute GENERIC_JUMP_BOOST = new RangedAttribute("attribute.name.generic_jump_boost", 1.0, 0.0, 2.0).setSyncable(true);
	// @end Attribute-Instance

	// @start Modifier-Instance
	public static final UUID SOME_MODIFIER_ID = UUID.fromString("de17612a-adec-11ec-b909-0242ac120002");

	public static final AttributeModifier SOME_MODIFIER = new AttributeModifier(
			SOME_MODIFIER_ID,
			"Jump stick modifier",
			0.5,
			AttributeModifier.Operation.ADDITION
	);
	// @end Modifier-Instance

	public static final JumpStick JUMP_STICK = new JumpStick(new QuiltItemSettings().tab(CreativeModeTab.TAB_TOOLS));

	@Override
	public void onInitialize(ModContainer mod) {
		var modId = mod.metadata().id();

		// @start Register-Attribute
		Registry.register(Registry.ATTRIBUTE, new ResourceLocation(modId, "generic.jump_boost"), GENERIC_JUMP_BOOST);
		// @end Register-Attribute
		Registry.register(Registry.ITEM, new ResourceLocation(modId, "jump_stick"), JUMP_STICK);
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> {
			JumpBoostCommand.register(dispatcher);
		});
	}
}
