package net.galaxycore.galaxycorecore.chatlog;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.chattools.ChatToolsCommand;
import net.galaxycore.galaxycorecore.playerFormatting.events.FormattedChatMessageEvent;
import net.galaxycore.galaxycorecore.utils.DiscordWebhook;
import net.galaxycore.galaxycorecore.utils.ServerNameUtil;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ForkJoinPool;

public class ChatLog implements Listener {

    GalaxyCoreCore core;

    public ChatLog(GalaxyCoreCore core) {
        this.core = core;
    }

    @EventHandler
    public void onAsyncChat(FormattedChatMessageEvent event) throws IOException {
        handleChatMessage(event.getFormatted(), event.getPlayer(), ServerNameUtil.getName());
    }

    public void handleChatMessage(String message, Player player, String server) throws IOException {

        // Declare the neccessary variables
        DiscordWebhook webhook = new DiscordWebhook(core.getCoreNamespace().get("chatlog.webhook_url"));

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();

        message = ChatToolsCommand.replaceColorCodes(message);

        // Set the Details of the Embed
        embed.setDescription(quote(message));
        embed.setThumbnail("https://minotar.net/bust/" + player.getName() + "/190.png");
        embed.setFooter(server, "");

        // Set the Current time for better readability
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        embed.setAuthor(dateTimeFormatter.format(LocalDateTime.now()), "", "");

        // Check if the Message is a Command, and set the Sidebar Color accordingly
        if (message.startsWith("/")) {
            embed.setColor(Color.MAGENTA);
        }else {
            embed.setColor(Color.YELLOW);
        }

        // Add the embed to the Webhook and send the webhook to the URL
        webhook.addEmbed(embed);
        webhook.execute();

    }

    private String quote(String string) {
        return "`" + string + "`";
    }

}
