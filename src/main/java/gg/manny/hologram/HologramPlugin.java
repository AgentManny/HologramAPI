package gg.manny.hologram;

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

    public static HologramPlugin getInstance() {
        return instance;
    }
}