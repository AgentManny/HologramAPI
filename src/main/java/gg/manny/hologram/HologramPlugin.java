package gg.manny.hologram;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HologramPlugin extends JavaPlugin {

    private static HologramPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static int getProtocolVersion(Player player) {
        return player.getProtocolVersion(); // TODO Hook into ProtocolSupport
    }

    public static HologramPlugin getInstance() {
        return instance;
    }
}