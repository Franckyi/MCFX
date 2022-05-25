package dev.franckyi.mcfx.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.franckyi.mcfx.MCFXMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MCFXMod.MOD_ID)
public class MCFXModForge {
    public MCFXModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MCFXMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MCFXMod.init();
    }
}
