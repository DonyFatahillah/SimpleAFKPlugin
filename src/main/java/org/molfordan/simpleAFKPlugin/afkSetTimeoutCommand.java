package org.molfordan.simpleAFKPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class afkSetTimeoutCommand implements TabExecutor {

    private final Main plugin;

    public afkSetTimeoutCommand(Main plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify a timeout value in minutes.");
            return true;
        }

        try {
            // Parse the new timeout value from the first argument
            int newTimeout = Integer.parseInt(args[0]);

            // Ensure the timeout is a valid positive value
            if (newTimeout <= 0) {
                sender.sendMessage(ChatColor.RED + "The timeout value must be a positive number.");
                return true;
            }

            // Get the config file and update the value
            FileConfiguration config = plugin.getConfig();
            config.set("timeout-time", newTimeout);
            plugin.saveConfig(); // Save changes to the file

            // Notify the sender
            sender.sendMessage(ChatColor.GREEN + "Timeout value has been updated to " + newTimeout + " minutes!");

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The timeout value must be a valid number.");
            return false;
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            suggestions.add("value");
        }

        return suggestions;
    }
}
