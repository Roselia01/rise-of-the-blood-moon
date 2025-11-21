package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.SpawnGroup;

@Mixin(SpawnGroup.class)
public class BloodMoonMobCategoryMixin {
    @Inject(method = "getCapacity", at = @At("RETURN"), cancellable = true)
    private void increaseSpawnCap(CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        cir.setReturnValue(original * 4); // Literally 4x the mob cap
    }
}