package edebe.beta_brewing_system.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import edebe.beta_brewing_system.init.ModFluids;
import edebe.beta_brewing_system.world.block.entity.CauldronBlockEntity;
import edebe.doglib.api.client.renderer.CubeRenderInfo;
import edebe.doglib.api.helper.FluidHelper;
import edebe.doglib.api.helper.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import javax.annotation.ParametersAreNonnullByDefault;

public class CauldronBlockEntityRenderer implements BlockEntityRenderer<CauldronBlockEntity> {
    public CauldronBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    @ParametersAreNonnullByDefault
    public void render(CauldronBlockEntity blockEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffers, int light, int overlay) {
        if (blockEntityIn.getAmount() > 0) RenderHelper.renderFluidTankFluid(blockEntityIn, matrixStack, buffers, light,
                CubeRenderInfo.create(Direction.UP, new Vector3f(1, 1, 1),
                        new Vector3f(0, ((1 - (blockEntityIn.getAmount() / (float) blockEntityIn.getCapacity())) * 0.7f) + 0.05f, 0)
                )
        );
    }
}