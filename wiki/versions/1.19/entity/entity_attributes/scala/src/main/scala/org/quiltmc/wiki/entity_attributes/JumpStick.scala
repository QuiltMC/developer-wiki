package org.quiltmc.wiki.entity_attributes

import com.google.common.collect.{ImmutableMultimap, Multimap}
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.{EntityAttribute, EntityAttributeModifier}
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings

class JumpStick(settings: Settings) extends Item(settings) {

    override def getAttributeModifiers(slot: EquipmentSlot): Multimap[EntityAttribute, EntityAttributeModifier] =
        // If held in main hand, return our modifier, otherwise nothing
        if (slot == EquipmentSlot.MAINHAND) ImmutableMultimap.of(AttributesExample.GENERIC_JUMP_BOOST, AttributesExample.SOME_MODIFIER)
        else super.getAttributeModifiers(slot)
}
