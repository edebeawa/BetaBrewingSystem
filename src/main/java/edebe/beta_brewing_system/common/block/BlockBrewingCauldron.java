package edebe.beta_brewing_system.common.block;


import edebe.beta_brewing_system.common.Register;
import edebe.beta_brewing_system.common.block.tile.TileBrewingCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

import static edebe.beta_brewing_system.common.helper.BetaBrewingSystemHelper.setItemMetadata;

@ParametersAreNonnullByDefault
public class BlockBrewingCauldron extends Block implements ITileEntityProvider {
    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);

    public BlockBrewingCauldron(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @NotNull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult resultIn) {
        if (world.isRemote)
            world.notifyBlockUpdate(pos, state, state, 3);

        TileBrewingCauldron brewingCauldron = (TileBrewingCauldron) world.getTileEntity(pos);
        ItemStack heldItem = player.getHeldItem(hand);

        int cauldronMetadata = brewingCauldron.getPotionFluidData();

        if (heldItem.getItem() instanceof BucketItem && ((BucketItem) heldItem.getItem()).getFluid() == Fluids.WATER) {
            if (brewingCauldron.fillWater()) {
                if (!player.abilities.isCreativeMode) {
                    heldItem.shrink(1);
                    player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                if (!brewingCauldron.isPotionFluidDataZero() && cauldronMetadata != brewingCauldron.getPotionFluidData()) {
                    world.notifyBlockUpdate(pos, state, state, 3);
                }
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }

        if (heldItem.getItem() == Items.GLASS_BOTTLE || heldItem.getItem() == Register.SPLASH_GLASS_BOTTLE_ITEM || heldItem.getItem() == Register.LINGERING_GLASS_BOTTLE_ITEM) {
            if (brewingCauldron.getFluid().getFluid() == Register.POTION_FLUID) {
                Item potionItem;

                if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                    potionItem = Register.POTION_ITEM;
                } else if (heldItem.getItem() == Register.SPLASH_GLASS_BOTTLE_ITEM) {
                    potionItem = Register.SPLASH_POTION_ITEM;
                } else {
                    potionItem = Register.LINGERING_POTION_ITEM;
                }

                ItemStack potionItemStack = new ItemStack(potionItem, 1);
                setItemMetadata(potionItemStack, brewingCauldron.getPotionFluidData());

                addItemStack(potionItemStack, 1, heldItem, brewingCauldron, player, world, pos, hand);
            }
        } else if (heldItem.getItem() == Items.ARROW) {
            if (brewingCauldron.getFluid().getFluid() == Register.POTION_FLUID) {
                int arrowCount;
                ItemStack potionArrowStack = new ItemStack(Register.TIPPED_ARROW_ITEM);

                if (heldItem.getCount() >= 64) {
                    switch (brewingCauldron.getAmount()) {
                        case 1000:
                            arrowCount = heldItem.getCount();
                            break;
                        case 750:
                            arrowCount = 32;
                            break;
                        case 500:
                            arrowCount = 16;
                            break;
                        default:
                            arrowCount = 8;
                    }
                } else if (heldItem.getCount() >= 32) {
                    switch (brewingCauldron.getAmount()) {
                        case 1000:
                            arrowCount = heldItem.getCount();
                            break;
                        case 750:
                            arrowCount = 32;
                            break;
                        case 500:
                            arrowCount = 16;
                            break;
                        default:
                            arrowCount = 8;
                    }
                } else if (heldItem.getCount() >= 16) {
                    switch (brewingCauldron.getAmount()) {
                        case 1000:
                        case 750:
                            arrowCount = heldItem.getCount();
                            break;
                        case 500:
                            arrowCount = 16;
                            break;
                        default:
                            arrowCount = 8;
                    }
                } else {
                    arrowCount = heldItem.getCount();
                }

                potionArrowStack.setCount(arrowCount);
                setItemMetadata(potionArrowStack, brewingCauldron.getPotionFluidData());

                addItemStack(potionArrowStack, arrowCount, heldItem, brewingCauldron, player, world, pos, hand);
            }
        } else if (brewingCauldron.applyIngredient(heldItem)) {
            if (heldItem.getItem() == Register.POTION_ITEM) {
                if (!player.abilities.isCreativeMode) {
                    heldItem.shrink(1);
                }

                if (heldItem.getCount() <= 0) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
                player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            } else {
                if (!player.abilities.isCreativeMode) {
                    heldItem.shrink(1);
                }

                if (heldItem.getCount() <= 0) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
            }

            if (brewingCauldron.getFluid().getFluid() == Fluids.WATER)
                brewingCauldron.setFluid(new FluidStack(Register.POTION_FLUID, brewingCauldron.getAmount()));

            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.notifyBlockUpdate(pos, state, state, 3);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @NotNull
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this));
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return new TileBrewingCauldron();
    }

    private static void addItemStack(ItemStack stackIn, int shrinkCount, ItemStack heldItem, TileBrewingCauldron tileIn, PlayerEntity playerIn, World worldIn, BlockPos posIn, Hand handIn) {
        if (heldItem.getCount() > 1 || playerIn.abilities.isCreativeMode) {
            if (!playerIn.addItemStackToInventory(stackIn)) {
                worldIn.addEntity(new ItemEntity(worldIn, posIn.getX() + 0.5D, posIn.getY() + 1.5D, posIn.getZ() + 0.5D, stackIn));
            }
        } else if (heldItem.getCount() > 0) {
            playerIn.setHeldItem(handIn, stackIn);
        }

        if (!playerIn.abilities.isCreativeMode) {
            heldItem.shrink(shrinkCount);
            tileIn.shrinkAmount(250);
        }

        worldIn.playSound(null, posIn, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}