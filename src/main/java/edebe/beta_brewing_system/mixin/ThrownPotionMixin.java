package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin extends ThrowableItemProjectile {
    private ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
        throw new IllegalStateException("This class must not be instantiated");
    }

    @Inject(at = @At("HEAD"), method = "m_37537_", cancellable = true)
    private void makeAreaOfEffectCloud(ItemStack stack, Potion potion, CallbackInfo info) {
        info.cancel();
        AreaEffectCloud effectCloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity living)
            effectCloud.setOwner(living);
        effectCloud.setRadius(3.0F);
        effectCloud.setRadiusOnUse(-0.5F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(-effectCloud.getRadius() / (float)effectCloud.getDuration());
        effectCloud.setPotion(potion);
        for(MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(stack))
            effectCloud.addEffect(new MobEffectInstance(mobeffectinstance));
        CompoundTag tag = stack.getTag();
        if (PotionHelper.isBetaPotion(stack)) {
            effectCloud.setFixedColor(PotionHelper.getColor(stack));
        } else {
            if (tag != null && tag.contains("CustomPotionColor", 99))
                effectCloud.setFixedColor(tag.getInt("CustomPotionColor"));
        }

        this.level.addFreshEntity(effectCloud);
    }
}
