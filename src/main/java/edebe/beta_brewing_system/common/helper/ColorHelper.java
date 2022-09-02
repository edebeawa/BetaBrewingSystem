package edebe.beta_brewing_system.common.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public interface ColorHelper {
    static int hsvToRGB(Object h,Object s,Object v) {
        float hue;
        float saturation;
        float value;

        if (h instanceof Integer) {
            hue = ((Integer) h) % 360 / 360F;
        } else if (h instanceof Float) {
            hue = ((Float) h);
        } else {
            throw new IllegalArgumentException("the value " + h.getClass().getName() + " of hue is not of type integer or float");
        }

        if (s instanceof Integer) {
            saturation = ((Integer) s) / 100F;
        } else if (s instanceof Float) {
            saturation = ((Float) s);
        } else {
            throw new IllegalArgumentException("the value " + s.getClass().getName() + " of saturation is not of type integer or float");
        }

        if (v instanceof Integer) {
            value = ((Integer) v) / 100F;
        } else if (v instanceof Float) {
            value = ((Float) v);
        } else {
            throw new IllegalArgumentException("the value " + v.getClass().getName() + " of value is not of type integer or float");
        }

        return MathHelper.hsvToRGB(hue,saturation,value);
    }

    static float getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    static float getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    static float getBlue(int color) {
        return color & 0xFF;
    }

    static float getAlpha(int color) {
        return (color >> 24) & 0xff;
    }

    static void color(int color) {
        RenderSystem.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    static Color parseToColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (color >> 24) & 0xff;
        return new Color(r,g,b,a);
    }
}
