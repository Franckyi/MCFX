package dev.franckyi.mcfx

import dev.franckyi.mcfx.impl.setupVanillaImpl

object MCFXMod {
    const val MOD_ID = "mcfx"
    fun init() {
        setupVanillaImpl()
    }
}