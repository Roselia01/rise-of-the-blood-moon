package net.roselia.bloodmoon.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.Bloodmoon;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModSounds {
    public static final SoundEvent HEARTBEAT = register("misc.heartbeat");
    public static final SoundEvent SHEAPHARD = register("misc.sheaphard");
    public static final SoundEvent HUMAN_FLESH_RECORD = register("record.human_flesh");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier("bloodmoon", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Bloodmoon.LOGGER.info("Registering Mod Sounds for " + Bloodmoon.MOD_ID);
    }
}