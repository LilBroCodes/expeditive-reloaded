package org.lilbrocodes.expeditive_reloaded.recipe;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.lilbrocodes.expeditive_reloaded.ReloadedRecipes;
import org.lilbrocodes.expeditive_reloaded.items.BambooFlute;


@SuppressWarnings("ClassCanBeRecord")
public class DyableShapedRecipe implements CraftingRecipe {
    final Identifier id;
    final String group;
    final CraftingRecipeCategory category;
    final int width;
    final int height;
    final DefaultedList<Ingredient> ingredients;
    final ItemStack output;

    public DyableShapedRecipe(Identifier id, String group, CraftingRecipeCategory category,
                              int width, int height, DefaultedList<Ingredient> ingredients, ItemStack output) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        int invWidth = inv.getWidth();
        int invHeight = inv.getHeight();

        for (int offsetX = 0; offsetX <= invWidth - this.width; offsetX++) {
            for (int offsetY = 0; offsetY <= invHeight - this.height; offsetY++) {
                if (matchesAt(inv, offsetX, offsetY, false)) return true;
                if (matchesAt(inv, offsetX, offsetY, true)) return true;
            }
        }
        return false;
    }

    private boolean matchesAt(RecipeInputInventory inv, int offsetX, int offsetY, boolean flipHorizontally) {
        int invWidth = inv.getWidth();
        inv.getHeight();

        for (int patternY = 0; patternY < this.height; patternY++) {
            for (int patternX = 0; patternX < this.width; patternX++) {
                int targetX = offsetX + (flipHorizontally ? (this.width - patternX - 1) : patternX);
                int targetY = offsetY + patternY;

                int inventoryIndex = targetX + targetY * invWidth;
                Ingredient expected = this.ingredients.get(patternX + patternY * this.width);
                ItemStack actualStack = inv.getStack(inventoryIndex);

                if (!expected.test(actualStack)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
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
        return width >= this.width && height >= this.height;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack createIcon() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return category;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ReloadedRecipes.DYABLE_SHAPED_SERIALIZER;
    }
}
