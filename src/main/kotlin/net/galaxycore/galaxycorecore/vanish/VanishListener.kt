package net.galaxycore.galaxycorecore.vanish

import com.comphenix.protocol.ProtocolLibrary
import net.galaxycore.galaxycorecore.apiutils.CoreProvider
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent


class VanishListener : Listener {
    private val core = CoreProvider.core
    private val prevVanishedPlayers = mutableSetOf<Player>()

    init {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.isVanished) {
                vanishedPlayers.value.add(onlinePlayer)
                prevVanishedPlayers.add(onlinePlayer)
            }
        }


        vanishedPlayers.updatelistener {
            val deltaIn: Set<Player> = vanishedPlayers.value.minus(prevVanishedPlayers)
            deltaIn.forEach(this::vanish)

            for (player in prevVanishedPlayers) {
                if (vanishedPlayers.value.contains(player)) {
                    continue
                }
                unvanish(player)
            }
        }

    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isVanished) {
            vanishedPlayers.update {
                it.add(event.player)
                it
            }
        }


        for (player in vanishedPlayers.value) {
            vanish(player)
        }
    }

    private fun vanish(player: Player) {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue
            if (!onlinePlayer.hasPermission("galaxycore.vanish.see")) {
                onlinePlayer.hidePlayer(core, player)
            }
        }
        prevVanishedPlayers.add(player)

    }

    private fun unvanish(player: Player) {
        println("unvanish")
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            println("unvanish onlinePlayer ${onlinePlayer.name}")
            if (onlinePlayer == player) continue
            onlinePlayer.showPlayer(core, player)
        }

        prevVanishedPlayers.remove(player)
        vanishStateRestore?.invoke(player)
    }

    companion object {
        val vanishedPlayers = Reactive(mutableSetOf<Player>())
        var vanishStateRestore: ((Player) -> Unit)? = null
        fun isVanished(player: Player) = player.isVanished
    }
}