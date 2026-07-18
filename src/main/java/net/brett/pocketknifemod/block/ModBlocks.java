package net.brett.pocketknifemod.block;

import net.brett.pocketknifemod.PocketknifeMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block REFINED_STEEL_BLOCK = registerBlock("refined_steel_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block STEEL_ORE = registerBlock("steel_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.STONE).strength(2.0F), UniformIntProvider.create(2, 5)));
    public static final Block DEEPSLATE_STEEL_ORE = registerBlock("deepslate_steel_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(4.0F), UniformIntProvider.create(3, 5)));
    public static final Block NETHER_STEEL_ORE = registerBlock("nether_steel_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.NETHERRACK).strength(1.5F), UniformIntProvider.create(3, 7)));
    public static final Block END_STONE_STEEL_ORE = registerBlock("end_stone_steel_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.END_STONE).strength(4.0F), UniformIntProvider.create(4, 7)));



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
