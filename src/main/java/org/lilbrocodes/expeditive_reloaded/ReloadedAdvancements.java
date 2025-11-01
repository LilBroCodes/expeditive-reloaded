package org.lilbrocodes.expeditive_reloaded;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import org.lilbrocodes.expeditive_reloaded.criterion.PlayedFluteCriterion;

public class ReloadedAdvancements {
    public static final Identifier STRIDER_APPROVED = ExpeditiveReloaded.identify("strider_approved");

    public static class Criterion {
        public static final PlayedFluteCriterion PLAYED_FLUTE = Criteria.register(new PlayedFluteCriterion());

        @SuppressWarnings("EmptyMethod")
        public static void initialize() {

        }
    }

    public static void initialize() {
        Criterion.initialize();
    }
}
