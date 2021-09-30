package net.galaxycore.galaxycorecore.scoreboards;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

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
        Scoreboard scoreboard = player.getScoreboard();
        LuckPermsApiWrapper playerPerms = new LuckPermsApiWrapper(player);

        Bukkit.getScheduler().runTaskAsynchronously(core, () -> {

            String height = String.format("%04d", 9999 - playerPerms.getPermissionsWeight());
            String playerName = calculatePlayerName(player);

            if(!Objects.equals(player.playerListName(), Component.text(playerName)))
                player.playerListName(Component.text(playerName));

            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String teamplayername = player.getName();

                if(teamplayername.length() > 11)
                    teamplayername = player.getName().substring(0, 11);

                Team playerTeam = getTeam(onlinePlayer.getScoreboard(), height + teamplayername,
                        playerPerms.getPermissionsPrefix(), "");
                if(!playerTeam.hasEntry(player.getName()))
                    playerTeam.addEntry(player.getName());
            }

            // Scoreboard start

            Objective objective = scoreboard.getObjective("net.gc");
            if(objective == null)
                objective = scoreboard.registerNewObjective(
                        "net.gc",
                        "dummy",
                        Component.text(ScoreBoardController.getScoreBoardCallback().getTitle(player)),
                        RenderType.INTEGER
                );
            else
                objective.displayName(Component.text(ScoreBoardController.getScoreBoardCallback().getTitle(player)));



            for (int i = 0; i < 16; i++) {
                objective.getScore(getScoreboardUpdateTeam(scoreboard, ScoreBoardController.getTeamUniques().get(i), "Test" + i, "")).setScore(i);
            }

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

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

    public Team getScoreboardUpdateTeam(Scoreboard sb, String unique, String prefix, String suffix) {
        return getTeam(sb, unique, prefix, suffix, ChatColor.BLUE);
    }

    private ChatColor getLastEffectiveColor(String s) {
        String lastColors = ChatColor.getLastColors(s);
        if(lastColors.length() > 2) {
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
