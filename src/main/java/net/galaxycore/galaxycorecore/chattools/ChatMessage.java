package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;

@Getter
public class ChatMessage {
    private final int id;
    private final OfflinePlayer player;
    private final String message;

    @Setter private boolean deleted;

    public ChatMessage(int id, OfflinePlayer player, String message){
        this.id = id;
        this.player = player;
        this.message = message;
        this.deleted = false;
    }
}
