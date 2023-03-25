package edebe.beta_brewing_system.world.block.entity;

import edebe.beta_brewing_system.helper.PotionHelper;
import edebe.beta_brewing_system.init.ModBlockEntities;
import edebe.beta_brewing_system.init.ModFluids;
import edebe.beta_brewing_system.mixin.BiomeInvoker;
import edebe.beta_brewing_system.world.block.CauldronBlock;
import edebe.doglib.api.world.block.block_entity.SimpleFluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class CauldronBlockEntity extends SimpleFluidTankBlockEntity {
    private static final String TAG_FLUID_METADATA = "Metadata";
    private int fluidData;

    public CauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAULDRON.getObject(), pos, state);
    }

    protected FluidTank createFluidTank() {
        return new FluidTank(FluidType.BUCKET_VOLUME, stack -> {
            if ((isEmpty() || compareFluid(stack.getFluid()))) {
                if (stack.hasTag()) {
                    getOrCreateFluid(stack).setTag(stack.getTag());
                    return !getFluid().hasTag() || stack.getTag().equals(getFluid().getTag());
                }
                return true;
            }
            return false;
        }) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                notifyUpdate();
            }
        };
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        if (fluidData == 0) {
            if (getFluid().hasTag()) getFluid().getTag().remove(TAG_FLUID_METADATA);
        } else {
            if (compareFluid(ModFluids.POTION.getObject()) && PotionHelper.isBetaPotion(getFluid()))
                getFluid().getOrCreateTag().putInt(TAG_FLUID_METADATA, fluidData);
        }
    }

    @Override
    public void readNBT(CompoundTag tag) {
        if (compareFluid(ModFluids.POTION.getObject()) && PotionHelper.isBetaPotion(getFluid()))
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
        if (ModList.get().isLoaded("create") && blockEntity.getFluid().getFluid() == ForgeRegistries.FLUIDS.getValue(new ResourceLocation("create", "potion"))) {
            FluidStack stack = new FluidStack(ModFluids.POTION.getObject(), blockEntity.getAmount());
            if (blockEntity.getFluid().hasTag())
                stack.setTag(blockEntity.getFluid().getTag());
            blockEntity.setFluid(stack);
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
        if ((compareFluid(ModFluids.POTION.getObject()) && PotionHelper.isBetaPotion(getFluid())) || compareFluid(Fluids.WATER)) {
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
                    String ingredient = PotionHelper.getPotionIngredient(Item.getId(stack.getItem()));
                    int i = PotionHelper.applyIngredient(fluidData, ingredient);

                    if (i != fluidData) {
                        fluidData = i;
                        return true;
                    }
                    return false;
                }
            } else if (compareFluid(Fluids.WATER) && stack.getItem() == Items.BLAZE_POWDER) {
                FluidStack fluid = PotionHelper.newBetaPotionFluid(getAmount());
                PotionHelper.setFluidMetadata(fluid, 16384);
                setFluid(fluid);
                updatePotionFluidData(fluid);
                return true;
            }
        }
        return false;
    }

    public boolean fill(FluidStack stack) {
        if (getAmount() == 0) {
            fluidData = PotionHelper.getFluidMetadata(stack);
            setFluid(stack);
            return true;
        }

        if (getAmount() < getCapacity() && compareFluid(stack.getFluid()) && (stack.getFluid() != ModFluids.POTION.getObject() || stack.getTag().equals(getFluid().getTag()))) {
            fluidData = PotionHelper.applyIngredient(fluidData, "-1-3-5-7-9-11-13");
            growAmount(stack.getAmount());
            return true;
        }
        return false;
    }

    public void updatePotionFluidData(FluidStack stack) {
        fluidData = PotionHelper.getFluidMetadata(stack);
    }

    public int getPotionFluidData() {
        return fluidData;
    }
}