package net.roselia.bloodmoon.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.roselia.bloodmoon.Bloodmoon;

public class ModItems {
    public static final Item FLESH_CHUNK = registerItem("flesh_chunk", new Item(new FabricItemSettings()));
    public static final Item COOKED_FLESH_CHUNK = registerItem("cooked_flesh_chunk", new Item(new FabricItemSettings()));
    public static final Item SANGUINE_PRISM = registerItem("sanguine_prism", new Item(new FabricItemSettings()));
    public static final Item BLOODY_LENS = registerItem("bloody_lens", new Item(new FabricItemSettings()));
    public static final Item CRIMTANE_INGOT = registerItem("crimtane_ingot", new Item(new FabricItemSettings()));
    public static final Item CRIMTANE_CHUNK = registerItem("crimtane_chunk", new Item(new FabricItemSettings()));
    public static final Item INTESTINES = registerItem("intestines", new Item(new FabricItemSettings()));
    public static final Item COOKED_INTESTINES = registerItem("cooked_intestines", new Item(new FabricItemSettings()));
    public static final Item BLOODY_NEEDLE = registerItem("bloody_needle", new Item(new FabricItemSettings()));
    public static final Item SANGUINE_SUMMONER = registerItem("sanguine_summoner", new Item(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Bloodmoon.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Bloodmoon.LOGGER.info("Registering Mod Items for " + Bloodmoon.MOD_ID);
    }
}
