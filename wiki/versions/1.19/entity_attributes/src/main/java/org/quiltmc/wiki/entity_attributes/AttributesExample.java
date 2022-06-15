package org.quiltmc.wiki.entity_attributes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.UUID;

public class AttributesExample implements ModInitializer {

	// @start Attribute-Instance
	public static final EntityAttribute GENERIC_JUMP_BOOST = new ClampedEntityAttribute("attribute.name.generic_jump_boost", 1.0, 0.0, 2.0).setTracked(true);
	// @end Attribute-Instance

	// @start Modifier-Instance
	public static final UUID SOME_MODIFIER_ID = UUID.fromString("de17612a-adec-11ec-b909-0242ac120002");

	public static final EntityAttributeModifier SOME_MODIFIER = new EntityAttributeModifier(
			SOME_MODIFIER_ID,
			"Jump stick modifier",
			0.5,
			EntityAttributeModifier.Operation.ADDITION
	);
	// @end Modifier-Instance

	public static final JumpStick JUMP_STICK = new JumpStick(new QuiltItemSettings().group(ItemGroup.TOOLS));

	@Override
	public void onInitialize(ModContainer mod) {
		// @start Register-Attribute
		Registry.register(Registry.ATTRIBUTE, new Identifier("example", "generic.jump_boost"), GENERIC_JUMP_BOOST);
		// @end Register-Attribute
		Registry.register(Registry.ITEM, new Identifier("example", "jump_stick"), JUMP_STICK);
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> {
			JumpBoostCommand.register(dispatcher);
		});
	}
}
