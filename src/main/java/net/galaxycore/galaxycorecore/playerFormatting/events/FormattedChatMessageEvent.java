package net.galaxycore.galaxycorecore.playerFormatting.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FormattedChatMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;

    @Getter
    private final String message;

    @Getter
    @Setter
    private String formatted;

    @Getter
    @Setter
    private boolean cancelled = false;

    public FormattedChatMessageEvent(Player player, String message, String formatted) {
        super(true);

        this.player = player;
        this.message = message;
        this.formatted = formatted;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
