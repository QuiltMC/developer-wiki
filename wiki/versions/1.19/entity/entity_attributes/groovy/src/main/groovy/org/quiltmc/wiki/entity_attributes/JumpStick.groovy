package org.quiltmc.wiki.entity_attributes

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import groovy.transform.CompileStatic
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item

@CompileStatic
class JumpStick extends Item {

    JumpStick(Properties properties) {
        super(properties)
    }

    @Override
    Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        // If held in main hand, return our modifier, otherwise nothing
        (slot == EquipmentSlot.MAINHAND) ? ImmutableMultimap.of(AttributesExample.GENERIC_JUMP_BOOST, AttributesExample.SOME_MODIFIER)
                : super.getDefaultAttributeModifiers(slot)
    }
}
