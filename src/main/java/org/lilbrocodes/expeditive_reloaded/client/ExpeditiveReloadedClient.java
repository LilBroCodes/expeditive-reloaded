package org.lilbrocodes.expeditive_reloaded.client;

import net.fabricmc.api.ClientModInitializer;
import org.lilbrocodes.expeditive_reloaded.ReloadedItems;

public class ExpeditiveReloadedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ReloadedItems.initializeClient();
    }
}
