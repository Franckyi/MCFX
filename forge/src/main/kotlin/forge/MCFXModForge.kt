package forge

import dev.architectury.platform.forge.EventBuses
import dev.franckyi.mcfx.MCFXMod
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(MCFXMod.MOD_ID)
object MCFXModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MCFXMod.MOD_ID, MOD_BUS)
        MCFXMod.init()
    }
}