package net.brett.pocketknifemod.block.entity.custom;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class AutoSorterBlockEntityRenderer implements BlockEntityRenderer<AutoSorterBlockEntity> {
    private final ItemRenderer itemRenderer;

    public AutoSorterBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(AutoSorterBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        System.out.println("Rendering AutoSorter at " + entity.getPos());
        for (Direction dir : Direction.values()) {
            ItemStack filter = entity.getFilter(dir);
            System.out.println(dir + " filter: " + filter);
            if (filter.isEmpty()) continue;

            matrices.push();

            // move to the center of the block, then out to the face
            matrices.translate(0.5, 0.5, 0.5);
            matrices.translate(
                    dir.getOffsetX() * 0.51,
                    dir.getOffsetY() * 0.51,
                    dir.getOffsetZ() * 0.51
            );

            // rotate the icon to face outward from whichever side it's on
            switch (dir) {
                case UP -> matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(-90));
                case DOWN -> matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(90));
                case NORTH -> matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(180));
                case SOUTH -> {} // default orientation already faces south
                case EAST -> matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(-90));
                case WEST -> matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(90));
            }

            matrices.scale(0.35f, 0.35f, 0.35f);

            itemRenderer.renderItem(filter, net.minecraft.client.render.model.json.ModelTransformationMode.FIXED,
                    light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);

            matrices.pop();
        }
    }
}