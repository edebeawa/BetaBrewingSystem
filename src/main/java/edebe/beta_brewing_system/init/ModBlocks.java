package edebe.beta_brewing_system.init;

import edebe.beta_brewing_system.world.block.CauldronBlock;
import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

@SuppressWarnings("unused")
public class ModBlocks {
    public static final RegisterManager<Block> BLOCKS = new RegisterManager<>("minecraft");

    public static final RegisterObject<Block> MILK = BLOCKS.add("milk", new LiquidBlock(() -> ModFluids.MILK.getObject(FlowingFluid.class), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noLootTable()));
    public static final RegisterObject<Block> CAULDRON = BLOCKS.add("cauldron", new CauldronBlock(Properties.of(Material.METAL, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()));
}