package edebe.beta_brewing_system.common;

import edebe.beta_brewing_system.common.event.setup.ClientSetupEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static edebe.beta_brewing_system.common.BetaBrewingSystem.MODID;

@Mod(MODID)
public class BetaBrewingSystem {
    public static final String MODID = "beta_brewing_system";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public BetaBrewingSystem() {
        MinecraftForge.EVENT_BUS.register(new MoreRedstoneFMLBusEvents(this));
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.register(this);
        modBus.addListener(ClientSetupEvent::init);
        modBus.addGenericListener(Block.class, Register::registerBlocks);
        modBus.addGenericListener(TileEntityType.class, Register::registerTiles);
        modBus.addGenericListener(Fluid.class, Register::registerFluids);
        modBus.addGenericListener(Item.class, Register::registerItems);
        modBus.addGenericListener(EntityType.class, Register::registerEntities);
        modBus.addGenericListener(IRecipeSerializer.class, Register::registerRecipes);
    }

    private static class MoreRedstoneFMLBusEvents {
        private final BetaBrewingSystem parent;

        MoreRedstoneFMLBusEvents(BetaBrewingSystem parent) {
            this.parent = parent;
        }
    }
}
