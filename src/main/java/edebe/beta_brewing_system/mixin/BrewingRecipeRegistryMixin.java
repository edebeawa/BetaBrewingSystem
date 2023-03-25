package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {
    @Invoker("getOutput")
    private static ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "hasOutput", cancellable = true)
    private static void hasOutput(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> infoReturnable) {
        infoReturnable.setReturnValue(!PotionHelper.isBetaPotion(input) && !getOutput(input, ingredient).isEmpty());
    }
}