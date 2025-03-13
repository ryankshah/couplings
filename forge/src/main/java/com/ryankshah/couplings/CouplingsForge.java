package com.ryankshah.couplings;

import fuzs.forgeconfigapiport.forge.api.neoforge.v4.NeoForgeConfigRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod(Constants.MOD_ID)
public class CouplingsForge
{
    public CouplingsForge(IEventBus eventBus) {
        CouplingsCommon.init();
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
        eventBus.addListener((ModConfigEvent.Reloading event) -> ConfigHandler.onReload());
    }
}