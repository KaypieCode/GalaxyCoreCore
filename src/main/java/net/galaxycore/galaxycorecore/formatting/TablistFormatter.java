package net.galaxycore.galaxycorecore.formatting;

import lombok.Getter;
import net.galaxycore.galaxycorecore.Galaxycorecore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@Getter
public class TablistFormatter implements Listener {
    private final Galaxycorecore galaxycorecore;
    private final ConfigNamespace configNamespace;

    public TablistFormatter(Galaxycorecore galaxycorecore){
        this.galaxycorecore = galaxycorecore;
        this.configNamespace = galaxycorecore.getCoreNamespace();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        LuckPermsApiWrapper luckPermsApiWrapper = new LuckPermsApiWrapper(player);

        String playerName = StringUtils.replaceRelevant(configNamespace.get("tablist.format"), luckPermsApiWrapper);

        // Paper wants its Components, it would be not performant to use that here
        //noinspection deprecation
        player.setPlayerListName(playerName);
    }
}
