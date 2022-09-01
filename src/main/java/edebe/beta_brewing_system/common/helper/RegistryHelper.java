package edebe.beta_brewing_system.common.helper;

import edebe.beta_brewing_system.common.BetaBrewingSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface RegistryHelper {
    static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> registry, ResourceLocation name, IForgeRegistryEntry<V> thing) {
        registry.register(thing.setRegistryName(name));
    }

    static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> registry, String name, IForgeRegistryEntry<V> thing) {
        register(registry, new ResourceLocation(BetaBrewingSystem.MODID, name), thing);
    }
}
