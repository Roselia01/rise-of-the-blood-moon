package net.roselia.bloodmoon.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import net.roselia.bloodmoon.effect.ModEffects;

public class MenacingStatusEffect extends StatusEffect {
    public MenacingStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x7E36D8); // Blue Violet color ゴゴゴ。。。
    }

    // Make entities with this effect cause fear in others that are weaker
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
                if (currentFear == null || currentFear.getDuration() < 200) {
                    target.addStatusEffect(new StatusEffectInstance(
                        ModEffects.FEAR,
                        200,
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
