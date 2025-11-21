// package net.roselia.bloodmoon.mixin;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import net.minecraft.server.world.ServerWorld;
// import net.minecraft.world.SpawnHelper;

// @Mixin(ServerWorld.class)
// public abstract class BloodMoonServerWorldMixin {
//     @Inject(method = "tickSpawns", at = @At("HEAD"))
//     private void increaseSpawnAttempts(boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
//         // Call spawnEntities multiple times
//         for (int i = 0; i < 4; i++) {
//             SpawnHelper; // repeat natural spawning
//         }
//     }
// }