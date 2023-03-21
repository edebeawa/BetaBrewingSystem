package edebe.beta_brewing_system.world.fluid.type;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class PotionFluidType extends FluidType {
    public PotionFluidType() {
        super(Properties.create().descriptionId("fluid.minecraft.potion"));
    }

    @Override
    public Component getDescription(FluidStack stack) {
        return PotionHelper.getFluidTranslation(stack);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return new ResourceLocation("block/potion_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return new ResourceLocation("block/potion_flow");
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return PotionHelper.getColor(stack);
            }
        });
    }
}