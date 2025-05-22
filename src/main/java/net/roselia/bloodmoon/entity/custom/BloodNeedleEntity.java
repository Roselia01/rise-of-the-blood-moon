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

    private @Nullable LivingEntity lockedTarget = null;
    private final double lockOnFOV = 0.9;
    private double lastDistance = Double.MAX_VALUE;
    private boolean hasMissed = false;

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
        if (this.getWorld().isClient || this.inGround) return;

        if (lockedTarget == null) {
            lockedTarget = findLockOnTarget();
            return;
        }

        if (!lockedTarget.isAlive()) {
            lockedTarget = null;
            return;
        }

        Vec3d targetPos = lockedTarget.getPos().add(0, lockedTarget.getStandingEyeHeight() * 0.6, 0);
        double currentDistance = this.getPos().squaredDistanceTo(targetPos);

        // Check if we've passed the target
        if (!hasMissed && currentDistance > lastDistance + 1.0) {
            hasMissed = true;
        }

        lastDistance = currentDistance;

        Vec3d toTarget = targetPos.subtract(this.getPos());

        // Obstacle avoidance still active
        Vec3d avoidVec = getAvoidanceVector();
        toTarget = toTarget.add(avoidVec);

        // Homing curve behavior (stronger if it missed)
        double steerStrength = hasMissed ? 0.5 : 0.3;

        Vec3d velocity = this.getVelocity();
        Vec3d newVelocity = velocity
            .add(toTarget.normalize().multiply(steerStrength))
            .normalize()
            .multiply(velocity.length());

        this.setVelocity(newVelocity);
        this.velocityDirty = true;
    }

    @Nullable
    private LivingEntity findLockOnTarget() {
        Vec3d forward = this.getVelocity().normalize();
        double radius = 16.0;

        return this.getWorld().getEntitiesByClass(
            LivingEntity.class,
            this.getBoundingBox().expand(radius),
            entity -> {
                if (entity == this.getOwner()) return false;
                if (!entity.isAlive() || entity.isSpectator()) return false;

                Vec3d toTarget = entity.getPos().subtract(this.getPos()).normalize();
                if (forward.dotProduct(toTarget) < lockOnFOV) return false;

                Vec3d eyePos = entity.getPos().add(0, entity.getStandingEyeHeight() * 0.6, 0);
                BlockHitResult hit = this.getWorld().raycast(new RaycastContext(
                    this.getPos(), eyePos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    this
                ));
                return hit.getType() == HitResult.Type.MISS || hit.getPos().isInRange(eyePos, 1.0);
            }
        ).stream().min(Comparator.comparingInt(this::getTargetPriority)).orElse(null);
    }

    private Vec3d getAvoidanceVector() {
        List<Entity> obstacles = this.getWorld().getOtherEntities(
            this,
            this.getBoundingBox().expand(1.5), // close obstacles
            entity -> entity != lockedTarget && entity instanceof LivingEntity && entity.isAlive()
        );

        Vec3d avoidance = Vec3d.ZERO;
        for (Entity e : obstacles) {
            Vec3d away = this.getPos().subtract(e.getPos()).normalize();
            avoidance = avoidance.add(away);
        }

        return avoidance.normalize().multiply(0.5);
    }

    private int getTargetPriority(Entity entity) {
    if (entity instanceof TameableEntity tame && tame.isTamed()) return 4;
    if (entity instanceof ArmorStandEntity || entity.hasVehicle()) return 4;

    if (entity.getType().toString().equals("minecraft:zombified_piglin")) return 3;
    if (entity.getType().toString().equals("minecraft:iron_golem")) return 3;

    if (entity instanceof PassiveEntity) return 3;
    if (entity instanceof PlayerEntity) return 2;
    if (entity instanceof HostileEntity) return 1;

    return 5;
    }
}