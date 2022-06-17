package org.quiltmc.wiki.entity_attributes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class JumpBoostCommand {

	public static int add(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		PlayerEntity player = context.getSource().getPlayer();
		// @start Apply-Direct
		EntityAttributeInstance instance = player.getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST);
		instance.addTemporaryModifier(AttributesExample.SOME_MODIFIER);
		// @end Apply-Direct
		return 0;
	}

	public static int remove(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		EntityAttributeInstance instance = context.getSource().getPlayer().getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST);
		// @start Remove-Direct
		instance.removeModifier(AttributesExample.SOME_MODIFIER_ID);
		// @end Remove-Direct
		return 0;
	}

	public static int stick(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		// @start Apply-NBT
		ItemStack stack = new ItemStack(Items.STICK);
		stack.addAttributeModifier(
				AttributesExample.GENERIC_JUMP_BOOST,
				AttributesExample.SOME_MODIFIER,
				EquipmentSlot.MAINHAND
		);
		// @end Apply-NBT
		context.getSource().getPlayer().giveItemStack(stack);
		return 0;
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("jump_boost")
				.then(CommandManager.literal("add").executes(JumpBoostCommand::add))
				.then(CommandManager.literal("remove").executes(JumpBoostCommand::remove))
				.then(CommandManager.literal("stick").executes(JumpBoostCommand::stick)));
	}
}
