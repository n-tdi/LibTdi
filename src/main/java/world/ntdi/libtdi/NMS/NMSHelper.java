package world.ntdi.libtdi.NMS;

import org.bukkit.Bukkit;

public class NMSHelper {
    private static String version = null;

    public static String getNMSVersion() {
        if (version == null) {
            String[] split = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            version = split[split.length - 1];
        }

        return version;
    }
}
