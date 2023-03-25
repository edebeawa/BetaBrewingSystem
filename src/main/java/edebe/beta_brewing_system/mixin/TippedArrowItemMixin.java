package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin extends ArrowItem {
    private TippedArrowItemMixin(Properties p_40512_) {
        super(p_40512_);
        throw new IllegalStateException("This class must not be instantiated");
    }

    @Inject(at = @At("HEAD"), method = "m_5671_", cancellable = true)
    private void getDescriptionId(ItemStack stack, CallbackInfoReturnable<String> infoReturnable) {
        infoReturnable.setReturnValue(PotionHelper.isBetaPotion(stack) ? this.getDescriptionId() : PotionUtils.getPotion(stack).getName(this.getDescriptionId() + ".effect."));
    }
}