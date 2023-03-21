package edebe.beta_brewing_system.helper;

@SuppressWarnings("unused")
public class Potion {
    public static final Potion[] potionTypes = new Potion[32];
    public static final Potion moveSpeed = (new Potion(1)).setPotionName("potion.moveSpeed").setIconIndex(0, 0);
    public static final Potion moveSlowdown = (new Potion(2)).setPotionUsable().setPotionName("potion.moveSlowdown").setIconIndex(1, 0);
    public static final Potion digSpeed = (new Potion(3)).setPotionName("potion.digSpeed").setIconIndex(2, 0);
    public static final Potion digSlowdown = (new Potion(4)).setPotionUsable().setPotionName("potion.digSlowDown").setIconIndex(3, 0);
    public static final Potion damageBoost = (new Potion(5)).setPotionName("potion.damageBoost").setIconIndex(4, 0);
    public static final Potion jump = (new Potion(8)).setPotionName("potion.jump").setIconIndex(2, 1);
    public static final Potion confusion = (new Potion(9)).setPotionUsable().setPotionName("potion.confusion").setIconIndex(3, 1);
    public static final Potion regeneration = (new Potion(10)).setPotionName("potion.regeneration").setIconIndex(7, 0);
    public static final Potion resistance = (new Potion(11)).setPotionName("potion.resistance").setIconIndex(6, 1);
    public static final Potion fireResistance = (new Potion(12)).setPotionName("potion.fireResistance").setIconIndex(7, 1);
    public static final Potion waterBreathing = (new Potion(13)).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
    public static final Potion invisibility = (new Potion(14)).setPotionName("potion.invisibility").setIconIndex(0, 1);
    public static final Potion blindness = (new Potion(15)).setPotionUsable().setPotionName("potion.blindness").setIconIndex(5, 1);
    public static final Potion nightVision = (new Potion(16)).setPotionName("potion.nightVision").setIconIndex(4, 1);
    public static final Potion hunger = (new Potion(17)).setPotionUsable().setPotionName("potion.hunger").setIconIndex(1, 1);
    public static final Potion weakness = (new Potion(18)).setPotionUsable().setPotionName("potion.weakness").setIconIndex(5, 0);
    public static final Potion poison = (new Potion(19)).setPotionUsable().setPotionName("potion.poison").setIconIndex(6, 0);

    public final int id;
    private boolean usable;
    private String name = "";
    private int statusIconIndex = -1;

    protected Potion(int paramInt) {
        this.id = paramInt;
        potionTypes[paramInt] = this;
    }

    protected Potion setIconIndex(int paramInt1, int paramInt2) {
        this.statusIconIndex = paramInt1 + paramInt2 * 8;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public boolean isInstant() {
        return false;
    }

    public Potion setPotionName(String paramString) {
        this.name = paramString;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasStatusIcon() {
        return (this.statusIconIndex >= 0);
    }

    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }

    public boolean isUsable() {
        return this.usable;
    }

    protected Potion setPotionUsable() {
        this.usable = true;
        return this;
    }
}
