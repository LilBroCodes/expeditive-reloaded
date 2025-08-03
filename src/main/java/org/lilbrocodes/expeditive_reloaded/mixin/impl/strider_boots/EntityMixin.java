package org.lilbrocodes.expeditive_reloaded.mixin.impl.strider_boots;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.FluidTags;
import org.lilbrocodes.expeditive_reloaded.items.StriderBootsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "setOnFireFromLava", at = @At("HEAD"), cancellable = true)
    public void expeditive$cancelFluidWalkingFire(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof PlayerEntity player && fluidWalking(player, player.getWorld().getFluidState(player.getBlockPos()))) ci.cancel();
    }

    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isInLava()Z", ordinal = 0))
    public boolean expeditive$giveFireDamageOnLava(boolean original) {
        Entity self = (Entity) (Object) this;
        return original && self instanceof PlayerEntity player && !fluidWalking(player, player.getWorld().getFluidState(player.getBlockPos()));
    }

    @Unique
    protected boolean fluidWalking(PlayerEntity player, FluidState state) {
        FluidState headFluidState = player.getWorld().getFluidState(player.getBlockPos().mutableCopy().add(0, 1, 0));
        return player.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem &&
                state.isIn(FluidTags.LAVA) &&
                state.getLevel() > 3 &&
                player.fallDistance < player.getSafeFallDistance() &&
                !headFluidState.isOf(Fluids.LAVA);
    }
}
