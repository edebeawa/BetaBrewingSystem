package edebe.beta_brewing_system.world.item;

import edebe.beta_brewing_system.init.ModFluids;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MilkBucketItem extends BucketItem {
    private static final int DRINK_DURATION = 32;

    public MilkBucketItem(Properties properties) {
        super(ModFluids.MILK::getObject, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (entity.isShiftKeyDown()) {
            return super.finishUsingItem(stack, world, entity);
        } else {
            if (!world.isClientSide) entity.curePotionEffects(stack);
            if (entity instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
                serverplayer.awardStat(Stats.ITEM_USED.get(this));
            }

            if (entity instanceof Player && !((Player) entity).getAbilities().instabuild) {
                stack.shrink(1);
            }

            return stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        return player.isShiftKeyDown() ? super.use(world, player, hand) : ItemUtils.startUsingInstantly(world, player, hand);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @org.jetbrains.annotations.Nullable net.minecraft.nbt.CompoundTag nbt) {
        return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
    }
}
