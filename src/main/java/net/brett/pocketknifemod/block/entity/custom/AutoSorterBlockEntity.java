package net.brett.pocketknifemod.block.entity.custom;


import net.brett.pocketknifemod.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AutoSorterBlockEntity extends BlockEntity implements SidedInventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> filters = DefaultedList.ofSize(6, ItemStack.EMPTY); // indexed by Direction.ordinal()
    private int tickCounter = 0;

    public AutoSorterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AUTO_SORTER_BLOCK, pos, state);
    }

    // --- Filter management ---

    public void setFilter(Direction side, ItemStack stack) {
        filters.set(side.ordinal(), stack.copy());
        filters.get(side.ordinal()).setCount(1);
        markDirty();
    }

    public void clearFilter(Direction side) {
        filters.set(side.ordinal(), ItemStack.EMPTY);
        markDirty();
    }

    public ItemStack getFilter(Direction side) {
        return filters.get(side.ordinal());
    }

    private boolean matchesFilter(Direction side, ItemStack stack) {
        ItemStack filter = filters.get(side.ordinal());
        return !filter.isEmpty() && ItemStack.areItemsEqual(filter, stack);
    }

    // --- Tick logic ---

    public static void tick(World world, BlockPos pos, BlockState state, AutoSorterBlockEntity be) {
        if (world.isClient) return;

        be.tickCounter++;
        if (be.tickCounter < 10) return;
        be.tickCounter = 0;

        for (int slot = 0; slot < be.inventory.size(); slot++) {
            ItemStack stack = be.inventory.get(slot);
            if (stack.isEmpty()) continue;

            if (be.tryFilteredPush(world, pos, stack)) continue;
            be.tryFallbackPush(world, pos, stack);
        }
    }

    private boolean tryFilteredPush(World world, BlockPos pos, ItemStack stack) {
        for (Direction dir : Direction.values()) {
            if (!matchesFilter(dir, stack)) continue;

            Inventory neighborInv = HopperBlockEntity.getInventoryAt(world, pos.offset(dir));
            if (neighborInv == null) continue;

            ItemStack toMove = stack.copy();
            toMove.setCount(1);

            ItemStack remainder = HopperBlockEntity.transfer(this, neighborInv, toMove, dir.getOpposite());
            if (remainder.isEmpty()) {
                stack.decrement(1);
                markDirty();
                return true;
            }
        }
        return false;
    }

    private void tryFallbackPush(World world, BlockPos pos, ItemStack stack) {
        for (Direction dir : Direction.values()) {
            if (!filters.get(dir.ordinal()).isEmpty()) continue; // skip faces that have a (non-matching) filter

            Inventory neighborInv = HopperBlockEntity.getInventoryAt(world, pos.offset(dir));
            if (neighborInv == null) continue;

            if (containsMatchingItem(neighborInv, stack)) {
                ItemStack toMove = stack.copy();
                toMove.setCount(1);

                ItemStack remainder = HopperBlockEntity.transfer(this, neighborInv, toMove, dir.getOpposite());
                if (remainder.isEmpty()) {
                    stack.decrement(1);
                    markDirty();
                    return;
                }
            }
        }
    }

    private static boolean containsMatchingItem(Inventory inv, ItemStack stack) {
        for (int i = 0; i < inv.size(); i++) {
            ItemStack slotStack = inv.getStack(i);
            if (!slotStack.isEmpty() && ItemStack.areItemsEqual(slotStack, stack)) {
                return true;
            }
        }
        return false;
    }

    // --- Inventory boilerplate ---

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(pos) != this) {
            return false;
        }
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] slots = new int[inventory.size()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    // --- NBT read/write ---

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);

        NbtList filterList = new NbtList();
        for (ItemStack filter : filters) {
            NbtCompound filterNbt = new NbtCompound();
            if (!filter.isEmpty()) {
                filter.writeNbt(filterNbt);
            }
            filterList.add(filterNbt);
        }
        nbt.put("Filters", filterList);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);

        NbtList filterList = nbt.getList("Filters", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < filterList.size() && i < filters.size(); i++) {
            filters.set(i, ItemStack.fromNbt(filterList.getCompound(i)));
        }
    }
}
