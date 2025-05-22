package net.roselia.bloodmoon.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

public class TrackedNeedle {
    private final LivingEntity entity;
    private int needleCount = 1;
    private int ticksLeft = 120;

    public TrackedNeedle(LivingEntity entity) {
        this.entity = entity;
    }

    public void resetTimer() {
        ticksLeft = 120;
        if (needleCount < 7) {
            needleCount++;
        }
    }

    public boolean tickDown(ServerWorld world) {
        if (!entity.isAlive()) return true;

        if (--ticksLeft <= 0) {
            explode(world);
            return true;
        }

        return false;
    }

    private void explode(ServerWorld world) {
        float damage;

        if (needleCount == 1) {
            damage = 2.0f;
        } else if (needleCount == 2) {
            damage = 4.0f * 2;
        } else {
            damage = 6.0f * needleCount;
        }

        entity.damage(world.getDamageSources().explosion(null), damage);
    }
}