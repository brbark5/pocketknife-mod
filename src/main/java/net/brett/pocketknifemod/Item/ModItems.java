
package net.brett.pocketknifemod.Item;

import net.brett.pocketknifemod.Item.custom.MetalDetectorItem;
import net.brett.pocketknifemod.PocketknifeMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BASIC_KNIFE = registerItem("basic_knife", new Item(new FabricItemSettings()));
    public static final Item GOLD_KNIFE = registerItem("gold_knife", new Item(new FabricItemSettings()));
    public static final Item CRUDE_STEEL_KNIFE = registerItem("crude_steel_knife", new Item(new FabricItemSettings()));
    public static final Item REFINED_STEEL_KNIFE = registerItem("refined_steel_knife", new Item(new FabricItemSettings()));
    public static final Item METAL_DETECTOR = registerItem("metal_detector",
            new MetalDetectorItem(new FabricItemSettings().maxDamage(64)));


    public static final Item RAW_STEEL = registerItem("raw_steel", new Item(new FabricItemSettings()));
    public static final Item REFINED_STEEL = registerItem("refined_steel", new Item(new FabricItemSettings()));


    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(BASIC_KNIFE);
        entries.add(GOLD_KNIFE);
        entries.add(CRUDE_STEEL_KNIFE);
        entries.add(REFINED_STEEL_KNIFE);
        entries.add(METAL_DETECTOR);


        entries.add(RAW_STEEL);
        entries.add(REFINED_STEEL);

    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(PocketknifeMod.MOD_ID, name), item);
    }
    public static void registerModItems() {
        PocketknifeMod.LOGGER.info("Registering Mod Items for" + PocketknifeMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
