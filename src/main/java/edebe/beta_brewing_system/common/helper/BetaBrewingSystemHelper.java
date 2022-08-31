package edebe.beta_brewing_system.common.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public interface BetaBrewingSystemHelper {
    static double numberFormat(double number,int mf){
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(mf);
        return Double.parseDouble(format.format(number));
    }

    static String TranslationTextComponentProcessing(String text) {
        String[] target = new String[]{"{TTC_A}"};
        String[] replacement = new String[]{"%"};
        for (int i = 0;i < target.length;i++) {
            text = text.replace(target[i],replacement[i]);
        }
        return text;
    }

    static String numberToString(Object number) {
        return new DecimalFormat("0.#").format(number);
    }

    static String translationString(String key) {
        return new TranslationTextComponent(key).getString();
    }

    static String translationString(String key, Object... args) {
        return new TranslationTextComponent(key, args).getString();
    }

    static void setItemMetadata(ItemStack stack, int metadata) {
        stack.getOrCreateTag().putInt("metadata", metadata);
    }

    static int getItemMetadata(ItemStack stack) {
        return stack.getOrCreateTag().getInt("metadata");
    }
}
