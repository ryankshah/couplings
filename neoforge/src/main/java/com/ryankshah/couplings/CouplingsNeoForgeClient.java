package com.ryankshah.couplings;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CouplingsNeoForgeClient
{
    @SubscribeEvent
    public static void onInitClient(FMLClientSetupEvent event) {
        CouplingsClient.init();
    }
}