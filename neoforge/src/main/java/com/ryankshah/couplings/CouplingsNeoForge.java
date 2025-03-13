package com.ryankshah.couplings;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

@Mod(Constants.MOD_ID)
public class CouplingsNeoForge {

    public CouplingsNeoForge(ModContainer container, IEventBus eventBus) {
        CouplingsCommon.init();
        container.registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
        eventBus.addListener((ModConfigEvent.Reloading event) -> ConfigHandler.onReload());
    }
}