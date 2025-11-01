package org.lilbrocodes.expeditive_reloaded.mixin.impl.bamboo_flute;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;
import org.lilbrocodes.expeditive_reloaded.mixin.accessor.WolfEntityMethodAccessor;
import org.lilbrocodes.expeditive_reloaded.util.Misc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends LivingEntity implements WolfEntityMethodAccessor {
    protected WolfEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique private boolean targetIsBoat = false;
    @Unique private BoatEntity boatTarget = null;

    @Unique private boolean targetIsArmorStand = false;
    @Unique private ArmorStandEntity armorStandTarget = null;

    @Unique
    public void expeditiveReloaded$setTargetIsBoat(boolean value) {
        this.targetIsBoat = value;
    }

    @Unique
    public boolean expeditiveReloaded$getTargetIsBoat() {
        return this.targetIsBoat;
    }

    @Unique
    public void expeditiveReloaded$setBoatTarget(BoatEntity value) {
        this.boatTarget = value;
    }

    @Unique
    public BoatEntity expeditiveReloaded$getBoatTarget() {
        return this.boatTarget;
    }

    @Unique
    public void expeditiveReloaded$setTargetIsArmorStand(boolean value) {
        this.targetIsArmorStand = value;
    }

    @Unique
    public boolean expeditiveReloaded$getTargetIsArmorStand() {
        return this.targetIsArmorStand;
    }

    @Unique
    public void expeditiveReloaded$setArmorStandTarget(ArmorStandEntity value) {
        this.armorStandTarget = value;
    }

    @Unique
    public ArmorStandEntity expeditiveReloaded$getArmorStandTarget() {
        return this.armorStandTarget;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        WolfEntity wolf = (WolfEntity) (Object) this;
        if (wolf.getTarget() != null && expeditiveReloaded$getTargetIsBoat()) {
            BoatEntity target = expeditiveReloaded$getBoatTarget();
            assert target != null;
            if (Misc.distanceTo2D(wolf, target) < 2 && !target.hasPassengers()) {
                wolf.startRiding(target);
                wolf.getNavigation().stop();
                wolf.setTarget(null);
            } else {
                wolf.getNavigation().stop();
                wolf.getNavigation().startMovingTo(wolf.getOwner(), 1.0F);
            }
            expeditiveReloaded$setBoatTarget(null);
            expeditiveReloaded$setTargetIsBoat(false);
        } else if (wolf.getTarget() != null && expeditiveReloaded$getTargetIsArmorStand()) {
            ArmorStandEntity target = expeditiveReloaded$getArmorStandTarget();
            assert target != null;
            if(Misc.distanceTo2D(wolf, target) < 2) {
                wolf.setAngerTime(0);
                wolf.setTarget(null);
                wolf.getNavigation().stop();
                wolf.getNavigation().startMovingTo(wolf.getOwner(), 1.0f);
            }
            expeditiveReloaded$setArmorStandTarget(null);
            expeditiveReloaded$setTargetIsArmorStand(false);
        }
    }
}
