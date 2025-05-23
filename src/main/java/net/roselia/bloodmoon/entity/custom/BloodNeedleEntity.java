package net.roselia.bloodmoon.entity.custom;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.item.ModItems;


import net.minecraft.entity.projectile.ArrowEntity;

public class BloodNeedleEntity extends ArrowEntity {

    private @Nullable LivingEntity lockedTarget = null;
    public Vec3d hitPos = null;

    public BloodNeedleEntity(EntityType<? extends BloodNeedleEntity> type, World world) {
        super(type, world);
    }

    public BloodNeedleEntity(World world, LivingEntity owner) {
        super(ModEntities.BLOOD_NEEDLE, world);
        this.setOwner(owner);
        this.setDamage(1.0);
        this.hasNoGravity();
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public BloodNeedleEntity(World world, double x, double y, double z) {
        super(ModEntities.BLOOD_NEEDLE, world);
        this.setPos(x, y, z);
        this.setDamage(1.0);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.BLOOD_NEEDLE);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient || this.inGround) return;

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

    private Vec3d rotateTowards(Vec3d from, Vec3d to, double maxAngle) {
        double dot = from.dotProduct(to);
        dot = MathHelper.clamp(dot, -1.0, 1.0);
        double angle = Math.acos(dot);

        if (angle < 1e-5) return to;

        double t = Math.min(1.0, maxAngle / angle);

        return from.lerp(to, t).normalize();
    }
}