package net.roselia.bloodmoon.entity.custom;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.roselia.bloodmoon.effect.ModEffects;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.item.ModItems;

import net.minecraft.entity.projectile.ArrowEntity;

public class BloodNeedleEntity extends ArrowEntity {

    private @Nullable LivingEntity lockedTarget = null;
    public Vec3d hitPos = null;
    private int ticksStuckInGround = 0;

    public BloodNeedleEntity(EntityType<? extends BloodNeedleEntity> type, World world) {
        super(type, world);
    }

    // Blood Needle Entity acts like an arrow, but with a few custom behaviors
    public BloodNeedleEntity(World world, LivingEntity owner) {
        super(ModEntities.BLOOD_NEEDLE, world);
        this.setOwner(owner);
        this.setDamage(3.0);
        this.hasNoGravity();
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public BloodNeedleEntity(World world, double x, double y, double z) {
        super(ModEntities.BLOOD_NEEDLE, world);
        this.setPos(x, y, z);
        this.setDamage(3.0);
        this.hasNoGravity();
        this.pickupType = PickupPermission.DISALLOWED;
    }

    // Override the Arrow entity texture to use the Blood Needle texture
    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.BLOOD_NEEDLE);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) return;

        // If the needle is stuck in the ground for too long, it explodes
        if (this.inGround) {
            ticksStuckInGround++;

            // Play explosion particle effect
            if (ticksStuckInGround >= 60) {
                ((ServerWorld) this.getWorld()).spawnParticles(
                    ParticleTypes.EXPLOSION,
                    this.getX(), this.getY(), this.getZ(),
                    1,
                    0, 0, 0,
                    0.1
                );

                // and sound effect :3
                this.getWorld().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE,
                    SoundCategory.HOSTILE,
                    1.0F,
                    1.0F
                );

                // Delete the needle after the 'splosion
                this.discard();
            }

            return;
        }

        ticksStuckInGround = 0;

        // If the needle is not stuck, we check if it has a locked target
        if (this.getOwner() instanceof LivingEntity shooter && shooter.isAlive()) {
            Vec3d currentVelocity = this.getVelocity();
            double speed = currentVelocity.length();
            if (speed <= 0) return;

            Vec3d lookVec = shooter.getRotationVec(1.0f).normalize();
            Vec3d newVelocity = rotateTowards(currentVelocity.normalize(), lookVec, 0.05).multiply(speed);

            this.setVelocity(newVelocity);
            this.velocityDirty = true;
        }
    }

    // Half Life 2 style projectile tracking around the crosshair type shit
    private Vec3d rotateTowards(Vec3d from, Vec3d to, double maxAngle) {
        double dot = from.dotProduct(to);
        dot = MathHelper.clamp(dot, -1.0, 1.0);
        double angle = Math.acos(dot);

        if (angle < 1e-5) return to;

        double t = Math.min(1.0, maxAngle / angle);

        return from.lerp(to, t).normalize();
    }

    // ON HIT give the target the Supercombine effect for like 3 seconds
	@Override
	protected void onHit(LivingEntity target) {
		super.onHit(target);
        int amp = 0;
        StatusEffectInstance existing = target.getStatusEffect(ModEffects.SUPERCOMBINE);
        if (existing != null) {
        amp = Math.min(existing.getAmplifier() + 1, 6);
        }

        target.addStatusEffect(new StatusEffectInstance(ModEffects.SUPERCOMBINE, 60, amp));
	}
}