package edebe.beta_brewing_system.common.item;

import edebe.beta_brewing_system.common.entity.ThrowablePotionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ThrowablePotionItem extends PotionItem {
    private final SoundEvent soundIn;
    private final SoundCategory categoryIn;

    public ThrowablePotionItem(Properties builder, SoundEvent soundIn, SoundCategory categoryIn) {
        super(builder);
        this.soundIn = soundIn;
        this.categoryIn = categoryIn;
    }

    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), soundIn, categoryIn, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            ThrowablePotionEntity potionEntity = new ThrowablePotionEntity(worldIn, playerIn);
            potionEntity.setItem(stack);
            potionEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
            worldIn.addEntity(potionEntity);
        }

        playerIn.addStat(Stats.ITEM_USED.get(this));

        if (!playerIn.abilities.isCreativeMode) {
            stack.shrink(1);
        }

        return ActionResult.func_233538_a_(stack, worldIn.isRemote());
    }
}
