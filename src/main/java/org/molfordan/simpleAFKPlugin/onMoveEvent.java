package org.molfordan.simpleAFKPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class onMoveEvent implements Listener {

    private final Main plugin;

    private final afkCommand afkCommand;
    private final HashMap<UUID, Long> lastMovement = new HashMap<>();
    private long AFK_TIMEOUT;// 2 minutes in milliseconds


    public onMoveEvent(Main plugin, afkCommand afkCommand, long timeout) {
        this.plugin = plugin;
        this.afkCommand = afkCommand;
        this.AFK_TIMEOUT = timeout;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Check if the player's movement is only rotation (not actual movement)
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        // Update last movement timestamp
        lastMovement.put(playerId, System.currentTimeMillis());

        // If the player is marked as AFK and moves, remove their AFK status
        if (afkCommand.isAfk(player)) {
            afkCommand.setAfk(player, false, null);
        }
    }

    // Method to check for inactive players
    public void checkInactivePlayers() {
        long currentTime = System.currentTimeMillis();
        for (UUID playerId : lastMovement.keySet()) {
            Player player = org.bukkit.Bukkit.getPlayer(playerId);
            if (player != null && !afkCommand.isAfk(player)) {
                long lastMove = lastMovement.get(playerId);
                String timeoutmsg = plugin.getConfig().getString("timeout-message");
                int timeoutTimes = plugin.getConfig().getInt("timeout-time");
                if ((currentTime - lastMove) >= AFK_TIMEOUT) {
                    afkCommand.setAfk(player, true, null);

                    if (timeoutmsg == null) {

                        // Mark as AFK
                        player.sendMessage(ChatColor.GREEN + "You are now set to AFK after" + timeoutTimes + "minutes idling");
                    } else {
                        timeoutmsg = timeoutmsg.replace("%timeout-time%", String.valueOf(timeoutTimes));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', timeoutmsg));
                    }
                }
            }
        }
    }
}
