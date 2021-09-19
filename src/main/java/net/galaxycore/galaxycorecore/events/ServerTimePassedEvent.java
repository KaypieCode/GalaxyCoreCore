package net.galaxycore.galaxycorecore.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ServerTimePassedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public ServerTimePassedEvent() {
        super(false);
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
