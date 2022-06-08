package forge

import dev.architectury.platform.forge.EventBuses
import dev.franckyi.mcfx.MCFXMod
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLLoader
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(MCFXMod.MOD_ID)
object MCFXModForge {
    init {
        EventBuses.registerModEventBus(MCFXMod.MOD_ID, MOD_BUS)
        if (FMLLoader.getDist() == Dist.CLIENT) {
            MCFXMod.init()
        }
    }
}