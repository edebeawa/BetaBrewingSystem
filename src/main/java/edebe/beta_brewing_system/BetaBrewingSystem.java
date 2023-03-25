package edebe.beta_brewing_system;

import edebe.beta_brewing_system.event.CommonEvent;
import edebe.beta_brewing_system.event.lifecycle.ClientSetup;
import edebe.beta_brewing_system.init.*;
import edebe.beta_brewing_system.mixin.BlocksAccessor;
import edebe.beta_brewing_system.mixin.ItemsAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static edebe.doglib.api.helper.RegistryHelper.bind;
import static edebe.doglib.api.helper.RegistryHelper.register;

@Mod(BetaBrewingSystem.MODID)
public class BetaBrewingSystem {
    private static final String PROTOCOL_VERSION = "1";

    public static final String MODID = "betabrewingsystem";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public BetaBrewingSystem() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.register(this);
        CommonEvent.register(MinecraftForge.EVENT_BUS, modBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->modBus.register(new ClientSetup()));
        modBus.addListener(this::CommonSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->modBus.addListener(ClientSetup::init));
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DogLibConfig.COMMON_CONFIG);
        bind(modBus, Registry.BLOCK_REGISTRY, (registry) -> register(registry, ModBlocks.BLOCKS));
        bind(modBus, Registry.BLOCK_ENTITY_TYPE_REGISTRY, (registry) -> register(registry, ModBlockEntities.BLOCK_ENTITY_TYPES));
        bind(modBus, ForgeRegistries.Keys.FLUID_TYPES, (registry) -> register(registry, ModFluidTypes.FLUID_TYPES));
        bind(modBus, Registry.FLUID_REGISTRY, (registry) -> register(registry, ModFluids.FLUIDS));
        bind(modBus, Registry.ITEM_REGISTRY, (registry) -> register(registry, ModItems.ITEMS));
        bind(modBus, Registry.MOB_EFFECT_REGISTRY, (registry) -> register(registry, ModMobEffects.MOB_EFFECTS));
        bind(modBus, Registry.POTION_REGISTRY, (registry) -> register(registry, ModPotions.POTIONS));
    }

    private void CommonSetup(FMLCommonSetupEvent event) {
        BlocksAccessor.setCauldron(ModBlocks.CAULDRON.getObject());
        BlocksAccessor.setWaterCauldron(ModBlocks.CAULDRON.getObject());
        BlocksAccessor.setLavaCauldron(ModBlocks.CAULDRON.getObject());
        BlocksAccessor.setPowderSnowCauldron(ModBlocks.CAULDRON.getObject());
        ItemsAccessor.setCauldron(ModItems.CAULDRON.getObject());
        ItemsAccessor.setMilkBucket(ModItems.MILK_BUCKET.getObject());
        ItemsAccessor.setPowderSnowBucket(ModItems.POWDER_SNOW_BUCKET.getObject());
        ItemsAccessor.setGlassBottle(ModItems.GLASS_BOTTLE.getObject());
    }
}
