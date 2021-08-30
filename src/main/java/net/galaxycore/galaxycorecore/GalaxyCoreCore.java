package net.galaxycore.galaxycorecore;

import lombok.Getter;
import net.galaxycore.galaxycorecore.chattools.ChatBuffer;
import net.galaxycore.galaxycorecore.chattools.ChatToolsCommand;
import net.galaxycore.galaxycorecore.configuration.*;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.playerFormatting.ChatFormatter;
import net.galaxycore.galaxycorecore.playerFormatting.FormatRoutine;
import net.galaxycore.galaxycorecore.playerFormatting.TablistFormatter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class GalaxyCoreCore extends JavaPlugin {
    // CONFIGURATION //
    @Getter
    private DatabaseConfiguration databaseConfiguration;
    private ConfigNamespace coreNamespace;

    // FORMATTING //
    private ChatFormatter chatFormatter;
    private TablistFormatter tablistFormatter;
    private FormatRoutine formatRoutine;

    // CHAT TOOLS //
    private ChatBuffer chatBuffer;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        // SET OWN INSTANCE //
        CoreProvider.setCore(this);

        // CONFIGURATION //
        InternalConfiguration internalConfiguration = new InternalConfiguration(getDataFolder());
        databaseConfiguration = new DatabaseConfiguration(internalConfiguration);

        coreNamespace = databaseConfiguration.getNamespace("core");

        // I18N

        I18N.init(this);

        /* Why? Because other Plugins can load their defaults in the mean time */
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, I18N::load);

        // DEFAULT CONFIG VALUES //
        coreNamespace.setDefault("prefix", "§5Galaxy§6Core §l§8»§r§7 ");
        coreNamespace.setDefault("nopermissions", "§cFür diese Aktion hast du keine Rechte!");
        coreNamespace.setDefault("chat.format", "%rank_displayname% §8| %rank_color%%player% §8» §7%chat_important%");
        coreNamespace.setDefault("chat.maxbufferlength", "100");
        coreNamespace.setDefault("tablist.format", "%rank_prefix%%rank_color% %player%");

        // LOAD PREFIX AND MESSAGES //

        PrefixProvider.setPrefix(coreNamespace.get("prefix"));

        MessageProvider.setNoPermissionMessage(coreNamespace.get("nopermissions"));

        // CHAT TOOLS //
        chatBuffer = new ChatBuffer(this);
        Objects.requireNonNull(getCommand("chattools")).setExecutor(new ChatToolsCommand(this));

        I18N.setDefaultByLang("de_DE", "core.chat.tools.open", "§eÖffne die Chattools");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.commandfail", "§cBitte verwende §e/chattools [ID]");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.msgnotfound", "§cDiese Nachricht wurde nicht gefunden");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.themessage", "§eNachricht: §7");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.delete", "§eNachricht löschen");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.undelete", "§eLöschen rückgängig machen");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.confirm", "§eAktion ausgeführt!");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.name", "§6ChatTools§8 «");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.copy", "§ein die Zwischenablage kopieren");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.copy.website", "Kopieren");
        I18N.setDefaultByLang("de_DE", "core.chat.clear.placeholder", "§c<Chat gecleared>");

        // FORMATTING //
        chatFormatter = new ChatFormatter(this);
        tablistFormatter = new TablistFormatter(this);
        formatRoutine = new FormatRoutine(getSLF4JLogger(), Bukkit.getServer(), tablistFormatter);

        pluginManager.registerEvents(chatFormatter, this);
        pluginManager.registerEvents(tablistFormatter, this);
    }

    @Override
    public void onDisable() {
        formatRoutine.shutdown();

        databaseConfiguration.disable();
    }
}
