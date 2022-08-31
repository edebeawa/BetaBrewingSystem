package edebe.beta_brewing_system.common.item;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class SplashPotionItem extends ThrowablePotionItem {
    public SplashPotionItem(Properties builder) {
        super(builder, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS);
    }
}
