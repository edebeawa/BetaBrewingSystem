package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.extensions.IForgePotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IForgePotion.class)
public interface IForgePotionMixin {
    @Inject(at = @At("HEAD"), method = "isFoil", cancellable = true)
    private void isFoil(ItemStack stack, CallbackInfoReturnable<Boolean> infoReturnable) {
        infoReturnable.setReturnValue(!PotionUtils.getMobEffects(stack).isEmpty() || PotionHelper.getPotionFluid(PotionUtils.getPotion(stack)) == Fluids.EMPTY);
    }
}