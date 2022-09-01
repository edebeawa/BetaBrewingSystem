package edebe.beta_brewing_system.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import edebe.beta_brewing_system.common.block.tile.TileBrewingCauldron;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static edebe.beta_brewing_system.common.Register.POTION_FLUID;
import static edebe.beta_brewing_system.common.helper.FluidHelper.getLiquidColorWithBiome;
import static edebe.beta_brewing_system.common.helper.FluidRenderHelper.Render.createFluidRenderInfo;
import static edebe.beta_brewing_system.common.helper.FluidRenderHelper.Render.renderFluidTank;

public class RenderTileBrewingCauldron extends TileEntityRenderer<TileBrewingCauldron> {
    public RenderTileBrewingCauldron(TileEntityRendererDispatcher manager) {
        super(manager);
    }

    @Override
    public void render(@Nonnull TileBrewingCauldron tileIn, float partialTicks, MatrixStack matrixStack, @NotNull IRenderTypeBuffer buffers, int light, int overlay) {
        if (tileIn.getAmount() > 0) {
            renderFluidTank(tileIn, matrixStack, buffers, (tile, ms, stack, bufferIn, info) -> stack.getFluid() == POTION_FLUID ? ((TileBrewingCauldron) tile).getPotionFluidColor() : getLiquidColorWithBiome(stack, tile), createFluidRenderInfo(Direction.UP, 1, 1, 1, 0, ((1 - (tileIn.getAmount() / (float)tileIn.getCapacity())) * 0.7f) + 0.05f, 0));
            /*renderFluidTank(tileIn, matrixStack, buffers, createFluidRenderInfo(
                    new Direction[]{Direction.UP,Direction.DOWN,Direction.WEST,Direction.EAST,Direction.SOUTH,Direction.NORTH},
                    new float[]{1,1,1,1,1,1},
                    new float[]{1,1,1,1,1,1},
                    new float[]{1,1,1,1,1,1},
                    new float[]{0,0,0,0,0,0},
                    new float[]{1,1,1,1,1,1},
                    new float[]{0,0,0,0,0,0}
            ));*/
        }
    }
}
