package edebe.beta_brewing_system.world.item;

import edebe.beta_brewing_system.world.fluid.SolidFluid;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SolidBucketItem extends BucketItem {
    private final BlockState block;

    public SolidBucketItem(Block block, Properties properties) {
        super(Fluids.EMPTY, properties);
        this.block = block.defaultBlockState();
    }

    public SolidBucketItem(SolidFluid fluid, Properties properties) {
        super(fluid, properties);
        this.block = fluid.defaultFluidState().createLegacyBlock();
    }

    public SolidBucketItem(Supplier<? extends SolidFluid> supplier, Properties builder) {
        super(supplier, builder);
        this.block = supplier.get().defaultFluidState().createLegacyBlock();
    }

    @Override
    public InteractionResult useOn(UseOnContext p_151197_) {
        InteractionResult interactionresult = super.useOn(p_151197_);
        Player player = p_151197_.getPlayer();
        if (interactionresult.consumesAction() && player != null && !player.isCreative()) {
            InteractionHand interactionhand = p_151197_.getHand();
            player.setItemInHand(interactionhand, Items.BUCKET.getDefaultInstance());
        }

        return interactionresult;
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hit) {
        if (world.isInWorldBounds(pos) && world.isEmptyBlock(pos)) {
            if (!world.isClientSide) world.setBlock(pos, block, 3);
            world.gameEvent(player, GameEvent.FLUID_PLACE, pos);
            world.playSound(player, pos, getPlaceSound(block), SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }

    protected SoundEvent getPlaceSound(BlockState state) {
        return getFluid().getPickupSound().orElse(SoundEvents.BUCKET_EMPTY);
    }
}
