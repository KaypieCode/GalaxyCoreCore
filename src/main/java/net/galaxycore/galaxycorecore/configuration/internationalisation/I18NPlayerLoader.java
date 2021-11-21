package net.galaxycore.galaxycorecore.configuration.internationalisation;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class I18NPlayerLoader implements Listener {
    @Getter
    @Setter
    public static I18NPlayerLoader playerLoaderInstance;


    @Getter
    private final HashMap<Player, String> languages = new HashMap<>();

    @SneakyThrows
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerLoader playerLoader = PlayerLoader.loadNew(player);
        if (playerLoader == null) {
            return;
        }


        PreparedStatement loadLanguage = CoreProvider.getCore().getDatabaseConfiguration().getConnection()
                .prepareStatement("SELECT language_id FROM I18N_player_data WHERE id=?");

        loadLanguage.setInt(1, playerLoader.getId());
        ResultSet loadResult = loadLanguage.executeQuery();

        if(!loadResult.next()){
            loadResult.close();
            loadLanguage.close();
            return;
        }

        AtomicReference<String> lang = new AtomicReference<>("");

        I18N.getInstanceRef().get().getLanguages().forEach((s, minecraftLocale) -> {
            try {
                if(loadResult.getInt("language_id") == minecraftLocale.getId()){
                    lang.set(s);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if(!lang.get().equals(""))
            languages.put(player, lang.get());

        loadResult.close();
        loadLanguage.close();
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event){
        languages.remove(event.getPlayer());
    }

    public static String getLocale(Player player) {
        return playerLoaderInstance.getLanguages().getOrDefault(player, "en_GB");
    }
}
