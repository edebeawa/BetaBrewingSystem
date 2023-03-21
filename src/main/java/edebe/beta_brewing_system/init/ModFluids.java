package edebe.beta_brewing_system.init;

import edebe.beta_brewing_system.world.fluid.MilkFluid;
import edebe.beta_brewing_system.world.fluid.PotionFluid;
import edebe.beta_brewing_system.world.fluid.PowderSnowFluid;
import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraft.world.level.material.Fluid;

public class ModFluids {
    public static final RegisterManager<Fluid> FLUIDS = new RegisterManager<>("minecraft");

    public static final RegisterObject<Fluid> MILK = FLUIDS.add("milk", new MilkFluid.Source());
    public static final RegisterObject<Fluid> FLOWING_MILK = FLUIDS.add("flowing_milk", new MilkFluid.Flowing());
    public static final RegisterObject<Fluid> POTION = FLUIDS.add("potion", new PotionFluid.Source());
    public static final RegisterObject<Fluid> FLOWING_POTION = FLUIDS.add("flowing_potion", new PotionFluid.Flowing());
    public static final RegisterObject<Fluid> POWDER_SNOW = FLUIDS.add("powder_snow", new PowderSnowFluid());
}