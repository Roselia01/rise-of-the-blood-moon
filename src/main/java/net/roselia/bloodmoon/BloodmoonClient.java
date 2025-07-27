package net.roselia.bloodmoon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.entity.ModEntities;
import net.roselia.bloodmoon.item.ModItems;

public class BloodmoonClient implements ClientModInitializer {
    private static final Identifier NEEDLE_TEX = new Identifier("bloodmoon", "textures/entity/projectiles/blood_needle.png");
    public static float fearZoomProgress = 0.0F;

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(
        ModEntities.BLOOD_NEEDLE,
        ctx -> new ArrowEntityRenderer(ctx) {
            @Override
            public Identifier getTexture(ArrowEntity arrow) {
                return NEEDLE_TEX;
            }}
        );

        ModelPredicateProviderRegistry.register(ModItems.NEEDLER,
            new Identifier("pulling"), (stack, world, entity, seed) -> {
                return (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) ? 1.0F : 0.0F;
            }
        );

        ModelPredicateProviderRegistry.register(ModItems.NEEDLER,
            new Identifier("pull"), (stack, world, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                }
                float useTime = (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
                return useTime;
            }
        );

        ModelPredicateProviderRegistry.register(ModItems.NETHERITE_BOW,
            new Identifier("pulling"), (stack, world, entity, seed) -> {
                return (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) ? 1.0F : 0.0F;
            }
        );

        ModelPredicateProviderRegistry.register(ModItems.NETHERITE_BOW,
            new Identifier("pull"), (stack, world, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                }
                float useTime = (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
                return useTime;
            }
        );
    }
}