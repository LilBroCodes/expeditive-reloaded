package org.lilbrocodes.expeditive_reloaded.mixin.accessor;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Unique;

public interface WolfEntityMethodAccessor {
    @Unique void expeditiveReloaded$setTargetIsBoat(boolean value);
    @Unique boolean expeditiveReloaded$getTargetIsBoat();

    @Unique void expeditiveReloaded$setBoatTarget(BoatEntity value);
    @Unique BoatEntity expeditiveReloaded$getBoatTarget();

    @Unique void expeditiveReloaded$setTargetIsArmorStand(boolean value);
    @Unique boolean expeditiveReloaded$getTargetIsArmorStand();

    @Unique void expeditiveReloaded$setArmorStandTarget(ArmorStandEntity value);
    @Unique ArmorStandEntity expeditiveReloaded$getArmorStandTarget();
}
