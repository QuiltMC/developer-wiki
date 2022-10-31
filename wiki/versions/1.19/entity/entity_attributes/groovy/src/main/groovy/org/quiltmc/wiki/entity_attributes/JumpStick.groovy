package org.quiltmc.wiki.entity_attributes

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import groovy.transform.CompileStatic
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.Item

@CompileStatic
class JumpStick extends Item {

    JumpStick(Settings settings) {
        super(settings)
    }

    @Override
    Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        // If held in main hand, return our modifier, otherwise nothing
        (slot == EquipmentSlot.MAINHAND) ? ImmutableMultimap.of(AttributesExample.GENERIC_JUMP_BOOST, AttributesExample.SOME_MODIFIER)
                : super.getAttributeModifiers(slot)
    }
}
