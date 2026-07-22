package net.brett.pocketknifemod.block.entity;


import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.brett.pocketknifemod.PocketknifeMod;
import net.brett.pocketknifemod.block.ModBlocks;
import net.brett.pocketknifemod.block.entity.custom.AutoSorterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ModBlockEntities {
    public static final BlockEntityType<AutoSorterBlockEntity> AUTO_SORTER_BLOCK =
            register("auto_sorter_block", FabricBlockEntityTypeBuilder.create(
                    AutoSorterBlockEntity::new,
                    ModBlocks.AUTO_SORTER_BLOCK
            ).build());

    private static <T extends BlockEntityType<?>> T register(String name, T type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(PocketknifeMod.MOD_ID, name), type);
    }

    public static void registerBlockEntities() {
        PocketknifeMod.LOGGER.info("Registering Block Entities for " + PocketknifeMod.MOD_ID);
    }

    /**
     * Type-safe helper for BlockEntityTicker. Confirms the ticked block entity type
     * matches the one this block actually uses before handing back the ticker,
     * avoiding an unchecked cast at every call site.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity, E extends BlockEntity> BlockEntityTicker<T> validateTicker(
            BlockEntityType<T> givenType,
            BlockEntityType<E> targetType,
            BlockEntityTicker<E> ticker) {
        return givenType == targetType ? (BlockEntityTicker<T>) ticker : null;
    }
}