package edebe.beta_brewing_system.world.fluid;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class SolidFluid extends FlowingFluid {
    public abstract Fluid getFluid();

    public abstract Block getBlock();

    @Override
    public Fluid getSource() {
        return getFluid();
    }

    @Override
    public Fluid getFlowing() {
        return getFluid();
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {}

    @Override
    public int getSlopeFindDistance(LevelReader world) {
        return -1;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return getBlock().defaultBlockState();
    }

    @Override
    public int getDropOff(LevelReader world) {
        return -1;
    }

    @Override
    public int getTickDelay(LevelReader world) {
        return -1;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter getter, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected float getExplosionResistance() {
        return -1;
    }

    @Override
    public int getAmount(FluidState state) {
        return -1;
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }
}
