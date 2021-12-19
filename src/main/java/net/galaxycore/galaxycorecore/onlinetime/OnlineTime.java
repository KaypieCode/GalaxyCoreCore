package net.galaxycore.galaxycorecore.onlinetime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class OnlineTime implements Listener {
    private double hours;
    private double minutes;
    private static final HashMap<Player, Long> currentInterpolationOnlineTime = new HashMap<>(); /* Millis */
    private static final HashMap<Player, Long> lastSaveMillis = new HashMap<>();
    public static final OnlineTime zero = new OnlineTime(0, 0);

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        currentInterpolationOnlineTime.remove(event.getPlayer());
        lastSaveMillis.remove(event.getPlayer());
    }

    @SneakyThrows
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (PlayerLoader.load(player) == null)
            return;

        PreparedStatement select_query = CoreProvider.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT `onlinetime` FROM `core_onlinetime` WHERE `id`=?");
        select_query.setInt(1, PlayerLoader.load(player).getId());
        ResultSet select_answer = select_query.executeQuery();

        if (!select_answer.next()) {
            select_answer.close();
            select_query.close();

            PreparedStatement statement = CoreProvider.getCore().getDatabaseConfiguration().getConnection().prepareStatement("INSERT INTO `core_onlinetime` VALUES (?, 0)");
            statement.setInt(1, PlayerLoader.load(player).getId());
            statement.executeUpdate();

            currentInterpolationOnlineTime.put(player, 0L);
            lastSaveMillis.put(player, System.currentTimeMillis());
            return;
        }

        currentInterpolationOnlineTime.put(player, select_answer.getLong("onlinetime"));
        lastSaveMillis.put(player, System.currentTimeMillis());
        select_answer.close();
        select_query.close();
    }

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
