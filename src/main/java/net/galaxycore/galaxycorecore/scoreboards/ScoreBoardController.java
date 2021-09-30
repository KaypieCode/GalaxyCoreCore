package net.galaxycore.galaxycorecore.scoreboards;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ScoreBoardController implements IScoreBoardCallback{
    @Getter
    @Setter
    private static IScoreBoardCallback scoreBoardCallback;

    @Getter
    private static final ArrayList<String> teamUniques = new ArrayList<>();

    public ScoreBoardController(){
        teamUniques.add("§1"); //        // 15
        teamUniques.add("§2"); // Key    // 14
        teamUniques.add("§3"); // Value  // 13
        teamUniques.add("§4"); //        // 12
        teamUniques.add("§5"); // Key    // 11
        teamUniques.add("§6"); // Value  // 10
        teamUniques.add("§7"); //        // 09
        teamUniques.add("§8"); // Key    // 08
        teamUniques.add("§9"); // Value  // 07
        teamUniques.add("§0"); //        // 06
        teamUniques.add("§a"); // Key    // 05
        teamUniques.add("§b"); // Value  // 04
        teamUniques.add("§c"); //        // 03
        teamUniques.add("§d"); // Key    // 02
        teamUniques.add("§e"); // Value  // 01
        teamUniques.add("§f"); //        // 00
    }

    /**
     * Get The Key-Value Pair for a Scoreboard entry
     *
     * @param player The Player, the Scoreboard is generated for
     * @param id     The number of the scoreboard entry [0,1,2,3,4]
     * @return [Key, Value]
     */
    @Override
    public String[] getKV(Player player, int id) {
        String[] kv = new String[3];
        kv[0] = "Server-Name";
        kv[1] = "Galaxy";
        kv[2] = "Core.net";
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
