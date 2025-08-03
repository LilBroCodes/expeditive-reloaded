package org.lilbrocodes.expeditive_reloaded.items;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class StriderBoots implements ArmorMaterial {
    private static final int BASE_DURABILITY = 25;
    private static final int BASE_PROTECTION = 2;

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY * 11;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return BASE_PROTECTION;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.DIAMOND);
    }

    @Override
    public String getName() {
        return "strider";
    }

    @Override
    public float getToughness() {
        return 0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0F;
    }
}