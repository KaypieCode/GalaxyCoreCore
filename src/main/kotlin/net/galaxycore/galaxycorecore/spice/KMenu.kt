package net.galaxycore.galaxycorecore.spice

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

abstract class KMenu : InventoryHolder {
    private lateinit var inventory: Inventory
    private val items: Array<KMenuItem?> = Array(this.getSize()) { null }

    abstract fun getNameI18NKey(): String
    abstract fun getSize(): Int

    protected fun item(position: Int, item: ItemStack): KMenuItem {
        val kitem = KMenuItem(position, item)
        this.items[position] = kitem

        kitem.itemStack.updatelistener {
            kitem.place(this.inventory)
        }
        return kitem
    }

    protected fun item(position: Int, material: Material, displayName: String, vararg lore: String): KMenuItem {
        return item(position, makeItem(material, displayName, *lore))
    }

    fun open(player: Player) {
        build(player)
        player.openInventory(this.inventory)
    }

    private fun build(player: Player) {
        inventory = Bukkit.createInventory(this, getSize(), Component.text(I18N.getByPlayer(player, getNameI18NKey())))
        val spacer = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ")

        for (i in items.indices) {
            val item = items[i]
            if (item != null) {
                item.place(inventory)
            } else {
                inventory.setItem(i, spacer)
            }
        }
    }

    open fun handleClick(event: InventoryClickEvent) {}

    fun onclick(inventoryClickEvent: InventoryClickEvent) {
        if (inventoryClickEvent.rawSlot < 0 || inventoryClickEvent.rawSlot >= getSize()) return
        items[inventoryClickEvent.slot]?.onClick(inventoryClickEvent)
        handleClick(inventoryClickEvent)
    }

    open fun onclose(inventoryCloseEvent: InventoryCloseEvent){}

    fun makeItem(material: Material, displayName: String, vararg lore: String): ItemStack {
        val item = ItemStack(material)
        val itemMeta = item.itemMeta
        itemMeta.displayName(Component.text(displayName))
        itemMeta.lore(listOf(*(lore.map { Component.text(it) }.toTypedArray())))
        item.itemMeta = itemMeta
        return item
    }

    override fun getInventory(): Inventory {
        return inventory
    }

    inner class KMenuItem (private val position: Int, itemStack: ItemStack) {
        var itemStack: Reactive<ItemStack> = Reactive(itemStack)
        var clickType: ClickType = ClickType.LEFT
        private var clickbacks: MutableList<((KMenuItem) -> Unit)> = mutableListOf()

        fun place(inventory: Inventory) {
            inventory.setItem(position, itemStack.value)
        }

        fun then(clickback: ((KMenuItem) -> Unit)): KMenuItem {
            this.clickbacks += clickback
            return this
        }

        fun onClick(inventoryClickEvent: InventoryClickEvent) {
            this.clickType = inventoryClickEvent.click
            clickbacks.forEach {it(this)}
        }
    }
}