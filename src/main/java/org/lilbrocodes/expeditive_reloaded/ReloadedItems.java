package org.lilbrocodes.expeditive_reloaded;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredItemGroupRegistry;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredItemRegistry;
import org.lilbrocodes.expeditive_reloaded.items.StriderBoots;
import org.lilbrocodes.expeditive_reloaded.items.StriderBootsItem;
import org.lilbrocodes.expeditive_reloaded.items.StridersFoot;
import org.lilbrocodes.expeditive_reloaded.items.BambooFlute;

import static org.lilbrocodes.expeditive_reloaded.items.BambooFlute.getColor;

public class ReloadedItems {
    private static final DeferredItemGroupRegistry GROUPS = new DeferredItemGroupRegistry(ExpeditiveReloaded.MOD_ID);
    public static final RegistryKey<ItemGroup> EXPEDITIVE_ITEM_GROUP_KEY = GROUPS.registerItemGroup(
            "expeditive_reloaded",
            () -> new ItemStack(ReloadedItems.STRIDER_BOOTS)
    );
    private static final DeferredItemRegistry ITEMS = new DeferredItemRegistry(ExpeditiveReloaded.MOD_ID, EXPEDITIVE_ITEM_GROUP_KEY);

    private static final ArmorMaterial STRIDER_BOOTS_M = new StriderBoots();

    public static final Item STRIDER_BOOTS = ITEMS.register("strider_boots", new StriderBootsItem(STRIDER_BOOTS_M, new FabricItemSettings()));
    public static final Item STRIDERS_FOOT = ITEMS.register("striders_foot", new StridersFoot(new FabricItemSettings().maxCount(1)));

    public static final Item BAMBOO_FLUTE = ITEMS.register("bamboo_flute", new BambooFlute());

    public static void initialize() {
        ITEMS.finalizeRegistration();
    }

    public static void initializeClient() {
        ModelPredicateProviderRegistry.register(ReloadedItems.BAMBOO_FLUTE, new Identifier("playing"),
                ((stack, world, entity, seed) -> entity != null && entity.isUsingItem() ? 1.0F : 0.0F));
        ModelPredicateProviderRegistry.register(ReloadedItems.BAMBOO_FLUTE, new Identifier("color"), (stack, world, entity, seed) -> {
            DyeColor color = getColor(stack);
            if (color != null) {
                return color.getId() / 16f;
            }
            return 0.9999f;
        });
    }
}
