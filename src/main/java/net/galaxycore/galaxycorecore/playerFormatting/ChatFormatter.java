package net.galaxycore.galaxycorecore.playerFormatting;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.II18NPort;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.playerFormatting.events.FormattedChatMessageEvent;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEventSource;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Getter
public class ChatFormatter implements Listener {
    private final GalaxyCoreCore galaxycorecore;
    private final ConfigNamespace configNamespace;

    public ChatFormatter(GalaxyCoreCore galaxycorecore) {
        this.galaxycorecore = galaxycorecore;
        this.configNamespace = galaxycorecore.getCoreNamespace();
    }

    @SuppressWarnings("deprecation") // Paper wants its Components, but it wouldn't be performant to use those here
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        handleChatMessage(event, new LuckPermsApiWrapper(event.getPlayer()));

        FormattedChatMessageEvent chatMessageEvent = new FormattedChatMessageEvent(event.getPlayer(), event.getMessage(), event.getFormat());

        event.setCancelled(true);

        Bukkit.getServer().getPluginManager().callEvent(chatMessageEvent);

        if (chatMessageEvent.isCancelled())
            return;

        int id = galaxycorecore.getChatBuffer().addMessage(event.getPlayer(), event.getFormat());

        TextComponent chatTools = new TextComponent("§8 [§7☰§8]");

        chatTools.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(I18N.getInstanceRef().get().get(event.getPlayer(), "core.chat.tools.open"))));
        chatTools.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chattools " + id));

        Bukkit.broadcast(new TextComponent(new TextComponent(event.getFormat()), chatTools));
    }

    @SuppressWarnings("deprecation") // Paper wants its Components, but it wouldn't be performant to use those here
    public void handleChatMessage(AsyncPlayerChatEvent event, LuckPermsApiWrapper permissionsApiWrapper) {
        String message = event.getMessage();
        if (event.getPlayer().hasPermission("core.chat.format")) {
            message = message.replace("&l", "§l");
            message = message.replace("&n", "§n");
            message = message.replace("&o", "§o");
            message = message.replace("&k", "§k");
            message = message.replace("&m", "§m");
        }

        if (event.getPlayer().hasPermission("core.chat.color")) {
            message = message.replace("&0", "§0");
            message = message.replace("&1", "§1");
            message = message.replace("&2", "§2");
            message = message.replace("&3", "§3");
            message = message.replace("&4", "§4");
            message = message.replace("&5", "§5");
            message = message.replace("&6", "§6");
            message = message.replace("&7", "§7");
            message = message.replace("&8", "§8");
            message = message.replace("&9", "§9");
            message = message.replace("&a", "§a");
            message = message.replace("&b", "§b");
            message = message.replace("&c", "§c");
            message = message.replace("&d", "§d");
            message = message.replace("&e", "§e");
            message = message.replace("&f", "§f");
            message = message.replace("&r", "§r");
        }

        message = message.replace("%", "⁒");

        String format = configNamespace.get("chat.format");
        format = StringUtils.replaceRelevant(format, permissionsApiWrapper);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format + message + " §f";

        event.setMessage(message);
        event.setFormat(format);
    }
}
