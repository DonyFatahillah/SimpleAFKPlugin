package org.molfordan.simpleAFKPlugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class onMentionPlayer implements Listener {
    private final afkCommand afkCommand;

    private final Main plugin;

    public onMentionPlayer(Main plugin, afkCommand afkCommand) {
        this.plugin = plugin;
        this.afkCommand = afkCommand;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage().toLowerCase(); // Convert to lowercase for case-insensitive comparison

        String mentionAFKPlayer = plugin.getConfig().getString("mention-afk-player-message");

        // Check for mentions of players in the format "@playerName" or just player names
        for (Player mentionedPlayer : sender.getServer().getOnlinePlayers()) {
            String playerName = mentionedPlayer.getName().toLowerCase();

            // Check if the message contains the player's name
            if (message.contains(playerName)) {
                // Check if the mentioned player is AFK
                if (afkCommand.isAfk(mentionedPlayer)) {
                    // Retrieve the reason for AFK
                    String reason = afkCommand.getAfkReason(mentionedPlayer);
                    String reasonConfig = plugin.getConfig().getString("reason");
                    if (reason != null) {
                        if (mentionAFKPlayer == null) {
                            // Send a message to the sender that the mentioned player is AFK
                            sender.sendMessage(ChatColor.GREEN + "* " + mentionedPlayer.getName() + " is currently AFK! Reason: " + reason);
                        } else {
                            mentionAFKPlayer = mentionAFKPlayer.replace("%player%", mentionedPlayer.getName());

                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mentionAFKPlayer) + " " + ChatColor.translateAlternateColorCodes('&', reasonConfig) + " " + reason);
                        }
                    } else {
                        if (mentionAFKPlayer == null) {
                            sender.sendMessage(ChatColor.GREEN + mentionedPlayer.getName() + " is currently AFK!");
                        } else {
                            mentionAFKPlayer = mentionAFKPlayer.replace("%player%", mentionedPlayer.getName());

                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mentionAFKPlayer));
                        }
                    }

                }
            }
        }
    }
}
