package net.roselia.bloodmoon.entity.custom;

import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.item.ModItems;

import net.minecraft.entity.projectile.ArrowEntity;

public class BloodNeedleEntity extends ArrowEntity {

    private boolean isHoming = false;

    public BloodNeedleEntity(EntityType<? extends BloodNeedleEntity> type, World world) {
        super(type, world);
    }

    public BloodNeedleEntity(World world, LivingEntity owner) {
        super(ModEntities.BLOOD_NEEDLE, world);
        this.setOwner(owner);
        this.setDamage(1.0);
        this.pickupType = PickupPermission.DISALLOWED;
        this.setNoGravity(false);
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
        if (!isHoming) {
            isHoming = true;
            this.setNoGravity(true);
        }

        Vec3d toTarget = target.getPos().add(0, target.getStandingEyeHeight() / 2, 0)
            .subtract(this.getPos()).normalize();

        Vec3d currentVelocity = this.getVelocity();
        Vec3d newVelocity = currentVelocity.add(toTarget.multiply(0.2)).normalize().multiply(currentVelocity.length());

        this.setVelocity(newVelocity);
        this.velocityDirty = true;
    }}

    @Nullable
    private LivingEntity findNearestTarget() {
        double radius = 16.0;
        Vec3d forward = this.getVelocity().normalize();



        List<LivingEntity> candidates = this.getWorld().getEntitiesByClass(
            LivingEntity.class,
            this.getBoundingBox().expand(radius),
            entity -> {
                if (entity == this.getOwner()) return false;
                if (!entity.isAlive() || entity.isSpectator()) return false;
                if (entity instanceof TameableEntity tame && tame.isTamed()) return false;
                if (entity instanceof ArmorStandEntity || entity.hasVehicle()) return false;

                Vec3d toTarget = entity.getPos().subtract(this.getPos()).normalize();
                if (forward.dotProduct(toTarget) < 0.5) return false;

                Vec3d eyePos = entity.getPos().add(0, entity.getStandingEyeHeight() / 2.0, 0);
                BlockHitResult hit = this.getWorld().raycast(new RaycastContext(
                    this.getPos(),
                    eyePos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    this
                ));
                return hit.getType() == HitResult.Type.MISS || hit.getPos().isInRange(eyePos, 1.0);
            }
        );

        if (candidates.isEmpty()) return null;

        return candidates.stream()
            .min(Comparator.comparingDouble(entity -> {
                int priority = getTargetPriority(entity);
                double distance = this.squaredDistanceTo(entity);
                return (priority * 1000.0) + distance;
            }))
            .orElse(null);
    }


    private int getTargetPriority(Entity entity) {
    if (entity instanceof TameableEntity tame && tame.isTamed()) return 0;
    if (entity instanceof ArmorStandEntity || entity.hasVehicle()) return 0;

    if (entity instanceof PassiveEntity) return 2;
    if (entity instanceof HostileEntity) return 4;
    if (entity instanceof PlayerEntity) return 3;

    return 1;
    }

}