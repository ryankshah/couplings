package com.ryankshah.couplings;

import com.ryankshah.couplings.impl.CouplingsPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CouplingsCommon
{
    public static final int COUPLING_DISTANCE = 64;
    public static final int COUPLING_SIGNAL = 8;

    public static void init() {
    }

    public static boolean ignoresSneaking(final Player player) {
        if (player instanceof CouplingsPlayer) {
            return CouplingsPlayer.ignoresSneaking(player);
        }

        return ConfigHandler.ignoreSneaking();
    }

    public static boolean couplesDoors(final Level level) {
        return level.isClientSide() ? CouplingsClient.serverCouplesDoors() : ConfigHandler.coupleDoors();
    }

    public static boolean couplesFenceGates(final Level level) {
        return level.isClientSide() ? CouplingsClient.serverCouplesFenceGates() : ConfigHandler.coupleFenceGates();
    }

    public static boolean couplesTrapdoors(final Level level) {
        return level.isClientSide() ? CouplingsClient.serverCouplesTrapdoors() : ConfigHandler.coupleTrapdoors();
    }
}