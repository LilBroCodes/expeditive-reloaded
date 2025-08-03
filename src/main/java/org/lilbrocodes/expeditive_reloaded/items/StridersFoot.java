package org.lilbrocodes.expeditive_reloaded.items;

import net.minecraft.item.Item;

public class StridersFoot extends Item {
    public StridersFoot(Item.Settings settings) {
        super(settings.maxCount(64).fireproof());
    }
}
