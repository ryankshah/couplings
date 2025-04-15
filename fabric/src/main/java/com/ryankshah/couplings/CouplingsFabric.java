package com.ryankshah.couplings;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class CouplingsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CouplingsCommon.init();
        ModConfigEvents.reloading(Constants.MOD_ID).register(CouplingsFabric::onReload);
        ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
    }

    private static void onReload(ModConfig config) {
        if (config.getModId().equals(Constants.MOD_ID)) {
            ConfigHandler.onReload();
        }
    }
}
