package edebe.beta_brewing_system.event;

import edebe.beta_brewing_system.init.ModFluidTypes;
import edebe.doglib.api.event.EntityInFluidEvent;
import edebe.doglib.api.event.client.ItemFoilModifyEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEvent {
    public static void register(IEventBus forgeBus, IEventBus modBus) {
        forgeBus.register(new Render());
        forgeBus.register(new Entity());
    }

    public static class Render {
        @SubscribeEvent
        public void onItemFoilModify(ItemFoilModifyEvent event) {
            if (event.getItemStack().getItem() instanceof PotionItem) event.setResourceLocation(new ResourceLocation("textures/misc/potion_item_glint.png"));
        }
    }

    public static class Entity {
        @SubscribeEvent
        public void inFluid(EntityInFluidEvent event) {
            if (event.getEntity() instanceof LivingEntity living && !event.isFirstTick() && event.getForgeFluidTypeHeight().getDouble(ModFluidTypes.MILK.getObject()) > 0) {
                living.removeAllEffects();
            }
        }
    }
}
