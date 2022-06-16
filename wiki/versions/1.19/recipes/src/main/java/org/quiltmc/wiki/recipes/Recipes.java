package org.quiltmc.wiki.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.registry.Registry;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.recipe.api.RecipeManagerHelper;
import org.quiltmc.qsl.recipe.api.builder.VanillaRecipeBuilders;

public class Recipes implements ModInitializer {
	// @start RecipeType-Instance
	public static final RecipeType<MyRecipe> MY_RECIPE = new RecipeType<>() {}; // Subclasses it anonymously
	// @end RecipeType-Instance
	// @start Serializer-Instance
	public static final RecipeSerializer<MyRecipe> MY_RECIPE_SERIALIZER = new MyRecipe.Serializer();
	// @end Serializer-Instance

	@Override
	public void onInitialize(ModContainer mod) {
		// @start Registration
		Registry.register(Registry.RECIPE_TYPE, new Identifier("example", "my_recipe"), MY_RECIPE);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("example", "my_recipe"), MY_RECIPE_SERIALIZER);
		// @end Registration

		// @start Static-Recipe
		RecipeManagerHelper.registerStaticRecipe(
				VanillaRecipeBuilders.shapedRecipe("IAI") // Shaped recipe with the IAI pattern
						.output(new ItemStack(Items.CHAINMAIL_CHESTPLATE)) // That outputs a chestplate
						.ingredient('I', Items.IRON_INGOT) // I being an ingot
						.ingredient('A', Items.APPLE) // and A an Apple
						.build(new Identifier("example", "apple_iron_ingot_fun"), "")
		);
		// @end Static-Recipe

		// @start Adding
		RecipeManagerHelper.addRecipes(handler -> handler.register(new Identifier("example", "random"), id ->
				VanillaRecipeBuilders.shapelessRecipe(new ItemStack(
						// We can't know which item it's going to output in advance, therefore, it's impossible
						// to create a JSON for this recipe.
						Registry.ITEM.getRandom(RandomGenerator.createLegacy()).orElseThrow().value()))
					.ingredient(Items.ACACIA_BUTTON)
					.build(id, "")));
		// @end Adding

		// @start Modification
		RecipeManagerHelper.modifyRecipes(handler -> {
			// Modifies the name of the output of the stone smelting recipe
			handler.getRecipe(new Identifier("minecraft", "stone"), RecipeType.SMELTING)
					.getOutput().setCustomName(Text.literal("Modified!").formatted(Formatting.RED));
			// Replaces the enchanting table recipe with different ingredients
			handler.replace(VanillaRecipeBuilders.shapedRecipe(" B ", "D#D", "###")
					.ingredient('B', Items.WRITABLE_BOOK)
					.ingredient('D', Items.GOLD_INGOT)
					.ingredient('#', Items.POLISHED_BASALT)
					.output(new ItemStack(Items.ENCHANTING_TABLE))
					.build(new Identifier("minecraft", "enchanting_table"), "")
			);
		});
		// @end Modification

		// @start Removal
		RecipeManagerHelper.removeRecipes(handler -> {
			handler.remove(new Identifier("minecraft", "acacia_door"));
			handler.removeIf(RecipeType.BLASTING, rec -> rec.getOutput().isOf(Items.GOLD_INGOT));
		});
		// @end Removal
	}
}
