package net.galaxycore.galaxycorecore.chattools;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class ChatManager {
    public void sendToAll(Component msg) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(msg));
    }

    public void sendToPermission(Component msg, String permission) {
        Bukkit.getOnlinePlayers().forEach(player -> {if (player.hasPermission(permission)) player.sendMessage(msg);});
    }

    public void sendToNoPermission(Component msg, String permission) {
        Bukkit.getOnlinePlayers().forEach(player -> {if (!player.hasPermission(permission)) player.sendMessage(msg);});
    }
}
