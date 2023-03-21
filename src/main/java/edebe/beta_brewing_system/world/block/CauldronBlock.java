package edebe.beta_brewing_system.world.block;

import edebe.beta_brewing_system.helper.PotionHelper;
import edebe.beta_brewing_system.init.ModBlockEntities;
import edebe.beta_brewing_system.init.ModFluids;
import edebe.beta_brewing_system.init.ModItems;
import edebe.beta_brewing_system.init.ModPotions;
import edebe.beta_brewing_system.world.block.entity.CauldronBlockEntity;
import edebe.doglib.api.helper.BlockHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CauldronBlock extends AbstractCauldronBlock implements EntityBlock, IForgeBlock {
    public static final int AMOUNT = 250;

    public CauldronBlock(Properties properties) {
        super(properties, CauldronInteraction.EMPTY);
    }

    @Override
    public boolean isFull(BlockState state) {
        return false;
    }

    @NotNull
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide)
            world.sendBlockUpdated(pos, state, state, 3);

        CauldronBlockEntity blockEntity = (CauldronBlockEntity) world.getBlockEntity(pos);
        if (blockEntity == null) return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() instanceof BucketItem bucket) {
            if (bucket.getFluid() != Fluids.EMPTY) {
                if (blockEntity.fill(bucket.getFluid(), AMOUNT * 4)) {
                    if (heldItem.getItem() instanceof MobBucketItem mobBucket) mobBucket.checkExtraContent(player, world, heldItem, pos);
                    SoundEvent sound = bucket.getFluid().getFluidType().getSound(SoundActions.BUCKET_EMPTY);
                    if (!player.getAbilities().instabuild) if (heldItem.getCount() > 1) {
                        heldItem.shrink(1);
                        player.addItem(new ItemStack(Items.BUCKET));
                    } else if (heldItem.getCount() == 1) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                    world.playSound(null, pos, sound == null ? SoundEvents.BUCKET_EMPTY : sound, SoundSource.BLOCKS, 1, 1);
                    return InteractionResult.SUCCESS;
                }
            } else if (!blockEntity.compareFluid(ModFluids.POTION.getObject())) {
                Item bucketItem;
                if (blockEntity.compareFluid(Fluids.WATER)) bucketItem = Items.WATER_BUCKET;
                else if (blockEntity.compareFluid(Fluids.LAVA)) bucketItem = Items.LAVA_BUCKET;
                else bucketItem = blockEntity.getFluid().getFluid().getBucket();
                if (bucketItem != Items.AIR && blockEntity.getAmount() >= AMOUNT * 4) {
                    SoundEvent sound = blockEntity.getFluid().getFluid().getFluidType().getSound(SoundActions.BUCKET_FILL);
                    blockEntity.shrinkAmount(AMOUNT * 4);
                    if (!player.getAbilities().instabuild) if (heldItem.getCount() > 1) {
                        heldItem.shrink(1);
                        player.addItem(new ItemStack(bucketItem));
                    } else if (heldItem.getCount() == 1) player.setItemInHand(hand, new ItemStack(bucketItem));
                    world.playSound(null, pos, sound == null ? SoundEvents.BUCKET_FILL : sound, SoundSource.BLOCKS, 1, 1);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        } else if (PotionHelper.getPotionFluid(PotionUtils.getPotion(heldItem)) != Fluids.EMPTY) {
            if (blockEntity.fill(PotionHelper.getPotionFluid(PotionUtils.getPotion(heldItem)), AMOUNT)) {
                if (!player.getAbilities().instabuild) {
                    Item potionItem;
                    if (heldItem.getItem() == Items.POTION)
                        potionItem = Items.GLASS_BOTTLE;
                    else if (heldItem.getItem() == Items.SPLASH_POTION)
                        potionItem = ModItems.SPLASH_GLASS_BOTTLE.getObject();
                    else
                        potionItem = ModItems.LINGERING_GLASS_BOTTLE.getObject();
                    if (heldItem.getCount() > 1) {
                        if (!player.addItem(new ItemStack(potionItem)))
                            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, new ItemStack(potionItem)));
                    } else if (heldItem.getCount() > 0) player.setItemInHand(hand, new ItemStack(potionItem));

                    heldItem.shrink(1);
                }

                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1, 1);
                return InteractionResult.SUCCESS;
            }
        }

        if (heldItem.getItem() == Items.GLASS_BOTTLE || heldItem.getItem() == ModItems.SPLASH_GLASS_BOTTLE.getObject() || heldItem.getItem() == ModItems.LINGERING_GLASS_BOTTLE.getObject()) {
            if (blockEntity.compareFluid(ModFluids.POTION.getObject())) {
                Item potionItem;
                if (heldItem.getItem() == Items.GLASS_BOTTLE)
                    potionItem = Items.POTION;
                else if (heldItem.getItem() == ModItems.SPLASH_GLASS_BOTTLE.getObject())
                    potionItem = Items.SPLASH_POTION;
                else
                    potionItem = Items.LINGERING_POTION;
                ItemStack potionItemStack = PotionHelper.newBetaPotion(potionItem);
                PotionHelper.setItemMetadata(potionItemStack, blockEntity.getPotionFluidData());
                if (addItemStack(blockEntity, potionItemStack, 1, heldItem, player, world, pos, hand)) {
                    world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1, 1);
                    return InteractionResult.SUCCESS;
                }
            } else if (PotionHelper.getFluidPotion(blockEntity.getFluid().getFluid()) != Potions.EMPTY) {
                if (addPotionItem(blockEntity, PotionHelper.getFluidPotion(blockEntity.getFluid().getFluid()), heldItem, player, world, pos, hand)) return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else if (heldItem.getItem() == Items.ARROW) {
            if (blockEntity.compareFluid(ModFluids.POTION.getObject())) {
                ItemStack potionArrowStack = PotionHelper.newBetaPotion(Items.TIPPED_ARROW);
                int arrowCount;
                if (heldItem.getCount() >= 64 && blockEntity.getAmount() >= AMOUNT * 4) {
                    if (blockEntity.getAmount() >= AMOUNT * 4) {
                        arrowCount = heldItem.getCount();
                        if (!player.getAbilities().instabuild) blockEntity.shrinkAmount(AMOUNT * 3);
                    } else if (blockEntity.getAmount() >= AMOUNT * 3) {
                        arrowCount = 32;
                        if (!player.getAbilities().instabuild) blockEntity.shrinkAmount(AMOUNT * 2);
                    } else if (blockEntity.getAmount() >= AMOUNT * 2) arrowCount = 16;
                    else arrowCount = 0;
                } else if (heldItem.getCount() >= 32 && blockEntity.getAmount() >= AMOUNT * 4) {
                    if (blockEntity.getAmount() >= AMOUNT * 4) {
                        arrowCount = heldItem.getCount();
                        if (!player.getAbilities().instabuild) blockEntity.shrinkAmount(AMOUNT * 3);
                    } else if (blockEntity.getAmount() >= AMOUNT * 3) {
                        arrowCount = 32;
                        if (!player.getAbilities().instabuild) blockEntity.shrinkAmount(AMOUNT * 2);
                    } else if (blockEntity.getAmount() >= AMOUNT * 2) arrowCount = 16;
                    else arrowCount = 0;
                } else if (heldItem.getCount() >= 16 && blockEntity.getAmount() >= AMOUNT * 3) {
                    if (blockEntity.getAmount() >= AMOUNT * 3) {
                        arrowCount = heldItem.getCount();
                        if (!player.getAbilities().instabuild) blockEntity.shrinkAmount(AMOUNT * 2);
                    } else if (blockEntity.getAmount() >= AMOUNT * 2) arrowCount = 16;
                    else arrowCount = 0;
                } else arrowCount = heldItem.getCount();
                if (arrowCount > 0) {
                    potionArrowStack.setCount(arrowCount);
                    PotionHelper.setItemMetadata(potionArrowStack, blockEntity.getPotionFluidData());
                    if (addItemStack(blockEntity, potionArrowStack, arrowCount, heldItem, player, world, pos, hand)) {
                        world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1);
                        return InteractionResult.SUCCESS;
                    } else return InteractionResult.PASS;
                } else return InteractionResult.PASS;
            }
        } else if (blockEntity.applyIngredient(heldItem)) {
            if (heldItem.getItem() == Items.POTION) {
                if (!player.getAbilities().instabuild)
                    heldItem.shrink(1);
                if (heldItem.getCount() <= 0)
                    player.setItemInHand(hand, ItemStack.EMPTY);
                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            } else {
                if (!player.getAbilities().instabuild)
                    heldItem.shrink(1);
                if (heldItem.getCount() <= 0)
                    player.setItemInHand(hand, ItemStack.EMPTY);
            }

            world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        CauldronBlockEntity blockEntity = (CauldronBlockEntity) world.getBlockEntity(pos);
        if (blockEntity != null) {
            int signal = (int) ((blockEntity.getAmount() / (float) blockEntity.getCapacity()) * 15);
            return signal == 0 && blockEntity.getAmount() > 0 ? 1 : signal;
        }
        return 0;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        CauldronBlockEntity blockEntity = (CauldronBlockEntity) world.getBlockEntity(pos);
        if (blockEntity != null) {
            FluidStack stack = blockEntity.getFluid();
            return stack.getFluid().getFluidType().getLightLevel(stack);
        }
        return 0;
    }

    @NotNull
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return BlockHelper.defaultDrop(super.getDrops(state, builder), this);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CauldronBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return BlockHelper.createTicker(type, ModBlockEntities.CAULDRON.getObject(), CauldronBlockEntity::tick);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return BlockHelper.getMenuProvider(state, world, pos);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
        super.triggerEvent(state, world, pos, id, param);
        return BlockHelper.triggerEvent(state, world, pos, id, param);
    }

    private static boolean addPotionItem(CauldronBlockEntity blockEntity, Potion potion, ItemStack heldItem, Player player, Level world, BlockPos pos, InteractionHand hand) {
        if (blockEntity.getAmount() >= AMOUNT) {
            Item potionItem;
            if (heldItem.getItem() == Items.GLASS_BOTTLE)
                potionItem = Items.POTION;
            else if (heldItem.getItem() == ModItems.SPLASH_GLASS_BOTTLE.getObject())
                potionItem = Items.SPLASH_POTION;
            else
                potionItem = Items.LINGERING_POTION;
            boolean result = addItemStack(blockEntity, PotionUtils.setPotion(new ItemStack(potionItem), potion), 1, heldItem, player, world, pos, hand);
            world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1, 1);
            return result;
        }
        return false;
    }

    private static boolean addItemStack(CauldronBlockEntity blockEntity, ItemStack stack, int shrinkCount, ItemStack heldItem, Player player, Level world, BlockPos pos, InteractionHand hand) {
        if (blockEntity.getAmount() >= AMOUNT) {
            if (heldItem.getCount() > shrinkCount || player.getAbilities().instabuild) {
                if (!player.addItem(stack))
                    world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, stack));
            } else if (heldItem.getCount() == shrinkCount) player.setItemInHand(hand, stack);
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(shrinkCount);
                blockEntity.shrinkAmount(AMOUNT);
            }
            return true;
        }
        return false;
    }
}