package com.ryankshah.couplings;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class CouplingsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CouplingsCommon.init();
        NeoForgeModConfigEvents.reloading(Constants.MOD_ID).register(CouplingsFabric::onReload);
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
    }

    private static void onReload(ModConfig config) {
        if (config.getModId().equals(Constants.MOD_ID)) {
            ConfigHandler.onReload();
        }
    }
}
