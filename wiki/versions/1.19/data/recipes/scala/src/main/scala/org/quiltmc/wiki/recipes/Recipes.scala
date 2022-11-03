package org.quiltmc.wiki.recipes

import net.minecraft.item.{ItemStack, Items}
import net.minecraft.recipe.{RecipeSerializer, RecipeType}
import net.minecraft.text.Text
import net.minecraft.util.{Formatting, Identifier}
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.recipe.api.RecipeManagerHelper
import org.quiltmc.qsl.recipe.api.builder.VanillaRecipeBuilders

class Recipes extends ModInitializer {

    override def onInitialize(mod: ModContainer): Unit = {
        val modId = mod.metadata.id

        // @start Registration
        Registry.register(Registry.RECIPE_TYPE, Identifier(modId, "my_recipe"), Recipes.MY_RECIPE)
        Registry.register(Registry.RECIPE_SERIALIZER, Identifier(modId, "my_recipe"), Recipes.MY_RECIPE_SERIALIZER)
        // @end Registration

        // @start Static-Recipe
        RecipeManagerHelper.registerStaticRecipe(
            VanillaRecipeBuilders.shapedRecipe("IAI")  // Shaped recipe with the IAI pattern
                    .output(ItemStack(Items.CHAINMAIL_CHESTPLATE)) // That outputs a chestplate
                    .ingredient('I', Items.IRON_INGOT) // 'I' being an ingot
                    .ingredient('A', Items.APPLE) // and 'A' an apple
                    .build(Identifier(modId, "apple_iron_ingot_fun"), "")
        )
        // @end Static-Recipe

        // @start Adding
        RecipeManagerHelper.addRecipes { handler => handler.register(Identifier(modId, "random"), id =>
                VanillaRecipeBuilders.shapelessRecipe(ItemStack(
                    // We can't know which item it's going to output in advance, therefore
                    // it's impossible to create a JSON for this recipe.
                    Registry.ITEM.getRandom(RandomGenerator.createLegacy).orElseThrow.value
                ))
                        .ingredient(Items.ACACIA_BUTTON)
                        .build(id, "")
        )}
        // @end Adding

        // @start Modification
        RecipeManagerHelper.modifyRecipes { handler =>
            // Modifies the name of the output of the stone smelting recipe
            handler.getRecipe(Identifier("minecraft", "stone"), RecipeType.SMELTING)
                    .getOutput.setCustomName(Text.literal("Modified!").formatted(Formatting.RED))
            // Replaces the enchanting table recipe with different ingredients
            handler.replace(VanillaRecipeBuilders.shapedRecipe(" B ", "D#D", "###")
                    .ingredient('B', Items.WRITABLE_BOOK)
                    .ingredient('D', Items.GOLD_INGOT)
                    .ingredient('#', Items.POLISHED_BASALT)
                    .output(ItemStack(Items.ENCHANTING_TABLE))
                    .build(Identifier("minecraft", "enchanting_table"), "")
            )
        }
        // @end Modification

        // @start Removal
        RecipeManagerHelper.removeRecipes { handler =>
            handler.remove(Identifier("minecraft", "acacia_door"))
            handler.removeIf(RecipeType.BLASTING, rec => rec.getOutput.isOf(Items.GOLD_INGOT))
        }
        // @end Removal
    }
}
object Recipes {

    // @start RecipeType-Instance
    final val MY_RECIPE: RecipeType[MyRecipe] = new RecipeType[MyRecipe] {}
    // @end RecipeType-Instance
    // @start Serializer-Instance
    final val MY_RECIPE_SERIALIZER: RecipeSerializer[MyRecipe] = MyRecipe.Serializer()
    // @end Serializer-Instance
}
