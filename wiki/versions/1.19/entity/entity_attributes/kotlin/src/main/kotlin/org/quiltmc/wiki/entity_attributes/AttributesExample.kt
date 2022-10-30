package org.quiltmc.wiki.entity_attributes

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.wrapper.minecraft.brigadier.argument.literal
import org.quiltmc.qkl.wrapper.minecraft.brigadier.execute
import org.quiltmc.qkl.wrapper.minecraft.brigadier.register
import org.quiltmc.qkl.wrapper.minecraft.brigadier.required
import org.quiltmc.qkl.wrapper.minecraft.registry.registryScope
import org.quiltmc.qkl.wrapper.qsl.commands.onCommandRegistration
import org.quiltmc.qkl.wrapper.qsl.items.itemSettingsOf
import org.quiltmc.qkl.wrapper.qsl.registerEvents
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import java.util.*

// @start Attribute-Instance
val GENERIC_JUMP_BOOST: EntityAttribute = ClampedEntityAttribute("attribute.name.generic_jump_boost", 1.0, 0.0, 2.0).setTracked(true)
// @end Attribute-Instance

// @start Modifier-Instance
val SOME_MODIFIER_ID: UUID = UUID.fromString("de17612a-adec-11ec-b909-0242ac120002")

val SOME_MODIFIER: EntityAttributeModifier = EntityAttributeModifier(
    SOME_MODIFIER_ID,
    "Jump stick modifier",
    0.5,
    EntityAttributeModifier.Operation.ADDITION
)
// @end Modifier-Instance

val JUMP_STICK: JumpStick = JumpStick(itemSettingsOf(group = ItemGroup.TOOLS))

object AttributesExample: ModInitializer {

    override fun onInitialize(mod: ModContainer) {
        registryScope(mod.metadata().id()) {
            // @start Register-Attribute
            GENERIC_JUMP_BOOST withPath "generic.jump_boost" toRegistry Registry.ATTRIBUTE
            // @end Register-Attribute
            JUMP_STICK withPath "jump_stick" toRegistry Registry.ITEM
        }

        registerEvents {
            onCommandRegistration { _, _ ->
                this.register("jump_boost") {
                    required(literal("add")) {
                        execute {
                            // @start Apply-Direct
                            this.source.player.getAttributeInstance(GENERIC_JUMP_BOOST)
                                ?.addTemporaryModifier(SOME_MODIFIER)
                            // @end Apply-Direct
                        }
                    }
                    required(literal("remove")) {
                        execute {
                            // @start Remove-Direct
                            this.source.player.getAttributeInstance(GENERIC_JUMP_BOOST)
                                ?.removeModifier(SOME_MODIFIER_ID)
                            // @end Remove-Direct
                        }
                    }
                    required(literal("stick")) {
                        execute {
                            // @start Apply-NBT
                            this.source.player.giveItemStack(ItemStack(Items.STICK).apply {
                                this.addAttributeModifier(GENERIC_JUMP_BOOST, SOME_MODIFIER, EquipmentSlot.MAINHAND)
                            })
                            // @end Apply-NBT
                        }
                    }
                }
            }
        }
    }
}
