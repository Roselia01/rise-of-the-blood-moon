package net.roselia.bloodmoon.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.roselia.bloodmoon.event.BloodMoonState;

@Mixin(ClientWorld.class)
public abstract class BloodMoonClientWorldMixin {

    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void bloodMoonSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        try (ClientWorld world = (ClientWorld)(Object)this) {
            float intensity = BloodMoonState.getBloodMoonIntensity(world.getTimeOfDay() % 24000);
            if (intensity > 0) {
                Vec3d normalColor = cir.getReturnValue();
                Vec3d bloodMoonColor = new Vec3d(0.075, 0.02, 0.001);
                cir.setReturnValue(normalColor.lerp(bloodMoonColor, intensity));
            }
        } catch (CancellationException | IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "getCloudsColor", at = @At("RETURN"), cancellable = true)
    private void bloodMoonCloudColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        try (ClientWorld world = (ClientWorld)(Object)this) {
            float intensity = BloodMoonState.getBloodMoonIntensity(world.getTimeOfDay() % 24000);
            if (intensity > 0) {
                Vec3d normalColor = cir.getReturnValue();
                Vec3d bloodMoonColor = new Vec3d(0.06, 0.02, 0.001);
                cir.setReturnValue(normalColor.lerp(bloodMoonColor, intensity));
            }
        } catch (CancellationException | IOException e) {
            e.printStackTrace();
        }
    }
}