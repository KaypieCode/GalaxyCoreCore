package net.galaxycore.galaxycorecore.utils;

import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import org.bukkit.OfflinePlayer;

public class StringUtils {
    public static String replaceRelevant(String input, LuckPermsApiWrapper wrapper){
        input = input.replaceAll("%player%", wrapper.getPlayer().getName());
        input = input.replaceAll("%rank_displayname%", wrapper.getPermissionsDisplayName());
        input = input.replaceAll("%rank_color%", wrapper.getPermissionsColor());
        input = input.replaceAll("%rank_prefix%", wrapper.getPermissionsPrefix());
        input = input.replaceAll("%rank_name%", wrapper.getPermissionsGroupNameRaw());
        input = input.replaceAll("%chat_important%", wrapper.getPlayer().hasPermission("core.chat.important") ? "§c" : "");

        return input;
    }
    public static String replaceRelevantNoPermissions(String input, OfflinePlayer player) {
        input = input.replaceAll("%player%", String.valueOf(player.getName()));

        return input;
    }
}
