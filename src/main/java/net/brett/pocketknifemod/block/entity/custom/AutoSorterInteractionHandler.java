package net.brett.pocketknifemod.block.entity.custom;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;


public class AutoSorterInteractionHandler {

    public static void register() {
        UseBlockCallback.EVENT.register(AutoSorterInteractionHandler::onUseBlock);
    }

    private static ActionResult onUseBlock(
            PlayerEntity player, World world, Hand hand,
            BlockHitResult hitResult) {
        if (!player.isSneaking()) return ActionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof AutoSorterBlockEntity sorter)) return ActionResult.PASS;

        if (world.isClient) return ActionResult.SUCCESS;

        Direction side = hitResult.getSide();
        ItemStack heldStack = player.getStackInHand(hand);

        if (heldStack.isEmpty()) {
            sorter.clearFilter(side);
            player.sendMessage(Text.literal("Filter cleared on " + side.getName() + " side"), true);
        } else {
            sorter.setFilter(side, heldStack);
            player.sendMessage(Text.literal("Filter set to " + heldStack.getItem().getName().getString()
                    + " on " + side.getName() + " side"), true);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_HAT.value(), SoundCategory.BLOCKS, 0.5f, 1.2f);
        return ActionResult.SUCCESS;
    }
}