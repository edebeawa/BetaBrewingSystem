package edebe.beta_brewing_system.world.block.entity;

import edebe.beta_brewing_system.helper.PotionHelper;
import edebe.beta_brewing_system.init.ModBlockEntities;
import edebe.beta_brewing_system.init.ModFluids;
import edebe.beta_brewing_system.mixin.BiomeInvoker;
import edebe.beta_brewing_system.world.block.CauldronBlock;
import edebe.doglib.api.world.block.block_entity.SimpleFluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class CauldronBlockEntity extends SimpleFluidTankBlockEntity {
    private static final String TAG_FLUID_METADATA = "Metadata";
    private int fluidData;

    public CauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAULDRON.getObject(), pos, state);
    }

    protected FluidTank createFluidTank() {
        return new FluidTank(FluidType.BUCKET_VOLUME, stack -> stack.getFluid() != ModFluids.POTION.getObject() || (stack.getFluid() == ModFluids.POTION.getObject() && PotionHelper.getFluidMetadata(stack) == fluidData)) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                notifyUpdate();
            }
        };
    }

    @Override
    public void writeNBT(CompoundTag cmp) {
        if (compareFluid(ModFluids.POTION.getObject()))
            getFluid().getOrCreateTag().putInt(TAG_FLUID_METADATA, fluidData);
    }

    @Override
    public void readNBT(CompoundTag cmp) {
        if (compareFluid(ModFluids.POTION.getObject()))
            fluidData = getFluid().getOrCreateTag().getInt(TAG_FLUID_METADATA);
        else fluidData = 0;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, CauldronBlockEntity blockEntity) {
        if (world.getGameTime() % 20 == 0) {
            boolean canGrow = blockEntity.compareFluid(Fluids.WATER) || blockEntity.compareFluid(ModFluids.POTION.getObject());
            blockEntity.onStalactiteDrip(canGrow);
            if (world.getLevelData().isRaining() && world.canSeeSky(pos)) blockEntity.onRaining(Fluids.WATER, canGrow);
            blockEntity.onVaporize();
        }
        blockEntity.notifyUpdate();
    }

    public void onStalactiteDrip(boolean canGrow) {
        if (level != null && !level.isClientSide && level instanceof ServerLevel server) {
            BlockPos pos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, worldPosition);
            if (pos != null) {
                Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(server, pos);
                if (isEmpty()) addFluid(fluid, 5);
                else if ((fluid == Fluids.WATER && canGrow) || compareFluid(fluid)) growAmount(5);
            }
        }
    }

    public void onRaining(Fluid fluid, boolean canGrow) {
        if (level != null && getCapacity() > getAmount()) {
            float temperature = ((BiomeInvoker)(Object)level.getBiome(worldPosition).get()).getTemperature(worldPosition);

            if (!(temperature < 0.15F)) {
                if (isEmpty()) addFluid(fluid, 5);
                else if (canGrow) growAmount(5);
            }
        }
    }

    public void onVaporize() {
        FluidType type = getFluid().getFluid().getFluidType();
        if (type.isVaporizedOnPlacement(level, worldPosition, getFluid())) {
            type.onVaporize(null, level, worldPosition, getFluid());
            shrinkAmount(getCapacity());
        }
    }

    public boolean applyIngredient(ItemStack stack) {
        if (compareFluid(ModFluids.POTION.getObject()) || compareFluid(Fluids.WATER)) {
            if (compareFluid(ModFluids.POTION.getObject())) {
                if (stack.getItem() == Items.NETHER_WART) {
                    int i = PotionHelper.applyNetherWart(fluidData);

                    if (i != fluidData) {
                        fluidData = i;
                        return true;
                    }
                    return false;
                }

                if (PotionHelper.isPotionIngredient(Item.getId(stack.getItem()))) {
                    String potionEffect = PotionHelper.getPotionEffect(Item.getId(stack.getItem()));
                    int i = PotionHelper.applyIngredient(fluidData, potionEffect);

                    if (i != fluidData) {
                        fluidData = i;
                        return true;
                    }
                    return false;
                }
            } else if (compareFluid(Fluids.WATER) && stack.getItem() == Items.BLAZE_POWDER) {
                setFluid(ModFluids.POTION.getObject());
                fluidData = 16384;
                return true;
            }
        }
        return false;
    }

    public boolean fill(Fluid fluid, int amount) {
        if (getAmount() == 0) {
            fluidData = 0;
            setFluid(fluid, amount);
            return true;
        }

        if (getAmount() < getCapacity() && compareFluid(fluid)) {
            fluidData = PotionHelper.applyIngredient(fluidData, "-1-3-5-7-9-11-13");
            growAmount(amount);
            return true;
        }
        return false;
    }

    public int getPotionFluidData() {
        return fluidData;
    }

    public int getPotionFluidColor() {
        return PotionHelper.getColor(fluidData).getRGB();
    }
}
