package edebe.beta_brewing_system.common.entity;

import edebe.beta_brewing_system.common.Register;
import edebe.beta_brewing_system.common.helper.potion.PotionHelper;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ThrowablePotionEntity extends PotionEntity {
    public ThrowablePotionEntity(EntityType<? extends PotionEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
    }

    public ThrowablePotionEntity(World worldIn, LivingEntity livingEntityIn) {
        super(worldIn, livingEntityIn);
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result) {
        RayTraceResult.Type resultType = result.getType();
        if (resultType == RayTraceResult.Type.ENTITY) {
            this.onEntityHit((EntityRayTraceResult)result);
        } else if (resultType == RayTraceResult.Type.BLOCK) {
            this.func_230299_a_((BlockRayTraceResult)result);
        }

        if (!this.world.isRemote) {
            ItemStack stack = this.getItem();
            Potion potion = PotionUtils.getPotionFromItem(stack);
            List<EffectInstance> list = PotionHelper.getPotionEffectList(stack);

            boolean flag = potion == Potions.WATER && list.isEmpty();
            if (flag) {
                this.applyWater();
            } else if (!list.isEmpty()) {
                if (this.isLingering()) {
                    this.makeAreaOfEffectCloud(stack);
                } else {
                    this.addEffectToLivingEntity(list, result.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult)result).getEntity() : null);
                }
            }

            int i = potion.hasInstantEffect() ? 2007 : 2002;
            this.world.playEvent(i, this.getPosition(), PotionHelper.getPotionColor(stack));
            this.remove();
        }
    }

    private void applyWater() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                double d0 = this.getDistanceSq(livingentity);
                if (d0 < 16.0D && livingentity.isWaterSensitive()) {
                    livingentity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(livingentity, this.getShooter()), 1.0F);
                }
            }
        }
    }

    private void addEffectToLivingEntity(List<EffectInstance> effectList, @Nullable Entity entity) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                if (livingentity.canBeHitWithPotion()) {
                    double d0 = this.getDistanceSq(livingentity);
                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        if (livingentity == entity) {
                            d1 = 1.0D;
                        }

                        for(EffectInstance effectinstance : effectList) {
                            Effect effect = effectinstance.getPotion();
                            if (effect.isInstant()) {
                                effect.affectEntity(this, this.getShooter(), livingentity, effectinstance.getAmplifier(), d1);
                            } else {
                                int i = (int)(d1 * (double)effectinstance.getDuration() + 0.5D);
                                if (i > 20) {
                                    livingentity.addPotionEffect(new EffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.doesShowParticles()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeAreaOfEffectCloud(ItemStack stack) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
        Entity entity = this.getShooter();
        if (entity instanceof LivingEntity) {
            areaEffectCloudEntity.setOwner((LivingEntity)entity);
        }

        areaEffectCloudEntity.setRadius(3.0F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.setPotion(Potions.EMPTY);

        for(EffectInstance effectInstance : PotionHelper.getPotionEffectList(stack)) {
            areaEffectCloudEntity.addEffect(new EffectInstance(effectInstance));
        }

        areaEffectCloudEntity.setColor(PotionHelper.getPotionColor(stack));

        this.world.addEntity(areaEffectCloudEntity);
    }

    private boolean isLingering() {
        return this.getItem().getItem() == Register.LINGERING_POTION_ITEM;
    }
}
