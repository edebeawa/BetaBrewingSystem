package edebe.beta_brewing_system.init;

import edebe.beta_brewing_system.world.fluid.SolidFluid;
import edebe.beta_brewing_system.world.item.ModBottleItem;
import edebe.beta_brewing_system.world.item.SolidBucketItem;
import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class ModItems {
    public static Item.Properties builder(CreativeModeTab tab) {
        return new Item.Properties().tab(tab);
    }

    public static final RegisterManager<Item> ITEMS = new RegisterManager<>("minecraft");

    public static final RegisterObject<Item> CAULDRON = ITEMS.add(ModBlocks.CAULDRON, builder(CreativeModeTab.TAB_BREWING));
    public static final RegisterObject<Item> MILK_BUCKET = ITEMS.add("milk_bucket", new BucketItem(ModFluids.MILK::getObject, builder(CreativeModeTab.TAB_MISC).craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegisterObject<Item> POWDER_SNOW_BUCKET = ITEMS.add("powder_snow_bucket", new SolidBucketItem(() -> ModFluids.POWDER_SNOW.getObject(SolidFluid.class), builder(CreativeModeTab.TAB_MISC).stacksTo(1)));
    public static final RegisterObject<Item> GLASS_BOTTLE = ITEMS.add("glass_bottle", new ModBottleItem(builder(CreativeModeTab.TAB_BREWING)));
    public static final RegisterObject<Item> SPLASH_GLASS_BOTTLE = ITEMS.add("splash_glass_bottle", new ModBottleItem(builder(CreativeModeTab.TAB_BREWING)));
    public static final RegisterObject<Item> LINGERING_GLASS_BOTTLE = ITEMS.add("lingering_glass_bottle", new ModBottleItem(builder(CreativeModeTab.TAB_BREWING)));
}