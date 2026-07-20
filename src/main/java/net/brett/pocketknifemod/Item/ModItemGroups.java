package net.brett.pocketknifemod.Item;

import net.brett.pocketknifemod.PocketknifeMod;
import net.brett.pocketknifemod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup KNIFE_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(PocketknifeMod.MOD_ID, "knife"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.knife"))
                    .icon(() -> new ItemStack(ModItems.BASIC_KNIFE)).entries((displayContext, entries) -> {
                        entries.add(ModItems.BASIC_KNIFE);
                        entries.add(ModItems.GOLD_KNIFE);
                        entries.add(ModItems.CRUDE_STEEL_KNIFE);
                        entries.add(ModItems.REFINED_STEEL_KNIFE);
                        entries.add(ModItems.METAL_DETECTOR);


                        entries.add(ModItems.RAW_STEEL);
                        entries.add(ModItems.REFINED_STEEL);


                        entries.add(ModBlocks.REFINED_STEEL_BLOCK);
                        entries.add(ModBlocks.STEEL_ORE);
                        entries.add(ModBlocks.DEEPSLATE_STEEL_ORE);
                        entries.add(ModBlocks.NETHER_STEEL_ORE);
                        entries.add(ModBlocks.END_STONE_STEEL_ORE);



                    }).build());

    public static void registerItemGroups() {
        PocketknifeMod.LOGGER.info("Registering Item Groups for " + PocketknifeMod.MOD_ID);
    }
}
