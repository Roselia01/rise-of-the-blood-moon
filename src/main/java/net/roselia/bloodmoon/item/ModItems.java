package net.roselia.bloodmoon.item;

import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.roselia.bloodmoon.Bloodmoon;
import net.roselia.bloodmoon.item.custom.NeedlerItem;
import net.roselia.bloodmoon.item.custom.NetheriteBowItem;
import net.roselia.bloodmoon.sound.ModSounds;

public class ModItems {

        // FOOD ITEMS (MAYBE REMOVE LATER)
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

        // MISC ITEMS
        public static final Item BLOODY_LENS = registerItem("bloody_lens", new Item(new FabricItemSettings())); // Might Remove Later

        public static final Item BLOOD_NEEDLE = registerItem("blood_needle", new Item(new FabricItemSettings()));

        public static final Item CRIMTANE_CHUNK = registerItem("crimtane_chunk", new Item(new FabricItemSettings()));

        public static final Item CRIMTANE_INGOT = registerItem("crimtane_ingot", new Item(new FabricItemSettings()));

        public static final Item SANGUINE_PRISM = registerItem("sanguine_prism", new Item(new FabricItemSettings().rarity(Rarity.EPIC)
                .maxCount(1)));

        public static final Item MUSIC_DISC_HUMAN_FLESH = Registry.register(
                Registries.ITEM,
                new Identifier("bloodmoon", "music_disc_human_flesh"),
                new MusicDiscItem(
                        15,
                        ModSounds.HUMAN_FLESH_RECORD,
                        new FabricItemSettings().maxCount(1).rarity(Rarity.RARE),
                        6280
                )
        );

        public static final Item SANGUINE_SUMMONER = registerItem("sanguine_summoner", new Item(new FabricItemSettings() // Might Remove Later
                .rarity(Rarity.EPIC)
                .maxCount(1)));
        
        // TOOLS & WEAPONS
        public static final Item NEEDLER = registerItem("needler", new NeedlerItem(new FabricItemSettings().maxCount(1)
                .maxDamage(192)
                .fireproof()));

        public static final Item NETHERITE_BOW = registerItem("netherite_bow", new NetheriteBowItem(new FabricItemSettings().maxCount(1)
                .maxDamage(768)
                .fireproof()));

        // SMITHING TEMPLATES
        private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
        private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
        private static final Identifier EMPTY_ARMOR_SLOT_HELMET_TEXTURE = new Identifier("item/empty_armor_slot_helmet");
	private static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE = new Identifier("item/empty_armor_slot_chestplate");
	private static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE = new Identifier("item/empty_armor_slot_leggings");
	private static final Identifier EMPTY_ARMOR_SLOT_BOOTS_TEXTURE = new Identifier("item/empty_armor_slot_boots");
	private static final Identifier EMPTY_SLOT_HOE_TEXTURE = new Identifier("item/empty_slot_hoe");
	private static final Identifier EMPTY_SLOT_AXE_TEXTURE = new Identifier("item/empty_slot_axe");
	private static final Identifier EMPTY_SLOT_SWORD_TEXTURE = new Identifier("item/empty_slot_sword");
	private static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = new Identifier("item/empty_slot_shovel");
	private static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = new Identifier("item/empty_slot_pickaxe");
	private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");

        private static List<Identifier> getCrimtaneUpgradeEmptyBaseSlotTextures() {
		return List.of(
			EMPTY_ARMOR_SLOT_HELMET_TEXTURE,
			EMPTY_SLOT_SWORD_TEXTURE,
			EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE,
			EMPTY_SLOT_PICKAXE_TEXTURE,
			EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE,
			EMPTY_SLOT_AXE_TEXTURE,
			EMPTY_ARMOR_SLOT_BOOTS_TEXTURE,
			EMPTY_SLOT_HOE_TEXTURE,
			EMPTY_SLOT_SHOVEL_TEXTURE
		);
	}

        private static List<Identifier> getCrimtaneUpgradeEmptyAdditionsSlotTextures() {
		return List.of(EMPTY_SLOT_INGOT_TEXTURE);
	}

        private static final Text CRIMTANE_UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("upgrade", new Identifier("crimtane_upgrade")))
                .formatted(TITLE_FORMATTING);
        
        private static final Text CRIMTANE_UPGRADE_APPLIES_TO_TEXT = Text.translatable(
                Util.createTranslationKey("item", new Identifier("smithing_template.crimtane_upgrade.applies_to"))
                )
                .formatted(DESCRIPTION_FORMATTING);
        private static final Text CRIMTANE_UPGRADE_INGREDIENTS_TEXT = Text.translatable(
                Util.createTranslationKey("item", new Identifier("smithing_template.crimtane_upgrade.ingredients"))
                )
                .formatted(DESCRIPTION_FORMATTING);
        private static final Text CRIMTANE_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
                Util.createTranslationKey("item", new Identifier("smithing_template.crimtane_upgrade.base_slot_description"))
                );
        private static final Text CRIMTANE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
                Util.createTranslationKey("item", new Identifier("smithing_template.crimtane_upgrade.additions_slot_description"))
                );

        public static final Item CRIMTANE_UPGRADE_SMITHING_TEMPLATE = registerItem("crimtane_upgrade_smithing_template",
                new SmithingTemplateItem(
                        CRIMTANE_UPGRADE_APPLIES_TO_TEXT,
                        CRIMTANE_UPGRADE_INGREDIENTS_TEXT,
                        CRIMTANE_UPGRADE_TEXT,
                        CRIMTANE_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT,
                        CRIMTANE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
                        getCrimtaneUpgradeEmptyBaseSlotTextures(),
                        getCrimtaneUpgradeEmptyAdditionsSlotTextures()
                )
        );

        // REGISTRATION
        private static Item registerItem(String name, Item item) {
                return Registry.register(Registries.ITEM, new Identifier(Bloodmoon.MOD_ID, name), item);
        }

        public static void registerModItems() {
                Bloodmoon.LOGGER.info("Registering Mod Items for " + Bloodmoon.MOD_ID);
        }
}
