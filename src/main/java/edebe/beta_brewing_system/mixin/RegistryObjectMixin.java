package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.init.ModFluidTypes;
import edebe.beta_brewing_system.init.ModFluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(RegistryObject.class)
public class RegistryObjectMixin {
    @Shadow private @Nullable Object value;
    @Shadow @Final private @Nullable ResourceLocation name;

    @Inject(at = @At("HEAD"), method = "get", cancellable = true)
    public void get(CallbackInfoReturnable<Object> infoReturnable) {
        infoReturnable.cancel();
        RegistryObject registryObject = (RegistryObject) (Object) this;
        if (registryObject.equals(ForgeMod.MILK_TYPE) || registryObject.equals(ForgeMod.MILK) || registryObject.equals(ForgeMod.FLOWING_MILK)) {
            if (registryObject.equals(ForgeMod.MILK_TYPE))
                infoReturnable.setReturnValue(ModFluidTypes.MILK.getObject());
            else if (registryObject.equals(ForgeMod.MILK))
                infoReturnable.setReturnValue(ModFluids.MILK.getObject());
            else if (registryObject.equals(ForgeMod.FLOWING_MILK))
                infoReturnable.setReturnValue(ModFluids.FLOWING_MILK.getObject());
        } else {
            Object ret = this.value;
            Objects.requireNonNull(ret, () -> "Registry Object not present: " + this.name);
            infoReturnable.setReturnValue(ret);
        }
    }
}
