package net.galaxycore.galaxycorecore;

import lombok.Getter;
import net.galaxycore.galaxycorecore.chatlog.ChatLog;
import net.galaxycore.galaxycorecore.chattools.ChatBuffer;
import net.galaxycore.galaxycorecore.chattools.ChatClearCommand;
import net.galaxycore.galaxycorecore.chattools.ChatToolsCommand;
import net.galaxycore.galaxycorecore.chattools.ChattoolsPlayerRegisterer;
import net.galaxycore.galaxycorecore.configuration.*;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.playerFormatting.ChatFormatter;
import net.galaxycore.galaxycorecore.playerFormatting.FormatRoutine;
import net.galaxycore.galaxycorecore.playerFormatting.TablistFormatter;
import net.galaxycore.galaxycorecore.tpswarn.TPSWarn;
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

    // CHATLOG //
    private ChatLog chatLog;

    // TPS WARN //
    private TPSWarn tpsWarn;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();

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
        coreNamespace.setDefault("chatlog.webhook_url", "https://discord.gg");
        coreNamespace.setDefault("tablist.format", "%rank_prefix%%rank_color% %player%");

        // LOAD PREFIX AND MESSAGES //

        PrefixProvider.setPrefix(coreNamespace.get("prefix"));

        MessageProvider.setNoPermissionMessage(coreNamespace.get("nopermissions"));

        // CHAT TOOLS //
        chatBuffer = new ChatBuffer(this);
        Objects.requireNonNull(getCommand("chattools")).setExecutor(new ChatToolsCommand(this));
        Objects.requireNonNull(getCommand("cc"       )).setExecutor(new ChatClearCommand(this));

        Bukkit.getPluginManager().registerEvents(new ChattoolsPlayerRegisterer(this), this);

        // DEFAULT I18N VALUES //

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
        I18N.setDefaultByLang("de_DE", "core.chat.tools.haste", "§eSpeicher alle Nachrichten ab dieser");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.haste.confirm", "§aFertig! Hier ist dein Link: ");
        I18N.setDefaultByLang("de_DE", "core.chat.clear.placeholder", "§c<Chat gecleared>");
        I18N.setDefaultByLang("de_DE", "core.error", "§4Es ist ein Fehler aufgetreten!");

        // FORMATTING //
        chatFormatter = new ChatFormatter(this);
        tablistFormatter = new TablistFormatter(this);
        formatRoutine = new FormatRoutine(getSLF4JLogger(), Bukkit.getServer(), tablistFormatter);

        // CHATLOGS //
        chatLog = new ChatLog(this);
        coreNamespace.setDefault("chatlog.webhook_url", "https://discord.com/api/webhooks/882263428591419442/eTztbTcJ5TvZMJJhLC5Q__dTqwLHe91ryfL5TGdmOhdNRj_j47N4GMeMwIguM15syQ1M");

        // TPS WARN //
        tpsWarn = new TPSWarn(this);
        coreNamespace.setDefault("tpswarn.webhook_url", "https://discord.com/api/webhooks/882263428591419442/eTztbTcJ5TvZMJJhLC5Q__dTqwLHe91ryfL5TGdmOhdNRj_j47N4GMeMwIguM15syQ1M");
        coreNamespace.setDefault("tpswarn.minimal_allowed_tps", "15");

        pluginManager.registerEvents(chatFormatter, this);
        pluginManager.registerEvents(tablistFormatter, this);
        pluginManager.registerEvents(chatLog, this);
    }

    @Override
    public void onDisable() {
        formatRoutine.shutdown();

        tpsWarn.shutdown();

        databaseConfiguration.disable();
    }
}
