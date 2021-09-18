package net.galaxycore.galaxycorecore.configuration.internationalisation;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class I18NPlayerLoader implements Listener {
    @Getter
    @Setter
    @Inject
    public static I18NPlayerLoader playerLoaderInstance;


    @Getter
    private final HashMap<Player, String> languages = new HashMap<>();

    public I18NPlayerLoader(){
        Bukkit.getPluginManager().registerEvents(this, CoreProvider.getCore());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

    }

    public static String getLocale(Player player) {
        return playerLoaderInstance.getLanguages().getOrDefault(player, "en_GB");
    }
}
