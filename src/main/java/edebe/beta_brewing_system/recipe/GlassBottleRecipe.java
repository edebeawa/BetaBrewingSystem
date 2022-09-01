package edebe.beta_brewing_system.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static edebe.beta_brewing_system.common.Register.LINGERING_GLASS_BOTTLE_ITEM;
import static edebe.beta_brewing_system.common.Register.SPLASH_GLASS_BOTTLE_ITEM;

public class GlassBottleRecipe extends SpecialRecipe {
    public static final SpecialRecipeSerializer<GlassBottleRecipe> SERIALIZER = new SpecialRecipeSerializer<>(GlassBottleRecipe::new);

    private Item material;

    public GlassBottleRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(@Nonnull final CraftingInventory inventory, @Nonnull final World world) {
        this.material = inventory.getStackInSlot(4).getItem();
        return isGlass(inventory.getStackInSlot(3)) && isGlass(inventory.getStackInSlot(5)) && isGlass(inventory.getStackInSlot(7));
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull CraftingInventory inventory) {
        if (this.material == Items.GUNPOWDER) {
            return new ItemStack(SPLASH_GLASS_BOTTLE_ITEM, 3);
        } else if (this.material == Items.DRAGON_BREATH) {
            return new ItemStack(LINGERING_GLASS_BOTTLE_ITEM, 3);
        } else if (this.material == Items.AIR) {
            return new ItemStack(Items.GLASS_BOTTLE, 3);
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);

        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == Items.DRAGON_BREATH) {
                nonNullList.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return nonNullList;
    }

    private static boolean isGlass(ItemStack glass) {
        return !glass.isEmpty() && glass.getItem() == Items.GLASS;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
