package edebe.beta_brewing_system.common.helper;

import net.minecraft.util.text.*;

import java.util.List;

public interface TooltipHelper {
    static void tooltipAdd(List<ITextComponent> tooltip, String key, TextFormatting formatting, Object... value) {
        String text = new TranslationTextComponent(key,value).getString();
        tooltip.add(new StringTextComponent(BetaBrewingSystemHelper.TranslationTextComponentProcessing(text)).mergeStyle(formatting));
    }

    static void tooltipAdd(List<ITextComponent> tooltip, String key, Color color, Object... value) {
        String text = new TranslationTextComponent(key,value).getString();
        tooltip.add(new StringTextComponent(BetaBrewingSystemHelper.TranslationTextComponentProcessing(text)).modifyStyle(s -> s.setColor(color)));
    }
}
