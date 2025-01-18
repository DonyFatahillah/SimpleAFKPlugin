package org.molfordan.simpleAFKPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class afkCommand implements TabExecutor {
    private final HashSet<UUID> afkPlayers = new HashSet<>();

    private final HashMap<UUID, String> afkReasons = new HashMap<>();

    private final Main plugin;

    public afkCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players were allowed to use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {

            toggleAFK(player, null);

            return true;
        } else {

            //String reason = args[0];

            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < args.length; i++){
                builder.append(args[i]);
                builder.append(" ");
            }

            String finalMessage = builder.toString();
            finalMessage = finalMessage.stripTrailing();

            toggleAFK(player, finalMessage);


        }
        return true;
    }

    public void toggleAFK(Player player, String reason) {
        if (!afkPlayers.contains(player.getUniqueId())) {
            setAfk(player, true, reason);
        } else {
            setAfk(player, false, reason);
        }
    }

    public void setAfk(Player player, boolean isAfk, String reason) {

        String afkMessage = plugin.getConfig().getString("afk-message");
        String reasonConfig = plugin.getConfig().getString("reason");
        String noLongerAFK = plugin.getConfig().getString("no-longer-afk");

        if (isAfk) {
            afkPlayers.add(player.getUniqueId());
            afkReasons.put(player.getUniqueId(), reason);
            if (reason != null) {
                if (afkMessage == null || reasonConfig == null) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "* " + player.getName() + " is now AFK! Reason: " + afkReasons.get(player.getUniqueId()));
                } else {
                    afkMessage = afkMessage.replace("%player%", player.getName());

                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', afkMessage) + " " + ChatColor.translateAlternateColorCodes('&', reasonConfig) + " : "+ afkReasons.get(player.getUniqueId()));
                }
            } else {
                if (afkMessage == null) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "* " + player.getName() + " is now AFK!");
                } else {
                    afkMessage = afkMessage.replace("%player%", player.getName());
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', afkMessage));
                }
            }
        } else {
            afkPlayers.remove(player.getUniqueId());
            afkReasons.remove(player.getUniqueId());
            if (noLongerAFK == null) {
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " is no longer AFK!");
            } else {
                noLongerAFK = noLongerAFK.replace("%player%", player.getName());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', noLongerAFK));
            }
        }
    }

    public boolean isAfk(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public String getAfkReason(Player player) {
        return afkReasons.getOrDefault(player.getUniqueId(), "No reason provided");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            suggestions.add("reason");
        } else if (args.length > 1){
            suggestions.add("more reasons");
        }

        return suggestions;
    }
}
