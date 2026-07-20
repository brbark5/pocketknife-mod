package net.brett.pocketknifemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.brett.pocketknifemod.client.ScanTickHandler;

public class PocketknifeModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// This code runs only on the client side.
		ClientTickEvents.END_CLIENT_TICK.register(Client -> ScanTickHandler.tick());
	}

}
