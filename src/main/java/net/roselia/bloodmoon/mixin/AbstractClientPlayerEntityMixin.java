package net.roselia.bloodmoon.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.roselia.bloodmoon.api.BowItemType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    private AbstractClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, world.getSpawnPos(), world.getSpawnAngle(), profile);
    }

    @ModifyVariable(
            method = "getFovMultiplier()F",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0
    )
    private float applyZoomToFov(float fov) {
        ItemStack stack = this.getActiveItem();
        Item item = stack.getItem();

        if (this.isUsingItem() && item instanceof BowItemType bow) {
            float drawTime = (float) this.getItemUseTime() / bow.getDrawSpeed();

            if (drawTime > 1.0F) drawTime = 1.0F;
            else drawTime *= drawTime; // vanilla squaring behavior

            fov *= 1.0F - (drawTime * 0.15F);
        }

        return fov;
    }
}
