package com.ryankshah.couplings;

public class CouplingsClient
{
    public static void init() {
    }

    static boolean serverCouplesDoors() {
        return ConfigHandler.serverCouplesDoors();
    }

    static boolean serverCouplesFenceGates() {
        return ConfigHandler.serverCouplesFenceGates();
    }

    static boolean serverCouplesTrapdoors() {
        return ConfigHandler.serverCouplesTrapdoors();
    }
}