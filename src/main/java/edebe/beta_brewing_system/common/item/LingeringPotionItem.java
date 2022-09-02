package edebe.beta_brewing_system.common.item;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class LingeringPotionItem extends ThrowablePotionItem {
    public LingeringPotionItem(Properties properties) {
        super(properties, SoundEvents.ENTITY_LINGERING_POTION_THROW, SoundCategory.NEUTRAL);
    }
}