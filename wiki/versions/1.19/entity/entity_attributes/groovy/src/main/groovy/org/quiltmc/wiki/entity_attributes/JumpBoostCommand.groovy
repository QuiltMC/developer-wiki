package org.quiltmc.wiki.entity_attributes

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import groovy.transform.CompileStatic
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

@CompileStatic
class JumpBoostCommand {

    static int add(CommandContext context) {
        // @start Apply-Direct
        context.player.getAttribute(AttributesExample.GENERIC_JUMP_BOOST)
        .addTransientModifier(AttributesExample.SOME_MODIFIER)
        // @end Apply-Direct
        1
    }

    static int remove(CommandContext context) {
        // @start Remove-Direct
        context.player.getAttribute(AttributesExample.GENERIC_JUMP_BOOST)
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
        context.player.addItem(stack)
        1
    }

    static void register(CommandDispatcher dispatcher) {
        dispatcher.register('jump_boost') {
            then('add') {
                executes {
                    add(it)
                }
            }
            then('remove') {
                executes {
                    remove(it)
                }
            }
            then('stick') {
                executes {
                    stick(it)
                }
            }
        }
    }
}
