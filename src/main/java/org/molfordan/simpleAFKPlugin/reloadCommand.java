package org.molfordan.simpleAFKPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class reloadCommand implements TabExecutor {

    private final Main plugin;

    public reloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Reload the configuration
        plugin.reloadConfig();




        // Send feedback to the command sender
        sender.sendMessage(ChatColor.GREEN + "[" + ChatColor.RED + "SimpleAFKPlugin" + ChatColor.GREEN + "] has been reloaded!");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {



        if (args.length > 1){
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }
}
