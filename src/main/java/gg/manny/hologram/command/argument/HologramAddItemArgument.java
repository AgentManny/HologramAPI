package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class HologramAddItemArgument implements CommandArgument {

    @Override
    public String description() {
        return "Adds an item in your hand as a hologram";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be ran by players.");
            return;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram additem");
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

        Hologram hologram = hologramOptional.get();
        hologram.addItem(item);
        sender.sendMessage(ChatColor.GOLD + "Set item " + ChatColor.WHITE + WordUtils.capitalizeFully(item.getType().name().toLowerCase().replace("_", " ")) + ChatColor.GOLD + " for " + ChatColor.RESET + hologram.getId() + ChatColor.GOLD + " hologram.");
    }
}
