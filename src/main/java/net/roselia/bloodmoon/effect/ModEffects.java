package net.roselia.bloodmoon.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.Bloodmoon;
import net.roselia.bloodmoon.effect.custom.FearStatusEffect;
import net.roselia.bloodmoon.effect.custom.MenacingStatusEffect;
import net.roselia.bloodmoon.effect.custom.SupercombineStatusEffect;

public class ModEffects {
    public static final StatusEffect SUPERCOMBINE = Registry.register(
        Registries.STATUS_EFFECT,
        new Identifier(Bloodmoon.MOD_ID, "supercombine"),
        new SupercombineStatusEffect()
    );

    public static final StatusEffect MENACING = Registry.register(
        Registries.STATUS_EFFECT,
        new Identifier(Bloodmoon.MOD_ID, "menacing"),
        new MenacingStatusEffect()
    );

    public static final StatusEffect FEAR = Registry.register(
        Registries.STATUS_EFFECT,
        new Identifier(Bloodmoon.MOD_ID, "fear"),
        new FearStatusEffect()
    );

    public static void registerEffects() {
        Bloodmoon.LOGGER.info("Registering Bloodmoon Effects...");
    }
}