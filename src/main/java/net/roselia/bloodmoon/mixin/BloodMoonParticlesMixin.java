package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.roselia.bloodmoon.event.BloodMoonState;

@Mixin(ClientPlayerEntity.class)
public class BloodMoonParticlesMixin {
    private static final Random RANDOM = Random.create();
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void spawnBloodMoonParticles(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        ClientWorld world = player.clientWorld;
        
        if (world == null) return;
        
        float intensity = BloodMoonState.getBloodMoonIntensity(world.getTimeOfDay() % 24000);
        if (intensity <= 0) return;

        // Spawn particles in a radius around the player
        for (int i = 0; i < 3; i++) {  // Spawn 3 particles per tick
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double radius = RANDOM.nextDouble() * 15.0; // Random radius up to 15 blocks
            double height = RANDOM.nextDouble() * 10.0; // Random height up to 10 blocks
            
            double x = player.getX() + Math.cos(angle) * radius;
            double y = player.getY() + height;
            double z = player.getZ() + Math.sin(angle) * radius;
            
            world.addParticle(
                ParticleTypes.CRIMSON_SPORE,
                x, y, z,
                0, -0.02, 0  // Slowly fall down
            );
        }
    }
}
