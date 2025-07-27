package net.roselia.bloodmoon.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class SupercombineStatusEffect extends StatusEffect {

    public SupercombineStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xF02543); // Fear the red mist...
    }

    // This effect detonates when the duration runs out, dealing damage based on the amplifier
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        if (!world.isClient) {
            int duration = entity.getStatusEffect(this).getDuration();
            if (duration <= 1) {
                detonate(entity, amplifier);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    // The detonator in question
    private void detonate(LivingEntity entity, int needleCount) {
        int count = Math.min(needleCount + 1, 7);
        int damagePer;

        if (count <= 3) damagePer = 2; // 1-3 needles deal 2 damage each
        else if (count <= 5) damagePer = 4; // 4-5 needles deal 4 damage each
        else damagePer = 6; // 6+ needles deal 6 damage each

        int totalDamage = count * damagePer;

        entity.damage(entity.getDamageSources().magic(), totalDamage);

        // Spawn explosion particles
        ((ServerWorld) entity.getWorld()).spawnParticles(
            ParticleTypes.EXPLOSION, 
            entity.getX(), entity.getBodyY(0.5), entity.getZ(), 
            1,
            0.0, 0.0, 0.0,
            1.0
        );

        // and sound effects
        entity.getWorld().playSound(
            null,
            entity.getX(), entity.getY(), entity.getZ(),
            SoundEvents.ENTITY_GENERIC_EXPLODE,
            SoundCategory.AMBIENT,
            1.0F,
            1.0F
        );
    }
}