package net.galaxycore.galaxycorecore.vanish

import net.galaxycore.galaxycorecore.apiutils.CoreProvider
import net.galaxycore.galaxycorecore.configuration.PlayerLoader
import org.bukkit.entity.Player
import java.sql.PreparedStatement

var Player.isVanished: Boolean
    get(): Boolean {
        return PlayerLoader.load(this)?.vanished ?: false
    }
    set(value) {
        val id = PlayerLoader.load(this).id
        val load: PreparedStatement =
            CoreProvider.core.databaseConfiguration.connection.prepareStatement("UPDATE `core_playercache` SET vanished = ? WHERE id = ?")
        load.setBoolean(1, value)
        load.setInt(2, id)
        load.executeUpdate()
        load.close()
        PlayerLoader.loadNew(this)
    }
