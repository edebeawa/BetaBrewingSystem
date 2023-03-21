package edebe.beta_brewing_system.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Blocks.class)
public interface BlocksAccessor {
    @Accessor("f_50256_")
    static void setCauldron(Block block) {
        throw new AssertionError();
    }

    @Accessor("f_152476_")
    static void setWaterCauldron(Block block) {
        throw new AssertionError();
    }

    @Accessor("f_152477_")
    static void setLavaCauldron(Block block) {
        throw new AssertionError();
    }

    @Accessor("f_152478_")
    static void setPowderSnowCauldron(Block block) {
        throw new AssertionError();
    }
}