package org.lilbrocodes.expeditive_reloaded.mixin.impl.strider_boots;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.lilbrocodes.expeditive_reloaded.ReloadedItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntity.class)
public class PiglinEntityMixin {
    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void addStriderBoots(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        PiglinEntity piglin = (PiglinEntity) (Object) this;
        if (random.nextFloat() < 0.1f) {
            piglin.equipStack(EquipmentSlot.FEET, new ItemStack(ReloadedItems.STRIDER_BOOTS));
        }
    }
}
