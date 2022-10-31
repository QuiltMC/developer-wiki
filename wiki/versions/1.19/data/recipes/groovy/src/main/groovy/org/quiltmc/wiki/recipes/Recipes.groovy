package org.quiltmc.wiki.recipes

import groovy.transform.CompileStatic
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.recipe.api.RecipeManagerHelper
import org.quiltmc.qsl.recipe.api.builder.VanillaRecipeBuilders

@CompileStatic
class Recipes implements ModInitializer {
    // @start RecipeType-Instance
    static final RecipeType MY_RECIPE = new RecipeType() {} // Subclasses it anonymously
    // @end RecipeType-Instance
    // @start Serializer-Instance
    static final RecipeSerializer MY_RECIPE_SERIALIZER = new MyRecipe.Serializer()
    // @end Serializer-Instance

    @Override
    void onInitialize(ModContainer mod) {
        def modId = mod.metadata().id()

        // @start Registration
        Registry.RECIPE_TYPE[new Identifier(modId, 'my_recipe')] = MY_RECIPE
        Registry.RECIPE_SERIALIZER[new Identifier(modId, 'my_recipe')] = MY_RECIPE_SERIALIZER
        // @end Registration

        // @start Static-Recipe
        RecipeManagerHelper.registerStaticRecipe(
                VanillaRecipeBuilders.shapedRecipe('IAI') // Shaped recipe with the IAI pattern
                .output(new ItemStack(Items.CHAINMAIL_CHESTPLATE)) // That outputs a chestplate
                .ingredient('I' as char, Items.IRON_INGOT) // 'I' being an ingot
                .ingredient('A' as char, Items.APPLE) // and 'A' an apple
                .build(new Identifier(modId, 'apple_iron_ingot_fun'), '')
        )
        // @end Static-Recipe

        // @start Adding
        RecipeManagerHelper.addRecipes {
            it.register(new Identifier(modId, 'random')) {id ->
                VanillaRecipeBuilders.shapelessRecipe(new ItemStack(
                        // We can't know which item it's going to output in advance, therefore
                        // it's impossible to create a JSON for this recipe.
                        Registry.ITEM.getRandom(RandomGenerator.createLegacy()).orElseThrow().value()
                )).ingredient(Items.ACACIA_BUTTON)
                .build(id, '')
            }
        }
        // @end Adding

        // @start Modification
        RecipeManagerHelper.modifyRecipes {
            // Modifies the name of the output of the stone smelting recipe
            it.getRecipe(new Identifier('minecraft', 'stone'), RecipeType.SMELTING)
            .output.setCustomName(Text.literal('Modified!') << Formatting.RED)
            // Replaces the enchanting table recipe with different ingredients
            it.replace(VanillaRecipeBuilders.shapedRecipe(' B ', 'D#D', '###')
            .ingredient('B' as char, Items.WRITABLE_BOOK)
            .ingredient('D' as char, Items.GOLD_INGOT)
            .ingredient('#' as char, Items.POLISHED_BASALT)
            .output(new ItemStack(Items.ENCHANTING_TABLE))
            .build(new Identifier('minecraft', 'enchanting_table'), ''))
        }
        // @end Modification

        // @start Removal
        RecipeManagerHelper.removeRecipes {
            it.remove(new Identifier('minecraft', 'acacia_door'))
            it.removeIf(RecipeType.BLASTING) { rec ->
                rec.output.isOf(Items.GOLD_INGOT)
            }
        }
        // @end Removal
    }
}
