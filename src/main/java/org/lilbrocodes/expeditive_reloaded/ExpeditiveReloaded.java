package org.lilbrocodes.expeditive_reloaded;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ExpeditiveReloaded implements ModInitializer {
    public static String MOD_ID = "expeditive_reloaded";

    @Override
    public void onInitialize() {
        ReloadedItems.initialize();
        ReloadedRecipes.initialize();
        ReloadedAdvancements.initialize();
    }

    public static Identifier identify(String name) {
        return new Identifier(MOD_ID, name);
    }
}
