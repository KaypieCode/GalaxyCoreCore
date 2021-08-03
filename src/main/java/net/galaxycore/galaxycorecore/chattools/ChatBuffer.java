package net.galaxycore.galaxycorecore.chattools;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.entity.Player;

import java.util.Optional;

@Getter
public class ChatBuffer {
    private final GalaxyCoreCore galaxyCoreCore;
    private final CircularFifoQueue<ChatMessage> ringBuffer;
    private int currentId;

    public ChatBuffer(GalaxyCoreCore galaxyCoreCore){
        this.galaxyCoreCore = galaxyCoreCore;

        int chatBufferSize = Integer.parseInt(galaxyCoreCore.getCoreNamespace().get("chat.maxbufferlength"));

        this.ringBuffer = new CircularFifoQueue<>(chatBufferSize);
        this.currentId = 0;
    }

    public void addMessage(Player player, String message){
        this.ringBuffer.add(new ChatMessage(currentId, player, message));
        currentId ++;
    }

    public ChatMessage getMessage(int id){
        Optional<ChatMessage> optionalChatMessage = ringBuffer.stream().filter(chatMessage -> chatMessage.getId() == id).findFirst();
        return optionalChatMessage.orElse(null);
    }
}
