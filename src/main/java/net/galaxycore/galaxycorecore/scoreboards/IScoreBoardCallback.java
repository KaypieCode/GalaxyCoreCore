package net.galaxycore.galaxycorecore.scoreboards;


import org.bukkit.entity.Player;

public interface IScoreBoardCallback {
    /**
     * Get The Key-Value Pair for a Scoreboard entry
     *
     * @param player The Player, the Scoreboard is generated for
     * @param id The number of the scoreboard entry [0,1,2,3,4]
     * @return [Key, Prefix, Suffix]
     */
    String[] getKV(Player player, int id);

    /**
     * Get the Scoreboard Title
     *
     * @param player The Player, the Scoreboard is generated for
     * @return The Title for the Scoreboard
     */
    String getTitle(Player player);

    /**
     * Should the ScoreBoard be showed
     *
     * @param player The Player, the Scoreboard is generated for
     * @return The Display Status
     */
    boolean active(Player player);
}
