package org.quiltmc.wiki.recipes;

import I;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

public class MyRecipe implements Recipe<Container> {
	// @start Starting
	private final ResourceLocation id;
	private final Ingredient input;
	private final ItemStack output;

	public MyRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
		this.id = id;
		this.input = input;
		this.output = output;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}
	// @end Starting

	// @start Match
	@Override
	public boolean matches(Container inventory, Level world) {
		return this.input.test(inventory.getItem(0));
	}
	// @end Match

	// @start Output
	@Override
	public ItemStack getResultItem() {
		return this.output;
	}

	@Override
	public ItemStack assemble(Container inventory) {
		return this.output.copy();
	}
	// @end Output

	@Override
	public boolean canCraftInDimensions(int width, int height) {
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
		public MyRecipe fromJson(ResourceLocation id, JsonObject json) {
			// Gets the object specified by the "input" key
			var inputObject = json.getAsJsonObject("input");
			// Helper method to read Ingredient from JsonElement
			var input = Ingredient.fromJson(inputObject);
			// Gets the string in the "output" key
			var outputIdentifier = json.get("output").getAsString();
			// Gets the integer in the "count" key, fallbacking to 1 if it does not exist
			var count = GsonHelper.getAsInt(json, "count", 1);
			// Attempts to get the item in "output" with the registry and creates a stack with it
			var output = new ItemStack(Registry.ITEM.getOptional(new ResourceLocation(outputIdentifier))
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
			obj.addProperty("output", Registry.ITEM.getKey(recipe.output.getItem()).toString());

			return obj;
		}
		// @end Serializer-JSON

		// @start Serializer-Packet
		@Override
		public MyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			var input = Ingredient.fromNetwork(buf);
			var output = buf.readItem();

			return new MyRecipe(id, input, output);
		}

		@Override
		public void write(FriendlyByteBuf buf, MyRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeItem(recipe.output);
		}
		// @end Serializer-Packet
	}
}
