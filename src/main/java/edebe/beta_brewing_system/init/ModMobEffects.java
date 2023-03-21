package edebe.beta_brewing_system.init;

import edebe.beta_brewing_system.world.effect.RemoveAllMobEffect;
import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraft.world.effect.MobEffect;

public class ModMobEffects {
    public static final RegisterManager<MobEffect> MOB_EFFECTS = new RegisterManager<>("minecraft");

    public static final RegisterObject<MobEffect> CLEAR = MOB_EFFECTS.add("clear", new RemoveAllMobEffect());
}