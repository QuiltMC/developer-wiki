package org.quiltmc.wiki.entity_attributes;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;

public class JumpStick extends Item {

	public JumpStick(Settings settings) {
		super(settings);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		// If held in main hand, return our modifier, otherwise nothing
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(AttributesExample.GENERIC_JUMP_BOOST, AttributesExample.SOME_MODIFIER);
			return builder.build();
		}
		return super.getAttributeModifiers(slot);
	}
}
