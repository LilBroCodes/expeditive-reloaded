package org.lilbrocodes.expeditive_reloaded.recipe;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.lilbrocodes.expeditive_reloaded.ReloadedRecipes;
import org.lilbrocodes.expeditive_reloaded.items.BambooFlute;

@SuppressWarnings("ClassCanBeRecord")
public class DyableShapelessRecipe implements CraftingRecipe {
    private final Identifier id;
    final String group;
    final CraftingRecipeCategory category;
    final ItemStack output;
    final DefaultedList<Ingredient> input;

    public DyableShapelessRecipe(Identifier id, String group, CraftingRecipeCategory category, ItemStack output, DefaultedList<Ingredient> input) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.output = output;
        this.input = input;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ReloadedRecipes.DYABLE_SHAPELESS_SERIALIZER;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.input;
    }

    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for (int j = 0; j < recipeInputInventory.size(); j++) {
            ItemStack itemStack = recipeInputInventory.getStack(j);
            if (!itemStack.isEmpty()) {
                i++;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, null);
    }

    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack result = output.copy();

        for (ItemStack stack : inv.getInputStacks()) {
            if (!stack.isEmpty() && stack.getItem() instanceof DyeItem dyeItem) {
                BambooFlute.setDyed(result, dyeItem.getColor());
                return result;
            }
        }

        BambooFlute.setDyed(result, null);

        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= this.input.size();
    }
}
