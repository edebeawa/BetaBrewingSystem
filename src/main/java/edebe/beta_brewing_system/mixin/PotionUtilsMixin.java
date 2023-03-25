package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionUtils.class)
public class PotionUtilsMixin {
    @Inject(at = @At("HEAD"), method = "m_43547_", cancellable = true)
    private static void getMobEffects(ItemStack stack, CallbackInfoReturnable<List<MobEffectInstance>> infoReturnable) {
        infoReturnable.setReturnValue(PotionHelper.isBetaPotion(stack) ? PotionHelper.getMobEffects(stack) : PotionUtils.getAllEffects(stack.getTag()));
    }

    @Inject(at = @At("HEAD"), method = "m_43571_ (Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", cancellable = true)
    private static void getCustomEffects(ItemStack stack, CallbackInfoReturnable<List<MobEffectInstance>> infoReturnable) {
        infoReturnable.setReturnValue(PotionHelper.isBetaPotion(stack) ? PotionHelper.getMobEffects(stack) : PotionUtils.getCustomEffects(stack.getTag()));
    }

    @Inject(at = @At("HEAD"), method = "m_43575_ (Lnet/minecraft/world/item/ItemStack;)I", cancellable = true)
    private static void getColor(ItemStack stack, CallbackInfoReturnable<Integer> infoReturnable) {
        infoReturnable.cancel();
        if (PotionHelper.isBetaPotion(stack)) {
            infoReturnable.setReturnValue(PotionHelper.getColor(stack));
        } else {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("CustomPotionColor", 99))
                infoReturnable.setReturnValue(tag.getInt("CustomPotionColor"));
            else infoReturnable.setReturnValue(PotionUtils.getPotion(stack) == Potions.EMPTY ? 16253176 : PotionUtils.getColor(PotionUtils.getMobEffects(stack)));
        }
    }


}
