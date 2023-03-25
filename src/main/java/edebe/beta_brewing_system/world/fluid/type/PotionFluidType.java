package edebe.beta_brewing_system.world.fluid.type;

import edebe.beta_brewing_system.helper.PotionHelper;
import edebe.doglib.api.helper.ColorHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModList;

import java.awt.*;
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
                if (ModList.get().isLoaded("create"))
                    return new ResourceLocation("create", "fluid/potion_still");
                else return new ResourceLocation("block/potion_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                if (ModList.get().isLoaded("create"))
                    return new ResourceLocation("create", "fluid/potion_flow");
                else return new ResourceLocation("block/potion_flow");
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return PotionHelper.isBetaPotion(stack) ? PotionHelper.getColor(stack) : ColorHelper.parseToColor(PotionUtils.getColor(PotionHelper.getPotion(stack)), ColorHelper.MAX_VALUE, ColorHelper.ColorOverrideMode.ALPHA).getRGB();
            }
        });
    }
}