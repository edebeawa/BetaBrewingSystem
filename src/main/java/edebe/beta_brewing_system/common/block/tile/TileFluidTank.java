package edebe.beta_brewing_system.common.block.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class TileFluidTank extends TileExposedSimpleInventory {
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());
    private final FluidTank fluidTank = getFluidTank();

    public TileFluidTank(TileEntityType<?> type) {
        super(type);
    }

    protected abstract FluidTank getFluidTank();

    @Override
    public void writePacketNBT(CompoundNBT cmp) {
        cmp.put("fluidTank", Objects.requireNonNull(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null)));
        writeNBT(cmp);
    }

    @Override
    public void readPacketNBT(CompoundNBT cmp) {
        if (cmp.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, cmp.get("fluidTank"));
        readNBT(cmp);
    }

    protected void writeNBT(CompoundNBT cmp) {}

    protected void readNBT(CompoundNBT cmp) {}

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return handlers[facing.ordinal()].cast();
        if (!this.removed && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void remove() {
        super.remove();
        for (LazyOptional<? extends IItemHandler> handler : handlers)
            handler.invalidate();
    }

    public void setFluid(FluidStack stack) {
        this.fluidTank.setFluid(stack);
    }

    public FluidStack getFluid() {
        return this.fluidTank.getFluid();
    }

    public void setAmount(int amount) {
        this.fluidTank.setFluid(new FluidStack(this.fluidTank.getFluid().getFluid(), amount));
    }

    public int getAmount() {
        return this.fluidTank.getFluidAmount();
    }

    public void growAmount(int amount) {
        setAmount(getAmount() + amount);
    }

    public void shrinkAmount(int amount) {
        setAmount(getAmount() - amount);
    }

    public int getCapacity() {
        return this.fluidTank.getCapacity();
    }
}
