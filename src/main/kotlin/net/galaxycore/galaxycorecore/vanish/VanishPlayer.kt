package net.galaxycore.galaxycorecore.vanish

import net.galaxycore.galaxycorecore.apiutils.CoreProvider
import net.galaxycore.galaxycorecore.configuration.PlayerLoader
import org.bukkit.entity.Player
import java.sql.PreparedStatement

var Player.isVanished: Boolean
    get() = PlayerLoader.load(this).isVanished
    set(value) {
        val id = PlayerLoader.load(this).id
        val load: PreparedStatement =
            CoreProvider.getCore().databaseConfiguration.connection.prepareStatement("UPDATE `core_playercache` SET isVanished = ? WHERE id = ?")
        load.setBoolean(1, value)
        load.setInt(2, id)
        load.executeUpdate()
        load.close()
        PlayerLoader.loadNew(this)
    }
