package gg.manny.hologram;

import gg.manny.hologram.command.HologramCommand;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

@Getter
public class HologramPlugin extends JavaPlugin implements Listener {

    private static int HOLOGRAM_DISTANCE_RADIUS = 15;

    private static HologramPlugin instance;

    private Set<Hologram> holograms = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;

        getCommand("hologram").setExecutor(new HologramCommand(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static int getProtocolVersion(Player player) {
        return player.getProtocolVersion(); // TODO Hook into ProtocolSupport
    }

    public static boolean onLegacyVersion(Player player) {
        return getProtocolVersion(player) < 5;
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