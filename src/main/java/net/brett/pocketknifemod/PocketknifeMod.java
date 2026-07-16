package net.brett.pocketknifemod;

import net.brett.pocketknifemod.Item.ModItemGroups;
import net.brett.pocketknifemod.Item.ModItems;
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
		

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
