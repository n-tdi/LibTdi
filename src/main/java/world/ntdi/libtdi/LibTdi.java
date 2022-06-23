package world.ntdi.libtdi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LibTdi extends JavaPlugin {
    public static LibTdi libTdi;
    public static int midVersion;

    @Override
    public void onLoad() {
        libTdi = this;
    }

    @Override
    public void onEnable() {
        midVersion = getMidVersion();
    }

    private static int getMidVersion() {
        Pattern pattern = Pattern.compile("1\\.([0-9]+)");
        Matcher matcher = pattern.matcher(Bukkit.getBukkitVersion());
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getInstance() {
        return (Plugin)(libTdi != null ? libTdi.getInstance() : JavaPlugin.getProvidingPlugin(LibTdi.class));
    }
}
