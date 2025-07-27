package net.roselia.bloodmoon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

@Mixin(LightmapTextureManager.class)
public interface LightmapTextureManagerAccessor {
    @Accessor("image")
    NativeImage getImage();

    @Accessor("texture")
    NativeImageBackedTexture getTexture();
}