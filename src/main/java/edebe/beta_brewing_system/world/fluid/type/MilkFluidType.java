package edebe.beta_brewing_system.world.fluid.type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModList;

import java.util.function.Consumer;

public class MilkFluidType extends FluidType {
    public MilkFluidType() {
        super(Properties.create()
                .descriptionId("fluid.minecraft.milk")
                .fallDistanceModifier(0F)
                .canExtinguish(true)
                .canConvertToSource(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(true)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                if (ModList.get().isLoaded("create"))
                    return new ResourceLocation("create", "fluid/milk_still");
                else return new ResourceLocation("forge", "block/milk_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                if (ModList.get().isLoaded("create"))
                    return new ResourceLocation("create", "fluid/milk_flow");
                else return new ResourceLocation("forge", "block/milk_flowing");
            }

            @Override
            public int getTintColor() {
                return -1;
            }
        });
    }
}
