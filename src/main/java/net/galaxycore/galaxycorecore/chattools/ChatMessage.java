package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import lombok.Setter;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import org.bukkit.OfflinePlayer;

@Getter
public class ChatMessage {
    private final int id;
    private final OfflinePlayer player;
    private final String message;
    private final int elevation;

    @Setter private boolean deleted = false;
    @Setter private boolean chat_clearer = false;
    @Setter private OtherChatMessageTypes othertype = OtherChatMessageTypes.NORMAL;
    @Setter private LuckPermsApiWrapper frozen_lp = null;

    public ChatMessage(int id, OfflinePlayer player, String message, int elevation){
        this.id = id;
        this.player = player;
        this.message = message;
        this.elevation = elevation;
    }
}
