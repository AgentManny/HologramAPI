package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import gg.manny.hologram.line.HologramItemLine;
import gg.manny.hologram.line.HologramLine;
import gg.manny.hologram.line.HologramTextLine;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HologramInfoArgument implements CommandArgument {

    @Override
    public String description() {
        return "Information on a hologram";
    }

    @Override
    public String usage() {
        return "<id>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram info " + usage());
            return;
        }

        Optional<Hologram> hologramOptional = HologramAPI.getHologram(args[1]);
        if (!hologramOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Hologram " + args[1] + " not found.");
            return;
        }

        Hologram hologram = hologramOptional.get();
        Location location = hologram.getLocation();
        List<HologramLine> lines = hologram.getLines();

        new FancyMessage(ChatColor.WHITE + hologram.getId() + ChatColor.GOLD + " Hologram" + ChatColor.GRAY + " (" + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")")
                .suggest(ChatColor.YELLOW + "Click to teleport")
                .command("/tppos " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getWorld().getName())
                .send(sender);
        sender.sendMessage(ChatColor.GRAY + "Viewers: " + ChatColor.WHITE + hologram.getViewers().stream()
                .map((id) -> {
                    Player viewer = Bukkit.getPlayer(id);
                    return viewer == null ? id.toString() : viewer.getName();
                })
                .collect(Collectors.joining(", ")));
        for (int i = 1; i <= lines.size(); i++) {
            HologramLine line = lines.get(i - 1);
            String formattedLine = line instanceof HologramTextLine ? ((HologramTextLine)line).getText() : line instanceof HologramItemLine ? WordUtils.capitalize(((HologramItemLine)line).getItem().getType().name().toLowerCase()) : "Unknown";
            new FancyMessage(ChatColor.GOLD.toString() + i + ". " + ChatColor.WHITE + formattedLine)
                    .tooltip(ChatColor.AQUA + line.getClass().getSimpleName())
                    .send(sender);
        }
    }
}
