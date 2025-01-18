package org.molfordan.simpleAFKPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class setAFKMessage implements TabExecutor {

    private final Main plugin;

    public setAFKMessage(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setafkmessage <option> <message>");
            return true;
        }

        String option = args[0].toLowerCase();
        String message = combineArgs(args, 1);

        FileConfiguration config = plugin.getConfig();
        String configKey = "";

        switch (option) {
            case "playerafkmessage":
                configKey = "afk-message";
                break;
            case "timeoutmessage":
                configKey = "timeout-message";
                break;
            case "nolongerafk":
                configKey = "no-longer-afk";
                break;
            case "mentionafk":
                configKey = "mention-afk-player-message";
                break;
            case "reason":
                configKey = "reason";
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid option. Valid options are: playerafkmessage, timeoutmessage, nolongerafk, mentionafk, reason.");
                return true;
        }

        config.set(configKey, message);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.GREEN + "The " + option + " has been set to: " + message);
        return true;
    }

    /**
     * Combines arguments from the specified start index into a single string.
     *
     * @param args  The array of arguments.
     * @param start The starting index to combine from.
     * @return A single string representing the combined arguments.
     */
    private String combineArgs(String[] args, int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString().stripTrailing();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions  = new ArrayList<>();

        if (args.length == 1){
            suggestions.add("playerafkmessage");
            suggestions.add("timeoutmessage");
            suggestions.add("nolongerafk");
            suggestions.add("mentionafk");
            suggestions.add("reason");
        } else if (args.length > 1){
            suggestions.add("messages");
        }


        return suggestions;
    }
}
