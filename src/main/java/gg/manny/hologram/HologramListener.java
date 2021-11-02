package gg.manny.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HologramListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Hologram hologram : HologramAPI.getHolograms().values()) {
            if (hologram.getLocation().getWorld().equals(player.getWorld())) {
                double distance = player.getLocation().distance(hologram.getLocation());
                if (!hologram.getViewers().contains(player.getUniqueId())) {
                    if (distance < CraftHologram.DISTANCE_RADIUS) {
                        hologram.sendTo(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // We don't need to send a destroy packet since the entity doesn't exist
        for (Hologram hologram : HologramAPI.getHolograms().values()) {
            hologram.getViewers().removeIf(id -> player.getUniqueId().equals(id));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.getX() != from.getX() || to.getZ() != from.getZ()) {
            for (Hologram hologram : HologramAPI.getHolograms().values()) {
                if (hologram.getLocation().getWorld().equals(player.getWorld())) {
                    double distance = player.getLocation().distance(hologram.getLocation());
                    if (hologram.getViewers().contains(player.getUniqueId())) {
                        if (distance > CraftHologram.DISTANCE_RADIUS) {
                            hologram.destroy(player);
                        }
                    } else {
                        if (distance < CraftHologram.DISTANCE_RADIUS) {
                            hologram.sendTo(player);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerWorldSwitch(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = event.getWorld();
        for (Hologram hologram : HologramAPI.getHolograms().values()) {
            boolean sameWorld = hologram.getLocation().getWorld().equals(world);
            if (hologram.getViewers().contains(player.getUniqueId()) && !sameWorld) {
                hologram.destroy(player);
            } else if (sameWorld) {
                hologram.sendTo(player);
            }
        }
    }
}
