package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {
    private PotionItemMixin(Properties p_41383_) {
        super(p_41383_);
        throw new IllegalStateException("This class must not be instantiated");
    }

    @Inject(at = @At("HEAD"), method = "m_5671_", cancellable = true)
    private void getDescriptionId(ItemStack stack, CallbackInfoReturnable<String> infoReturnable) {
        infoReturnable.setReturnValue(PotionHelper.isBetaPotion(stack) ? this.getDescriptionId() + ".beta" : PotionUtils.getPotion(stack).getName(this.getDescriptionId() + ".effect."));
    }

    @Inject(at = @At("HEAD"), method = "m_5812_", cancellable = true)
    private void isFoil(ItemStack stack, CallbackInfoReturnable<Boolean> infoReturnable) {
        infoReturnable.setReturnValue(PotionHelper.getPotionFluid(PotionUtils.getPotion(stack)) == Fluids.EMPTY && (PotionHelper.isBetaPotion(stack) || super.isFoil(stack) || PotionUtils.getPotion(stack).isFoil(stack)));
    }
}