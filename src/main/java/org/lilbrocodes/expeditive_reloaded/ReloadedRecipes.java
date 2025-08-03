package org.lilbrocodes.expeditive_reloaded;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.lilbrocodes.expeditive_reloaded.recipe.DyableShapedRecipe;
import org.lilbrocodes.expeditive_reloaded.recipe.DyableShapedRecipeSerializer;
import org.lilbrocodes.expeditive_reloaded.recipe.DyableShapelessRecipe;
import org.lilbrocodes.expeditive_reloaded.recipe.DyableShapelessRecipeSerializer;

public class ReloadedRecipes {
    public static final RecipeSerializer<DyableShapedRecipe> DYABLE_SHAPED_SERIALIZER =
            Registry.register(Registries.RECIPE_SERIALIZER,
                    new Identifier("expeditive_reloaded", "dyable_shaped"),
                    new DyableShapedRecipeSerializer());

    public static final RecipeSerializer<DyableShapelessRecipe> DYABLE_SHAPELESS_SERIALIZER =
            Registry.register(Registries.RECIPE_SERIALIZER,
                    new Identifier("expeditive_reloaded", "dyable_shapeless"),
                    new DyableShapelessRecipeSerializer());

    public static void initialize() {

    }
}
