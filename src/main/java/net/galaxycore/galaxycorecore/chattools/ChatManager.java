package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ChatManager {
    @Getter
    private static final HashMap<UUID, Integer> messageIdAfterJoin = new HashMap<>();

    public void registerPlayer(Player player, Integer currentId) {
        getMessageIdAfterJoin().put(player.getUniqueId(), currentId);
    }

    public void sendToAll(Component msg) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(msg));
    }

    public void sendToAllI18NPrepAfterId(String key, LuckPermsApiWrapper wrapper, int id) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (getMessageIdAfterJoin().get(player.getUniqueId()) <= id)
                player.sendMessage(StringUtils.replaceRelevant(I18N.getInstanceRef().get().get("de_DE", key), wrapper));
        });
    }

    public void sendToAllAfterId(Component msg, int id) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (getMessageIdAfterJoin().get(player.getUniqueId()) <= id) player.sendMessage(msg);
        });
    }

    public void sendToPermissionAfterId(Component msg, String permission, int id) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission) && getMessageIdAfterJoin().get(player.getUniqueId()) <= id)
                player.sendMessage(msg);
        });
    }

    public void sendToNoPermissionAfterId(Component msg, String permission, int id) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.hasPermission(permission) && getMessageIdAfterJoin().get(player.getUniqueId()) <= id)
                player.sendMessage(msg);
        });
    }

    public void sendToPermission(Component msg, String permission) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission)) player.sendMessage(msg);
        });
    }

    public void sendToNoPermission(Component msg, String permission) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.hasPermission(permission)) player.sendMessage(msg);
        });
    }
}
