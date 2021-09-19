package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
                player.sendMessage(StringUtils.replaceRelevant(I18N.getInstanceRef().get().get(player, key), wrapper));
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

    public void sendToPermissionAfterIdWithExtra(TextComponent text, String s, int id, int chatmessage) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Component component = Component.text("§8 [§7☰§8]").hoverEvent(HoverEvent.showText(Component.text(I18N.getInstanceRef().get().get(player, "core.chat.tools.open")))).clickEvent(ClickEvent.runCommand("/chattools " + chatmessage));

            if (player.hasPermission(s) && getMessageIdAfterJoin().get(player.getUniqueId()) <= id)
                player.sendMessage(text.append(component));
        });
    }

    public void sendToAllAfterIdWithExtra(TextComponent text, int id, int chatmessage) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Component component = Component.text("§8 [§7☰§8]").hoverEvent(HoverEvent.showText(Component.text(I18N.getInstanceRef().get().get(player, "core.chat.tools.open")))).clickEvent(ClickEvent.runCommand("/chattools " + chatmessage));

            if (getMessageIdAfterJoin().get(player.getUniqueId()) <= id)
                player.sendMessage(text.append(component));
        });
    }
}
