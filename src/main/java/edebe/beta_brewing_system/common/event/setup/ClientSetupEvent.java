package edebe.beta_brewing_system.common.event.setup;

import edebe.beta_brewing_system.client.render.tile.RenderTileBrewingCauldron;
import edebe.beta_brewing_system.common.Register;
import edebe.beta_brewing_system.common.helper.potion.PotionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static edebe.beta_brewing_system.common.Register.*;
import static net.minecraft.client.renderer.RenderTypeLookup.setRenderLayer;

@OnlyIn(Dist.CLIENT)
public class ClientSetupEvent {
    public static void init(FMLClientSetupEvent event) {
        setBlockRenderType();
        registerTileEntityRender();
        registerItemColors();
    }

    private static void setBlockRenderType() {
        setRenderLayers(RenderType.getCutout(), BREWING_CAULDRON_BLOCK);
        setRenderLayers(RenderType.getTranslucent(), POTION_FLUID, FLOWING_POTION_FLUID);
    }

    private static void registerTileEntityRender() {
        ClientRegistry.bindTileEntityRenderer(Register.BREWING_CAULDRON_TILE, (RenderTileBrewingCauldron::new));
    }

    private static void registerItemColors() {
        ItemColors itemColors = Minecraft.getInstance().getItemColors();
        itemColors.register((stack, color) -> color > 0 ? -1 : PotionHelper.getPotionColor(stack), POTION_ITEM, SPLASH_POTION_ITEM, LINGERING_POTION_ITEM, TIPPED_ARROW_ITEM);
    }

    private static void setRenderLayers(RenderType type, Fluid... fluids) {
        for (Fluid fluid : fluids) {
            setRenderLayer(fluid, type);
        }
    }

    private static void setRenderLayers(RenderType type, Block... blocks) {
        for (Block block : blocks) {
            setRenderLayer(block, type);
        }
    }
}
