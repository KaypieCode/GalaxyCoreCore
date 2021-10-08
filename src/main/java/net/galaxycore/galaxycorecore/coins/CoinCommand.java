package net.galaxycore.galaxycorecore.coins;

import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
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
import java.util.Objects;

public class CoinCommand implements CommandExecutor, TabCompleter {
    /**
     * Executes the given command, returning its success.
     * /coins [pay|set|add|remove|bal]
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
        if (args.length == 0) {
            CoinDAO coinDAO = new CoinDAO(PlayerLoader.load((Player) sender), CoreProvider.getCore());
            sender.sendMessage(String.format(I18N.getByPlayer(((Player) sender), "core.command.coins.bal"), coinDAO.get()));
            return true;
        }

        if (args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            return sendBal(sender, player);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("bal")) {
            Player player = Bukkit.getPlayer(args[1]);
            return sendBal(sender, player);
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("pay")) {
                CoinDAO daoOther = resolvePlayer(args, sender);
                if (daoOther == null) return true;

                try {
                    Long.parseLong(args[2]);
                } catch (NumberFormatException ignored) {
                    sender.sendMessage(I18N.getByPlayer(((Player) sender), "core.command.coins.nonumber"));
                    return true;
                }

                long transaction = Math.abs(Long.parseLong(args[2]));

                CoinDAO coinDAO = new CoinDAO(PlayerLoader.load((Player) sender), CoreProvider.getCore());
                coinDAO.transact(daoOther.getPlayer(), transaction, "coins_pay");

                sender.sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(((Player) sender), "core.command.coins.pay.success"), new LuckPermsApiWrapper(Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())))), transaction));
                Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())).sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())), "core.command.coins.pay.success.other"), new LuckPermsApiWrapper(((Player) sender))), transaction));

                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                CoinDAO daoOther = resolvePlayer(args, sender);
                if (daoOther == null) return true;

                try {
                    Long.parseLong(args[2]);
                } catch (NumberFormatException ignored) {
                    sender.sendMessage(I18N.getByPlayer(((Player) sender), "core.command.coins.nonumber"));
                    return true;
                }

                long transaction = -(Math.abs(Long.parseLong(args[2])) - daoOther.get());

                daoOther.transact(null, transaction, "coins_set::" + sender.getName());

                sender.sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(((Player) sender), "core.command.coins.set.success"), new LuckPermsApiWrapper(Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())))), daoOther.get()));
                Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())).sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())), "core.command.coins.set.success.other"), new LuckPermsApiWrapper(((Player) sender))), daoOther.get()));
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (transactImmutable(sender, args, true, "add")) return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (transactImmutable(sender, args, false, "remove")) return true;
            }
        }

        if (args.length > 3) {
            sender.sendMessage(I18N.getByPlayer(((Player) sender), "core.command.coins.usage"));
        }

        return true;
    }

    private boolean transactImmutable(@NotNull CommandSender sender, @NotNull String[] args, boolean doNegate, String type) {
        CoinDAO daoOther = resolvePlayer(args, sender);
        if (daoOther == null) return true;

        try {
            Long.parseLong(args[2]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage(I18N.getByPlayer(((Player) sender), "core.command.coins.nonumber"));
            return true;
        }

        long transaction;

        if (doNegate)
            transaction = -Math.abs(Long.parseLong(args[2]));
        else
            transaction = Math.abs(Long.parseLong(args[2]));

        daoOther.transact(null, transaction, "coins_" + type + "::" + sender.getName());

        sender.sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(((Player) sender), "core.command.coins." + type + ".success"), new LuckPermsApiWrapper(Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())))), daoOther.get()));
        Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())).sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(Objects.requireNonNull(Bukkit.getPlayer(daoOther.getPlayer().getUuid())), "core.command.coins." + type + ".success.other"), new LuckPermsApiWrapper(((Player) sender))), daoOther.get()));
        return true;
    }

    private CoinDAO resolvePlayer(String[] args, CommandSender sender) {
        Player player = Bukkit.getPlayer(args[1]);
        if (player != null) {
            return new CoinDAO(PlayerLoader.load(player), CoreProvider.getCore());
        }
        sender.sendMessage(I18N.getByPlayer(((Player) sender), "core.command.coins.player404"));
        return null;
    }

    private boolean sendBal(@NotNull CommandSender sender, Player player) {
        if (player != null) {
            CoinDAO coinDAO = new CoinDAO(PlayerLoader.load(player), CoreProvider.getCore());
            sender.sendMessage(String.format(StringUtils.replaceRelevant(I18N.getByPlayer(((Player) sender), "core.command.coins.bal.others"), new LuckPermsApiWrapper(player)), coinDAO.get()));
            return true;
        }

        sender.sendMessage(I18N.getByPlayer(((Player) sender), "core.command.coins.player404"));

        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
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
        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("core.command.coins.pay") && "pay".contains(args[0])) completions.add("pay");
            if (sender.hasPermission("core.command.coins.set") && "set".contains(args[0])) completions.add("set");
            if (sender.hasPermission("core.command.coins.add") && "add".contains(args[0])) completions.add("add");
            if (sender.hasPermission("core.command.coins.remove") && "remove".contains(args[0]))
                completions.add("remove");
            if (sender.hasPermission("core.command.coins.bal") && "bal".contains(args[0])) completions.add("bal");
        } else if (args.length == 2) {
            if (
                    (sender.hasPermission("core.command.coins.pay") && args[0].equalsIgnoreCase("pay")) ||
                            (sender.hasPermission("core.command.coins.set") && args[0].equalsIgnoreCase("set")) ||
                            (sender.hasPermission("core.command.coins.add") && args[0].equalsIgnoreCase("add")) ||
                            (sender.hasPermission("core.command.coins.remove") && args[0].equalsIgnoreCase("remove")) ||
                            (sender.hasPermission("core.command.coins.bal") && args[0].equalsIgnoreCase("bal"))
            ) {
                Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
            }
        }

        return completions;
    }
}
