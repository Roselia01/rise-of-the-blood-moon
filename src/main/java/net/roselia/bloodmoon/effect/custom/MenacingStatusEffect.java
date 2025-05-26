package net.roselia.bloodmoon.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import net.roselia.bloodmoon.effect.ModEffects;

public class MenacingStatusEffect extends StatusEffect {
    public MenacingStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x7E36D8);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        if (!world.isClient) {
            world.getEntitiesByClass(
                LivingEntity.class,
                entity.getBoundingBox().expand(10.0),
                target -> target != entity
                    && target.isAlive()
                    && target.getHealth() < entity.getHealth()
                    && !target.hasStatusEffect(ModEffects.MENACING)
            ).forEach(target -> {
                StatusEffectInstance currentFear = target.getStatusEffect(ModEffects.FEAR);
                if (currentFear == null || currentFear.getDuration() < 100) {
                    target.addStatusEffect(new StatusEffectInstance(
                        ModEffects.FEAR,
                        100,
                        0,
                        true,
                        true
                    ));
                }
            });
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
