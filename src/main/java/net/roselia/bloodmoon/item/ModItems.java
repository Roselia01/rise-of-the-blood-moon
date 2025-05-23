package net.roselia.bloodmoon.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.roselia.bloodmoon.Bloodmoon;
import net.roselia.bloodmoon.item.custom.NeedlerItem;
import net.roselia.bloodmoon.item.custom.NetheriteBowItem;

public class ModItems {
    public static final Item FLESH_CHUNK = registerItem("flesh_chunk", new Item(new FabricItemSettings().food(new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 0), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1200, 1), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1800, 0), 0.5f)
            .build())));
    public static final Item COOKED_FLESH_CHUNK = registerItem("cooked_flesh_chunk", new Item(new FabricItemSettings().food(new FoodComponent.Builder()
            .hunger(6)
            .saturationModifier(0.3f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 900, 0), 0.25f)
            .build())));
    public static final Item SANGUINE_PRISM = registerItem("sanguine_prism", new Item(new FabricItemSettings().rarity(Rarity.EPIC)
            .maxCount(1)));
    public static final Item BLOODY_LENS = registerItem("bloody_lens", new Item(new FabricItemSettings()));
    public static final Item CRIMTANE_INGOT = registerItem("crimtane_ingot", new Item(new FabricItemSettings()));
    public static final Item CRIMTANE_CHUNK = registerItem("crimtane_chunk", new Item(new FabricItemSettings()));
    public static final Item INTESTINES = registerItem("intestines", new Item(new FabricItemSettings().food(new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 900, 0), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1200, 2), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 1), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 2400, 1), 1.0f)
            .snack()
            .build())));
    public static final Item COOKED_INTESTINES = registerItem("cooked_intestines", new Item(new FabricItemSettings().food(new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 0), 0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 300, 0), 0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 0), 0.5f)
            .snack()
            .build())));
    public static final Item BLOOD_NEEDLE = registerItem("blood_needle", new Item(new FabricItemSettings()));
    public static final Item SANGUINE_SUMMONER = registerItem("sanguine_summoner", new Item(new FabricItemSettings().rarity(Rarity.EPIC)
            .maxCount(1)));
    public static final Item NEEDLER = registerItem("needler", new NeedlerItem(new FabricItemSettings().maxCount(1)
            .maxDamage(90)));

    public static final Item NETHERITE_BOW = registerItem("netherite_bow", new NetheriteBowItem(new FabricItemSettings().maxCount(1)
            .maxDamage(768)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Bloodmoon.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Bloodmoon.LOGGER.info("Registering Mod Items for " + Bloodmoon.MOD_ID);
    }
}
