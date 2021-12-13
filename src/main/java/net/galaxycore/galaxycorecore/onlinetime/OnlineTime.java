package net.galaxycore.galaxycorecore.onlinetime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class OnlineTime {
    private double hours;
    private double minutes;
    private static final HashMap<Player, Long> currentInterpolationOnlineTime = new HashMap<>(); /* Millis */
    private static final HashMap<Player, Long> lastSaveMillis = new HashMap<>();
    public static final OnlineTime zero = new OnlineTime(0, 0);

    public static OnlineTime getOnlimeTime(Player player) {
        if (PlayerLoader.load(player) == null)
            return zero;
        long currentInterpolationTime = currentInterpolationOnlineTime.get(player) + System.currentTimeMillis() - lastSaveMillis.get(player);
        lastSaveMillis.put(player, System.currentTimeMillis());
        currentInterpolationOnlineTime.put(player, currentInterpolationTime);

        return new OnlineTime(
                Math.floor((currentInterpolationTime / 60000F) / 60F),
                Math.floor((currentInterpolationTime / 60000F) % 60)
        );

    }

}
