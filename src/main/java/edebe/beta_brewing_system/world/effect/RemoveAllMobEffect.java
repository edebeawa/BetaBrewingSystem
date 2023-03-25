package edebe.beta_brewing_system.world.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RemoveAllMobEffect extends InstantenousMobEffect {
    public RemoveAllMobEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, @NotNull LivingEntity entity, int amplifier, double health) {
        entity.removeAllEffects();
    }
}
