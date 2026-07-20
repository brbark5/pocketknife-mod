package net.brett.pocketknifemod.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ScanTickHandler {
    private static final List<ScanInstance> activeScans = new ArrayList<>();

    public static void startScan(World world, PlayerEntity player, BlockPos center) {
        activeScans.add(new ScanInstance(world, player, center, 24));
    }

    public static void tick() {
        activeScans.removeIf(scan -> !scan.tick());
    }
}