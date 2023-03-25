package edebe.beta_brewing_system.world.item;

import edebe.beta_brewing_system.helper.PotionHelper;
import edebe.beta_brewing_system.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ModBottleItem extends BottleItem {
    public ModBottleItem(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        List<AreaEffectCloud> list = world.getEntitiesOfClass(AreaEffectCloud.class, player.getBoundingBox().inflate(2.0D), (p_40650_) -> p_40650_ != null && p_40650_.isAlive() && p_40650_.getOwner() instanceof EnderDragon);
        ItemStack heldItem = player.getItemInHand(hand);
        if (!list.isEmpty()) {
            AreaEffectCloud areaeffectcloud = list.get(0);
            areaeffectcloud.setRadius(areaeffectcloud.getRadius() - 0.5F);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1, 1);
            world.gameEvent(player, GameEvent.FLUID_PICKUP, player.position());
            return InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(heldItem, player, new ItemStack(Items.DRAGON_BREATH)), world.isClientSide());
        } else {
            HitResult hit = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
            //noinspection IfStatementWithIdenticalBranches
            if (hit.getType() == HitResult.Type.MISS) {
                return InteractionResultHolder.pass(heldItem);
            } else {
                if (hit.getType() == HitResult.Type.BLOCK) {
                    //noinspection CastCanBeRemovedNarrowingVariableType
                    BlockPos pos = ((BlockHitResult)hit).getBlockPos();
                    if (!world.mayInteract(player, pos))
                        return InteractionResultHolder.pass(heldItem);

                    Potion potion = PotionHelper.getFluidPotion(world.getFluidState(pos).getType());
                    if (potion != Potions.EMPTY) {
                        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        world.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                        Item potionItem;
                        if (heldItem.getItem() == Items.GLASS_BOTTLE)
                            potionItem = Items.POTION;
                        else if (heldItem.getItem() == ModItems.SPLASH_GLASS_BOTTLE.getObject())
                            potionItem = Items.SPLASH_POTION;
                        else if (heldItem.getItem() == ModItems.LINGERING_GLASS_BOTTLE.getObject())
                            potionItem = Items.LINGERING_POTION;
                        else return InteractionResultHolder.pass(heldItem);
                        return InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(heldItem, player, PotionUtils.setPotion(new ItemStack(potionItem), potion)), world.isClientSide());
                    }
                }
                return InteractionResultHolder.pass(heldItem);
            }

        }
    }
}
