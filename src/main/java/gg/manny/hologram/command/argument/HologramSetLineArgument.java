package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HologramSetLineArgument implements CommandArgument {

    @Override
    public String description() {
        return "Set a line to a hologram";
    }

    @Override
    public String usage() {
        return "<name> <id> <text...>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram setline <id> " + usage());
            return;
        }

        Optional<Hologram> hologramOptional = HologramAPI.getHologram(args[1]);
        if (!hologramOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Hologram " + args[1] + " not found.");
            return;
        }

        int index;
        try {
            index = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Value " + args[2] + " not valid.");
            return;
        }

        Hologram hologram = hologramOptional.get();
        String line = StringUtils.join(args, " ", 3, args.length);
        hologram.setLine(index, line);
        sender.sendMessage(ChatColor.GOLD + "Set line " + ChatColor.WHITE + index + ChatColor.GOLD + " to \"" + ChatColor.RESET + line + ChatColor.GOLD + "\" for " + ChatColor.RESET + hologram.getId() + ChatColor.GOLD + ".");
    }
}