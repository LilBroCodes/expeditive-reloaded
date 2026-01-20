package org.lilbrocodes.expeditive_reloaded.util;

import com.codex.composer.api.v1.targeting.TargetingContextBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import com.codex.composer.api.v1.targeting.TargetingContext;

public class Misc {
    public static final ContextBuilder PACIFIST_SETTINGS = player ->
            TargetingContextBuilder.create(player)
                    .targetDead(false)
                    .targetTamed(true)
                    .targetNonLiving(true)
                    .minDistance(0)
                    .maxDistance(64)
                    .decayTicks(10)
                    .build();

    public static double distanceTo2D(Entity from, Entity to) {
        Vec3d pos1 = from.getPos();
        Vec3d pos2 = to.getPos();
        double a = (pos1.x - pos2.x) * (pos1.x - pos2.x);
        double b = (pos1.z - pos2.z) * (pos1.z - pos2.z);
        return Math.sqrt(a + b);
    }

    public static MutableText getColoredDyeColorName(DyeColor color) {
        return Text.translatable("color.minecraft." + color.getName())
                .setStyle(Style.EMPTY.withColor(color == DyeColor.BLACK ? DyeColor.WHITE.getSignColor() : color.getSignColor()).withFormatting(Formatting.BOLD));
    }

    @FunctionalInterface
    public interface ContextBuilder {
        TargetingContext build(PlayerEntity player);
    }

    public static MutableText prependDyeColor(Text name, DyeColor color) {
        return getDyeColorName(color).append(" ").append(name);
    }

    public static MutableText getDyeColorName(DyeColor color) {
        return Text.translatable(String.format("color.minecraft.%s", color.getName()));
    }
}
