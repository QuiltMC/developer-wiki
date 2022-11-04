package org.quiltmc.wiki.entity_attributes;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class JumpStick extends Item {

	public JumpStick(Properties settings) {
		super(settings);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		// If held in main hand, return our modifier, otherwise nothing
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(AttributesExample.GENERIC_JUMP_BOOST, AttributesExample.SOME_MODIFIER);
			return builder.build();
		}
		return super.getDefaultAttributeModifiers(slot);
	}
}
