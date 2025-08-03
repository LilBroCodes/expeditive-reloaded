package org.lilbrocodes.expeditive_reloaded.mixin.accessor;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.BoatEntity;

public interface WolfEntityMethodAccessor {
    void expeditiveReloaded$setTargetIsBoat(boolean value);
    boolean expeditiveReloaded$getTargetIsBoat();

    void expeditiveReloaded$setBoatTarget(BoatEntity value);
    BoatEntity expeditiveReloaded$getBoatTarget();

    void expeditiveReloaded$setTargetIsArmorStand(boolean value);
    boolean expeditiveReloaded$getTargetIsArmorStand();

    void expeditiveReloaded$setArmorStandTarget(ArmorStandEntity value);
    ArmorStandEntity expeditiveReloaded$getArmorStandTarget();
}
