package net.galaxycore.galaxycorecore.tablist;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class SortTablist extends Thread {

    private GalaxyCoreCore core;

    public SortTablist(GalaxyCoreCore core) {
        this.core = core;
        new SortTablist(() -> Bukkit.getScheduler().runTaskTimer(core, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                setTablist(onlinePlayer);
            }
        }, 20, 20));
    }

    public void setTablist(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        LuckPermsApiWrapper playerPerms = new LuckPermsApiWrapper(player);

        Bukkit.getScheduler().runTaskAsynchronously(core, () -> {

            String height = String.format("%05d", 99999 - playerPerms.getPermissionsWeight());
            String playerName = calculatePlayerName(player);

            if(!Objects.equals(player.playerListName(), Component.text(playerName)))
                player.playerListName(Component.text(playerName));

            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Team playerTeam = getTeam(onlinePlayer.getScoreboard(), height + player.getName(),
                        playerPerms.getPermissionsPrefix(), "");
                if(!playerTeam.hasEntry(player.getName()))
                    playerTeam.addEntry(player.getName());
            }

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
