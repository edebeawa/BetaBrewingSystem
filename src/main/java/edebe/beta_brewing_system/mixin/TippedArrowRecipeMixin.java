package edebe.beta_brewing_system.mixin;

import edebe.beta_brewing_system.helper.PotionHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
    @Inject(at = @At("HEAD"), method = "m_5874_ (Lnet/minecraft/world/inventory/CraftingContainer;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
    private void assemble(CraftingContainer container, CallbackInfoReturnable<ItemStack> infoReturnable) {
        infoReturnable.cancel();
        ItemStack stack = container.getItem(1 + container.getWidth());
        if (!stack.is(Items.LINGERING_POTION)) {
            infoReturnable.setReturnValue(ItemStack.EMPTY);
        } else {
            ItemStack arrow;
            if (PotionHelper.isBetaPotion(stack)) {
                arrow = PotionHelper.newBetaPotionItem(Items.TIPPED_ARROW, 8);
                PotionHelper.setItemMetadata(arrow, PotionHelper.getItemMetadata(stack));
            } else arrow = new ItemStack(Items.TIPPED_ARROW, 8);
            PotionUtils.setPotion(arrow, PotionUtils.getPotion(stack));
            PotionUtils.setCustomEffects(arrow, PotionUtils.getCustomEffects(stack));
            infoReturnable.setReturnValue(arrow);
        }
    }
}
