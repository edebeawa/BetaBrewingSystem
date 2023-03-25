package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Invoker("m_5671_")
    abstract String getDescriptionId(ItemStack p_41455_);

    @Inject(at = @At("HEAD"), method = "m_7626_", cancellable = true)
    private void getName(ItemStack stack, CallbackInfoReturnable<Component> infoReturnable) {
        infoReturnable.setReturnValue(PotionHelper.isBetaPotion(stack) ? PotionHelper.getItemTranslation(stack) : Component.translatable(this.getDescriptionId(stack)));
    }
}
