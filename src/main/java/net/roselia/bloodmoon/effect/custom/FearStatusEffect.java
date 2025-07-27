package net.roselia.bloodmoon.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.roselia.bloodmoon.mixin.MobEntityAccessor;
import net.roselia.bloodmoon.effect.ModEffects;
import net.minecraft.entity.EntityType;

import java.util.UUID;

public class FearStatusEffect extends StatusEffect {
	private static final UUID SPEED_BOOST_ID = UUID.fromString("b27b1f6d-c2fc-44b6-8d01-b76e56da4fdd");

	public FearStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0x1A1A38); // Space Cadet color
	}

	// Entities that aren't players get a speed boost when affected by fear
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.getWorld().isClient && !(entity instanceof PlayerEntity)) {
			var attr = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
			if (attr != null && attr.getModifier(SPEED_BOOST_ID) == null) {
				attr.addTemporaryModifier(new EntityAttributeModifier(
					SPEED_BOOST_ID,
					"Fear speed boost",
					0.25D + 0.1D * amplifier,
					EntityAttributeModifier.Operation.MULTIPLY_TOTAL
				));
			}
		}
	}

	@Override
    public void onApplied(LivingEntity entity, net.minecraft.entity.attribute.AttributeContainer attributes, int amplifier) {
        if (entity instanceof PathAwareEntity mob && !shouldExcludeMob(mob)) {
            GoalSelector goals = ((MobEntityAccessor) mob).getGoalSelector();

            // Add flee behavior to entities that are not players
            goals.add(1, new FleeEntityGoal<>(mob, LivingEntity.class,
                10.0F, 1.0, 1.25, target ->
                    target.hasStatusEffect(ModEffects.MENACING) &&
                    target.getHealth() > mob.getHealth()
            ));
        }
    }

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	// Exclude certain mobs from the flee behavior
	private boolean shouldExcludeMob(PathAwareEntity mob) {
		EntityType<?> type = mob.getType();
		return type == EntityType.WARDEN || type == EntityType.ENDER_DRAGON;
	}
}