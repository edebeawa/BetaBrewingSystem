package edebe.beta_brewing_system.common.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FluidHelper {
    @Nullable
    static TextureAtlasSprite getFluidStillTexture(@Nonnull FluidStack stack) {
        FluidAttributes attributes = stack.getFluid().getAttributes();
        ResourceLocation still = attributes.getStillTexture(stack);
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
    }

    @Nullable
    static TextureAtlasSprite getFluidFlowingTexture(@Nonnull FluidStack stack) {
        FluidAttributes attributes = stack.getFluid().getAttributes();
        ResourceLocation flowing = attributes.getFlowingTexture(stack);
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(flowing);
    }

    static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, World world, BlockPos pos) {
        if(world.isRemote)
            if(fluid.isFluidEqual(new FluidStack(Fluids.WATER, 1000)))
                return BiomeColors.getWaterColor(world, pos) | 0xFF000000;
        return fluid.getFluid().getAttributes().getColor(fluid);
    }

    static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, @Nonnull TileEntity tileEntity) {
        return getLiquidColorWithBiome(fluid, tileEntity.getWorld(), tileEntity.getPos());
    }

    static int getFluidScaled(int pixels, FluidStack fluid, int maxLiquidAmount) {
        if(maxLiquidAmount == 0)
            return pixels;
        Long currentLiquidAmount = (long) fluid.getAmount();
        long x = currentLiquidAmount * pixels / maxLiquidAmount;
        return pixels - (int) x;
    }
}
