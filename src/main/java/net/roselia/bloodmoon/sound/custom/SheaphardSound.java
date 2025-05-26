package net.roselia.bloodmoon.sound.custom;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.roselia.bloodmoon.effect.ModEffects;
import net.roselia.bloodmoon.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;

public class SheaphardSound extends MovingSoundInstance {
    private final PlayerEntity player;
    private boolean fadingOut = false;
    private boolean fadingIn = true;

    public SheaphardSound(PlayerEntity player) {
        super(ModSounds.SHEAPHARD, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.player = player;
        this.volume = 0.0f;
        this.pitch = 1.0f;
        this.repeat = true;
        this.repeatDelay = 0;
        this.relative = true;
        this.attenuationType = AttenuationType.NONE;
    }

    @Override
    public void tick() {
        boolean shouldStop = player.isRemoved() || player.isDead() 
                             || !player.hasStatusEffect(ModEffects.FEAR);
        if (shouldStop) {
            if (!fadingOut) {
                fadingOut = true;
                fadingIn = false;
            }
        } else {
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
        }

        if (fadingIn) {
            this.volume = Math.min(1.0F, this.volume + 0.002F);
            if (this.volume >= 1.0F) {
                fadingIn = false;
            }
        } else if (fadingOut) {
            this.volume = Math.max(0.0F, this.volume - 0.005F);
            if (this.volume == 0.0F) {
                this.setDone();
            }
        }
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }
}
