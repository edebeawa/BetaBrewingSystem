package edebe.beta_brewing_system.common.fluid;

import edebe.beta_brewing_system.common.helper.potion.PotionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidAttributes.Builder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

import static edebe.beta_brewing_system.common.BetaBrewingSystem.MODID;
import static edebe.beta_brewing_system.common.Register.*;

@ParametersAreNonnullByDefault
public abstract class PotionFluid extends ForgeFlowingFluid {
    public static ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(() -> POTION_FLUID, () -> FLOWING_POTION_FLUID, PotionFluidAttributes.builder(new ResourceLocation(MODID + ":fluid/potion_still"), new ResourceLocation(MODID + ":fluid/potion_flow"))).block(() -> (FlowingFluidBlock) POTION_FLUID_BLOCK);

    protected PotionFluid() {
        super(PROPERTIES);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(World worldIn, BlockPos pos, FluidState state, Random random) {
        if (!state.isSource() && !state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            worldIn.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public IParticleData getDripParticleData() {
        return ParticleTypes.DRIPPING_WATER;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return true;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn, pos, tileentity);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    public BlockState getBlockState(FluidState state) {
        return POTION_FLUID_BLOCK.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == POTION_FLUID || fluidIn == FLOWING_POTION_FLUID;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    public boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    public static class Flowing extends PotionFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends PotionFluid {
        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    protected static class PotionBuilder extends Builder {
        protected PotionBuilder(ResourceLocation stillTexture, ResourceLocation flowingTexture, BiFunction<Builder, Fluid, FluidAttributes> factory) {
            super(stillTexture, flowingTexture, factory);
        }
    }

    public static class PotionFluidAttributes extends FluidAttributes {
        protected PotionFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(IBlockDisplayReader world, BlockPos pos) {
            return 0xFFFFFFFF;
        }

        @Override
        public ITextComponent getDisplayName(FluidStack stack) {
            return PotionHelper.getFluidTranslation(stack);
        }

        public static Builder builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            return new PotionBuilder(stillTexture, flowingTexture, PotionFluidAttributes::new);
        }
    }
}
