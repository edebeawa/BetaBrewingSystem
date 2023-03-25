package edebe.beta_brewing_system.world.fluid.type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class PowderSnowFluidType extends FluidType {
    public PowderSnowFluidType() {
        super(Properties.create()
                .descriptionId("block.minecraft.powder_snow")
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_POWDER_SNOW)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_POWDER_SNOW)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return new ResourceLocation("block/powder_snow");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return new ResourceLocation("block/powder_snow");
            }

            @Override
            public int getTintColor() {
                return -1;
            }
        });
    }
}
