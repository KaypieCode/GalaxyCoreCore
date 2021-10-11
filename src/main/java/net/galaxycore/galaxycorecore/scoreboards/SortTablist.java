package net.galaxycore.galaxycorecore.scoreboards;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SortTablist extends Thread {

    private GalaxyCoreCore core;

    public SortTablist(GalaxyCoreCore core) {
        this.core = core;
        new SortTablist(() -> Bukkit.getScheduler().runTaskTimer(core, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                setTablistAndScoreboard(onlinePlayer);
            }
        }, 20, 20));
    }

    public void setTablistAndScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() ? Bukkit.getScoreboardManager().getNewScoreboard() : player.getScoreboard();
        LuckPermsApiWrapper playerPerms = new LuckPermsApiWrapper(player);

        Bukkit.getScheduler().runTaskAsynchronously(core, () -> {

            String height = String.format("%04d", 9999 - playerPerms.getPermissionsWeight());
            String playerName = calculatePlayerName(player);

            if (!Objects.equals(player.playerListName(), Component.text(playerName)))
                player.playerListName(Component.text(playerName));

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String teamplayername = player.getName();

                if (teamplayername.length() > 11)
                    teamplayername = player.getName().substring(0, 11);

                Team playerTeam = getTeam(onlinePlayer.getScoreboard(), height + teamplayername,
                        playerPerms.getPermissionsPrefix(), "");
                if (!playerTeam.hasEntry(player.getName()))
                    playerTeam.addEntry(player.getName());
            }

            // Scoreboard start

            Objective objective = scoreboard.getObjective("net.gc");
            if (objective == null)
                objective = scoreboard.registerNewObjective(
                        "net.gc",
                        "dummy",
                        Component.text(ScoreBoardController.getScoreBoardCallback().getTitle(player)),
                        RenderType.INTEGER
                );
            else
                objective.displayName(Component.text(ScoreBoardController.getScoreBoardCallback().getTitle(player)));

            Objective finalObjective = objective;

            AtomicInteger c = new AtomicInteger();
            IntStream.rangeClosed(0, 4).forEach(value -> {
                String[] kv = ScoreBoardController.getScoreBoardCallback().getKV(player, value);

                finalObjective.getScore(
                        getScoreboardUpdateTeam(
                                scoreboard,
                                ScoreBoardController.getTeamUniques().get(c.getAndIncrement()),
                                "",
                                "")
                ).setScore(15 - value * 3);

                finalObjective.getScore(
                        getScoreboardUpdateTeam(
                                scoreboard,
                                ScoreBoardController.getTeamUniques().get(c.getAndIncrement()),
                                "§7» ",
                                kv[0])
                ).setScore(14 - value * 3);

                finalObjective.getScore(
                        getScoreboardUpdateTeam(
                                scoreboard,
                                ScoreBoardController.getTeamUniques().get(c.getAndIncrement()),
                                "  " + kv[1],
                                kv[2])
                ).setScore(13 - value * 3);
            });

            if (objective.getDisplaySlot() != (ScoreBoardController.getScoreBoardCallback().active(player) ? DisplaySlot.SIDEBAR : DisplaySlot.BELOW_NAME))
                objective.setDisplaySlot(ScoreBoardController.getScoreBoardCallback().active(player) ? DisplaySlot.SIDEBAR : DisplaySlot.BELOW_NAME);

            // Scoreboard end

            player.setScoreboard(scoreboard);
        });
    }

    public String calculatePlayerName(Player player) {

        LuckPermsApiWrapper playerPerms = new LuckPermsApiWrapper(player);

        return (playerPerms.getPermissionsPrefix() + player.getName());

    }

    @SuppressWarnings("deprecation")
    public Team getTeam(Scoreboard sb, String Team, String prefix, String suffix, ChatColor displayColor) {
        Team team = sb.getTeam(Team);
        if (team == null)
            team = sb.registerNewTeam(Team);
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(true);
        team.setColor(displayColor);
        return team;
    }

    public Team getTeam(Scoreboard sb, String team, String prefix, String suffix) {
        return getTeam(sb, team, prefix, suffix, getLastEffectiveColor(prefix));
    }

    public String getScoreboardUpdateTeam(Scoreboard sb, String unique, String prefix, String suffix) {
        Team team = getTeam(sb, unique, prefix, suffix, ChatColor.WHITE);

        if (!team.getEntries().contains(unique + "§e"))
            team.addEntry(unique + "§e");

        return unique + "§e";
    }

    private ChatColor getLastEffectiveColor(String s) {
        String lastColors = ChatColor.getLastColors(s);
        if (lastColors.length() > 2) {
            throw new UnsupportedOperationException("Can not convert format codes into Colors");
        }
        return ChatColor.getByChar(lastColors.toCharArray()[lastColors.toCharArray().length - 1]);
    }

    private SortTablist(Runnable runnable) {
        super(runnable);
        start();
    }

    @Override
    public synchronized void start() {
        System.out.println("Starting Tablist Sort Thread...");
        super.start();
    }

    public void shutdown() {
        System.out.println("Shutting down Tablist Sort Thread...");
        this.interrupt();
    }

}
