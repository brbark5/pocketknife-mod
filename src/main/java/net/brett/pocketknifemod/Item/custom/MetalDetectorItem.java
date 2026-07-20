package net.brett.pocketknifemod.Item.custom;
import net.brett.pocketknifemod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;



public class MetalDetectorItem extends  Item {
    public MetalDetectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient()) {
            BlockPos positionClicked = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            boolean foundBlock = false;

            for(int i = 0; i <= positionClicked.getY() + 64; i++) {
                BlockState state = context.getWorld().getBlockState(positionClicked.down(i));

                if(isValuableBlock(state)) {
                    outputValuableCoordinates(positionClicked.down(i), player, state.getBlock());
                    foundBlock = true;

                    break;
                }
            }

            if(!foundBlock) {
                player.sendMessage(Text.literal("No Valuables Found!"));
            }
        }

        context.getStack().damage(1, context.getPlayer(),
                playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));

        return ActionResult.SUCCESS;
    }

    private void outputValuableCoordinates(BlockPos blockPos, PlayerEntity player, Block block) {
player.sendMessage(Text.literal("Found " + block.asItem().getName().getString() + " at " +
        "(" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + ")"), false);
    }

    private boolean isValuableBlock(BlockState state) {
    return
            //Iron
            state.isOf(Blocks.IRON_ORE) || state.isOf(Blocks.DEEPSLATE_IRON_ORE) ||

                    //Gold
                    state.isOf(Blocks.GOLD_ORE) || state.isOf(Blocks.DEEPSLATE_GOLD_ORE) || state.isOf(Blocks.NETHER_GOLD_ORE) ||

                    //Diamond
                    state.isOf(Blocks.DIAMOND_ORE) || state.isOf(Blocks.DEEPSLATE_DIAMOND_ORE) ||

                    //Copper
                    state.isOf(Blocks.COPPER_ORE) || state.isOf(Blocks.DEEPSLATE_COPPER_ORE) ||

                    //Lapis
                    state.isOf(Blocks.LAPIS_ORE) || state.isOf(Blocks.DEEPSLATE_LAPIS_ORE) ||

                    //Emerald
                    state.isOf(Blocks.EMERALD_ORE) || state.isOf(Blocks.DEEPSLATE_EMERALD_ORE) ||

                    //Coal
                    state.isOf(Blocks.COAL_ORE) || state.isOf(Blocks.DEEPSLATE_COAL_ORE) ||

                    //Steel
                    state.isOf(ModBlocks.STEEL_ORE) || state.isOf(ModBlocks.DEEPSLATE_STEEL_ORE) ||
                    state.isOf(ModBlocks.NETHER_STEEL_ORE) || state.isOf(ModBlocks.END_STONE_STEEL_ORE);
    }
}
