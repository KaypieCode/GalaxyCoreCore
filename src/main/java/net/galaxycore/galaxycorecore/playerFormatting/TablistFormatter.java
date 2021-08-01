package net.galaxycore.galaxycorecore.playerFormatting;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Getter
public class TablistFormatter implements Listener {
    private final GalaxyCoreCore galaxycorecore;
    private final ConfigNamespace configNamespace;

    public TablistFormatter(GalaxyCoreCore galaxycorecore){
        this.galaxycorecore = galaxycorecore;
        this.configNamespace = galaxycorecore.getCoreNamespace();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        LuckPermsApiWrapper luckPermsApiWrapper = new LuckPermsApiWrapper(player);

        // Paper wants its Components, but it wouldn't be performant to use those here
        //noinspection deprecation
        player.setPlayerListName(calculatePlayerListName(luckPermsApiWrapper));
    }

    public String calculatePlayerListName(LuckPermsApiWrapper apiWrapper){
        return StringUtils.replaceRelevant(configNamespace.get("tablist.format"), apiWrapper);
    }
}
