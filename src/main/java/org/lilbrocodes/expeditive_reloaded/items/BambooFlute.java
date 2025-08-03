package org.lilbrocodes.expeditive_reloaded.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.composer_reloaded.api.targeting.Targeting;
import org.lilbrocodes.expeditive_reloaded.ReloadedAdvancements;
import org.lilbrocodes.expeditive_reloaded.mixin.accessor.WolfEntityMethodAccessor;
import org.lilbrocodes.expeditive_reloaded.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.lilbrocodes.expeditive_reloaded.util.Misc.distanceTo2D;

public class BambooFlute extends Item {

    public BambooFlute() {
        super(new FabricItemSettings().maxCount(1));
    }

    public void playSound(World world, Entity entity, SoundEvent soundEvent, float p) {
        world.playSoundFromEntity((PlayerEntity) entity, entity, soundEvent, SoundCategory.PLAYERS, 10, p);
        world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, entity.getPos(), GameEvent.Emitter.of(entity));
    }

    private int mapAngle(float pitch) {
        pitch = Math.max(-90, Math.min(90, pitch)) * -1;

        int actionIndex;
        if (pitch >= 80) {
            actionIndex = 16;
        } else if (pitch <= -80) {
            actionIndex = 1;
        } else {
            float normalizedPitch = (pitch + 80) / 160f;
            actionIndex = 2 + Math.round(normalizedPitch * 14);
        }

        return actionIndex;
    }

    private float getPitchFromSeg(int segment) {
        segment = Math.max(1, Math.min(16, segment));
        return 0.5f + (segment - 1) * (1.5f / 15);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 200;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    public static List<WolfEntity> getPlayerOwnedWolves(World world, PlayerEntity player) {
        List<WolfEntity> ownedWolves = new ArrayList<>();

        Box worldBox = new Box(player.getBlockPos()).expand(1024);

        for (WolfEntity wolf : world.getEntitiesByClass(WolfEntity.class, worldBox, LivingEntity::isAlive)) {
            if (wolf.isTamed()) {
                UUID wolfOwnerUuid = wolf.getOwnerUuid();
                if (wolfOwnerUuid != null && wolfOwnerUuid.equals(player.getUuid())) {
                    ownedWolves.add(wolf);
                }
            }
        }

        return ownedWolves;
    }

    private static List<WolfEntity> getPlayerOwnedWolves(World world, PlayerEntity player, DyeColor color) {
        List<WolfEntity> ownedWolves = getPlayerOwnedWolves(world, player);
        ownedWolves.removeIf(wolf -> wolf.getCollarColor() != color);
        return ownedWolves;
    }

    public static void setDyed(ItemStack stack, @Nullable DyeColor color) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (color != null) {
            nbt.putString("color", color.getName());
        }
    }

    @Nullable
    public static DyeColor getColor(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("color", NbtElement.STRING_TYPE)) {
            return DyeColor.byName(nbt.getString("color"), null);
        }
        return null;
    }

    public void executePlayedAction(PlayerEntity player, int segment, ItemStack stack) {
        List<WolfEntity> wolvesToCommand;
        if (getColor(stack) != null) {
            wolvesToCommand = getPlayerOwnedWolves(player.getWorld(), player, getColor(stack));
        } else {
            wolvesToCommand = getPlayerOwnedWolves(player.getWorld(), player);
        }
        if (segment == 1) {
            for (WolfEntity wolf : wolvesToCommand) {
                if (!wolf.isLeashed()) sendWolfTo(wolf, player, 1.0F, false);
            }
        } else if (segment == 16) {
            for (WolfEntity wolf : wolvesToCommand) {
                wolf.setSitting(!wolf.isSitting());
                wolf.setInSittingPose(wolf.isSitting());
            }
        } else {
            Entity target = Targeting.getTargetedEntity(player, Misc.PACIFIST_SETTINGS.build(player));
            if (target == null) return;
            double distance = distanceTo2D(player, target);
            if (!(target instanceof LivingEntity)) {
                if (target instanceof BoatEntity) {
                    for (WolfEntity wolf : wolvesToCommand) {
                        if (wolf instanceof WolfEntityMethodAccessor accessor) {
                            accessor.expeditiveReloaded$setTargetIsBoat(true);
                            accessor.expeditiveReloaded$setBoatTarget((BoatEntity) target);
                            sendWolfTo(wolf, target, 1.0F, false);
                        }
                    }
                } else if (target instanceof ArmorStandEntity) {
                    for (WolfEntity wolf : wolvesToCommand) {
                        if (wolf instanceof WolfEntityMethodAccessor accessor) {
                            accessor.expeditiveReloaded$setTargetIsArmorStand(true);
                            accessor.expeditiveReloaded$setArmorStandTarget((ArmorStandEntity) target);
                            sendWolfTo(wolf, target, 1.0F, false);
                        }
                    }
                }
            } else if (target instanceof WolfEntity wolf && wolvesToCommand.contains(wolf) && distance <= 64) {
                wolf.setSitting(!wolf.isSitting());
                wolf.setInSittingPose(wolf.isSitting());
            } else if (distance <= 64) {
                if (target instanceof WolfEntity && ((WolfEntity) target).getOwnerUuid() == player.getUuid()) return;
                for (WolfEntity wolf : wolvesToCommand) {
                    if (!wolf.isSitting() && !wolf.isLeashed()) {
                        sendWolfTo(wolf, target, 1.0F, true);
                    }
                }
            }
        }
    }

    public void sendWolfTo(WolfEntity wolf, Entity target, float speed, boolean attack) {
        wolf.getNavigation().stop();
        wolf.setTarget(null);
        wolf.setAngerTime(0);

        if (wolf.isSitting()) {
            wolf.setSitting(false);
            wolf.setInSittingPose(false);
        }

        if (!attack) {
            wolf.getNavigation().startMovingTo(target, speed);
        } else {
            wolf.setTarget((LivingEntity) target);
            wolf.setAngryAt(target.getUuid());
            wolf.setAngerTime(-1);
            wolf.setAttacking(true);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        user.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));

        int seg = mapAngle(user.getPitch());
        float pitch = getPitchFromSeg(seg);

        if (!world.isClient) {
            executePlayedAction(user, seg, itemStack);

            if (user instanceof ServerPlayerEntity serverPlayer) {
                ReloadedAdvancements.Criterion.PLAYED_FLUTE.trigger(serverPlayer, getColor(itemStack));
            }
        };
        playSound(world, user, SoundEvents.BLOCK_NOTE_BLOCK_FLUTE.value(), pitch);

        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        setDyed(stack, null);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        DyeColor color = getColor(stack);

        if (color != null) {
            tooltip.add(Text.translatable("itemGroup.expeditive_reloaded").formatted(Formatting.BLUE));

            tooltip.add(Text.translatable(
                    "expeditive_reloaded.bamboo_flute.tooltip_color",
                    Misc.getColoredDyeColorName(color)
            ).formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tooltip_all").formatted(Formatting.GRAY));
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (Screen.hasShiftDown()) {
            appendTutorial(tooltip);
        } else {
            tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.show", Text.translatable(client.options.sneakKey.getDefaultKey().getTranslationKey())).formatted(Formatting.GRAY));
        }
    }

    private void appendTutorial(List<Text> tooltip) {
        tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.header").formatted(Formatting.GOLD));

        tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.note_pitch").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.look_down").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.look_up").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.look_at_controlled_wolf").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("expeditive_reloaded.bamboo_flute.tutorial.look_at_other").formatted(Formatting.GRAY));
    }


    @Override
    public Text getName(ItemStack stack) {
        DyeColor color = getColor(stack);

        if (color != null) {
            return Misc.prependDyeColor(getName(), color);
        }

        return super.getName(stack);
    }
}
