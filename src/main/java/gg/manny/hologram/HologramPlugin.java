package gg.manny.hologram;

import gg.manny.hologram.command.HologramCommand;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

@Getter
public class HologramPlugin extends JavaPlugin {

    private static HologramPlugin instance;

    private Set<Hologram> holograms = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;

        getCommand("hologram").setExecutor(new HologramCommand(this));
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