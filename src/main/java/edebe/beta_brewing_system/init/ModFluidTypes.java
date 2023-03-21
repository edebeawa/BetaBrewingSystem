package edebe.beta_brewing_system.init;

import edebe.beta_brewing_system.world.fluid.type.MilkFluidType;
import edebe.beta_brewing_system.world.fluid.type.PotionFluidType;
import edebe.beta_brewing_system.world.fluid.type.PowderSnowFluidType;
import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

public class ModFluidTypes {
    public static final RegisterManager<FluidType> FLUID_TYPES = new RegisterManager<>("minecraft");

    public static final RegisterObject<FluidType> MILK = FLUID_TYPES.add("milk", new MilkFluidType());
    public static final RegisterObject<FluidType> POTION = FLUID_TYPES.add("potion", new PotionFluidType());
    public static final RegisterObject<FluidType> POWDER_SNOW = FLUID_TYPES.add("powder_snow", new PowderSnowFluidType());
}