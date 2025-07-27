package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.biome.BiomeEffects;
import net.roselia.bloodmoon.event.BloodMoonState;

@Mixin(BiomeEffects.class)
public class BloodMoonBiomeEffectsMixin {

    @Inject(method = "getWaterFogColor", at = @At("RETURN"), cancellable = true)
    private void overrideWaterFogColor(CallbackInfoReturnable<Integer> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        
        float intensity = BloodMoonState.getBloodMoonIntensity(client.world.getTimeOfDay() % 24000);
        if (intensity > 0) {
            int normalColor = cir.getReturnValue();
            int bloodColor = 0x230055;
            cir.setReturnValue(lerpColors(normalColor, bloodColor, intensity));
        }
    }

    private int lerpColors(int color1, int color2, float factor) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int)(r1 + (r2 - r1) * factor);
        int g = (int)(g1 + (g2 - g1) * factor);
        int b = (int)(b1 + (b2 - b1) * factor);

        return (r << 16) | (g << 8) | b;
    }

    @Inject(method = "getFogColor", at = @At("RETURN"), cancellable = true)
    private void overrideFogColor(CallbackInfoReturnable<Integer> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        
        float intensity = BloodMoonState.getBloodMoonIntensity(client.world.getTimeOfDay() % 24000);
        if (intensity > 0) {
            int normalColor = cir.getReturnValue();
            int bloodColor = 0xBF0000;
            cir.setReturnValue(lerpColors(normalColor, bloodColor, intensity));
        }
    }
}
