package net.galaxycore.galaxycorecore.spice

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class KMenuListener : Listener {
    @EventHandler
    fun InventoryClickEvent.onMenuClick() {
        if (clickedInventory == null) return
        if (clickedInventory!!.holder !is KMenu) return
        val menu = clickedInventory!!.holder as KMenu
        isCancelled = true
        menu.onclick(this)
    }

    @EventHandler
    fun InventoryCloseEvent.onMenuClick() {
        if (inventory.holder !is KMenu) return
        val menu = inventory.holder as KMenu
        menu.onclose(this)
    }
}