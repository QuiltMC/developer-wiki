package org.quiltmc.wiki.recipes

import com.google.gson.JsonObject
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import net.minecraft.core.Registry
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer

@CompileStatic
@TupleConstructor
class MyRecipe implements Recipe<Container> {
    // @start Starting
    final ResourceLocation id
    final Ingredient input
    final ItemStack output
    // @end Starting

    // @start Match
    @Override
    boolean matches(Container container, Level level) {
        this.input.test(container.getItem(0))
    }
    // @end Match

    // @start Output
    @Override
    ItemStack getResultItem() {
        this.output
    }

    @Override
    ItemStack assemble(Container container) {
        this.output.copy()
    }
    // @end Output

    @Override
    boolean canCraftInDimensions(int width, int height) {
        true
    }

    // @start Serializer/Type
    @Override
    RecipeSerializer getSerializer() {
        Recipes.MY_RECIPE_SERIALIZER
    }

    @Override
    RecipeType getType() {
        Recipes.MY_RECIPE
    }
    // @end Serializer/Type

    @CompileStatic
    static class Serializer implements QuiltRecipeSerializer<MyRecipe> {
        // @start Serializer-JSON
        @Override
        MyRecipe fromJson(ResourceLocation id, JsonObject json) {
            // Gets the object specified by the "input" key
            def inputObject = json.getAsJsonObject('input')
            // Helper method to read Ingredient from JsonElement
            def input = Ingredient.fromJson(inputObject)
            // Gets the string in the "output" key
            def outputIdentifier = json.get('output').asString
            // Gets the integer in the "count" key, fallbacking to 1 if it does not exist
            def count = GsonHelper.getAsInt(json, 'count', 1)
            // Attempts to get the item in "output" with the registry and creates a stack with it
            def output = new ItemStack(Registry.ITEM.getOptional(new ResourceLocation(outputIdentifier))
                    .orElseThrow {
                        new IllegalStateException("Item $outputIdentifier does not exist")
                    }, count)

            new MyRecipe(id, input, output)
        }

        @Override
        JsonObject toJson(MyRecipe recipe) {
            def obj = new JsonObject()
            // Puts the serialized ingredient json element into the "input" key
            obj.add('input', recipe.input.toJson())
            // Checks if the output count is higher than the default, and if it is, adds into the "count" key
            if (recipe.output.count > 1) {
                obj.addProperty('count', recipe.output.count)
            }
            // Gets the output identifier from the registry and adds it into the "output" key
            obj.addProperty('output', Registry.ITEM.getId(recipe.output.item).toString())

            obj
        }
        // @end Serializer-JSON

        // @start Serializer-Packet
        @Override
        MyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            def input = Ingredient.fromNetwork(buf)
            def output = buf.readItem()

            new MyRecipe(id, input, output)
        }

        @Override
        void toNetwork(FriendlyByteBuf buf, MyRecipe recipe) {
            recipe.input.toNetwork(buf)
            buf.writeItem(recipe.output)
        }
        // @end Serializer-Packet
    }
}
