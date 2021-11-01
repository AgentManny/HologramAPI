package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.command.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Set;

@RequiredArgsConstructor
public class ListArgument implements CommandArgument {

    private final HologramPlugin plugin;

    @Override
    public String description() {
        return "List all holograms";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Set<Hologram> holograms = plugin.getHolograms();
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Holograms (" + ChatColor.YELLOW + holograms.size() + ChatColor.LIGHT_PURPLE + "):");
        for (Hologram hologram : holograms) {
            sender.sendMessage(ChatColor.YELLOW + " - " + ChatColor.WHITE + hologram);
        }
    }

}
