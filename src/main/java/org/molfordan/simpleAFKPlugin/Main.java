package org.molfordan.simpleAFKPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save the default config if not already saved
        saveDefaultConfig();

        long timeout = getConfig().getInt("timeout-time", 1) * 60 * 1000L;


        // Create command executors
        afkCommand afkCommand = new afkCommand(this);

        // Register the AFK command
        getCommand("afk").setExecutor(afkCommand);
        getCommand("setafkmessage").setExecutor(new setAFKMessage(this));
        getCommand("settimeout").setExecutor(new afkSetTimeoutCommand(this));


        // Create and register the MoveEvent listener
        onMoveEvent onMoveEvent = new onMoveEvent(this, afkCommand, timeout);
        getServer().getPluginManager().registerEvents(onMoveEvent, this);

        // Schedule the task to check for inactive players every minute (1200 ticks)
        getServer().getScheduler().runTaskTimer(this, onMoveEvent::checkInactivePlayers, 20L, 20L * 1);


        // Register additional listeners if needed
        getServer().getPluginManager().registerEvents(new onMentionPlayer(this, afkCommand), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic (if needed)
    }
}
