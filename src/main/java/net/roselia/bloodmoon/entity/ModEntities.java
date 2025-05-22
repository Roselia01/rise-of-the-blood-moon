package net.roselia.bloodmoon.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.Bloodmoon;
import net.roselia.bloodmoon.entity.custom.BloodNeedleEntity;

public class ModEntities {

    public static final EntityType<BloodNeedleEntity> BLOOD_NEEDLE = Registry.register(
    Registries.ENTITY_TYPE,
    new Identifier(Bloodmoon.MOD_ID, "blood_needle"),
    FabricEntityTypeBuilder.<BloodNeedleEntity>create(SpawnGroup.MISC, BloodNeedleEntity::new)
        .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
        .trackRangeBlocks(8)
        .trackedUpdateRate(20)
        .build()
);

    public static void registerModEntities() {
        Bloodmoon.LOGGER.info("Registering Bloodmoon Entities...");
    }
}
