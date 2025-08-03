package org.lilbrocodes.expeditive_reloaded.mixin.impl.strider_boots;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.lilbrocodes.expeditive_reloaded.ReloadedItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LandPathNodeMaker.class)
public class LandPathNodeMakerMixin {
    @Inject(method = "getNodeType(Lnet/minecraft/world/BlockView;IIILnet/minecraft/entity/mob/MobEntity;)Lnet/minecraft/entity/ai/pathing/PathNodeType;", at = @At("HEAD"), cancellable = true)
    private void modifyLavaPathfinding(BlockView world, int x, int y, int z, MobEntity mob, CallbackInfoReturnable<PathNodeType> cir) {
        if (mob != null && hasStriderBoots(mob)) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = world.getBlockState(pos);

            if (state.isOf(Blocks.LAVA)) {
                cir.setReturnValue(PathNodeType.WALKABLE);
            }
        }
    }

    @Unique
    private boolean hasStriderBoots(MobEntity mob) {
        ItemStack boots = mob.getEquippedStack(EquipmentSlot.FEET);
        return boots.getItem() == ReloadedItems.STRIDER_BOOTS;
    }
}
