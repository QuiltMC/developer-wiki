package org.quiltmc.wiki.recipes

import com.google.gson.JsonObject
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

// @start Starting
data class MyRecipe(private val id: Identifier, private val input: Ingredient, private val output: ItemStack): Recipe<Inventory> {

    override fun getId(): Identifier = this.id
    // @end Starting

    // @start Match
    override fun matches(inventory: Inventory, world: World): Boolean = this.input.test(inventory.getStack(0))
    // @end Match

    // @start Output
    override fun getOutput(): ItemStack = this.output

    override fun craft(inventory: Inventory?): ItemStack = this.output.copy()
    // @end Output

    override fun fits(width: Int, height: Int): Boolean = true

    // @start Serializer/Type
    override fun getSerializer(): RecipeSerializer<*> = MY_RECIPE_SERIALIZER

    override fun getType(): RecipeType<*> = MY_RECIPE
    // @end Serializer/Type

    companion object {
        class Serializer: QuiltRecipeSerializer<MyRecipe> {
            // @start Serializer-JSON
            override fun read(id: Identifier, json: JsonObject): MyRecipe {
                // Gets the object specified by the "input" key
                val inputObject = json.getAsJsonObject("input")
                // Helper method to read Ingredient from JsonElement
                val input = Ingredient.fromJson(inputObject)
                // Gets the string in the "output" key
                val outputIdentifier = json.get("output").asString
                // Gets the integer in the "count" key, fallbacking to 1 if it does not exist
                val count = JsonHelper.getInt(json, "count", 1)
                // Attempts to get the item in "output" with the registry and creates a stack with it
                val output = ItemStack(
                    Registry.ITEM.getOrEmpty(Identifier(outputIdentifier))
                    .orElseThrow { IllegalStateException("Item $outputIdentifier does not exist") }, count)

                return MyRecipe(id, input, output)
            }

            override fun toJson(recipe: MyRecipe): JsonObject {
                val obj = JsonObject()
                // Puts the serialized ingredient json element into the "input" key
                obj.add("input", recipe.input.toJson())
                // Checks if the output count is higher than the default, and if it is, adds into the "count" key
                if (recipe.output.count > 1) {
                    obj.addProperty("count", recipe.output.count)
                }
                // Gets the output identifier from the registry and adds it into the "output" key
                obj.addProperty("output", Registry.ITEM.getId(recipe.output.item).toString())

                return obj
            }
            // @end Serializer-JSON

            // @start Serializer-Packet
            override fun read(id: Identifier, buf: PacketByteBuf): MyRecipe {
                val input = Ingredient.fromPacket(buf)
                val output = buf.readItemStack()

                return MyRecipe(id, input, output)
            }

            override fun write(buf: PacketByteBuf, recipe: MyRecipe) {
                recipe.input.write(buf)
                buf.writeItemStack(recipe.output)
            }
            // @end Serializer-Packet
        }
    }
}
