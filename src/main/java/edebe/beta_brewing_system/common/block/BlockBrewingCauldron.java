package edebe.beta_brewing_system.common.block;


import edebe.beta_brewing_system.common.Register;
import edebe.beta_brewing_system.common.block.tile.TileBrewingCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
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
        if (brewingCauldron == null)
            return ActionResultType.PASS;

        ItemStack heldItem = player.getHeldItem(hand);

        if (heldItem.getItem() == Items.WATER_BUCKET) {
            if (brewingCauldron.fillWater(1000)) {
                if (!player.abilities.isCreativeMode) {
                    if (heldItem.getCount() > 1) {
                        heldItem.shrink(1);
                        player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                    } else if (heldItem.getCount() == 1) {
                        player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                    }
                }

                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        } else if (isWaterBottle(heldItem)) {
            if (brewingCauldron.fillWater(300)) {
                if (!player.abilities.isCreativeMode) {
                    Item potionItem;

                    if (heldItem.getItem() == Items.POTION) {
                        potionItem = Items.GLASS_BOTTLE;
                    } else if (heldItem.getItem() == Items.SPLASH_POTION) {
                        potionItem = Register.SPLASH_GLASS_BOTTLE_ITEM;
                    } else {
                        potionItem = Register.LINGERING_GLASS_BOTTLE_ITEM;
                    }

                    if (heldItem.getCount() > 1) {
                        if (!player.addItemStackToInventory(new ItemStack(potionItem))) {
                            world.addEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, new ItemStack(potionItem)));
                        }
                    } else if (heldItem.getCount() > 0) {
                        player.setHeldItem(hand, new ItemStack(potionItem));
                    }

                    heldItem.shrink(1);
                }

                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }
        } else if (brewingCauldron.getFluid().getFluid() == Fluids.WATER) {
            if (heldItem.getItem() == Items.BUCKET) {
                if (brewingCauldron.getAmount() >= 1000) {
                    brewingCauldron.shrinkAmount(1000);

                    if (!player.abilities.isCreativeMode) {
                        if (heldItem.getCount() > 1) {
                            heldItem.shrink(1);
                            player.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET));
                        } else if (heldItem.getCount() == 1) {
                            player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                        }
                    }

                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            } else if (heldItem.getItem() == Items.GLASS_BOTTLE || heldItem.getItem() == Register.SPLASH_GLASS_BOTTLE_ITEM || heldItem.getItem() == Register.LINGERING_GLASS_BOTTLE_ITEM) {
                if (brewingCauldron.getAmount() >= 300) {
                    if (!player.abilities.isCreativeMode) {
                        Item potionItem;

                        if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                            potionItem = Items.POTION;
                        } else if (heldItem.getItem() == Register.SPLASH_GLASS_BOTTLE_ITEM) {
                            potionItem = Items.SPLASH_POTION;
                        } else {
                            potionItem = Items.LINGERING_POTION;
                        }
                        addItemStack(PotionUtils.addPotionToItemStack(new ItemStack(potionItem), Potions.WATER), 1, heldItem, brewingCauldron, player, world, pos, hand);
                    }

                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
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
                if (addItemStack(potionItemStack, 1, heldItem, brewingCauldron, player, world, pos, hand)) {
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResultType.SUCCESS;
                } else {
                    return ActionResultType.PASS;
                }
            }
        } else if (heldItem.getItem() == Items.ARROW) {
            if (brewingCauldron.getFluid().getFluid() == Register.POTION_FLUID) {
                int arrowCount;
                ItemStack potionArrowStack = new ItemStack(Register.TIPPED_ARROW_ITEM);

                if (heldItem.getCount() >= 64 && brewingCauldron.getAmount() >= 900) {
                    if (brewingCauldron.getAmount() >= 900) {
                        arrowCount = heldItem.getCount();

                        if (!player.abilities.isCreativeMode) {
                            brewingCauldron.shrinkAmount(600);
                        }
                    } else if (brewingCauldron.getAmount() >= 600) {
                        arrowCount = 32;

                        if (!player.abilities.isCreativeMode) {
                            brewingCauldron.shrinkAmount(300);
                        }
                    } else if (brewingCauldron.getAmount() >= 300) {
                        arrowCount = 16;
                    } else {
                        arrowCount = 0;
                    }
                } else if (heldItem.getCount() >= 32 && brewingCauldron.getAmount() >= 900) {
                    if (brewingCauldron.getAmount() >= 900) {
                        arrowCount = heldItem.getCount();

                        if (!player.abilities.isCreativeMode) {
                            brewingCauldron.shrinkAmount(600);
                        }
                    } else if (brewingCauldron.getAmount() >= 600) {
                        arrowCount = 32;

                        if (!player.abilities.isCreativeMode) {
                            brewingCauldron.shrinkAmount(300);
                        }
                    } else if (brewingCauldron.getAmount() >= 300) {
                        arrowCount = 16;
                    } else {
                        arrowCount = 0;
                    }
                } else if (heldItem.getCount() >= 16 && brewingCauldron.getAmount() >= 600) {
                    if (brewingCauldron.getAmount() >= 600) {
                        arrowCount = heldItem.getCount();

                        if (!player.abilities.isCreativeMode) {
                            brewingCauldron.shrinkAmount(300);
                        }
                    } else if (brewingCauldron.getAmount() >= 300) {
                        arrowCount = 16;
                    } else {
                        arrowCount = 0;
                    }
                } else {
                    arrowCount = heldItem.getCount();
                }

                if (arrowCount > 0) {
                    potionArrowStack.setCount(arrowCount);
                    setItemMetadata(potionArrowStack, brewingCauldron.getPotionFluidData());
                    if (addItemStack(potionArrowStack, arrowCount, heldItem, brewingCauldron, player, world, pos, hand)) {
                        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return ActionResultType.SUCCESS;
                    } else {
                        return ActionResultType.PASS;
                    }
                } else {
                    return ActionResultType.PASS;
                }
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

            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
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

    private static boolean isWaterBottle(ItemStack bottleIn) {
        return (bottleIn.getItem() == Items.POTION && PotionUtils.getPotionFromItem(bottleIn) == Potions.WATER) || (bottleIn.getItem() == Items.SPLASH_POTION && PotionUtils.getPotionFromItem(bottleIn) == Potions.WATER) || (bottleIn.getItem() == Items.LINGERING_POTION && PotionUtils.getPotionFromItem(bottleIn) == Potions.WATER);
    }

    private static boolean addItemStack(ItemStack itemIn, int shrinkCount, ItemStack heldItem, TileBrewingCauldron tileIn, PlayerEntity playerIn, World worldIn, BlockPos pos, Hand handIn) {
        if (tileIn.getAmount() >= 300) {
            if (heldItem.getCount() > shrinkCount || playerIn.abilities.isCreativeMode) {
                if (!playerIn.addItemStackToInventory(itemIn)) {
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemIn));
                }
            } else if (heldItem.getCount() == shrinkCount) {
                playerIn.setHeldItem(handIn, itemIn);
            }

            if (!playerIn.abilities.isCreativeMode) {
                heldItem.shrink(shrinkCount);
                tileIn.shrinkAmount(300);
            }
            return true;
        }
        return false;
    }
}