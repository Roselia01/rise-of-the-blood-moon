package net.roselia.bloodmoon.item.custom;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.roselia.bloodmoon.entity.custom.BloodNeedleEntity;
import net.roselia.bloodmoon.item.ModItems;
import java.util.function.Predicate;
import net.roselia.bloodmoon.api.BowItemType;

import net.minecraft.entity.projectile.PersistentProjectileEntity;

public class NeedlerItem extends BowItem implements BowItemType {
    public NeedlerItem(Settings settings) {
        super(settings);
    }

    @Override
	public float getDrawSpeed() {
		return 10.0f;
	}

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        boolean creative = player.getAbilities().creativeMode;
        boolean hasInfinity = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
		boolean isCreative = player.getAbilities().creativeMode;

		ItemStack ammo = findAmmo(player);
		boolean hasAmmo = !ammo.isEmpty();

		boolean infiniteAmmo = hasInfinity && hasAmmo;

		if (!hasAmmo && !isCreative && !infiniteAmmo) {
			return;
		}

        if (!ammo.isEmpty() || creative) {
            if (ammo.isEmpty()) {
                ammo = new ItemStack(ModItems.BLOOD_NEEDLE);
            }

            int useTicks = getMaxUseTime(stack) - remainingUseTicks;
            float pull = getPullProgress(useTicks);
            if (pull < 0.1f) return;

            if (!world.isClient) {
                BloodNeedleEntity needle = new BloodNeedleEntity(world, player);
                needle.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, pull * 3.0F, 1.0F);
				needle.setPosition(player.getX(), player.getEyeY() - 0.1, player.getZ());

                if (pull == 1.0f) {
                    needle.setCritical(true);
                }

                int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                if (powerLevel > 0) {
                    needle.setDamage(needle.getDamage() + (double)powerLevel * 0.5 + 0.5);
                }

                int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                if (punchLevel > 0) {
                    needle.setPunch(punchLevel);
                }

                if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                    needle.setOnFireFor(100);
                }

                stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));

                needle.pickupType = creative
                    ? PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY
                    : PersistentProjectileEntity.PickupPermission.DISALLOWED;

                world.spawnEntity(needle);
            }

            world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pull * 0.5F
            );

			if (!isCreative && !infiniteAmmo) {
				ammo.decrement(1);
				if (ammo.isEmpty()) {
					player.getInventory().removeOne(ammo);
				}
			}

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean hasAmmo = !findAmmo(user).isEmpty();
        if (!user.getAbilities().creativeMode && !hasAmmo) {
            return TypedActionResult.fail(stack);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    private ItemStack findAmmo(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == ModItems.BLOOD_NEEDLE) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.getItem() == ModItems.BLOOD_NEEDLE;
    }

    public static float getPullProgress(int useTicks) {
		float f = (float)useTicks / 10.0f;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}