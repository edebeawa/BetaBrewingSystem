package edebe.beta_brewing_system.init;

import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {
    public static final RegisterManager<Potion> POTIONS = new RegisterManager<>("minecraft");

    public static final RegisterObject<Potion> MILK = POTIONS.add("milk", new Potion(new MobEffectInstance(ModMobEffects.CLEAR.getObject(), 1)));
}
