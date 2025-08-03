package org.lilbrocodes.expeditive_reloaded.mixin.impl.bamboo_flute;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.DyeColor;
import org.lilbrocodes.expeditive_reloaded.ReloadedItems;
import org.lilbrocodes.expeditive_reloaded.items.BambooFlute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "fromNbt", at = @At("RETURN"), cancellable = true)
    private static void expeditive$dataFixFlutes(NbtCompound nbt, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();

        if (stack.isEmpty() && nbt.contains("id", NbtElement.STRING_TYPE)) {
            String oldId = nbt.getString("id");

            if (oldId.startsWith("expeditive:bamboo_flute")) {
                ItemStack newStack = new ItemStack(ReloadedItems.BAMBOO_FLUTE);

                if (oldId.contains("_")) {
                    String colorName = oldId.substring(oldId.lastIndexOf("_") + 1);
                    DyeColor color = DyeColor.byName(colorName, null);
                    if (color != null) {
                        BambooFlute.setDyed(newStack, color);
                    }
                }

                newStack.setCount(nbt.getByte("Count"));

                if (nbt.contains("tag", NbtElement.COMPOUND_TYPE)) {
                    newStack.setNbt(nbt.getCompound("tag").copy());
                }

                cir.setReturnValue(newStack);
            }
        }
    }
}
