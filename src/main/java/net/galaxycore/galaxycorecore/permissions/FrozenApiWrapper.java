package net.galaxycore.galaxycorecore.permissions;

import org.bukkit.entity.Player;

public class FrozenApiWrapper extends LuckPermsApiWrapper {

    private final String permissionsPrefix;
    private final String permissionsColor;
    private final String permissionsDisplayName;
    private final int permissionsWeight;
    private final String permissionsGroupNameRaw;
    private final String name;
    private final boolean chatImportant;

    public FrozenApiWrapper(LuckPermsApiWrapper unfrozenWrapper) {
        name = unfrozenWrapper.getPlayerName();
        permissionsPrefix = unfrozenWrapper.getPermissionsPrefix();
        permissionsColor = unfrozenWrapper.getPermissionsColor();
        permissionsDisplayName = unfrozenWrapper.getPermissionsDisplayName();
        permissionsWeight = unfrozenWrapper.getPermissionsWeight();
        permissionsGroupNameRaw = unfrozenWrapper.getPermissionsGroupNameRaw();
        chatImportant = unfrozenWrapper.isChatImportant();
    }

    public static FrozenApiWrapper wrapAutomatically(Player player) {
        return new FrozenApiWrapper(new LuckPermsApiWrapper(player));
    }

    @Override
    public String getPermissionsPrefix() {
        return permissionsPrefix;
    }

    @Override
    public String getPermissionsColor() {
        return permissionsColor;
    }

    @Override
    public String getPermissionsDisplayName() {
        return permissionsDisplayName;
    }

    @Override
    public int getPermissionsWeight() {
        return permissionsWeight;
    }

    @Override
    public String getPermissionsGroupNameRaw() {
        return permissionsGroupNameRaw;
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public boolean isChatImportant() {
        return chatImportant;
    }
}
