package org.lilbrocodes.expeditive_reloaded.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.expeditive_reloaded.ExpeditiveReloaded;

public class PlayedFluteCriterion extends AbstractCriterion<PlayedFluteCriterion.Conditions> {
    static final Identifier ID = ExpeditiveReloaded.identify("played_flute");

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, @Nullable DyeColor color) {
        this.trigger(player, conditions -> conditions.test(color));
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject json, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        String colorName = JsonHelper.getString(json, "color", null);
        DyeColor color = colorName != null ? DyeColor.byName(colorName, null) : null;
        return new Conditions(playerPredicate, color);
    }

    public static class Conditions extends AbstractCriterionConditions {
        @Nullable private final DyeColor color;

        public Conditions(LootContextPredicate playerPredicate, @Nullable DyeColor color) {
            super(ID, playerPredicate);
            this.color = color;
        }

        public boolean test(@Nullable DyeColor playedColor) {
            return color == null || color == playedColor;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
            JsonObject json = super.toJson(serializer);
            if (color != null) {
                json.addProperty("color", color.getName());
            }
            return json;
        }
    }
}

