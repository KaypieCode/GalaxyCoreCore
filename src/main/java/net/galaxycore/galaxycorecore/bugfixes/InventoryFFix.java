package net.galaxycore.galaxycorecore.bugfixes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class InventoryFFix implements Listener {
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (event.getPlayer().getOpenInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
    }
}
