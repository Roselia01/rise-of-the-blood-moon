package net.roselia.bloodmoon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.PathAwareEntity;
import net.roselia.bloodmoon.block.ModBlocks;
import net.roselia.bloodmoon.effect.ModEffects;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.item.ModItemGroups;
import net.roselia.bloodmoon.item.ModItems;
import net.roselia.bloodmoon.mixin.MobEntityAccessor;
import net.roselia.bloodmoon.sound.ModSounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bloodmoon implements ModInitializer {
	public static String MOD_ID = "bloodmoon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntities.registerModEntities();
		ModEffects.registerEffects();
		ModSounds.registerSounds();

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PathAwareEntity mob) {
                if (shouldExcludeMob(mob)) return;

                GoalSelector goals = ((MobEntityAccessor) mob).getGoalSelector();
                goals.add(1, new FleeEntityGoal<>(mob, LivingEntity.class, 
                        10.0F, 1.0, 1.25, target -> {
                    return target.hasStatusEffect(ModEffects.MENACING) 
                        && target.getHealth() > mob.getHealth();
                }));
            }
        });
    }

    private boolean shouldExcludeMob(PathAwareEntity mob) {
        return mob.getType() == EntityType.WARDEN 
			|| mob.getType() == EntityType.ENDER_DRAGON;
    }
}