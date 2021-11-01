package gg.manny.hologram.command;

import org.bukkit.command.CommandSender;

public interface CommandArgument {

    default boolean validate(CommandSender sender) {
        return sender.isOp();
    }

    default String description() {
        return "";
    }

    void execute(CommandSender sender, String[] args);

}
