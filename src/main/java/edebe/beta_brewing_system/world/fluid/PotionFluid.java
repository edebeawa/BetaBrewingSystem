package edebe.beta_brewing_system.world.fluid;

import edebe.beta_brewing_system.init.ModFluidTypes;
import edebe.beta_brewing_system.init.ModFluids;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class PotionFluid extends FlowingFluid {
    @Override
    public FluidType getFluidType() {
        return ModFluidTypes.POTION.getObject();
    }

    @Override
    public Fluid getSource() {
        return ModFluids.POTION.getObject();
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_POTION.getObject();
    }

    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    public void tick(Level world, BlockPos pos, FluidState state) {
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
    }

    @Override
    public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource randomSource) {
        if (!state.isSource() && !state.getValue(FALLING))
            if (randomSource.nextInt(64) == 0)
                world.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, randomSource.nextFloat() * 0.25F + 0.75F, randomSource.nextFloat() + 0.5F, false);
        else if (randomSource.nextInt(10) == 0)
            world.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + randomSource.nextDouble(), (double)pos.getY() + randomSource.nextDouble(), (double)pos.getZ() + randomSource.nextDouble(), 0.0D, 0.0D, 0.0D);
    }

    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    @Override
    protected boolean canConvertToSource() {
        return true;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockentity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockentity);
    }

    @Override
    public int getSlopeFindDistance(LevelReader world) {
        return 4;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.POTION.getObject() || fluid == ModFluids.FLOWING_POTION.getObject();
    }

    @Override
    public int getDropOff(LevelReader world) {
        return 1;
    }

    @Override
    public int getTickDelay(LevelReader world) {
        return 5;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter getter, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @NotNull
    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Source extends PotionFluid {
        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends PotionFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }
}
