package net.galaxycore.galaxycorecore.playerFormatting;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.chattools.ChatManager;
import net.galaxycore.galaxycorecore.chattools.ChatMessage;
import net.galaxycore.galaxycorecore.chattools.OtherChatMessageTypes;
import net.galaxycore.galaxycorecore.permissions.FrozenApiWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;


@RequiredArgsConstructor
public class PlayerJoinLeaveListener implements Listener {

    public @NonNull GalaxyCoreCore core;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ChatMessage chatMessage = new ChatMessage(core.getChatBuffer().getCurrentId(), player, "core.event.join", 500);
        chatMessage.setOthertype(OtherChatMessageTypes.I18N_PJL);
        chatMessage.setFrozen_lp(FrozenApiWrapper.wrapAutomatically(player));

        core.getChatBuffer().setCurrentId(core.getChatBuffer().getCurrentId() + 1);

        sendPJLMessage(chatMessage, new ChatManager());

        core.getChatBuffer().getRingBuffer().add(chatMessage);

        event.joinMessage(Component.text(""));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ChatMessage chatMessage = new ChatMessage(core.getChatBuffer().getCurrentId(), player, "core.event.leave", 500);
        chatMessage.setOthertype(OtherChatMessageTypes.I18N_PJL);
        chatMessage.setFrozen_lp(FrozenApiWrapper.wrapAutomatically(player));

        core.getChatBuffer().setCurrentId(core.getChatBuffer().getCurrentId() + 1);

        sendPJLMessage(chatMessage, new ChatManager());

        core.getChatBuffer().getRingBuffer().add(chatMessage);

        event.quitMessage(Component.text(""));
    }

    public static void sendPJLMessage(ChatMessage message, ChatManager manager){
        manager.sendToAllI18NPrepAfterId(message.getMessage(), message.getFrozen_lp(), message.getId());
    }
}
