package edebe.beta_brewing_system.common.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import edebe.beta_brewing_system.common.block.tile.TileFluidTank;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface IFluidRenderColor {
    int getColor(@Nonnull TileFluidTank tileFluidTank, MatrixStack matrixStack, FluidStack fluidStack, @NotNull IRenderTypeBuffer renderTypeBuffer, FluidRenderHelper.FluidRenderInfo[] fluidRenderInfo);
}
