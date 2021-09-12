package net.galaxycore.galaxycorecore.tabcompletion;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerTabCompleteListener implements Listener {

    public PlayerTabCompleteListener(GalaxyCoreCore core) {
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTabComplete(PlayerCommandSendEvent event) {
        event.getCommands().removeIf((command) -> command.contains(":"));
    }

}
