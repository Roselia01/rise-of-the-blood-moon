package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.event.BloodMoonState;

@Mixin(WorldRenderer.class)
public class BloodMoonWorldRendererMixin {
    private static final Identifier MOON_TEXTURE = new Identifier("minecraft", "textures/environment/moon_phases.png");
    private static final float TRANSITION_DURATION = 1000.0f; // Duration in ticks (30 seconds)
    private static float currentIntensity = 0.0f;
    private static boolean wasBloodMoon = false;
    
    @Redirect(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/render/WorldRenderer;MOON_PHASES:Lnet/minecraft/util/Identifier;"
        )
    )
    private Identifier redirectMoonTexture() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return MOON_TEXTURE;
        
        // Smoothly transition the intensity
        if (BloodMoonState.IS_BLOOD_MOON != wasBloodMoon) {
            wasBloodMoon = BloodMoonState.IS_BLOOD_MOON;
        }
        
        float targetIntensity = BloodMoonState.IS_BLOOD_MOON ? 1.0f : 0.0f;
        if (currentIntensity != targetIntensity) {
            float step = 1.0f / TRANSITION_DURATION;
            if (currentIntensity < targetIntensity) {
                currentIntensity = Math.min(currentIntensity + step, targetIntensity);
            } else {
                currentIntensity = Math.max(currentIntensity - step, targetIntensity);
            }
        }
        
        if (currentIntensity > 0) {
            // Pure red color with intensity-based transition
            float r = 1.0f;                    // Full red (always at max)
            float g = 1.0f - currentIntensity; // Fade out green completely
            float b = 1.0f - currentIntensity; // Fade out blue completely
            RenderSystem.setShaderColor(r, g, b, 1.0f);
        } else {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        
        return MOON_TEXTURE;
    }
    
    @Redirect(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"
        )
    )
    private int redirectMoonPhase(net.minecraft.client.world.ClientWorld world) {
        if (currentIntensity > 0) {
            return 0; // Force full moon during blood moon
        }
        return world.getMoonPhase(); // Return original moon phase otherwise
    }
}