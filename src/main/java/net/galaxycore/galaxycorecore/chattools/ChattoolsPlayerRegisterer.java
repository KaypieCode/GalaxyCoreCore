package net.galaxycore.galaxycorecore.chattools;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class ChattoolsPlayerRegisterer implements Listener {
    @NonNull
    private GalaxyCoreCore core;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new ChatManager().registerPlayer(event.getPlayer(), core.getChatBuffer().getCurrentId());
    }
}
