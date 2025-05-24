package net.roselia.bloodmoon.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import net.roselia.bloodmoon.effect.ModEffects;
import net.roselia.bloodmoon.entity.custom.BloodNeedleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckArrowsFeatureRenderer.class)
public abstract class BloodNeedleVisualMixin {

    @Inject(
        method = "renderObject(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelIfNotNeedled(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                                    Entity entity, float dx, float dy, float dz, float tickDelta, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            StatusEffectInstance needles = living.getStatusEffect(ModEffects.SUPERCOMBINE);
            if (needles == null || needles.getDuration() <= 0) {
                ci.cancel();
            }
        }
    }

    @Redirect(
        method = "renderObject(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFF)V",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/projectile/ArrowEntity",
            ordinal = 0
        )
    )
    private ArrowEntity redirectToBloodNeedle(World world, double x, double y, double z) {
        return new BloodNeedleEntity(world, x, y, z);
    }
}