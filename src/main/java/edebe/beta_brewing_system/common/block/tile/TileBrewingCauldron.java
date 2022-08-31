package edebe.beta_brewing_system.common.block.tile;

import edebe.beta_brewing_system.common.Register;
import edebe.beta_brewing_system.common.helper.potion.PotionHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.awt.*;

import static edebe.beta_brewing_system.common.helper.BetaBrewingSystemHelper.getItemMetadata;
import static edebe.beta_brewing_system.common.helper.ColorHelper.parseToColor;

public class TileBrewingCauldron extends TileFluidTank {
    private static final String TAG_FLUID_DATA = "fluidData";

    private int fluidData;

    public TileBrewingCauldron() {
        super(Register.BREWING_CAULDRON_TILE);
    }

    protected FluidTank getFluidTank() {
        return new FluidTank(1000, fs -> fs.getFluid() == Fluids.WATER) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                markDirty();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
            }
        };
    }

    @Override
    public void writeNBT(CompoundNBT cmp) {
        cmp.putInt(TAG_FLUID_DATA, this.fluidData);
    }

    @Override
    public void readNBT(CompoundNBT cmp) {
        this.fluidData = cmp.getInt(TAG_FLUID_DATA);
    }

    @Override
    protected Inventory createItemHandler() {
        return new Inventory(0) {
            @Override
            public int getInventoryStackLimit() {
                return 0;
            }
        };
    }

    public boolean applyIngredient(ItemStack stack) {
        if (stack.getItem() == Register.POTION_ITEM || (stack.getItem() == Items.POTION && getItemMetadata(stack) == 0 && (isPotionFluidDataZero() || getFluid().isEmpty()))) {
            if (getFluid().isEmpty()) {
                this.setAmount(250);
                this.fluidData = getItemMetadata(stack);
                return true;
            }

            if (this.fluidData == getItemMetadata(stack) && this.getAmount() < 1000) {
                this.growAmount(250);
                return true;
            }
            return false;
        }

        if (!getFluid().isEmpty() && stack.getItem() == Items.NETHER_WART) {
            int i = PotionHelper.applyNetherWart(this.fluidData);

            if (i != this.fluidData) {
                this.fluidData = i;
                return true;
            }
            return false;
        }

        if (!getFluid().isEmpty() && PotionHelper.isPotionIngredient(Item.getIdFromItem(stack.getItem()))) {
            String potionEffect = PotionHelper.getPotionEffect(Item.getIdFromItem(stack.getItem()));
            int i = PotionHelper.applyIngredient(this.fluidData, potionEffect);

            if (i != this.fluidData) {
                this.fluidData = i;
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean fillWater() {
        if (this.getAmount() == 0) {
            this.setFluid(new FluidStack(Fluids.WATER, 1000));
            this.fluidData = 0;
            return true;
        }

        if (this.getAmount() < 1000) {
            this.fluidData = PotionHelper.applyIngredient(this.fluidData, "-1-3-5-7-9-11-13");
            this.setAmount(1000);
            return true;
        }

        return false;
    }

    public boolean isPotionFluidDataZero() {
        return (this.fluidData == 0);
    }

    public int getPotionFluidData() {
        return this.fluidData;
    }

    public int getPotionFluidColor() {
        Color color = parseToColor(PotionHelper.getPotionColor(this.fluidData));
        return new Color(color.getRed(), color.getGreen(), color.getBlue()).getRGB();
    }
}
