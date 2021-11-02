package gg.manny.hologram.command;

import gg.manny.hologram.command.argument.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class HologramCommand implements CommandExecutor {

    private Map<String, CommandArgument> commandMap = new HashMap<>();

    public HologramCommand() {
        commandMap.put("additem", new HologramAddItemArgument());
        commandMap.put("addline", new HologramAddLineArgument());
        commandMap.put("create", new HologramCreateArgument());
        commandMap.put("info", new HologramInfoArgument());
        commandMap.put("list", new HologramListArgument());
        commandMap.put("remove", new HologramRemoveArgument());
        commandMap.put("setline", new HologramSetLineArgument());
        commandMap.put("setitem", new HologramSetItemArgument());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.");
            return true;
        }

        String arg = args.length == 0 ? "help" : args[0].toLowerCase();
        if (arg.equals("help")) {
            sender.sendMessage(ChatColor.GOLD + "Hologram commands (" + ChatColor.WHITE + commandMap.size() + ChatColor.GOLD + "):");
            commandMap.forEach((name, argument) -> {
                String description = argument.description();
                String usage = argument.usage();
                sender.sendMessage("  " + ChatColor.WHITE + "/" + label + " " + name + (usage.isEmpty() ? "" : " " + usage + " ") + (description.isEmpty() ? "" : ChatColor.GRAY + " (" + argument.description() + ")"));
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
