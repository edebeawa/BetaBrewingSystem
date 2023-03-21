package edebe.beta_brewing_system.helper;

public class PotionHealth extends Potion {
    public static final Potion heal = (new PotionHealth(6)).setPotionName("potion.heal");
    public static final Potion harm = (new PotionHealth(7)).setPotionUsable().setPotionName("potion.harm");

    public PotionHealth(int paramInt) {
        super(paramInt);
    }

    public boolean isInstant() {
        return true;
    }

    public boolean isReady(int paramInt1, int paramInt2) {
        return (paramInt1 >= 1);
    }
}
