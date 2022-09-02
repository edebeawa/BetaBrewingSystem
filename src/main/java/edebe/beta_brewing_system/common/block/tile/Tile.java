package edebe.beta_brewing_system.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class Tile extends TileEntity {
    public Tile(TileEntityType<?> type) {
        super(type);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT ret = super.write(tag);
        writePacketNBT(ret);
        return ret;
    }

    @Nonnull
    @Override
    public final CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        readPacketNBT(tag);
    }

    public void writePacketNBT(CompoundNBT cmp) {}

    public void readPacketNBT(CompoundNBT cmp) {}

    @Override
    public final SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        writePacketNBT(tag);
        return new SUpdateTileEntityPacket(pos, -999, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        readPacketNBT(packet.getNbtCompound());
    }

}
