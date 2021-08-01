package net.galaxycore.galaxycorecore.permissions;

import lombok.Getter;
import net.galaxycore.galaxycorecore.permissions.exceptions.MissingFieldException;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;

@Getter
public class LuckPermsApiWrapper {
    private final Player player;
    private final LuckPerms api;
    private final User user;
    private final Group primaryGroup;

    public LuckPermsApiWrapper(Player player){
        api = LuckPermsProvider.get();
        user = api.getUserManager().getUser(player.getUniqueId());

        assert user != null;

        primaryGroup = api.getGroupManager().getGroup(user.getPrimaryGroup());

        this.player = player;
    }
    public String getPermissionsPrefix(){
        @Nullable String prefix = primaryGroup.getCachedData().getMetaData().getPrefix();

        if (prefix != null)
            return prefix;
        else
            throw new MissingFieldException("Prefix");
    }

    public String getPermissionsColor(){
        @Nullable String suffix = primaryGroup.getCachedData().getMetaData().getSuffix();

        if (suffix != null)
            return suffix;
        else
            throw new MissingFieldException("Suffix");
    }

    public String getPermissionsDisplayName(){
        @Nullable String name = primaryGroup.getDisplayName();

        if (name != null)
            return name;
        else
            throw new MissingFieldException("DisplayName");
    }

    public int getPermissionsWeight(){
        OptionalInt weight = primaryGroup.getWeight();

        if (weight.isPresent())
            return weight.getAsInt();
        else
            throw new MissingFieldException("Weight");
    }

    public String getPermissionsGroupNameRaw(){
        return user.getPrimaryGroup();
    }

}
