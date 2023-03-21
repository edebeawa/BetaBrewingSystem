package edebe.beta_brewing_system.event.lifecycle;

import edebe.beta_brewing_system.client.render.block_entity.CauldronBlockEntityRenderer;
import edebe.beta_brewing_system.init.ModBlockEntities;
import edebe.beta_brewing_system.init.ModPotions;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
    public static void init(FMLClientSetupEvent event) {
        registerItemProperty(event);
    }

    private static void registerItemProperty(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (Item item : ForgeRegistries.ITEMS.getValues()) if (item instanceof PotionItem)
                ItemProperties.register(item, ModPotions.MILK.getResourceLocation(), (stack, world, entity, seed) -> PotionUtils.getPotion(stack) == ModPotions.MILK.getObject() ? 1 : 0);
        });
    }

    @SubscribeEvent
    public void registerBlockEntityRender(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CAULDRON.getObject(), CauldronBlockEntityRenderer::new);
    }
}