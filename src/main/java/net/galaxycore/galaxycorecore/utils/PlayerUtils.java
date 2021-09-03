package net.galaxycore.galaxycorecore.utils;

import net.galaxycore.galaxycorecore.configuration.PrefixProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static void sendMessage(Player player, String message) {
        player.sendMessage(PrefixProvider.getPrefix() + message);
    }

    public static void sendMessage(Player player, Component message) {
        player.sendMessage(Component.text(PrefixProvider.getPrefix()).append(message));
    }
}
