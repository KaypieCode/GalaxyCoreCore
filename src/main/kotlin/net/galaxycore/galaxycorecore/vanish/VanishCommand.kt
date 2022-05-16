package net.galaxycore.galaxycorecore.vanish

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VanishCommand : CommandExecutor {
    /**
     * Executes the given command, returning its success.
     * <br></br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("core.vanish")) {
            player.sendMessage(I18N.getC(player, "core.nopermissions"))
            return true
        }

        player.isVanished = !player.isVanished

        if (player.isVanished) {
            player.sendMessage(I18N.getC(player, "core.vanish.on"))
            VanishListener.vanishedPlayers.update {
                it.add(player)
                it
            }
        } else {
            player.sendMessage(I18N.getC(player, "core.vanish.off"))
            VanishListener.vanishedPlayers.update {
                it.remove(player)
                it
            }
        }

        return true
    }
}