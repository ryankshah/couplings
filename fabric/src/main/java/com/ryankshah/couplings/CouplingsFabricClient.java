package com.ryankshah.couplings;

import net.fabricmc.api.ClientModInitializer;

public class CouplingsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CouplingsClient.init();
    }
}
