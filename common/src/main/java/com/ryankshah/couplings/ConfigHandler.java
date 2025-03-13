package com.ryankshah.couplings;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigHandler
{
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final Signal<Lazy<?>> RELOAD = new Signal<>(Lazy::reset);

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    static {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static void onReload() {
        RELOAD.run();
    }

    public static boolean serverCouplesDoors() {
        return COMMON.serverCouplesDoors.get();
    }
    public static boolean serverCouplesFenceGates() {
        return COMMON.serverCouplesFenceGates.get();
    }
    public static boolean serverCouplesTrapdoors() { return COMMON.serverCouplesTrapdoors.get(); }

    public static boolean ignoreSneaking() { return CLIENT.ignoreSneaking.get(); }
    public static boolean coupleDoors() { return CLIENT.coupleDoors.get(); }
    public static boolean coupleFenceGates() { return CLIENT.coupleFenceGates.get(); }
    public static boolean coupleTrapdoors() { return CLIENT.coupleTrapdoors.get(); }

    public static class Common {
        public final ModConfigSpec.BooleanValue serverCouplesDoors;
        public final ModConfigSpec.BooleanValue serverCouplesFenceGates;
        public final ModConfigSpec.BooleanValue serverCouplesTrapdoors;

        public Common(ModConfigSpec.Builder builder) {
            builder.push("couplings");
            serverCouplesDoors = builder.comment("Server Couples Doors").define("serverCouplesDoors", true);
            serverCouplesFenceGates = builder.comment("Server Couples Fence Gates").define("serverCouplesFenceGates", true);
            serverCouplesTrapdoors = builder.comment("Server Couples Trapdoors").define("serverCouplesTrapdoors", true);
            builder.pop();
        }

        @SuppressWarnings("deprecation")
        private String getPrettyValueName(Object value) {
            return Arrays.stream(value.toString().toLowerCase().split("_"))
                    .map(WordUtils::capitalize)
                    .collect(Collectors.joining());
        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue ignoreSneaking;
        public final ModConfigSpec.BooleanValue coupleDoors;
        public final ModConfigSpec.BooleanValue coupleFenceGates;
        public final ModConfigSpec.BooleanValue coupleTrapdoors;

        public Client(ModConfigSpec.Builder builder) {
            builder.push("couplings");
            ignoreSneaking = builder.comment("Ignore Sneaking").define("ignoreSneaking", false);
            coupleDoors = builder.comment("Couple Doors").define("coupleDoors", true);
            coupleFenceGates = builder.comment("Couple Fence Gates").define("coupleFenceGates", true);
            coupleTrapdoors = builder.comment("Couple Trapdoors").define("coupleTrapdoors", true);
            builder.pop();
        }

        @SuppressWarnings("deprecation")
        private String getPrettyValueName(Object value) {
            return Arrays.stream(value.toString().toLowerCase().split("_"))
                    .map(WordUtils::capitalize)
                    .collect(Collectors.joining());
        }
    }
}