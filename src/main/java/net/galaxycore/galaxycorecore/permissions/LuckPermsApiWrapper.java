package net.galaxycore.galaxycorecore.permissions;

import lombok.Getter;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import net.galaxycore.galaxycorecore.permissions.exceptions.MissingFieldException;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;

@Getter
public class LuckPermsApiWrapper {
    private final Player player;
    private final LuckPermsApi api;
    private final User user;
    private final Group primaryGroup;

    public LuckPermsApiWrapper(Player player){
        api = LuckPerms.getApi();
        user = api.getUser(player.getUniqueId());

        assert user != null;

        primaryGroup = api.getGroup(user.getPrimaryGroup());

        this.player = player;
    }
    public String getPermissionsPrefix(){
        @Nullable String prefix = primaryGroup.getCachedData().getMetaData(Contexts.allowAll()).getPrefix();

        if (prefix != null)
            return prefix;
        else
            throw new MissingFieldException("Prefix");
    }

    public String getPermissionsChatColor(){
        @Nullable String suffix = primaryGroup.getCachedData().getMetaData(Contexts.allowAll()).getSuffix();

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
