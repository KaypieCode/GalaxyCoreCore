package net.galaxycore.galaxycorecore.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PlayerLoader {
    @Getter
    private static final HashMap<UUID, PlayerLoader> loaderHashMap = new HashMap<>();

    private final int id;
    private final UUID uuid;
    private final String lastname;
    private final Date firstlogin;
    private final Date lastlogin;
    private final Date last_daily_reward;
    private final int banpoints;
    private final int mutepoints;
    private final int warnpoints;
    private final int reports;
    private final boolean teamlogin;
    private final boolean debug;
    private final boolean socialspy;
    private final boolean commandspy;
    private final boolean vanished;
    private final boolean nicked;
    private final int lastnick;
    private final long coins;

    public static PlayerLoader loadNew(Player player){
        PlayerLoader playerLoader = PlayerLoader.newLoader(player);

        loaderHashMap.put(player.getUniqueId(), playerLoader);

        return playerLoader;
    }

    public static PlayerLoader load(Player player) {
        if (loaderHashMap.containsKey(player.getUniqueId())) {
            return loaderHashMap.get(player.getUniqueId());
        }

        return loadNew(player);
    }

    private static PlayerLoader newLoader(Player player) {
        return null;
    }

}
