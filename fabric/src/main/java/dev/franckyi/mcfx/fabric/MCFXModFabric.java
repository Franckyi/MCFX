package dev.franckyi.mcfx.fabric;

import dev.franckyi.mcfx.fabriclike.MCFXModFabricLike;
import net.fabricmc.api.ModInitializer;

public class MCFXModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MCFXModFabricLike.init();
    }
}
