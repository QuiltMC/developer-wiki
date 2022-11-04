package org.quiltmc.wiki.entity_attributes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class JumpBoostCommand {

	public static int add(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Player player = context.getSource().getPlayerOrException();
		// @start Apply-Direct
		AttributeInstance instance = player.getAttribute(AttributesExample.GENERIC_JUMP_BOOST);
		instance.addTransientModifier(AttributesExample.SOME_MODIFIER);
		// @end Apply-Direct
		return 1;
	}

	public static int remove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		AttributeInstance instance = context.getSource().getPlayerOrException().getAttribute(AttributesExample.GENERIC_JUMP_BOOST);
		// @start Remove-Direct
		instance.removeModifier(AttributesExample.SOME_MODIFIER_ID);
		// @end Remove-Direct
		return 1;
	}

	public static int stick(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		// @start Apply-NBT
		ItemStack stack = new ItemStack(Items.STICK);
		stack.addAttributeModifier(
				AttributesExample.GENERIC_JUMP_BOOST,
				AttributesExample.SOME_MODIFIER,
				EquipmentSlot.MAINHAND
		);
		// @end Apply-NBT
		context.getSource().getPlayerOrException().addItem(stack);
		return 1;
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("jump_boost")
				.then(Commands.literal("add").executes(JumpBoostCommand::add))
				.then(Commands.literal("remove").executes(JumpBoostCommand::remove))
				.then(Commands.literal("stick").executes(JumpBoostCommand::stick)));
	}
}
