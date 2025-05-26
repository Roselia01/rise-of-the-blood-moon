package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;

@Mixin(MobEntity.class)
public interface MobEntityAccessor {
    @Accessor("goalSelector")
    GoalSelector getGoalSelector();
}
