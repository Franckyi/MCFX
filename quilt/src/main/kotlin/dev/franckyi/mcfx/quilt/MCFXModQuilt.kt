package dev.franckyi.mcfx.quilt

import dev.franckyi.mcfx.fabriclike.MCFXModFabricLike
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer

object MCFXModQuilt : ClientModInitializer {
    override fun onInitializeClient(mod: ModContainer) {
        MCFXModFabricLike.init()
    }
}