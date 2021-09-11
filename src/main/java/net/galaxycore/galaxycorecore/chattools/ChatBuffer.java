package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import lombok.Setter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.playerFormatting.PlayerJoinLeaveListener;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.Optional;

@Getter
public class ChatBuffer {
    private final GalaxyCoreCore galaxyCoreCore;
    private final CircularFifoQueue<ChatMessage> ringBuffer;
    @Setter private int currentId;

    public ChatBuffer(GalaxyCoreCore galaxyCoreCore) {
        this.galaxyCoreCore = galaxyCoreCore;

        int chatBufferSize = Integer.parseInt(galaxyCoreCore.getCoreNamespace().get("chat.maxbufferlength"));

        this.ringBuffer = new CircularFifoQueue<>(chatBufferSize);
        this.currentId = 0;
    }

    public static int getElevation(Permissible playerLike) {
        for (int elevation = 0; elevation < 400; elevation++) {
            if (!playerLike.hasPermission("core.chat.message.elevated." + elevation)) {
                return elevation;
            }
        }

        return 400;
    }

    public int addMessage(Player player, String message) {
        this.ringBuffer.add(new ChatMessage(currentId, player, message, ChatBuffer.getElevation(player)));
        currentId++;

        return currentId - 1;
    }

    public ChatMessage getMessage(int id) {
        Optional<ChatMessage> optionalChatMessage = ringBuffer.stream().filter(chatMessage -> chatMessage.getId() == id).findFirst();
        return optionalChatMessage.orElse(null);
    }

    public void clearChatNoPerms(ChatManager chatManager) {
        for (int i = 0; i < 300; i++) {
            chatManager.sendToAll(Component.text(""));
        }
    }

    public void resendChat(ChatManager chatManager) {
        clearChatNoPerms(chatManager);
        getRingBuffer().forEach(chatMessage -> {
            Component component = Component.text("§8 [§7☰§8]").hoverEvent(HoverEvent.showText(Component.text(I18N.getInstanceRef().get().get("de_DE", "core.chat.tools.open")))).clickEvent(ClickEvent.runCommand("/chattools " + chatMessage.getId()));

            if (chatMessage.getOthertype() == OtherChatMessageTypes.I18N_PJL) {
                PlayerJoinLeaveListener.sendPJLMessage(chatMessage, chatManager);
                return;
            }


            if (chatMessage.isChat_clearer()){
                chatManager.sendToNoPermissionAfterId(Component.text(chatMessage.getMessage()), "core.command.chat.clear.bypass", chatMessage.getId());
                chatManager.sendToAllAfterId(Component.text(StringUtils.replaceRelevantNoPermissions(I18N.getInstanceRef().get().get("de_DE", "core.chat.clear.placeholder"), chatMessage.getPlayer())), chatMessage.getId());
                return;
            }

            if (!chatMessage.isDeleted())
                chatManager.sendToAllAfterId(Component.text(chatMessage.getMessage()).append(component), chatMessage.getId());
            else
                chatManager.sendToPermissionAfterId(Component.text("§c[DELETED]§7 " + chatMessage.getMessage()).append(component), "core.chat.resend.bypass.deleted", chatMessage.getId());
        });
    }
}
