package org.quiltmc.wiki.recipes

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.text.Style
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.wrapper.minecraft.registry.registryScope
import org.quiltmc.qkl.wrapper.minecraft.text.buildText
import org.quiltmc.qkl.wrapper.minecraft.text.literal
import org.quiltmc.qkl.wrapper.minecraft.text.styled
import org.quiltmc.qkl.wrapper.qsl.recipe.*
import org.quiltmc.qkl.wrapper.qsl.registerEvents
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

// @start RecipeType-Instance
val MY_RECIPE: RecipeType<MyRecipe> = object: RecipeType<MyRecipe> {}
// @end RecipeType-Instance
// @start Serializer-Instance
val MY_RECIPE_SERIALIZER: RecipeSerializer<MyRecipe> = MyRecipe.Companion.Serializer()
// @end Serializer-Instance

object Recipes: ModInitializer {

    override fun onInitialize(mod: ModContainer) {
        val modId = mod.metadata().id()

        // @start Registration
        registryScope(modId) {
            MY_RECIPE withPath "my_recipe" toRegistry Registry.RECIPE_TYPE
            MY_RECIPE_SERIALIZER withPath "my_recipe" toRegistry Registry.RECIPE_SERIALIZER
        }
        // @end Registration

        // @start Static-Recipe
        registerStaticRecipe(shapedRecipe(
            Identifier(modId, "apple_iron_ingot_fun"),
            "",
            "IAI", // Shaped recipe with the IAI pattern
            ItemStack(Items.CHAINMAIL_CHESTPLATE), // That outputs a chestplate
            'I' to Items.IRON_INGOT, // 'I' being an ingot
            'A' to Items.APPLE // and 'A' an apple
        ))
        // @end Static-Recipe

        registerEvents {
            // @start Adding
            onAddRecipes {
                it.register(Identifier(modId, "random")) { id ->
                    shapelessRecipe(
                        id,
                        "",
                        // We can't know which item it's going to output in advance, therefore
                        // it's impossible to create a JSON for this recipe.
                        ItemStack(Registry.ITEM.getRandom(RandomGenerator.createLegacy()).orElseThrow().value()),
                        Items.ACACIA_BUTTON
                    )
                }
            }
            // @end Adding

            // @start Modification
            onModifyRecipes {
                // Modifies the name of the output of the stone smelting recipe
                it.getRecipe(Identifier("minecraft", "stone"), RecipeType.SMELTING)!!
                    .output.setCustomName(buildText {
                        styled(Style.EMPTY.withFormatting(Formatting.RED)) {
                            literal("Modified!")
                        }
                    })
                // Replaces the enchanting table recipe with different ingredients
                it.replace(shapedRecipe(
                    Identifier("minecraft", "enchanting_table"),
                    "",
                    """
                    | B
                    |D#D
                    |###
                    """.trimMargin(),
                    ItemStack(Items.ENCHANTING_TABLE),
                    'B' to Items.WRITABLE_BOOK,
                    'D' to Items.GOLD_INGOT,
                    '#' to Items.POLISHED_BASALT
                ))
            }
            // @end Modification

            // @start Removal
            onRemoveRecipes {
                it.remove(Identifier("minecraft", "acacia_door"))
                it.removeIf(RecipeType.BLASTING) { rec ->
                    rec.output.isOf(Items.GOLD_INGOT)
                }
            }
            // @end Removal
        }
    }
}
