package edebe.beta_brewing_system.mixin;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeMod.class)
public class ForgeModMixin {
    @Inject(at = @At("HEAD"), method = "registerFluids", cancellable = true)
    private void registerFluids(RegisterEvent event, CallbackInfo info) {
        info.cancel();
    }
}
