package edebe.beta_brewing_system.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffectCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectCategory.class)
public class MobEffectCategoryMixin {
    @Shadow @Final private ChatFormatting f_19490_;//tooltipFormatting

    @Inject(at = @At("HEAD"), method = "m_19497_", cancellable = true)
    private void getTooltipFormatting(CallbackInfoReturnable<ChatFormatting> infoReturnable) {
        infoReturnable.setReturnValue((Object)this == MobEffectCategory.NEUTRAL ? ChatFormatting.GOLD : this.f_19490_);
    }
}