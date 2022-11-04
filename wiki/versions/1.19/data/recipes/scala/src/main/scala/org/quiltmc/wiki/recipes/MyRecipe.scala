package org.quiltmc.wiki.recipes

import com.google.gson.JsonObject
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.{Ingredient, Recipe, RecipeSerializer, RecipeType}
import net.minecraft.util.{Identifier, JsonHelper}
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer

// @start Starting
class MyRecipe(private val id: Identifier, private val input: Ingredient, private val output: ItemStack) extends Recipe[Inventory] {

    override def getId: Identifier = id
    // @end Starting

    // @start Match
    override def matches(inventory: Inventory, world: World): Boolean = input.test(inventory.getStack(0))
    // @end Match

    // @start Output
    override def getOutput: ItemStack = output

    override def craft(inventory: Inventory): ItemStack = output.copy
    // @end Output

    override def fits(width: Int, height: Int): Boolean = true

    // @start Serializer/Type
    override def getSerializer: RecipeSerializer[_] = Recipes.MY_RECIPE_SERIALIZER

    override def getType: RecipeType[_] = Recipes.MY_RECIPE
    // @end Serializer/Type
}
object MyRecipe {
    class Serializer extends QuiltRecipeSerializer[MyRecipe] {

        // @start Serializer-JSON
        override def read(id: Identifier, json: JsonObject): MyRecipe = {
            // Gets the object specified by the "input" key
            val inputObject = json.getAsJsonObject("input")
            // Helper method to read Ingredient from JsonElement
            val input = Ingredient.fromJson(inputObject)
            // Gets the string in the "output" key
            val outputIdentifier = json.get("output").getAsString
            // Gets the integer in the "count" key, fallbacking to 1 if it does not exist
            val count = JsonHelper.getInt(json, "count", 1)
            // Attempts to get the item in "output" with the registry and creates a stack with it
            val output = ItemStack(Registry.ITEM.getOrEmpty(Identifier(outputIdentifier))
                    .orElseThrow(() => IllegalStateException(s"Item $outputIdentifier does not exist")), count)

            MyRecipe(id, input, output)
        }

        override def toJson(recipe: MyRecipe): JsonObject = {
            val obj = JsonObject()
            // Puts the serialized ingredient json element into the "input" key
            obj.add("input", recipe.input.toJson)
            // Checks if the output count is higher than the default, and if it is, adds into the "count" key
            if (recipe.output.getCount > 1) {
                obj.addProperty("count", recipe.output.getCount)
            }
            // Gets the output identifier from the registry and adds it into the "output" key
            obj.addProperty("output", Registry.ITEM.getId(recipe.output.getItem).toString)

            obj
        }
        // @end Serializer-JSON

        // @start Serializer-Packet
        override def read(id: Identifier, buf: PacketByteBuf): MyRecipe = {
            val input = Ingredient.fromPacket(buf)
            val output = buf.readItemStack

            MyRecipe(id, input, output)
        }

        override def write(buf: PacketByteBuf, recipe: MyRecipe): Unit = {
            recipe.input.write(buf)
            buf.writeItemStack(recipe.output)
        }
        // @end Serializer-Packet
    }
}
