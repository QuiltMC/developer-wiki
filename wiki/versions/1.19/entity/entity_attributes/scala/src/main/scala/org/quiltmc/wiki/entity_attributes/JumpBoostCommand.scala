package org.quiltmc.wiki.entity_attributes

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.{ItemStack, Items}
import net.minecraft.server.command.{CommandManager, ServerCommandSource}

object JumpBoostCommand {

    // @start Apply-Direct
    def add(context: CommandContext[ServerCommandSource]): Int = Option(
        context.getSource.getPlayer.getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST)
    ).map { instance =>
        instance.addTemporaryModifier(AttributesExample.SOME_MODIFIER)
        1
    }.getOrElse(0)
    // @end Apply-Direct
    
    def remove(context: CommandContext[ServerCommandSource]): Int = Option(
        context.getSource.getPlayer.getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST)
    ).map { instance =>
        // @start Remove-Direct
        instance.removeModifier(AttributesExample.SOME_MODIFIER)
        // @end Remove-Direct
        1
    }.getOrElse(0)
    
    def stick(context: CommandContext[ServerCommandSource]): Int = {
        // @start Apply-NBT
        val stack = ItemStack(Items.STICK)
        stack.addAttributeModifier(
            AttributesExample.GENERIC_JUMP_BOOST,
            AttributesExample.SOME_MODIFIER,
            EquipmentSlot.MAINHAND
        )
        // @end Apply-NBT
        context.getSource.getPlayer.giveItemStack(stack)
        1
    }
    
    def register(dispatcher: CommandDispatcher[ServerCommandSource]): Unit = dispatcher.register(
        CommandManager.literal("jump_boost")
                .`then`(CommandManager.literal("add").executes(JumpBoostCommand.add))
                .`then`(CommandManager.literal("remove").executes(JumpBoostCommand.remove))
                .`then`(CommandManager.literal("stick").executes(JumpBoostCommand.stick))
    )
}
