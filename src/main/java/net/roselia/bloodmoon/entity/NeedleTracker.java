package net.roselia.bloodmoon.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class NeedleTracker {
    private static final Map<UUID, TrackedNeedle> tracked = new HashMap<>();

    public static void registerHit(LivingEntity entity) {
        UUID id = entity.getUuid();
        if (tracked.containsKey(id)) {
            TrackedNeedle needle = tracked.get(id);
            needle.resetTimer();
        } else {
            tracked.put(id, new TrackedNeedle(entity));
        }
    }

    public static void tick(ServerWorld world) {
        Iterator<Map.Entry<UUID, TrackedNeedle>> it = tracked.entrySet().iterator();
        while (it.hasNext()) {
            TrackedNeedle needle = it.next().getValue();
            if (needle.tickDown(world)) {
                it.remove();
            }
        }
    }
}
