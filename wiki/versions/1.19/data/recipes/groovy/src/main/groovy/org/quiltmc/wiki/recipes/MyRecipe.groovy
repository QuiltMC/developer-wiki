package org.quiltmc.wiki.recipes

import com.google.gson.JsonObject
import groovy.transform.CompileStatic
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer

class MyRecipe implements Recipe<Inventory> {
    // @start Starting
    private final Identifier id
    private final Ingredient input
    private final ItemStack output

    MyRecipe(id, input, output) {
        this.id = id
        this.input = input
        this.output = output
    }

    @Override
    Identifier getId() {
        this.id
    }
    // @end Starting

    // @start Match
    @Override
    boolean matches(Inventory inventory, World world) {
        this.input.test(inventory.getStack(0))
    }
    // @end Match

    // @start Output
    @Override
    ItemStack getOutput() {
        this.output
    }

    @Override
    ItemStack craft(Inventory inventory) {
        this.output.copy()
    }
    // @end Output

    @Override
    boolean fits(int width, int height) {
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
        MyRecipe read(Identifier id, JsonObject json) {
            // Gets the object specified by the "input" key
            def inputObject = json.getAsJsonObject('input')
            // Helper method to read Ingredient from JsonElement
            def input = Ingredient.fromJson(inputObject)
            // Gets the string in the "output" key
            def outputIdentifier = json.get('output').getAsString()
            // Gets the integer in the "count" key, fallbacking to 1 if it does not exist
            def count = JsonHelper.getInt(json, 'count', 1)
            // Attempts to get the item in "output" with the registry and creates a stack with it
            def output = new ItemStack(Registry.ITEM.getOrEmpty(new Identifier(outputIdentifier))
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
            if (recipe.output.getCount() > 1) {
                obj.addProperty('count', recipe.output.getCount())
            }
            // Gets the output identifier from the registry and adds it into the "output" key
            obj.addProperty('output', Registry.ITEM.getId(recipe.output.getItem()).toString())

            obj
        }
        // @end Serializer-JSON

        // @start Serializer-Packet
        @Override
        MyRecipe read(Identifier id, PacketByteBuf buf) {
            def input = Ingredient.fromPacket(buf)
            def output = buf.readItemStack()

            new MyRecipe(id, input, output)
        }

        @Override
        void write(PacketByteBuf buf, MyRecipe recipe) {
            recipe.input.write(buf)
            buf.writeItemStack(recipe.output)
        }
        // @end Serializer-Packet
    }
}
