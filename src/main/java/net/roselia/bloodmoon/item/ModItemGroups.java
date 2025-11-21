package net.roselia.bloodmoon.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.Bloodmoon;
import net.roselia.bloodmoon.block.ModBlocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class ModItemGroups {
    public static final ItemGroup BLOODMOON_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Bloodmoon.MOD_ID, "sanguine_prism"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.bloodmoon"))
                    .icon(() -> new ItemStack(ModItems.SANGUINE_PRISM)).entries((displayContext, entries) -> {
                        entries.add(ModItems.SANGUINE_PRISM);
                        entries.add(ModItems.INTESTINES);
                        entries.add(ModItems.SAUSAGE);
                        entries.add(ModItems.CRIMTANE_CHUNK);
                        entries.add(ModItems.CRIMTANE_INGOT);
                        entries.add(ModItems.NEEDLER);
                        entries.add(ModItems.BLOOD_NEEDLE);
                        entries.add(ModItems.CRIMTANE_UPGRADE_SMITHING_TEMPLATE);
                        entries.add(ModItems.MUSIC_DISC_HUMAN_FLESH);

                        entries.add(ModBlocks.CRIMTANE_CLUMP);
                        entries.add(ModBlocks.CRIMTANE_BLOCK);
            }).build());
            
    public static void registerItemGroups() {
        Bloodmoon.LOGGER.info("Registering Item Groups for " + Bloodmoon.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.BOW, ModItems.NETHERITE_BOW);
        });
    }
}
