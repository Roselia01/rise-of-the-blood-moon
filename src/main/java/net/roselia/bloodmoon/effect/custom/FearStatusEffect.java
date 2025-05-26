package net.roselia.bloodmoon.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class FearStatusEffect extends StatusEffect {
    private static final UUID SPEED_BOOST_ID = UUID.fromString("b27b1f6d-c2fc-44b6-8d01-b76e56da4fdd");

    public FearStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1A1A38);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient && !(entity instanceof net.minecraft.entity.player.PlayerEntity)) {
            boolean alreadyBoosted = entity.getAttributes()
                .getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                .getModifier(SPEED_BOOST_ID) != null;

            if (!alreadyBoosted) {
                entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                    .addTemporaryModifier(new EntityAttributeModifier(
                        SPEED_BOOST_ID,
                        "Fear speed boost",
                        0.25D + 0.1D * amplifier,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}