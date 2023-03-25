package edebe.beta_brewing_system.helper;

import edebe.beta_brewing_system.init.ModFluids;
import edebe.doglib.api.helper.TranslationHelper;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PotionHelper {
    private static final HashMap<Integer, List<MobEffectInstance>> effects = new HashMap<>();
    private static final HashMap<Integer, String> requirements = new HashMap<>();
    private static final HashMap<Integer, String> amplifiers = new HashMap<>();
    private static final HashMap<Integer, String> ingredient = new HashMap<>();

    private static final String[] potionPrefixes = new String[] {
            "mundane", "uninteresting", "bland", "clear", "milky", "diffuse", "artless", "thin", "awkward", "flat",
            "bulky", "bungling", "buttered", "smooth", "suave", "debonair", "thick", "elegant", "fancy", "charming",
            "dashing", "refined", "cordial", "sparkling", "potent", "foul", "odorless", "rank", "harsh", "acrid",
            "gross", "stinky"
    };

    private static boolean calculationOfBoolean(int liquidData, int paramInt2) {
        return ((liquidData & 1 << paramInt2 % 15) != 0);
    }

    private static boolean b(int paramInt1, int paramInt2) {
        return ((paramInt1 & 1 << paramInt2) != 0);
    }

    private static int getPotionPrefix(int paramInt1, int paramInt2) {
        return b(paramInt1, paramInt2) ? 1 : 0;
    }

    private static int d(int paramInt1, int paramInt2) {
        return b(paramInt1, paramInt2) ? 0 : 1;
    }

    public static int getPrefixNumber(int metadata) {
        return getNumberInBinaryNumber(metadata, 14, 9, 7, 3, 2);
    }

    public static ItemStack newBetaPotionItem(Item item) {
        return newBetaPotionItem(item, 1);
    }

    public static ItemStack newBetaPotionItem(Item item, int count) {
        ItemStack stack = new ItemStack(item, count);
        stack.getOrCreateTag().putBoolean("Beta", true);
        return stack;
    }

    public static boolean isBetaPotion(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Beta");
    }

    public static FluidStack newBetaPotionFluid(int count) {
        FluidStack stack = new FluidStack(ModFluids.POTION.getObject(), count);
        stack.getOrCreateTag().putBoolean("Beta", true);
        return stack;
    }

    public static boolean isBetaPotion(FluidStack stack) {
        return stack.getOrCreateTag().getBoolean("Beta");
    }

    public static String getPotionPrefix(int metadata) {
        int prefixNumber = getPrefixNumber(metadata);
        return potionPrefixes[prefixNumber];
    }

    public static MutableComponent getItemTranslation(ItemStack stack) {
        String prefix = getItemMetadata(stack) == 0 ? "error" : getPotionPrefix(getItemMetadata(stack));
        return Component.translatable(stack.getItem().getDescriptionId(stack), TranslationHelper.getTranslation("misc.minecraft.potion." + prefix));
    }

    public static MutableComponent getFluidTranslation(FluidStack stack) {
        String prefix = getFluidMetadata(stack) == 0 ? "error" : getPotionPrefix(getFluidMetadata(stack));
        return Component.translatable(stack.getTranslationKey(), TranslationHelper.getTranslation("misc.minecraft.potion." + prefix));
    }

    public static void setItemMetadata(ItemStack stack, int metadata) {
        stack.getOrCreateTag().putInt("Metadata", metadata);
    }

    public static int getItemMetadata(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Metadata");
    }

    public static void setFluidMetadata(FluidStack stack, int metadata) {
        stack.getOrCreateTag().putInt("Metadata", metadata);
    }

    public static int getFluidMetadata(FluidStack stack) {
        return stack.getOrCreateTag().getInt("Metadata");
    }

    public static net.minecraft.world.item.alchemy.Potion getPotion(FluidStack stack) {
        return PotionUtils.getPotion(stack.getTag());
    }

    public static FluidStack setPotion(FluidStack stack, net.minecraft.world.item.alchemy.Potion potion) {
        ResourceLocation key = Registry.POTION.getKey(potion);
        if (potion == Potions.EMPTY) stack.removeChildTag("Potion");
        else stack.getOrCreateTag().putString("Potion", key.toString());
        return stack;
    }

    public static Fluid getPotionFluid(net.minecraft.world.item.alchemy.Potion potion) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(ForgeRegistries.POTIONS.getKey(potion));
        return fluid == null ? Fluids.EMPTY : fluid;
    }

    public static net.minecraft.world.item.alchemy.Potion getFluidPotion(Fluid fluid) {
        net.minecraft.world.item.alchemy.Potion potion = ForgeRegistries.POTIONS.getValue(ForgeRegistries.FLUIDS.getKey(fluid));
        return potion == null ? Potions.EMPTY : potion;
    }

    private static int a(boolean b1, boolean b2, boolean b3, int i1, int i2, int i3, int i4) {
        int i = 0;
        if (b1) {
            i = d(i4, i2);
        } else if (i1 != -1) {
            if (i1 == 0 && h(i4) == i2) {
                i = 1;
            } else if (i1 == 1 && h(i4) > i2) {
                i = 1;
            } else if (i1 == 2 && h(i4) < i2) {
                i = 1;
            }
        } else {
            i = getPotionPrefix(i4, i2);
        }
        if (b2) {
            i *= i3;
        }
        if (b3) {
            i *= -1;
        }
        return i;
    }

    private static int h(int paramInt) {
        int i = 0;
        for (; paramInt > 0; i++) {
            paramInt &= paramInt - 1;
        }
        return i;
    }

    private static int a(String paramString, int paramInt1, int paramInt2, int paramInt3) {
        if (paramInt1 >= paramString.length() || paramInt2 < 0 || paramInt1 >= paramInt2) return 0;
        int i = paramString.indexOf('|', paramInt1);
        if (i >= 0 && i < paramInt2) {
            int i2 = a(paramString, paramInt1, i - 1, paramInt3);
            if (i2 > 0) {
                return i2;
            }
            int i3 = a(paramString, i + 1, paramInt2, paramInt3);
            return Math.max(i3, 0);
        }
        int j = paramString.indexOf('&', paramInt1);
        if (j >= 0 && j < paramInt2) {
            int i2 = a(paramString, paramInt1, j - 1, paramInt3);
            if (i2 <= 0) {
                return 0;
            }
            int i3 = a(paramString, j + 1, paramInt2, paramInt3);
            if (i3 <= 0) {
                return 0;
            }
            return Math.max(i2, i3);
        }
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        byte b = -1;
        int k = 0;
        int m = 0;
        int n = 0;
        for (int i1 = paramInt1; i1 < paramInt2; i1++) {
            char c = paramString.charAt(i1);
            if (c >= '0' && c <= '9') {
                if (bool1) {
                    m = c - 48;
                    bool2 = true;
                } else {
                    k *= 10;
                    k += c - 48;
                    bool3 = true;
                }
            } else if (c == '*') {
                bool1 = true;
            } else if (c == '!') {
                if (bool3) {
                    n += a(bool4, bool2, bool5, b, k, m, paramInt3);
                    bool3 = bool2 = bool1 = bool5 = false;
                    k = m = 0;
                    b = -1;
                }
                bool4 = true;
            } else if (c == '-') {
                if (bool3) {
                    n += a(bool4, bool2, bool5, b, k, m, paramInt3);
                    bool3 = bool2 = bool1 = bool4 = false;
                    k = m = 0;
                    b = -1;
                }
                bool5 = true;
            } else if (c == '=' || c == '<' || c == '>') {
                if (bool3) {
                    n += a(bool4, bool2, bool5, b, k, m, paramInt3);
                    bool3 = bool2 = bool1 = bool5 = bool4 = false;
                    k = m = 0;
                }
                if (c == '=') {
                    b = 0;
                } else if (c == '<') {
                    b = 2;
                } else {
                    b = 1;
                }
            } else if (c == '+' && bool3) {
                n += a(bool4, bool2, bool5, b, k, m, paramInt3);
                bool3 = bool2 = bool1 = bool5 = bool4 = false;
                k = m = 0;
                b = -1;
            }
        }
        if (bool3) {
            n += a(bool4, bool2, bool5, b, k, m, paramInt3);
        }
        return n;
    }

    @SuppressWarnings("ConstantConditions")
    public static List<MobEffectInstance> getEffects(int metadata) {
        ArrayList<MobEffectInstance> localArrayList = new ArrayList<>();
        for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
            if (effect != null) {
                String str = requirements.get(MobEffect.getId(effect));
                if (str != null) {
                    int k = a(str, 0, str.length(), metadata);
                    if (k > 0) {
                        int m = 0;
                        String str2 = amplifiers.get(MobEffect.getId(effect));
                        if (str2 != null) {
                            m = a(str2, 0, str2.length(), metadata);
                            if (m < 0) m = 0;
                        }

                        if (effect.isInstantenous()) {
                            k = 1;
                        } else {
                            k = 1200 * (k * 3 + (k - 1) * 2);
                            if (effect.getCategory() == MobEffectCategory.HARMFUL) k >>= 1;
                        }

                        localArrayList.add(new MobEffectInstance(Registry.MOB_EFFECT.byId(MobEffect.getId(effect)), k, m));
                    }
                }
            }
        }
        return localArrayList;
    }

    @SuppressWarnings("ConstantConditions")
    public static int calculationOfNetherWarts1(int liquidData) {
        if ((liquidData & 0x1) == 0) {
            return liquidData;
        }

        byte b = 14;
        while ((liquidData & 1 << b) == 0 && b >= 0) {
            b--;
        }

        if (b < 2 || (liquidData & 1 << b - 1) != 0) {
            return liquidData;
        }

        if (b >= 0) {
            liquidData &= ~(1 << b);
        }

        liquidData <<= 1;

        if (b >= 0) {
            liquidData |= 1 << b;
            liquidData |= 1 << b - 1;
        }
        return liquidData & 0x7FFF;
    }

    public static int calculationOfNetherWarts2(int liquidData) {
        byte b = 14;
        while ((liquidData & 1 << b) == 0 && b >= 0) {
            b--;
        }
        if (b >= 0) {
            liquidData &= ~(1 << b);
        }
        int i = 0;
        int j = liquidData;
        while (j != i) {
            j = liquidData;
            i = 0;
            for (byte b1 = 0; b1 < 15; b1++) {
                boolean bool = calculationOfBoolean(liquidData, b1);
                if (bool) {
                    if (!calculationOfBoolean(liquidData, b1 + 1) && calculationOfBoolean(liquidData, b1 + 2)) {
                        bool = false;
                    } else if (!calculationOfBoolean(liquidData, b1 - 1) && calculationOfBoolean(liquidData, b1 - 2)) {
                        bool = false;
                    }
                } else {
                    bool = (calculationOfBoolean(liquidData, b1 - 1) && calculationOfBoolean(liquidData, b1 + 1));
                }
                if (bool)
                    i |= 1 << b1;
            }
            liquidData = i;
        }
        if (b >= 0)
            i |= 1 << b;
        return i & 0x7FFF;
    }

    public static int applyNetherWart(int paramInt) {
        if ((paramInt & 0x1) != 0) {
            paramInt = calculationOfNetherWarts1(paramInt);
        }
        return calculationOfNetherWarts2(paramInt);
    }

    private static int a(int i1, int i2, boolean b1, boolean b2) {
        if (b1) {
            i1 &= ~(1 << i2);
        } else if (b2) {
            if ((i1 & 1 << i2) != 0) {
                i1 &= ~(1 << i2);
            } else {
                i1 |= 1 << i2;
            }
        } else {
            i1 |= 1 << i2;
        }
        return i1;
    }

    public static int applyIngredient(int paramInt, String paramString) {
        byte b1 = 0;
        int i = paramString.length();
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        int j = 0;
        for (byte b2 = b1; b2 < i; b2++) {
            char c = paramString.charAt(b2);
            if (c >= '0' && c <= '9') {
                j *= 10;
                j += c - 48;
                bool1 = true;
            } else if (c == '!') {
                if (bool1) {
                    paramInt = a(paramInt, j, bool3, bool2);
                    bool1 = bool3 = false;
                    j = 0;
                }
                bool2 = true;
            } else if (c == '-') {
                if (bool1) {
                    paramInt = a(paramInt, j, bool3, bool2);
                    bool1 = bool2 = false;
                    j = 0;
                }
                bool3 = true;
            } else if (c == '+' && bool1) {
                paramInt = a(paramInt, j, bool3, bool2);
                bool1 = bool3 = bool2 = false;
                j = 0;
            }
        }
        if (bool1) {
            paramInt = a(paramInt, j, bool3, bool2);
        }
        return paramInt & 0x7FFF;
    }

    public static int getNumberInBinaryNumber(int i1, int i2, int i3, int i4, int i5, int i6) {
        return (b(i1, i2) ? 16 : 0) | (b(i1, i3) ? 8 : 0) | (b(i1, i4) ? 4 : 0) | (b(i1, i5) ? 2 : 0) | (b(i1, i6) ? 1 : 0);
    }

    public static boolean isPotionIngredient(int itemID) {
        return ingredient.containsKey(itemID);
    }

    public static String getPotionIngredient(int itemID) {
        return ingredient.get(itemID);
    }

    static {
        requirements.put(MobEffect.getId(MobEffects.MOVEMENT_SPEED), "!10 & !4 & 5*2+0 & >1 | !7 & !4 & 5*2+0 & >1");
        requirements.put(MobEffect.getId(MobEffects.MOVEMENT_SLOWDOWN), "10 & 7 & !4 & 7+5+1-0");
        requirements.put(MobEffect.getId(MobEffects.DIG_SPEED), "2 & 12+2+6-1-7 & <8");
        requirements.put(MobEffect.getId(MobEffects.DIG_SLOWDOWN), "!2 & !1*2-9 & 14-5");
        requirements.put(MobEffect.getId(MobEffects.DAMAGE_BOOST), "9 & 3 & 9+4+5 & <11");
        requirements.put(MobEffect.getId(MobEffects.HEAL), "11 & <6");
        requirements.put(MobEffect.getId(MobEffects.HARM), "!11 & 1 & 10 & !7");
        requirements.put(MobEffect.getId(MobEffects.JUMP), "8 & 2+0 & <5");
        requirements.put(MobEffect.getId(MobEffects.CONFUSION), "8*2-!7+4-11 & !2 | 13 & 11 & 2*3-1-5");
        requirements.put(MobEffect.getId(MobEffects.REGENERATION), "!14 & 13*3-!0-!5-8");
        requirements.put(MobEffect.getId(MobEffects.DAMAGE_RESISTANCE), "10 & 4 & 10+5+6 & <9");
        requirements.put(MobEffect.getId(MobEffects.FIRE_RESISTANCE), "14 & !5 & 6-!1 & 14+13+12");
        requirements.put(MobEffect.getId(MobEffects.WATER_BREATHING), "0+1+12 & !6 & 10 & !11 & !13");
        requirements.put(MobEffect.getId(MobEffects.INVISIBILITY), "2+5+13-0-4 & !7 & !1 & >5");
        requirements.put(MobEffect.getId(MobEffects.BLINDNESS), "9 & !1 & !5 & !3 & =3");
        requirements.put(MobEffect.getId(MobEffects.NIGHT_VISION), "8*2-!7 & 5 & !0 & >3");
        requirements.put(MobEffect.getId(MobEffects.HUNGER), ">4>6>8-3-8+2");
        requirements.put(MobEffect.getId(MobEffects.WEAKNESS), "=1>5>7>9+3-7-2-11 & !10 & !0");
        requirements.put(MobEffect.getId(MobEffects.POISON), "12+9 & !13 & !0");

        /*
        requirements.put(MobEffect.getId(MobEffects.WITHER), "!8 & !2-0 & >5");
        requirements.put(MobEffect.getId(MobEffects.HEALTH_BOOST), "8*2+7+!4+!11 & 2 | !13 & !11 & !2*3+1+5");
        requirements.put(MobEffect.getId(MobEffects.ABSORPTION), "14 & !13*3+0+5+8");
        requirements.put(MobEffect.getId(MobEffects.SATURATION), "!10 & !4 & !10-5-6 & >9");
        requirements.put(MobEffect.getId(MobEffects.GLOWING), "!14 & 5 & !6+1 & !14-13-12");
        requirements.put(MobEffect.getId(MobEffects.LEVITATION), "0-1-12 & 6 & !10 & 11 & 13");
        requirements.put(MobEffect.getId(MobEffects.LUCK), "!11 & >6");
        requirements.put(MobEffect.getId(MobEffects.UNLUCK), "11 & !1 & !10 & 7");
        requirements.put(MobEffect.getId(MobEffects.SLOW_FALLING), "=1<5<7<9-3+7+2+11 & 10 & 0");
        requirements.put(MobEffect.getId(MobEffects.DARKNESS), "12-9 & 13 & 0");
        */

        amplifiers.put(MobEffect.getId(MobEffects.MOVEMENT_SPEED), "7+!3-!1");
        amplifiers.put(MobEffect.getId(MobEffects.DIG_SPEED), "1+0-!11");
        amplifiers.put(MobEffect.getId(MobEffects.DAMAGE_BOOST), "2+7-!12");
        amplifiers.put(MobEffect.getId(MobEffects.HEAL), "11+!0-!1-!14");
        amplifiers.put(MobEffect.getId(MobEffects.HARM), "!11-!14+!0-!1");
        amplifiers.put(MobEffect.getId(MobEffects.DAMAGE_RESISTANCE), "12-!2");
        amplifiers.put(MobEffect.getId(MobEffects.POISON), "14>5");

        ingredient.put(Item.getId(Items.GHAST_TEAR), "+11");
        ingredient.put(Item.getId(Items.BLAZE_POWDER), "+14");
        ingredient.put(Item.getId(Items.MAGMA_CREAM), "+14+6+1");
        ingredient.put(Item.getId(Items.SUGAR), "+0");
        ingredient.put(Item.getId(Items.SPIDER_EYE), "+10+7+5");
        ingredient.put(Item.getId(Items.FERMENTED_SPIDER_EYE), "+14+9");
    }

    public static List<MobEffectInstance> getMobEffects(ItemStack stack) {
        return getAllEffects(getItemMetadata(stack));
    }

    public static List<MobEffectInstance> getAllEffects(int metadata) {
        List<MobEffectInstance> list = effects.get(metadata);

        if (list == null) {
            list = getEffects(metadata);
            effects.put(metadata, list);
        }

        return list;
    }

    public static int getColor(ItemStack stack) {
        return getColor(getItemMetadata(stack)).getRGB();
    }

    public static int getColor(FluidStack stack) {
        return getColor(getFluidMetadata(stack)).getRGB();
    }

    public static Color getColor(int metadata) {
        int red = (getNumberInBinaryNumber(metadata, 2, 14, 11, 8, 5) ^ 0x3) << 3;
        int green = (getNumberInBinaryNumber(metadata, 0, 12, 9, 6, 3) ^ 0x6) << 3;
        int blue = (getNumberInBinaryNumber(metadata, 13, 10, 4, 1, 7) ^ 0x8) << 3;
        return new Color(red, green, blue);
    }
}