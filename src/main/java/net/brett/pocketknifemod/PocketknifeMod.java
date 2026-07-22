package net.brett.pocketknifemod;

import net.brett.pocketknifemod.block.ModBlocks;
import net.brett.pocketknifemod.Item.ModItemGroups;
import net.brett.pocketknifemod.Item.ModItems;
import net.brett.pocketknifemod.block.entity.ModBlockEntities;
import net.brett.pocketknifemod.block.entity.custom.AutoSorterInteractionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class PocketknifeMod implements ModInitializer {
	public static final String MOD_ID = "pocketknifemod";

	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		AutoSorterInteractionHandler.register();

	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
