package edebe.beta_brewing_system.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.stream.IntStream;

public abstract class TileExposedSimpleInventory extends TileSimpleInventory implements ISidedInventory {
    private final LazyValue<int[]> slots = new LazyValue<>(() -> IntStream.range(0, getSizeInventory()).toArray());

    public TileExposedSimpleInventory(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public boolean isEmpty() {
        return getItemHandler().isEmpty();
    }

    @Override
    public int getSizeInventory() {
        return inventorySize();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int index) {
        return getItemHandler().getStackInSlot(index);
    }

    @NotNull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getItemHandler().decrStackSize(index, count);
    }

    @NotNull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getItemHandler().removeStackFromSlot(index);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setInventorySlotContents(int index, ItemStack stack) {
        getItemHandler().setInventorySlotContents(index, stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isUsableByPlayer(PlayerEntity player) {
        return getItemHandler().isUsableByPlayer(player);
    }

    @Override
    public void clear() {
        getItemHandler().clear();
    }

    @Override
    public int getInventoryStackLimit() {
        return getItemHandler().getInventoryStackLimit();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void openInventory(PlayerEntity player) {
        getItemHandler().openInventory(player);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void closeInventory(PlayerEntity player) {
        getItemHandler().closeInventory(player);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getItemHandler().isItemValidForSlot(index, stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int count(Item item) {
        return getItemHandler().count(item);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hasAny(Set<Item> set) {
        return getItemHandler().hasAny(set);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull Direction side) {
        return slots.getValue();
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        if (isItemValidForSlot(index, stack)) {
            ItemStack existing = getStackInSlot(index);
            return existing.isEmpty() || existing.getCount() + stack.getCount() <= getInventoryStackLimit();
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> new SidedInvWrapper(this, side)));
    }
}
