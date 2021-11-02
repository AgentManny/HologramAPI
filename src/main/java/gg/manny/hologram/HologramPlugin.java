package gg.manny.hologram;

import gg.manny.hologram.command.HologramCommand;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

@Getter
public class HologramPlugin extends JavaPlugin implements Listener {

    private static HologramPlugin instance;

    private boolean protocolSupport = false;

    @Override
    public void onEnable() {
        instance = this;

        protocolSupport = getServer().getPluginManager().isPluginEnabled("ProtocolSupport");

        getCommand("hologram").setExecutor(new HologramCommand(this));

        getServer().getPluginManager().registerEvents(new HologramListener(), this);
    }

    @Override
    public void onDisable() {
        for (Hologram hologram : HologramAPI.getHolograms().values()) {
            hologram.destroy();
        }

        instance = null;
    }

    public int getProtocolVersion(Player player) {
        if (protocolSupport) {
            return ProtocolSupportAPI.getProtocolVersion(player).getId();
        }
        return player.getProtocolVersion();
    }

    public boolean onLegacyVersion(Player player) {
        if (protocolSupport) {
            return ProtocolSupportAPI.getProtocolVersion(player).isBefore(ProtocolVersion.MINECRAFT_1_8);
        }
         return getProtocolVersion(player) >= 5;
    }

    public static HologramPlugin getInstance() {
        return instance;
    }

}