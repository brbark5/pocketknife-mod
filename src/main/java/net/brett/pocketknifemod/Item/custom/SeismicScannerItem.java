package net.brett.pocketknifemod.Item.custom;

import net.brett.pocketknifemod.client.ScanTickHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SeismicScannerItem extends Item {
    public SeismicScannerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            ScanTickHandler.startScan(world, player, player.getBlockPos());
        }

        player.getStackInHand(hand).damage(1, player,
                p -> p.sendToolBreakStatus(hand));

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}