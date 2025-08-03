package org.lilbrocodes.expeditive_reloaded.mixin.impl.strider_boots;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.lilbrocodes.expeditive_reloaded.ReloadedAdvancements;
import org.lilbrocodes.expeditive_reloaded.items.StriderBootsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lilbrocodes.composer_reloaded.api.util.AdvancementManager.grantAdvancement;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Random getRandom();
    @Unique private int lavaWalkingTicks = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "canWalkOnFluid", at = @At("RETURN"))
    protected boolean expeditive$fluidWalking(boolean original, FluidState state) {
        FluidState headFluidState = getWorld().getFluidState(getBlockPos().mutableCopy().add(0, 1, 0));
        return original || (this.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem &&
                state.isIn(FluidTags.LAVA) &&
                state.getLevel() > 3 &&
                fallDistance < getSafeFallDistance() &&
                !headFluidState.isOf(Fluids.LAVA));
    };

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void expeditive$tickLavaWalking(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!getWorld().isClient && self instanceof ServerPlayerEntity serverPlayer) {
            if (this.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem) {
                if (getWorld().getFluidState(getBlockPos()).isIn(FluidTags.LAVA)) {
                    if (this.age % 150 == 0) {
                        this.getEquippedStack(EquipmentSlot.FEET).damage(1, this.getRandom(), null);
                    }
                    if (lavaWalkingTicks != -1) lavaWalkingTicks++;
                    if (lavaWalkingTicks >= 60) {
                        grantAdvancement(serverPlayer, ReloadedAdvancements.STRIDER_APPROVED);
                        lavaWalkingTicks = -1;
                    }
                } else if (lavaWalkingTicks != -1) lavaWalkingTicks = 0;
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void preventLavaDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem) {
                if (source.isOf(DamageTypes.HOT_FLOOR) && !expeditive$fluidWalking(false, getWorld().getFluidState(getBlockPos()))) {
                    cir.setReturnValue(false);
                }
        }
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void expeditive$riseOutOfLava(CallbackInfo ci) {
        updateFloating();
    }

    @Unique
    protected boolean fluidWalking(FluidState state) {
        return getEquippedStack(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem &&
                state.isIn(FluidTags.LAVA) &&
                state.getLevel() > 3 &&
                fallDistance < getSafeFallDistance();
    }

    @Unique
    private void updateFloating() {
        if (this.isInLava() && fluidWalking(getWorld().getFluidState(getBlockPos()))) {
            ShapeContext shapeContext = ShapeContext.of(this);
            if (shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, this.getBlockPos(), true) && !this.getWorld().getFluidState(this.getBlockPos().up()).isIn(FluidTags.LAVA)) {
                this.setOnGround(true);
            } else {
                LivingEntity self = (LivingEntity) (Object) this;
                if (!(self instanceof PlayerEntity)) this.setVelocity(this.getVelocity().multiply(0.5F).add(0.0F, 0.05, 0.0F));
            }
        }
    }
}