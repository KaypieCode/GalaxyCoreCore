package net.galaxycore.galaxycorecore.scoreboards;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class ScoreBoardController implements IScoreBoardCallback{
    @Getter
    @Setter
    private static IScoreBoardCallback scoreBoardCallback = new ScoreBoardController();

    /**
     * Get The Key-Value Pair for a Scoreboard entry
     *
     * @param player The Player, the Scoreboard is generated for
     * @param id     The number of the scoreboard entry [0,1,2,3]
     * @return [Key, Value]
     */
    @Override
    public String[] getKV(Player player, int id) {
        String[] kv = new String[2];
        kv[0] = "Entry";
        kv[1] = "Value";
        return kv;
    }

    /**
     * Get the Scoreboard Title
     *
     * @param player The Player, the Scoreboard is generated for
     * @return The Title for the Scoreboard
     */
    @Override
    public String getTitle(Player player) {
        return "§5Galaxy§6Core";
    }
}
