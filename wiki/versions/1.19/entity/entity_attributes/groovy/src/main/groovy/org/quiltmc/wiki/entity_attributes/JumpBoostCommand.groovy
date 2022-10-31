package org.quiltmc.wiki.entity_attributes

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class JumpBoostCommand {

    static int add(CommandContext context) {
        // @start Apply-Direct
        context.getPlayer().getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST)
        .addTemporaryModifier(AttributesExample.SOME_MODIFIER)
        // @end Apply-Direct
        1
    }

    static int remove(CommandContext context) {
        // @start Remove-Direct
        context.getPlayer().getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST)
                .removeModifier(AttributesExample.SOME_MODIFIER_ID)
        // @end Remove-Direct
        1
    }

    static int stick(CommandContext context) {
        // @start Apply-NBT
        ItemStack stack = new ItemStack(Items.STICK)
        stack.addAttributeModifier(
                AttributesExample.GENERIC_JUMP_BOOST,
                AttributesExample.SOME_MODIFIER,
                EquipmentSlot.MAINHAND
        )
        // @end Apply-NBT
        context.getPlayer().giveItemStack(stack)
        1
    }

    static void register(CommandDispatcher dispatcher) {
        dispatcher.register("jump_boost") {
            then("add") {
                executes {
                    add(it)
                }
            }
            then("remove") {
                executes {
                    remove(it)
                }
            }
            then("stick") {
                executes {
                    stick(it)
                }
            }
        }
    }
}
