package com.ryankshah.couplings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CouplingsForgeClient
{
    @SubscribeEvent
    public static void onInitClient(FMLClientSetupEvent event) {
        CouplingsClient.init();
    }
}