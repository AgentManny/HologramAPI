package gg.manny.hologram.command.argument;

import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramAPI;
import gg.manny.hologram.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HologramRemoveArgument implements CommandArgument {

    @Override
    public String description() {
        return "Remove a hologram";
    }

    @Override
    public String usage() {
        return "<name>";
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

        Optional<Hologram> hologram = HologramAPI.getHologram(args[1]);
        if (!hologram.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Hologram " + args[1] + " doesn't exist");
            return;
        }

        hologram.ifPresent(holo -> {
            holo.destroy();
            HologramAPI.getHolograms().remove(holo.getId());
            sender.sendMessage(ChatColor.GOLD + "Removed " + ChatColor.WHITE + holo.getId() + ChatColor.GOLD + " hologram.");
        });

    }
}
