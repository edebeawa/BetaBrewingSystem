package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(Arrow.class)
public abstract class ArrowMixin extends AbstractArrow {
    private ArrowMixin(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
        throw new IllegalStateException("This class must not be instantiated");
    }

    @Shadow private Potion f_36855_;//potion
    @Shadow @Final private Set<MobEffectInstance> f_36852_;//effects
    @Shadow @Final private static EntityDataAccessor<Integer> f_36854_;//ID_EFFECT_COLOR

    @Invoker("m_36890_")
    abstract void updateColor();

    @Invoker("m_36882_")
    abstract void setFixedColor(int p_36883_);

    @Inject(at = @At("HEAD"), method = "m_36878_", cancellable = true)
    private void setEffectsFromItem(ItemStack stack, CallbackInfo info) {
        info.cancel();
        if (stack.is(Items.TIPPED_ARROW)) {
            this.f_36855_ = PotionUtils.getPotion(stack);
            Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(stack);
            if (!collection.isEmpty()) {
                for(MobEffectInstance mobeffectinstance : collection)
                    this.f_36852_.add(new MobEffectInstance(mobeffectinstance));
            }

            if (PotionHelper.isBetaPotion(stack)) {
                this.setFixedColor(PotionHelper.getColor(stack));
            } else {
                int color = Arrow.getCustomColor(stack);
                if (color == -1) this.updateColor();
                else this.setFixedColor(color);
            }
        } else if (stack.is(Items.ARROW)) {
            this.f_36855_ = Potions.EMPTY;
            this.f_36852_.clear();
            this.entityData.set(f_36854_, -1);
        }
    }
}
