package org.quiltmc.wiki.recipes;

import com.google.gson.JsonObject;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

public class MyRecipe implements Recipe<Inventory> {
	// @start Starting
	private final Identifier id;
	private final Ingredient input;
	private final ItemStack output;

	public MyRecipe(Identifier id, Ingredient input, ItemStack output) {
		this.id = id;
		this.input = input;
		this.output = output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}
	// @end Starting

	// @start Match
	@Override
	public boolean matches(Inventory inventory, World world) {
		return this.input.test(inventory.getStack(0));
	}
	// @end Match

	// @start Output
	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return this.output.copy();
	}
	// @end Output

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	// @start Serializer/Type
	@Override
	public RecipeSerializer<?> getSerializer() {
		return Recipes.MY_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return Recipes.MY_RECIPE;
	}
	// @end Serializer/Type

	public static class Serializer implements QuiltRecipeSerializer<MyRecipe> {
		// @start Serializer-JSON
		@Override
		public MyRecipe read(Identifier id, JsonObject json) {
			// Gets the object specified by the "input" key
			var inputObject = json.getAsJsonObject("input");
			// Helper method to read Ingredient from JsonElement
			var input = Ingredient.fromJson(inputObject);
			// Gets the string in the "output" key
			var outputIdentifier = json.get("output").getAsString();
			// Gets the integer in the "count" key, fallbacking to 1 if it does not exist
			var count = JsonHelper.getInt(json, "count", 1);
			// Attempts to get the item in "output" with the registry and creates a stack with it
			var output = new ItemStack(Registry.ITEM.getOrEmpty(new Identifier(outputIdentifier))
					.orElseThrow(() -> new IllegalStateException("Item " + outputIdentifier + " does not exist")), count);

			return new MyRecipe(id, input, output);
		}

		@Override
		public JsonObject toJson(MyRecipe recipe) {
			var obj = new JsonObject();
			// Puts the serialized ingredient json element into the "input" key
			obj.add("input", recipe.input.toJson());
			// Checks if the output count is higher than the default, and if it is, adds into the "count" key
			if (recipe.output.getCount() > 1) {
				obj.addProperty("count", recipe.output.getCount());
			}
			// Gets the output identifier from the registry and adds it into the "output" key
			obj.addProperty("output", Registry.ITEM.getId(recipe.output.getItem()).toString());

			return obj;
		}
		// @end Serializer-JSON

		// @start Serializer-Packet
		@Override
		public MyRecipe read(Identifier id, PacketByteBuf buf) {
			var input = Ingredient.fromPacket(buf);
			var output = buf.readItemStack();

			return new MyRecipe(id, input, output);
		}

		@Override
		public void write(PacketByteBuf buf, MyRecipe recipe) {
			recipe.input.write(buf);
			buf.writeItemStack(recipe.output);
		}
		// @end Serializer-Packet
	}
}
