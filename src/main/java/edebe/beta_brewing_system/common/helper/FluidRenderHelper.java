package edebe.beta_brewing_system.common.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import edebe.beta_brewing_system.common.block.tile.TileFluidTank;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

import static edebe.beta_brewing_system.common.helper.ColorHelper.parseToColor;
import static edebe.beta_brewing_system.common.helper.FluidHelper.*;

public class FluidRenderHelper {
    public interface Render {
        static void renderFluidTank(@Nonnull TileFluidTank tileFluidTank, MatrixStack matrixStack, @NotNull IRenderTypeBuffer bufferIn, FluidRenderInfo... renders) {
            renderFluidTank(tileFluidTank, matrixStack, bufferIn, (tile, ms, stack, buffers, info) -> getLiquidColorWithBiome(stack, tile), renders);
        }

        static void renderFluidTank(@Nonnull TileFluidTank tileFluidTank, MatrixStack matrixStack, @NotNull IRenderTypeBuffer bufferIn, IFluidRenderColor fluidColor, FluidRenderInfo... renders) {
            if (tileFluidTank.isRemoved() || tileFluidTank.getFluid().isEmpty())
                return;

            FluidStack stack = tileFluidTank.getFluid();
            renderFluid(matrixStack, stack, bufferIn, fluidColor.getColor(tileFluidTank, matrixStack, stack, bufferIn, renders), renders);
        }

        static void renderFluid(MatrixStack matrixStack, FluidStack stack, @NotNull IRenderTypeBuffer bufferIn, int color, FluidRenderInfo... renders) {
            for (FluidRenderInfo render : renders) {
                matrixStack.push();
                Matrix4f matrix4f = matrixStack.getLast().getMatrix();

                TextureAtlasSprite sprite = render.isFlowing() ? getFluidFlowingTexture(stack) : getFluidStillTexture(stack);
                if (sprite == null)
                    return;

                IVertexBuilder builder = bufferIn.getBuffer(RenderType.getText(sprite.getAtlasTexture().getTextureLocation()));

                float u1 = sprite.getInterpolatedU(0);
                float v1 = sprite.getInterpolatedV(0);
                float u2 = sprite.getInterpolatedU(render.isFlowing() ? 8 : 16);
                float v2 = sprite.getInterpolatedV(render.isFlowing() ? 8 : 16);

                Color toColor = parseToColor(color);

                float r = toColor.getRed() / 255f;
                float g = toColor.getGreen() / 255f;
                float b = toColor.getBlue() / 255f;
                float a = toColor.getAlpha() / 255f;
                int light = 15728880;

                render.render(matrix4f, builder, r, g, b, a, u1, v1, u2, v2, light);
                matrixStack.pop();
            }
        }

        static FluidRenderInfo[] createFluidRenderInfo(Direction direction, float x, float y, float z, float offsetX, float offsetY, float offsetZ) {
            return new FluidRenderInfo[]{new FluidRenderInfo(direction, x, y, z, offsetX, offsetY, offsetZ)};
        }

        static FluidRenderInfo[] createFluidRenderInfo(Direction[] direction, float[] x, float[] y, float[] z, float[] offsetX, float[] offsetY, float[] offsetZ) {
            FluidRenderInfo[] fluidRenders = new FluidRenderInfo[direction.length];

            for (int i = 0;i < direction.length;i++) {
                fluidRenders[i] = new FluidRenderInfo(direction[i], x[i], y[i], z[i], offsetX[i], offsetY[i], offsetZ[i]);
            }

            return fluidRenders;
        }
    }

    public static class FluidRenderInfo {
        private final Direction direction;
        private final Matrix4f matrix4f;
        private final float offsetX;
        private final float offsetY;
        private final float offsetZ;

        protected FluidRenderInfo(Direction direction, float x, float y, float z, float offsetX, float offsetY, float offsetZ) {
            this.direction = direction;
            this.matrix4f = Matrix4f.makeScale(x, y, z);
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
        }

        protected boolean isFlowing() {
            return !(this.direction.equals(Direction.UP) || this.direction.equals(Direction.DOWN));
        }

        protected void render(Matrix4f matrix4f, IVertexBuilder renderer, float r, float g, float b, float a, float u1, float v1, float u2, float v2, int light) {
            matrix4f.mul(this.matrix4f);

            if (this.direction.equals(Direction.WEST)) {
                //WEST
                rendererPos(renderer, matrix4f, 1, 1, 1).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 0, 1).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 0, 0).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 1, 0).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
            } else if (this.direction.equals(Direction.EAST)) {
                //EAST
                rendererPos(renderer, matrix4f, 0, 1, 0).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 0, 0).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 0, 1).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 1, 1).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
            } else if (this.direction.equals(Direction.NORTH)) {
                //NORTH
                rendererPos(renderer, matrix4f, 0, 1, 1).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 0, 1).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 0, 1).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 1, 1).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
            } else if (this.direction.equals(Direction.SOUTH)) {
                //SOUTH
                rendererPos(renderer, matrix4f, 1, 1, 0).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 0, 0).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 0, 0).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 1, 0).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
            } else if (this.direction.equals(Direction.UP)) {
                //UP
                rendererPos(renderer, matrix4f, 0, 1, 0).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 1, 1).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 1, 1).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 1, 0).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
            } else if (this.direction.equals(Direction.DOWN)) {
                //DOWN
                rendererPos(renderer, matrix4f, 1, 0, 1).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 0, 1).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 0, 0, 0).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
                rendererPos(renderer, matrix4f, 1, 0, 0).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
            }
        }

        private IVertexBuilder rendererPos(IVertexBuilder renderer, Matrix4f matrixIn, float x, float y, float z) {
            return renderer.pos(matrixIn, x - this.offsetX, y - this.offsetY, z - this.offsetZ);
        }
    }
}
        /*
        //WEST
        renderer.pos(matrix4f, 1.001f, 0.66f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
        renderer.pos(matrix4f, 1.001f, 0.34f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 1.001f, 0.34f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 1.001f, 0.66f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

        //EAST
        renderer.pos(matrix4f, -0.001f, 0.34f, 0.435f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
        renderer.pos(matrix4f, -0.001f, 0.34f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, -0.001f, 0.66f, 0.752f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, -0.001f, 0.66f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

        //NORTH
        renderer.pos(matrix4f, 0.245f + offsetSide, 0.657f, 1.001f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.245f + offsetSide, 0.339f, 1.001f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.565f + offsetSide, 0.339f, 1.001f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.565f + offsetSide, 0.657f, 1.001f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

        //SOUTH
        renderer.pos(matrix4f, 0.245f + offsetSide, 0.339f, -0.001f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.245f + offsetSide, 0.657f, -0.001f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.565f + offsetSide, 0.657f, -0.001f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.565f + offsetSide, 0.339f, -0.001f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

        //UP
        renderer.pos(matrix4f, 0.34f, 1.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.34f, 1.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.66f, 1.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.66f, 1.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

        //DOWN
        renderer.pos(matrix4f, 0.66f, -0.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.34f, -0.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.34f, -0.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
        renderer.pos(matrix4f, 0.66f, -0.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
        */