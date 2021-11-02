package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.command.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class HologramListArgument implements CommandArgument {

    private final HologramPlugin plugin;

    @Override
    public String description() {
        return "List all holograms";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Collection<Hologram> holograms = HologramAPI.getHolograms().values();
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GOLD + "Holograms (" + ChatColor.WHITE + holograms.size() + ChatColor.GOLD + "):");
        for (Hologram hologram : holograms) {
            List<String> lines = hologram.getTextLines();
            Location location = hologram.getLocation();
            sender.sendMessage(ChatColor.YELLOW + " - " + ChatColor.WHITE + hologram.getId() + ChatColor.GRAY +
                    " (Lines: " + ChatColor.WHITE + lines.size() + ChatColor.GRAY + ") " +
                    "(Location: " + ChatColor.WHITE + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ChatColor.GRAY + ")");
        }
    }

}
