package net.galaxycore.galaxycorecore.utils;

import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;

public class StringUtils {
    public static String replaceRelevant(String input, LuckPermsApiWrapper wrapper){
        input = input.replaceAll("%player%", wrapper.getPlayer().getName());
        input = input.replaceAll("%rank_displayname%", wrapper.getPermissionsDisplayName());
        input = input.replaceAll("%rank_color%", wrapper.getPermissionsChatColor());
        input = input.replaceAll("%rank_prefix%", wrapper.getPermissionsPrefix());
        input = input.replaceAll("%rank_name%", wrapper.getPermissionsGroupNameRaw());
        input = input.replaceAll("%chat_important%", wrapper.getPlayer().hasPermission("core.chat.important") ? "§c" : "");

        return input;
    }
}
