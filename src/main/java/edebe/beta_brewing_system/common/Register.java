package edebe.beta_brewing_system.common;

import edebe.beta_brewing_system.common.block.BlockBrewingCauldron;
import edebe.beta_brewing_system.common.block.tile.TileBrewingCauldron;
import edebe.beta_brewing_system.common.entity.ThrowablePotionEntity;
import edebe.beta_brewing_system.common.fluid.PotionFluid;
import edebe.beta_brewing_system.common.item.LingeringPotionItem;
import edebe.beta_brewing_system.common.item.PotionItem;
import edebe.beta_brewing_system.common.item.SplashPotionItem;
import edebe.beta_brewing_system.common.item.TippedArrowItem;
import edebe.beta_brewing_system.recipe.GlassBottleRecipe;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static edebe.beta_brewing_system.common.helper.RegistryHelper.register;

public class Register {
    public static final Item.Properties properties = new Item.Properties().group(ItemGroup.BREWING).rarity(Rarity.COMMON);

    public static final String BREWING_CAULDRON_NAME = "brewing_cauldron";
    public static final Block BREWING_CAULDRON_BLOCK = new BlockBrewingCauldron(AbstractBlock.Properties.from(Blocks.CAULDRON));
    public static final TileEntityType<TileBrewingCauldron> BREWING_CAULDRON_TILE = TileEntityType.Builder.create(TileBrewingCauldron::new, BREWING_CAULDRON_BLOCK).build(null);

    public static final String POTION_NAME = "potion";
    public static final FlowingFluid POTION_FLUID = new PotionFluid.Source();
    public static final FlowingFluid FLOWING_POTION_FLUID = new PotionFluid.Flowing();
    public static final Block POTION_FLUID_BLOCK = new FlowingFluidBlock(POTION_FLUID, AbstractBlock.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops());

    public static final String SPLASH_GLASS_BOTTLE_NAME = "splash_glass_bottle";
    public static final Item SPLASH_GLASS_BOTTLE_ITEM = new Item(defaultBuilder());
    public static final String LINGERING_GLASS_BOTTLE_NAME = "lingering_glass_bottle";
    public static final Item LINGERING_GLASS_BOTTLE_ITEM = new Item(defaultBuilder());
    public static final Item POTION_ITEM = new PotionItem(properties.maxStackSize(1));
    public static final Item SPLASH_POTION_ITEM = new SplashPotionItem(properties.maxStackSize(1));
    public static final Item LINGERING_POTION_ITEM = new LingeringPotionItem(properties.maxStackSize(1));

    public static final String TIPPED_ARROW_NAME = "tipped_arrow";
    public static final Item TIPPED_ARROW_ITEM = new TippedArrowItem(defaultBuilder());

    public static final EntityType<ThrowablePotionEntity> THROWABLE_POTION_ENTITY = EntityType.Builder.<ThrowablePotionEntity>create(ThrowablePotionEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build("");

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        register(registry, POTION_NAME + "_block", POTION_FLUID_BLOCK);
        register(registry, BREWING_CAULDRON_NAME, BREWING_CAULDRON_BLOCK);
    }

    public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        register(registry, BREWING_CAULDRON_NAME, BREWING_CAULDRON_TILE);
    }

    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        IForgeRegistry<Fluid> registry = event.getRegistry();
        register(registry, POTION_NAME, POTION_FLUID);
        register(registry, "flowing_" + POTION_NAME, FLOWING_POTION_FLUID);
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        register(registry, Registry.BLOCK.getKey(BREWING_CAULDRON_BLOCK), new BlockItem(BREWING_CAULDRON_BLOCK, defaultBuilder()));
        register(registry, SPLASH_GLASS_BOTTLE_NAME, SPLASH_GLASS_BOTTLE_ITEM);
        register(registry, LINGERING_GLASS_BOTTLE_NAME, LINGERING_GLASS_BOTTLE_ITEM);
        register(registry, POTION_NAME, POTION_ITEM);
        register(registry, "splash_" + POTION_NAME, SPLASH_POTION_ITEM);
        register(registry, "lingering_" + POTION_NAME, LINGERING_POTION_ITEM);
        register(registry, TIPPED_ARROW_NAME, TIPPED_ARROW_ITEM);
    }

    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        register(registry, "throwable_" + POTION_NAME, THROWABLE_POTION_ENTITY);
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
        register(registry, "glass_bottle_recipe", GlassBottleRecipe.SERIALIZER);
    }

    public static Item.Properties defaultBuilder() {
        return properties.maxStackSize(64);
    }
}
