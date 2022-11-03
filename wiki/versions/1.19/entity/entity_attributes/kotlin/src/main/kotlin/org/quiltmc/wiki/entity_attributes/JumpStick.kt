package org.quiltmc.wiki.entity_attributes

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.Item

class JumpStick(settings: Settings): Item(settings) {

    override fun getAttributeModifiers(slot: EquipmentSlot): Multimap<EntityAttribute, EntityAttributeModifier> {
        // If held in main hand, return our modifier, otherwise nothing
        return if (slot == EquipmentSlot.MAINHAND) ImmutableMultimap.of(GENERIC_JUMP_BOOST, SOME_MODIFIER)
        else super.getAttributeModifiers(slot)
    }
}
