package org.lilbrocodes.expeditive_reloaded.mixin.impl.strider_boots;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import org.lilbrocodes.expeditive_reloaded.items.StriderBootsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow
    public abstract boolean isSneaking();

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isTouchingWater()Z", ordinal = 0))
    private boolean expeditive$fluidWalking0(boolean original) {
        if (this.getWorld().getBlockState(this.getBlockPos().up()).isOf(Blocks.LAVA)) {
            return false;
        }
        return original && !(this.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem);
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isTouchingWater()Z", ordinal = 1))
    private boolean expeditive$fluidWalking1(boolean original) {
        if (this.getWorld().getBlockState(this.getBlockPos().up()).isOf(Blocks.LAVA)) {
            return false;
        }
        return original && !(this.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem);
    }

    @Unique
    protected boolean fluidWalking(FluidState state) {
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        return self.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem &&
                state.isIn(FluidTags.LAVA) &&
                state.getLevel() > 3 &&
                self.fallDistance < self.getSafeFallDistance();
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void expeditive$riseUp(CallbackInfo ci) {
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        if (self.isInLava() && fluidWalking(self.getWorld().getFluidState(self.getBlockPos()))) {
            ShapeContext shapeContext = ShapeContext.of(self);
            if (shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, self.getBlockPos(), true) && !self.getWorld().getFluidState(self.getBlockPos().up()).isIn(FluidTags.LAVA)) {
                self.setOnGround(true);
            } else {
                self.setVelocity(self.getVelocity().multiply(0.5F).add(0.0F, net.minecraft.client.MinecraftClient.getInstance().options.jumpKey.isPressed() ? 0.05 : -0.05, 0.0F));
            }
        }
    }
}
