package net.roselia.bloodmoon.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.entity.Entity;
import net.roselia.bloodmoon.effect.ModEffects;
import net.roselia.bloodmoon.sound.custom.HeartBeatSound;
import net.roselia.bloodmoon.sound.custom.SheaphardSound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    private HeartBeatSound heartbeatInstance = null;
    private SheaphardSound sheaphardInstance = null;

    @Inject(method = "update", at = @At("TAIL"))
    private void applyFearJitter(BlockView area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.isPaused()) return;

        if (!(entity instanceof LivingEntity living)) return;
        if (!living.hasStatusEffect(ModEffects.FEAR)) {
            return;
        }

        StatusEffectInstance fear = living.getStatusEffect(ModEffects.FEAR);
        if (fear == null) return;

        int amp = fear.getAmplifier() + 1;
        float baseStrength = 0.15F * amp;
        float proximityMultiplier = 1.0F;

        if (client.world != null) {
            Vec3d playerPos = living.getPos();
            for (Entity e : client.world.getEntities()) {
                if (e instanceof LivingEntity other && other.hasStatusEffect(ModEffects.MENACING)) {
                    double distance = playerPos.distanceTo(other.getPos());
                    if (distance < 10.0) {
                        float mult = (float)(10.0 - distance) * 0.25F;
                        proximityMultiplier += mult;
                    }
                }
            }
        }

        float finalStrength = baseStrength * proximityMultiplier;
        float yawJitter = (float) ((Math.random() - 0.5F) * finalStrength);
        float pitchJitter = (float) ((Math.random() - 0.5F) * finalStrength);

        living.setYaw(living.getYaw() + yawJitter);
        living.setPitch(living.getPitch() + pitchJitter);

        double motionJitter = 0.005 * amp * proximityMultiplier;
        double dx = (Math.random() - 0.5) * motionJitter;
        double dz = (Math.random() - 0.5) * motionJitter;

        Vec3d velocity = living.getVelocity();
        living.setVelocity(velocity.add(dx, 0, dz));

        if (!client.player.hasStatusEffect(ModEffects.FEAR)) {
        if (heartbeatInstance != null) {
            heartbeatInstance = null;
        }
        else if (sheaphardInstance != null) {
            sheaphardInstance = null;
        }
        return;
        }

        if (heartbeatInstance == null || heartbeatInstance.isDone()) {
            heartbeatInstance = new HeartBeatSound(client.player);
            client.getSoundManager().play(heartbeatInstance);
        }

        if (sheaphardInstance == null || sheaphardInstance.isDone()) {
            sheaphardInstance = new SheaphardSound(client.player);
            client.getSoundManager().play(sheaphardInstance);
        }
    }
}