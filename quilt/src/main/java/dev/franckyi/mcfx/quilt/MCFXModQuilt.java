package dev.franckyi.mcfx.quilt;

import dev.franckyi.mcfx.fabriclike.MCFXModFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class MCFXModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        MCFXModFabricLike.init();
    }
}
