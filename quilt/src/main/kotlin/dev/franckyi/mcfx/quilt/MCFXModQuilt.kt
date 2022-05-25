package dev.franckyi.mcfx.quilt

import dev.franckyi.mcfx.fabriclike.MCFXModFabricLike
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

object MCFXModQuilt : ModInitializer {
    override fun onInitialize(mod: ModContainer) {
        MCFXModFabricLike.init()
    }
}