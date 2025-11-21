package net.roselia.bloodmoon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.block.ModBlocks;
import net.roselia.bloodmoon.effect.ModEffects;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.event.BloodMoonState;
import net.roselia.bloodmoon.item.ModItemGroups;
import net.roselia.bloodmoon.item.ModItems;
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

		// Register the bloodmoon command
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
				CommandManager.literal("bloodmoon")
					.requires(source -> source.hasPermissionLevel(2)) // Require permission level 2 (same as other game-changing commands)
					.executes(context -> {
						if (BloodMoonState.IS_BLOOD_MOON) {
							context.getSource().sendMessage(Text.literal("A Blood Moon is already planned for tonight!"));
							return 0;
						}

						BloodMoonState.IS_BLOOD_MOON = true;
						context.getSource().getServer().getPlayerManager().getPlayerList().forEach(player ->
							player.sendMessage(Text.literal("You catch the scent of blood in the air..."), true));
						return 1;
					})
			);
		});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (id.equals(new Identifier("minecraft", "chests/bastion_treasure"))) {
				LootPool.Builder poolBuilder = LootPool.builder()
					.with(ItemEntry.builder(ModItems.NETHERITE_BOW)
						.apply(SetDamageLootFunction.builder(
							UniformLootNumberProvider.create(0.01f, 0.3f)
						))
					)
					.rolls(ConstantLootNumberProvider.create(1))
					.conditionally(RandomChanceLootCondition.builder(0.5f));

				tableBuilder.pool(poolBuilder.build());
			}
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (world.isClient) return;

			long timeOfDay = world.getTimeOfDay() % 24000;

			// Trigger at exactly 12 PM
			if (timeOfDay >= 6000 && timeOfDay < 13000 && world.getRandom().nextFloat() < 0.001F && !BloodMoonState.IS_BLOOD_MOON) {
				LOGGER.info("Attempting to trigger Blood Moon at time: " + timeOfDay);
				if (world.getRandom().nextFloat() < 0.005F) {
					BloodMoonState.IS_BLOOD_MOON = true;
					world.getPlayers().forEach(player ->
						player.sendMessage(Text.literal("You catch the scent of blood in the air..."), true));
					LOGGER.info("Blood Moon successfully triggered at time: " + timeOfDay);
				}
			}

			// Reset at dawn
			if ((timeOfDay > 23480 || timeOfDay < 6000) && BloodMoonState.BLOOD_MOON_RISEN) {
				BloodMoonState.BLOOD_MOON_RISEN = false;
				BloodMoonState.IS_BLOOD_MOON = false;
				world.getPlayers().forEach(player ->
					player.sendMessage(Text.literal("The sun rises as the stench of blood fades."), true));
			}

			// Show message once the moon is visible in the sky
			if (timeOfDay >= 13000 && timeOfDay <= 23480 && BloodMoonState.IS_BLOOD_MOON && !BloodMoonState.BLOOD_MOON_RISEN) {
				BloodMoonState.BLOOD_MOON_RISEN = true;
				world.getPlayers().forEach(player -> {
					if (player instanceof ServerPlayerEntity) {
						ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
						serverPlayer.getAdvancementTracker().grantCriterion(
							serverPlayer.server.getAdvancementLoader().get(new Identifier("bloodmoon", "bloodmoon_trigger")),
							"triggered_bloodmoon"
						);
						player.sendMessage(Text.literal("The Blood Moon is rising..."), true);
						
						// Only play the sound if the player is in the overworld
						if (player.getWorld().getRegistryKey() == net.minecraft.world.World.OVERWORLD) {
							player.getWorld().playSound(
								null,
								player.getX(), player.getY(), player.getZ(),
								net.minecraft.registry.Registries.SOUND_EVENT.get(new Identifier("minecraft", "entity.ender_dragon.ambient")),
								net.minecraft.sound.SoundCategory.AMBIENT,
								1.0f,  // volume
								0.5f   // pitch
							);
						}
					}
				});
			}
		});
    }
}