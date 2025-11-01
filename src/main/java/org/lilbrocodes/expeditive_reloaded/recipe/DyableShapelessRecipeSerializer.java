package org.lilbrocodes.expeditive_reloaded.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class DyableShapelessRecipeSerializer implements RecipeSerializer<DyableShapelessRecipe> {
    public DyableShapelessRecipe read(Identifier identifier, JsonObject jsonObject) {
        String string = JsonHelper.getString(jsonObject, "group", "");
        CraftingRecipeCategory craftingRecipeCategory = CraftingRecipeCategory.CODEC
                .byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
        DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
        if (defaultedList.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (defaultedList.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        } else {
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new DyableShapelessRecipe(identifier, string, craftingRecipeCategory, itemStack, defaultedList);
        }
    }

    private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();

        for (int i = 0; i < json.size(); i++) {
            Ingredient ingredient = Ingredient.fromJson(json.get(i), false);
            if (!ingredient.isEmpty()) {
                defaultedList.add(ingredient);
            }
        }

        return defaultedList;
    }

    public DyableShapelessRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
        String string = packetByteBuf.readString();
        CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
        int i = packetByteBuf.readVarInt();
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

        defaultedList.replaceAll(ignored -> Ingredient.fromPacket(packetByteBuf));

        ItemStack itemStack = packetByteBuf.readItemStack();
        return new DyableShapelessRecipe(identifier, string, craftingRecipeCategory, itemStack, defaultedList);
    }

    public void write(PacketByteBuf packetByteBuf, DyableShapelessRecipe shapelessRecipe) {
        packetByteBuf.writeString(shapelessRecipe.group);
        packetByteBuf.writeEnumConstant(shapelessRecipe.category);
        packetByteBuf.writeVarInt(shapelessRecipe.input.size());

        for (Ingredient ingredient : shapelessRecipe.input) {
            ingredient.write(packetByteBuf);
        }

        packetByteBuf.writeItemStack(shapelessRecipe.output);
    }
}
