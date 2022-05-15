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
    private val core = CoreProvider.getCore()
    private val prevVanishedPlayers = mutableSetOf<Player>()
    private val manager = ProtocolLibrary.getProtocolManager().asynchronousManager

    init {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.isVanished) {
                vanishedPlayers.value.add(onlinePlayer)
                prevVanishedPlayers.add(onlinePlayer)
            }
        }


        vanishedPlayers.updatelistener {
            val deltaExit: Set<Player> = prevVanishedPlayers.minus(vanishedPlayers.value)
            val deltaIn: Set<Player> = vanishedPlayers.value.minus(prevVanishedPlayers)
            deltaExit.forEach(this::unvanish)
            deltaIn.forEach(this::vanish)
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
    }

    private fun vanish(player: Player) {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("galaxycore.vanish.tablist")) {
                // Send "Remove Player" packet
                manager.packetStream.sendServerPacket(onlinePlayer, core.packetFactory.createEntityDestroyPacket(player.entityId))
                continue
            }
            if (!player.hasPermission("galaxycore.vanish.see")) {
                onlinePlayer.hidePlayer(core, player)
            }
        }
    }

    private fun unvanish(player: Player) {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(core, player)
        }

        vanishStateRestore?.invoke(player)
    }

    companion object {
        val vanishedPlayers = Reactive(mutableSetOf<Player>())
        var vanishStateRestore: ((Player) -> Unit)? = null
    }
}