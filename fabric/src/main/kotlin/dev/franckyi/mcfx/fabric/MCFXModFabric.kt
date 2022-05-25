package dev.franckyi.mcfx.fabric

import dev.franckyi.mcfx.fabriclike.MCFXModFabricLike
import net.fabricmc.api.ModInitializer

object MCFXModFabric : ModInitializer {
    override fun onInitialize() {
        MCFXModFabricLike.init()
    }
}