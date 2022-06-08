package dev.franckyi.mcfx.fabric

import dev.franckyi.mcfx.fabriclike.MCFXModFabricLike
import net.fabricmc.api.ClientModInitializer

object MCFXModFabric : ClientModInitializer {
    override fun onInitializeClient() {
        MCFXModFabricLike.init()
    }
}