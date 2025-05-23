package net.roselia.bloodmoon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.roselia.bloodmoon.block.ModBlocks;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.entity.NeedleTracker;
import net.roselia.bloodmoon.item.ModItemGroups;
import net.roselia.bloodmoon.item.ModItems;
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

		ServerTickEvents.END_WORLD_TICK.register(world -> {
		if (!world.isClient) {
			NeedleTracker.tick((ServerWorld) world);
		}});
	}
}