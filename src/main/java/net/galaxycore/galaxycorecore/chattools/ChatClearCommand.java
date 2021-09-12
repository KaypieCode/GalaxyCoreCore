package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.MessageProvider;
import net.galaxycore.galaxycorecore.permissions.FrozenApiWrapper;
import net.galaxycore.galaxycorecore.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

@Getter
public class ChatClearCommand implements CommandExecutor {
    private final GalaxyCoreCore core;

    public ChatClearCommand(GalaxyCoreCore core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("core.command.chat.clear")) {
            PlayerUtils.sendMessage(player, MessageProvider.getNoPermissionMessage());
            return true;
        }

        ChatMessage fillerMessage = new ChatMessage(core.getChatBuffer().getCurrentId(), player, String.join("", Collections.nCopies(300, "\n")), 401);

        fillerMessage.setChat_clearer(true);
        fillerMessage.setFrozen_lp(FrozenApiWrapper.wrapAutomatically(player));

        core.getChatBuffer().getRingBuffer().add(fillerMessage);
        core.getChatBuffer().setCurrentId(core.getChatBuffer().getCurrentId() + 1);

        core.getChatBuffer().resendChat(new ChatManager());

        return true;
    }
}
