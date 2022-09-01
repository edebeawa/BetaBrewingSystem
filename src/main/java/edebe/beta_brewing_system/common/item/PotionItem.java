package edebe.beta_brewing_system.common.item;

import edebe.beta_brewing_system.common.helper.potion.PotionHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PotionItem extends net.minecraft.item.PotionItem {

    public PotionItem(Properties builder) {
        super(builder);
    }

    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {}

    @NotNull
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        PlayerEntity playerEntity = livingEntity instanceof PlayerEntity ? (PlayerEntity)livingEntity : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!worldIn.isRemote) {
            for(EffectInstance effectinstance : PotionHelper.getPotionEffectList(stack)) {
                if (effectinstance.getPotion().isInstant()) {
                    effectinstance.getPotion().affectEntity(playerEntity, playerEntity, livingEntity, effectinstance.getAmplifier(), 1.0D);
                } else {
                    livingEntity.addPotionEffect(new EffectInstance(effectinstance));
                }
            }
        }

        if (playerEntity != null) {
            playerEntity.addStat(Stats.ITEM_USED.get(this));
            if (!playerEntity.abilities.isCreativeMode) {
                stack.shrink(1);
            }
        }

        if (playerEntity == null || !playerEntity.abilities.isCreativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    @NotNull
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return PotionHelper.getItemTranslation(stack);
    }

    @NotNull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        PotionHelper.addPotionTooltip(stack, tooltip, 1.0F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
