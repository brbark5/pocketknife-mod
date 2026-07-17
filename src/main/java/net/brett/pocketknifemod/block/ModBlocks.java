package net.brett.pocketknifemod.block;

import net.brett.pocketknifemod.PocketknifeMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block STEEL_BLOCK = registerBlock("steel_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
    public static final Block RAW_STEEL_BLOCK = registerBlock("raw_steel_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
    

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(PocketknifeMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(PocketknifeMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }


    public static void registerModBlocks() {
        PocketknifeMod.LOGGER.info("Registering ModBlocks for " + PocketknifeMod.MOD_ID);
    }
}
