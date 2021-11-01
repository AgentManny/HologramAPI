package gg.manny.hologram.command;

import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.command.argument.DebugArgument;
import gg.manny.hologram.command.argument.ListArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class HologramCommand implements CommandExecutor {

    private Map<String, CommandArgument> commandMap = new HashMap<>();

    public HologramCommand(HologramPlugin plugin) {
        commandMap.put("list", new ListArgument(plugin));
        commandMap.put("debug", new DebugArgument(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.");
            return true;
        }

        String arg = args.length == 0 ? "help" : args[0].toLowerCase();
        if (arg.equals("help")) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hologram commands (" + ChatColor.YELLOW + commandMap.size() + ChatColor.LIGHT_PURPLE + "):");
            commandMap.forEach((name, argument) -> {
                String description = argument.description();
                sender.sendMessage("  " + ChatColor.LIGHT_PURPLE + "/" + label + " " + name + (description.isEmpty() ? "" : ChatColor.YELLOW + " - " + argument.description()));
            });
            return true;
        }

        CommandArgument argument = commandMap.get(arg);
        if (argument == null) {
            sender.sendMessage(ChatColor.RED + "Argument " + arg + " not found for " + label + ".");
            return true;
        }

        if (argument.validate(sender)) {
            argument.execute(sender, args);
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use '/" + label + " " + arg + "'.");
        }
        return true;
    }
}
