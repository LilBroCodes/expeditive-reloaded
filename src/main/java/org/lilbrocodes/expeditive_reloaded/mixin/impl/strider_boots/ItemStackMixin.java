package org.lilbrocodes.expeditive_reloaded.mixin.impl.strider_boots;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.lilbrocodes.expeditive_reloaded.ReloadedItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "fromNbt", at = @At("RETURN"), cancellable = true)
    private static void expeditive$dataFixBoots(NbtCompound nbt, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();

        if (stack.isEmpty() && nbt.contains("id", NbtElement.STRING_TYPE)) {
            String oldId = nbt.getString("id");

            if (oldId.startsWith("expeditive:strider_boots")) {
                ItemStack newStack = new ItemStack(ReloadedItems.STRIDER_BOOTS);
                                newStack.setCount(nbt.getByte("Count"));

                if (nbt.contains("tag", NbtElement.COMPOUND_TYPE)) {
                    newStack.setNbt(nbt.getCompound("tag").copy());
                }

                cir.setReturnValue(newStack);
            } else if (oldId.startsWith("expeditive:striders_foot")) {
                ItemStack newStack = new ItemStack(ReloadedItems.STRIDERS_FOOT);
                newStack.setCount(nbt.getByte("Count"));

                if (nbt.contains("tag", NbtElement.COMPOUND_TYPE)) {
                    newStack.setNbt(nbt.getCompound("tag").copy());
                }

                cir.setReturnValue(newStack);
            }
        }
    }
}
