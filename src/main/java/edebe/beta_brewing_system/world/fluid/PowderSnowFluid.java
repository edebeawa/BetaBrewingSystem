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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PowderSnowFluid extends SolidFluid {
    @Override
    public FluidType getFluidType() {
        return ModFluidTypes.POWDER_SNOW.getObject();
    }

    @Override
    public Fluid getFluid() {
        return ModFluids.POWDER_SNOW.getObject();
    }

    @Override
    public Block getBlock() {
        return Blocks.POWDER_SNOW;
    }

    @Override
    public Item getBucket() {
        return Items.POWDER_SNOW_BUCKET;
    }

    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
    }
}

