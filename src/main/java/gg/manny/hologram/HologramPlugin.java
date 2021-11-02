package gg.manny.hologram;

import gg.manny.hologram.command.HologramCommand;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

import java.util.HashSet;
import java.util.Set;

@Getter
public class HologramPlugin extends JavaPlugin implements Listener {

    private static int HOLOGRAM_DISTANCE_RADIUS = 15;

    private static HologramPlugin instance;

    private boolean protocolSupport = false;

    private Set<Hologram> holograms = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;

        protocolSupport = getServer().getPluginManager().isPluginEnabled("ProtocolSupport");

        getCommand("hologram").setExecutor(new HologramCommand(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Displaying holograms...");
        for (Hologram hologram : holograms) {
            if (!hologram.getViewers().contains(player.getUniqueId())) {
                double distance = player.getLocation().distance(hologram.getLocation());
                if (distance > HOLOGRAM_DISTANCE_RADIUS) {
                    hologram.sendTo(player);
                }
            }
        }
    }

}