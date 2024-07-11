package net.roselia.bloodmoon.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.Bloodmoon;
import net.roselia.bloodmoon.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup BLOODMOON_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Bloodmoon.MOD_ID, "sanguine_prism"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.bloodmoon"))
                    .icon(() -> new ItemStack(ModItems.SANGUINE_PRISM)).entries((displayContext, entries) -> {
                        entries.add(ModItems.SANGUINE_PRISM);
                        entries.add(ModItems.CRIMTANE_CHUNK);
                        entries.add(ModItems.CRIMTANE_INGOT);
                        entries.add(ModItems.FLESH_CHUNK);
                        entries.add(ModItems.COOKED_FLESH_CHUNK);
                        entries.add(ModItems.INTESTINES);
                        entries.add(ModItems.COOKED_INTESTINES);
                        entries.add(ModItems.BLOODY_NEEDLE);
                        entries.add(ModItems.BLOODY_LENS);

                        entries.add(ModBlocks.CRIMTANE_CLUMP);
                        entries.add(ModBlocks.CRIMTANE_BLOCK);

            }).build());

    public static void registerItemGroups() {
        Bloodmoon.LOGGER.info("Registering Item Groups for " + Bloodmoon.MOD_ID);
    }
}
