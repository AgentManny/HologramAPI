package gg.manny.hologram.command.argument;

import gg.manny.hologram.CraftHologram;
import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCreateArgument implements CommandArgument {

    @Override
    public String description() {
        return "Creates a hologram";
    }

    @Override
    public String usage() {
        return "<name> <text...>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
            return;
        }

        Player player = (Player) sender;

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram create " + usage());
            return;
        }

        if (HologramAPI.getHologram(args[1]).isPresent()) {
            sender.sendMessage(ChatColor.RED + "Hologram " + args[1] + " already exists.");
            return;
        }

        String line = StringUtils.join(args, " ", 2, args.length);
        Hologram hologram = new CraftHologram(args[1], player.getLocation());
        hologram.addLines(line);
        hologram.send();
        HologramAPI.register(hologram);

        sender.sendMessage(ChatColor.GOLD + "Created " + ChatColor.WHITE + args[1] + ChatColor.GOLD + " hologram: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', line));

    }
}
