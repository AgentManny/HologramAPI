package gg.manny.hologram.command.argument;

import gg.manny.hologram.CraftHologram;
import gg.manny.hologram.Hologram;
import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.command.CommandArgument;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DebugArgument implements CommandArgument {

    private final HologramPlugin plugin;
    private Hologram hologram;

    @Override
    public String description() {
        return "List all holograms";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Version: " + plugin.getProtocolVersion(player) + " (Legacy: " + plugin.onLegacyVersion(player) + ")");
            if (hologram == null) {
                CraftHologram test = new CraftHologram("Test", player.getLocation());
                test.addLines(ChatColor.GREEN + "This is a hologram :)", ChatColor.YELLOW + "Can we handle two");
//            test.addItem(new ItemStack(Material.MUSHROOM_SOUP));
                test.sendTo(player);
                hologram = test;
            } else {
                if (args.length != 1) {
                    hologram.setLine(0, args[1]);
                    sender.sendMessage("Updating text to: " + args[1]);
                }
            }
        }
    }

}
