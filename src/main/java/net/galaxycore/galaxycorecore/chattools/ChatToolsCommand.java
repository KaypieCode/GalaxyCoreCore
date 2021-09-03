package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.chattools.haste.Haste;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.utils.PlayerUtils;
import net.galaxycore.galaxycorecore.utils.UrlUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class ChatToolsCommand implements CommandExecutor {
    @Getter
    private final GalaxyCoreCore galaxyCoreCore;

    public ChatToolsCommand(GalaxyCoreCore galaxyCoreCore) {
        this.galaxyCoreCore = galaxyCoreCore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) {
            PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.commandfail"));
            return true;
        }

        ChatMessage chatMessage = galaxyCoreCore.getChatBuffer().getMessage(Integer.parseInt(args[0]));

        if (chatMessage == null || (!player.hasPermission("core.chat.resend.bypass.deleted") && chatMessage.isDeleted())) {
            PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.msgnotfound"));
            return true;
        }

        if (args.length == 2 && args[1].equals("tdel") && player.hasPermission("core.chat.tools.tool.delete") && chatMessage.getElevation() <= ChatBuffer.getElevation(player)) {
            chatMessage.setDeleted(!chatMessage.isDeleted());
            galaxyCoreCore.getChatBuffer().resendChat(new ChatManager());

            PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.confirm"));
            return true;
        }

        if (args.length == 2 && args[1].equals("hastefrom")){
            ArrayList<String> buf = new ArrayList<>();

            for (ChatMessage message : galaxyCoreCore.getChatBuffer().getRingBuffer()){
                if (message.getId() < chatMessage.getId()) continue;
                if (message.isDeleted()) continue;
                if (message.isChat_clearer()) {buf = new ArrayList<>(); continue;}

                String msg = message.getMessage();

                msg = replaceColorCodes(msg);
                msg = ChatColor.translateAlternateColorCodes('&', msg);

                buf.add(msg);
            }

            String[] bufArray = new String[buf.size()];
            bufArray = buf.toArray(bufArray);

            try {
                String link = Haste.hasteMessages(bufArray);
                PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.haste.confirm") + link);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.error"));
                return true;
            }
        }

        PlayerUtils.sendMessage(player, "===================================");
        PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.name"));
        PlayerUtils.sendMessage(player, "");
        PlayerUtils.sendMessage(player, I18N.getByLang("de_DE", "core.chat.tools.themessage") + chatMessage.getMessage());
        PlayerUtils.sendMessage(player, "");

        if (player.hasPermission("core.chat.tools.tool.delete") && chatMessage.getElevation() <= ChatBuffer.getElevation(player)) {
            PlayerUtils.sendMessage(player, Component.text(I18N.getByLang("de_DE", "core.chat.tools." + (chatMessage.isDeleted() ? "undelete" : "delete"))).clickEvent(ClickEvent.runCommand("/chattools " + chatMessage.getId() + " tdel")));
        }

        PlayerUtils.sendMessage(player, Component.text(I18N.getByLang("de_DE", "core.chat.tools.haste")).clickEvent(ClickEvent.runCommand("/chattools " + chatMessage.getId() + " hastefrom")));
        PlayerUtils.sendMessage(player, Component.text(I18N.getByLang("de_DE", "core.chat.tools.copy")).clickEvent(ClickEvent.openUrl(UrlUtils.getCpUrl(chatMessage.getMessage(), I18N.getByLang("de_DE", "core.chat.tools.copy.website")))));
        PlayerUtils.sendMessage(player, "===================================");

        return true;
    }

    @NotNull
    public static String replaceColorCodes(String msg) {
        msg = msg.replace("&l", "");
        msg = msg.replace("§l", "");
        msg = msg.replace("&n", "");
        msg = msg.replace("§n", "");
        msg = msg.replace("&o", "");
        msg = msg.replace("§o", "");
        msg = msg.replace("&k", "");
        msg = msg.replace("§k", "");
        msg = msg.replace("&m", "");
        msg = msg.replace("§m", "");
        msg = msg.replace("&0", "");
        msg = msg.replace("§0", "");
        msg = msg.replace("&1", "");
        msg = msg.replace("§1", "");
        msg = msg.replace("&2", "");
        msg = msg.replace("§2", "");
        msg = msg.replace("&3", "");
        msg = msg.replace("§3", "");
        msg = msg.replace("&4", "");
        msg = msg.replace("§4", "");
        msg = msg.replace("&5", "");
        msg = msg.replace("§5", "");
        msg = msg.replace("&6", "");
        msg = msg.replace("§6", "");
        msg = msg.replace("&7", "");
        msg = msg.replace("§7", "");
        msg = msg.replace("&8", "");
        msg = msg.replace("§8", "");
        msg = msg.replace("&9", "");
        msg = msg.replace("§9", "");
        msg = msg.replace("&a", "");
        msg = msg.replace("§a", "");
        msg = msg.replace("&b", "");
        msg = msg.replace("§b", "");
        msg = msg.replace("&c", "");
        msg = msg.replace("§c", "");
        msg = msg.replace("&d", "");
        msg = msg.replace("§d", "");
        msg = msg.replace("&e", "");
        msg = msg.replace("§e", "");
        msg = msg.replace("&f", "");
        msg = msg.replace("§f", "");
        msg = msg.replace("&r", "");
        msg = msg.replace("§r", "");
        return msg;
    }
}
