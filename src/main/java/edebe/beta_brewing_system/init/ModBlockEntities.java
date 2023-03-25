package edebe.beta_brewing_system.init;

import edebe.beta_brewing_system.world.block.entity.CauldronBlockEntity;
import edebe.doglib.api.register.RegisterManager;
import edebe.doglib.api.register.RegisterObject;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("unused")
public class ModBlockEntities {
    public static final RegisterManager<BlockEntityType<? extends BlockEntity>> BLOCK_ENTITY_TYPES = new RegisterManager<>("minecraft");

    public static final RegisterObject<BlockEntityType<CauldronBlockEntity>> CAULDRON = BLOCK_ENTITY_TYPES.add("cauldron", CauldronBlockEntity::new, ModBlocks.CAULDRON.getObject());
}