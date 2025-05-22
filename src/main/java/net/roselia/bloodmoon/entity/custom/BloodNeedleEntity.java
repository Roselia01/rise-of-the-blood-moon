package net.roselia.bloodmoon.entity.custom;

import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.item.ModItems;

import net.minecraft.entity.projectile.ArrowEntity;

public class BloodNeedleEntity extends ArrowEntity {

    public BloodNeedleEntity(EntityType<? extends BloodNeedleEntity> type, World world) {
        super(type, world);
    }

    public BloodNeedleEntity(World world, LivingEntity owner) {
        super(ModEntities.BLOOD_NEEDLE, world);
        this.setOwner(owner);
        this.setDamage(1.0);
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

    if (this.getWorld().isClient) return;
    if (this.inGround) return;

    LivingEntity target = findNearestTarget();
    if (target != null) {
        Vec3d toTarget = target.getPos().add(0, target.getStandingEyeHeight() / 2, 0)
            .subtract(this.getPos()).normalize();

        Vec3d currentVelocity = this.getVelocity();
        Vec3d newVelocity = currentVelocity.add(toTarget.multiply(0.3)).normalize().multiply(currentVelocity.length());

        this.setVelocity(newVelocity);
        this.velocityDirty = true;
    }}

    @Nullable
    private LivingEntity findNearestTarget() {
    double radius = 16.0;
    List<LivingEntity> targets = this.getWorld().getEntitiesByClass(
        LivingEntity.class,
        this.getBoundingBox().expand(radius),
        entity -> entity != this.getOwner() && entity.isAlive() && !entity.isSpectator()
    );

    targets.sort(Comparator.comparingDouble(e -> e.squaredDistanceTo(this)));
    return targets.isEmpty() ? null : targets.get(0);
    }
}