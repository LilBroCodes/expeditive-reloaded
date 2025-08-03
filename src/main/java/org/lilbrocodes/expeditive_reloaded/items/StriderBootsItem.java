package org.lilbrocodes.expeditive_reloaded.items;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class StriderBootsItem extends ArmorItem {
    public StriderBootsItem(ArmorMaterial material, Settings settings) {
        super(material, Type.BOOTS, settings.fireproof());
    }
}
