package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HologramAddLineArgument implements CommandArgument {

    @Override
    public String description() {
        return "Add a line to a hologram";
    }

    @Override
    public String usage() {
        return "<name> [text...]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram addline " + usage());
            return;
        }

        Optional<Hologram> hologramOptional = HologramAPI.getHologram(args[1]);
        if (!hologramOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Hologram " + args[1] + " not found.");
            return;
        }

        Hologram hologram = hologramOptional.get();
        String line = args.length < 3 ? " " : StringUtils.join(args, " ", 2, args.length);
        hologram.addLines(line);
        sender.sendMessage(ChatColor.GOLD + "Added \"" + ChatColor.WHITE + line + ChatColor.GOLD + "\" line to: " + ChatColor.RESET + hologram.getId());
    }
}
