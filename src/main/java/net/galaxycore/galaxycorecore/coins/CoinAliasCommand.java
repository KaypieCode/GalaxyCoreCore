package net.galaxycore.galaxycorecore.coins;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoinAliasCommand implements CommandExecutor, TabCompleter {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && (label.equalsIgnoreCase("bal") || label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("eco"))) {
            Bukkit.getServer().dispatchCommand(sender, "coins bal " + String.join(" ", args));
            return true;
        }

        if (args.length == 2 && label.equalsIgnoreCase("pay")) {
            Bukkit.getServer().dispatchCommand(sender, "coins pay " + String.join(" ", args));
            return true;
        }

        if (label.equalsIgnoreCase("pay"))
            sender.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(((Player) sender), "core.command.coins.pay.usage"), new LuckPermsApiWrapper((Player) sender)));
        else
            sender.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(((Player) sender), "core.command.coins.bal.usage"), new LuckPermsApiWrapper((Player) sender)));

        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param alias   The alias used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> tabcompletions = new ArrayList<>();

        if (args.length == 1)
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                if (onlinePlayer.getName().startsWith(args[0]))
                    tabcompletions.add(onlinePlayer.getName());

        return tabcompletions;
    }
}
