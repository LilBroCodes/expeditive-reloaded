package org.lilbrocodes.expeditive_reloaded;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.lilbrocodes.expeditive_reloaded.items.StriderBoots;
import org.lilbrocodes.expeditive_reloaded.items.StriderBootsItem;
import org.lilbrocodes.expeditive_reloaded.items.StridersFoot;
import org.lilbrocodes.expeditive_reloaded.items.BambooFlute;

import static org.lilbrocodes.expeditive_reloaded.items.BambooFlute.getColor;

public class ReloadedItems {
    public static final RegistryKey<ItemGroup> EXPEDITIVE_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), ExpeditiveReloaded.identify("item_group_expeditive"));
    public static final ItemGroup EXPEDITIVE_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ReloadedItems.STRIDER_BOOTS))
            .displayName(Text.translatable("itemGroup.expeditive_reloaded"))
            .build();

    private static final ArmorMaterial STRIDER_BOOTS_M = new StriderBoots();

    public static final Item STRIDER_BOOTS =
            register("strider_boots", new StriderBootsItem(STRIDER_BOOTS_M, new FabricItemSettings()));
    public static final Item STRIDERS_FOOT =
            register("striders_foot", new StridersFoot(new FabricItemSettings().maxCount(1)));

    public static final Item BAMBOO_FLUTE =
            register("bamboo_flute", new BambooFlute());

    public static <T extends Item> T register(String path, T item) {
        return Registry.register(Registries.ITEM, ExpeditiveReloaded.identify(path), item);
    }

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, EXPEDITIVE_ITEM_GROUP_KEY, EXPEDITIVE_ITEM_GROUP);
        ModelPredicateProviderRegistry.register(ReloadedItems.BAMBOO_FLUTE, new Identifier("playing"),
                ((stack, world, entity, seed) -> entity != null && entity.isUsingItem() ? 1.0F : 0.0F));
        ModelPredicateProviderRegistry.register(ReloadedItems.BAMBOO_FLUTE, new Identifier("color"), (stack, world, entity, seed) -> {
            DyeColor color = getColor(stack);
            if (color != null) {
                return color.getId() / 16f;
            }
            return 0.9999f;
        });

        ItemGroupEvents.modifyEntriesEvent(EXPEDITIVE_ITEM_GROUP_KEY).register(content -> {
            content.add(STRIDER_BOOTS);
            content.add(STRIDERS_FOOT);
            content.add(BAMBOO_FLUTE);
        });
    }
}
