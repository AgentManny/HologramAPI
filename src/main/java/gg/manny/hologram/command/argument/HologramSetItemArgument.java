package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import gg.manny.hologram.line.HologramLine;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class HologramSetItemArgument implements CommandArgument {

    @Override
    public String description() {
        return "Set a item to a hologram";
    }

    @Override
    public String usage() {
        return "<name> <id>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
            return;
        }

        Player player = (Player) sender;
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram setline <id> " + usage());
            return;
        }

        Optional<Hologram> hologramOptional = HologramAPI.getHologram(args[1]);
        if (!hologramOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Hologram " + args[1] + " not found.");
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null) {
            sender.sendMessage(ChatColor.RED + "You don't have an item in your hand.");
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
        hologram.setItem(index, item);
        sender.sendMessage(ChatColor.GOLD + "Set line " + ChatColor.WHITE + index + ChatColor.GOLD + " to \"" + ChatColor.RESET + WordUtils.capitalizeFully(item.getType().name().toLowerCase().replace("_", " ")) + ChatColor.GOLD + "\" for " + ChatColor.RESET + hologram.getId() + ChatColor.GOLD + ".");
    }
}