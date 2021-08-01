package net.galaxycore.galaxycorecore.playerFormatting;

import lombok.Getter;
import net.galaxycore.galaxycorecore.Galaxycorecore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Getter
public class ChatFormatter implements Listener {
    private final Galaxycorecore galaxycorecore;
    private final ConfigNamespace configNamespace;

    public ChatFormatter(Galaxycorecore galaxycorecore){
        this.galaxycorecore = galaxycorecore;
        this.configNamespace = galaxycorecore.getCoreNamespace();
    }

    @SuppressWarnings("deprecation") // Paper wants its Components, but it wouldn't be performant to use those here
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(AsyncPlayerChatEvent event){
        String message = event.getMessage();
        if (event.getPlayer().hasPermission("core.chat.format")){
            message = message.replace("&l", "§l");
            message = message.replace("&n", "§n");
            message = message.replace("&o", "§o");
            message = message.replace("&k", "§k");
            message = message.replace("&m", "§m");
        }

        if (event.getPlayer().hasPermission("core.chat.color")){
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

        String format = configNamespace.get("chat.format");
        format = StringUtils.replaceRelevant(format, new LuckPermsApiWrapper(event.getPlayer()));
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format + message + "§f";

        event.setMessage(message);
        event.setFormat(format);
    }
}
