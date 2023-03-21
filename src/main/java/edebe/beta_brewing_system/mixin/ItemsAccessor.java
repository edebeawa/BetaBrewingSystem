package edebe.beta_brewing_system.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Items.class)
public interface ItemsAccessor {
    @Accessor("f_42544_")
    static void setCauldron(Item item) {
        throw new AssertionError();
    }

    @Accessor("f_42455_")
    static void setMilkBucket(Item item) {
        throw new AssertionError();
    }

    @Accessor("f_151055_")
    static void setPowderSnowBucket(Item item) {
        throw new AssertionError();
    }

    @Accessor("f_42590_")
    static void setGlassBottle(Item item) {
        throw new AssertionError();
    }
}
