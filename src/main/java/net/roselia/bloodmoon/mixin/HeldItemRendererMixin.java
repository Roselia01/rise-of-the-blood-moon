package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.roselia.bloodmoon.api.BowItemType;

@Mixin(value = HeldItemRenderer.class, priority = 1100)
public class HeldItemRendererMixin {
    @ModifyConstant(
        method = "renderFirstPersonItem",
        constant = @Constant(floatValue = 20.0F)
    )
    private static float modifyBowDrawConstant(
        float original,
        AbstractClientPlayerEntity player,
        float tickDelta,
        float pitch,
        Hand hand,
        float swingProgress,
        ItemStack stack,
        float equipProgress,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light
    )
    
    {
        Item item = stack.getItem();
        if (item instanceof BowItemType bow) {
            return bow.getDrawSpeed();
        }
        return original;
    }
}
