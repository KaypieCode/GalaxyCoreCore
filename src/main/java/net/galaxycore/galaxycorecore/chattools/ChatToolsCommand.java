package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.utils.PlayerUtils;
import net.galaxycore.galaxycorecore.utils.UrlUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatToolsCommand implements CommandExecutor {
    @Getter
    private final GalaxyCoreCore galaxyCoreCore;

    public ChatToolsCommand(GalaxyCoreCore galaxyCoreCore) {
        this.galaxyCoreCore = galaxyCoreCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        System.out.println(galaxyCoreCore.getChatBuffer().getRingBuffer());

        if (args.length < 1) {
            PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.commandfail"));
            return true;
        }

        ChatMessage chatMessage = galaxyCoreCore.getChatBuffer().getMessage(Integer.parseInt(args[0]));

        if (chatMessage == null || (!player.hasPermission("core.chat.resend.bypass.deleted") && chatMessage.isDeleted())) {
            PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.msgnotfound"));
            return true;
        }

        if (args.length == 2 && args[1].equals("tdel")) {
            chatMessage.setDeleted(!chatMessage.isDeleted());
            galaxyCoreCore.getChatBuffer().resendChat(new ChatManager());

            PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.confirm"));
            return true;
        }

        PlayerUtils.sendMessage(player, "===================================");
        PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.name"));
        PlayerUtils.sendMessage(player, "");
        PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.themessage") + chatMessage.getMessage());
        PlayerUtils.sendMessage(player, "");
        PlayerUtils.sendMessage(player, Component.text(I18N.getByLang("de_DE", "core.chat.tools." + (chatMessage.isDeleted() ? "undelete" : "delete"))).clickEvent(ClickEvent.runCommand("/chattools " + chatMessage.getId() + " tdel")));
        PlayerUtils.sendMessage(player, Component.text(I18N.getByLang("de_DE", "core.chat.tools.copy")).clickEvent(ClickEvent.openUrl(UrlUtils.getCpUrl(chatMessage.getMessage(), I18N.getByLang("de_DE", "core.chat.tools.copy.website")))));
        PlayerUtils.sendMessage(player, "===================================");

        return true;
    }
}
