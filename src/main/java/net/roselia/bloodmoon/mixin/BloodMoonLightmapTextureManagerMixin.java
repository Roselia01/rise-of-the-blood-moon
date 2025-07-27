package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.roselia.bloodmoon.event.BloodMoonState;

@Mixin(LightmapTextureManager.class)
public abstract class BloodMoonLightmapTextureManagerMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void onUpdate(float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || !client.world.getDimension().hasSkyLight()) return;

        float bloodMoonIntensity = BloodMoonState.getBloodMoonIntensity(client.world.getTimeOfDay() % 24000);
        if (bloodMoonIntensity <= 0) return;

        NativeImage image = ((LightmapTextureManagerAccessor)(Object)this).getImage();
        NativeImageBackedTexture texture = ((LightmapTextureManagerAccessor)(Object)this).getTexture();

        // Calculate base multipliers for the blood moon effect
        float baseRMult = 1.0f - (0.7f * bloodMoonIntensity); // From 1.0 to 0.3
        float baseGMult = 1.0f - (0.7f * bloodMoonIntensity); // From 1.0 to 0.3
        float baseBMult = 1.0f + (0.25f * bloodMoonIntensity); // From 1.0 to 1.25

        // Skip the first row (y=0) to prevent UI/HUD tinting
        for (int y = 0; y < 16; y++) {
            // Sky light influence (0.0 to 1.0) - higher means more sky exposure
            float skyLightInfluence = (float)y / 15.0f;
            
            for (int x = 0; x < 15; x++) {
                int color = image.getColor(x, y);

                int a = (color >> 24) & 0xFF;
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;

                // Calculate block light influence (higher means brighter light source)
                float blockLightLevel = (float)x / 15.0f;
                
                // Blood moon effect is stronger with more sky exposure, but reduced by bright light sources
                float bloodMoonInfluence = skyLightInfluence * (1.0f - (blockLightLevel * 0.8f));
            
                // Interpolate between blood moon and original colors based on light level
                float rMult = baseRMult + (1.0f - baseRMult) * (1.0f - bloodMoonInfluence);
                float gMult = baseGMult + (1.0f - baseGMult) * (1.0f - bloodMoonInfluence);
                float bMult = baseBMult + (1.0f - baseBMult) * (1.0f - bloodMoonInfluence);

            // Apply the interpolated tint 
            int newR = Math.min(255, (int)(r * rMult));
            int newG = Math.min(255, (int)(g * gMult));
            int newB = Math.min(255, (int)(b * bMult));

            int newColor = (a << 24) | (newR << 16) | (newG << 8) | newB;

            image.setColor(x, y, newColor);
            }
        }

        texture.upload();
    }   
}