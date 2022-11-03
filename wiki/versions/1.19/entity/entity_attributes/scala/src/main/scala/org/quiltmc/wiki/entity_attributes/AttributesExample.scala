package org.quiltmc.wiki.entity_attributes

import net.minecraft.entity.attribute.{ClampedEntityAttribute, EntityAttribute, EntityAttributeModifier}
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

import java.util.UUID

class AttributesExample extends ModInitializer {

    override def onInitialize(mod: ModContainer): Unit = {
        val modId = mod.metadata.id

        // @start Register-Attribute
        Registry.register(Registry.ATTRIBUTE, Identifier(modId, "generic.jump_boost"), AttributesExample.GENERIC_JUMP_BOOST)
        // @end Register-Attribute
        Registry.register(Registry.ITEM, Identifier(modId, "jump_stick"), AttributesExample.JUMP_STICK)
        CommandRegistrationCallback.EVENT.register { (dispatcher, _, _) =>
            JumpBoostCommand.register(dispatcher)
        }
    }
}
object AttributesExample {

    // @start Attribute-Instance
    final val GENERIC_JUMP_BOOST: EntityAttribute = ClampedEntityAttribute("attribute.name.generic_jump_boost", 1.0, 0.0, 2.0).setTracked(true)
    // @end Attribute-Instance

    // @start Modifier-Instance
    final val SOME_MODIFIER_ID: UUID = UUID.fromString("de17612a-adec-11ec-b909-0242ac120002")

    final val SOME_MODIFIER: EntityAttributeModifier = EntityAttributeModifier(
        SOME_MODIFIER_ID,
        "Jump stick modifier",
        0.5,
        EntityAttributeModifier.Operation.ADDITION
    )
    // @end Modifier-Instance

    final val JUMP_STICK: JumpStick = JumpStick(QuiltItemSettings().group(ItemGroup.TOOLS))
}
