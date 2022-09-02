package edebe.beta_brewing_system.common.item;

import edebe.beta_brewing_system.common.helper.potion.PotionHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class TippedArrowItem extends net.minecraft.item.TippedArrowItem {
    public TippedArrowItem(Properties builder) {
        super(builder);
    }

    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {}

    @NotNull
    @Override
    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        ArrowEntity arrowEntity = new ArrowEntity(worldIn, shooter);

        for(EffectInstance effectInstance : PotionHelper.getPotionEffectList(stack)) {
            arrowEntity.addEffect(new EffectInstance(effectInstance));
        }

        ItemStack arrowItem = new ItemStack(Items.TIPPED_ARROW);
        arrowItem.getOrCreateTag().putInt("CustomPotionColor", PotionHelper.getPotionColor(stack));
        arrowEntity.setPotionEffect(arrowItem);
        return arrowEntity;
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
}
