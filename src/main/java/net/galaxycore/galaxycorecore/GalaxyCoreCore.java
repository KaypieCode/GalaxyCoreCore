package net.galaxycore.galaxycorecore;

import lombok.Getter;
import me.kodysimpson.menumanagersystem.listeners.MenuListener;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.chatlog.ChatLog;
import net.galaxycore.galaxycorecore.chattools.ChatBuffer;
import net.galaxycore.galaxycorecore.chattools.ChatClearCommand;
import net.galaxycore.galaxycorecore.chattools.ChatToolsCommand;
import net.galaxycore.galaxycorecore.chattools.ChattoolsPlayerRegisterer;
import net.galaxycore.galaxycorecore.coins.CoinAliasCommand;
import net.galaxycore.galaxycorecore.coins.CoinCommand;
import net.galaxycore.galaxycorecore.configuration.*;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18NPlayerLoader;
import net.galaxycore.galaxycorecore.events.ServerLoadedEvent;
import net.galaxycore.galaxycorecore.events.ServerTimePassedEvent;
import net.galaxycore.galaxycorecore.onlinetime.OnlineTime;
import net.galaxycore.galaxycorecore.playerFormatting.ChatFormatter;
import net.galaxycore.galaxycorecore.playerFormatting.PlayerJoinLeaveListener;
import net.galaxycore.galaxycorecore.scoreboards.ScoreBoardController;
import net.galaxycore.galaxycorecore.spice.KMenuListener;
import net.galaxycore.galaxycorecore.tabcompletion.PlayerTabCompleteListener;
import net.galaxycore.galaxycorecore.scoreboards.SortTablist;
import net.galaxycore.galaxycorecore.tpswarn.TPSWarn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
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

    // CHAT TOOLS //
    private ChatBuffer chatBuffer;

    // CHATLOG //
    private ChatLog chatLog;

    // TPS WARN //
    private TPSWarn tpsWarn;

    // TABLIST SORT AND NAMETAGS //
    private SortTablist sortTablist;

    // BLOCK TAB COMPLETION //
    private PlayerTabCompleteListener playerTabCompleteListener;

    // ONLINE TIME //
    private OnlineTime onlineTime;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        // SET OWN INSTANCE FOR API USAGE //
        new CoreProvider().set(this);
        getServer().getServicesManager().register(GalaxyCoreCore.class, this, this, ServicePriority.Highest);

        // CONFIGURATION //
        InternalConfiguration internalConfiguration = new InternalConfiguration(getDataFolder());
        databaseConfiguration = new DatabaseConfiguration(internalConfiguration);

        coreNamespace = databaseConfiguration.getNamespace("core");

        // MENU MANAGER SYSTEM //

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        // I18N

        I18N.init(this);
        
        I18NPlayerLoader.setPlayerLoaderInstance(new I18NPlayerLoader());
        Bukkit.getPluginManager().registerEvents(I18NPlayerLoader.getPlayerLoaderInstance(), this);

        /* Why? Because other Plugins can load their defaults in the meantime */
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            I18N.load();
            Bukkit.getPluginManager().callEvent(new ServerLoadedEvent());
        });

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
        Objects.requireNonNull(getCommand("cc")).setExecutor(new ChatClearCommand(this));

        Bukkit.getPluginManager().registerEvents(new ChattoolsPlayerRegisterer(this), this);

        // DEFAULT I18N VALUES //

        I18N.setDefaultByLang("de_DE", "core.chat.tools.open", "§eÖffne die Chattools");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.commandfail", "§cBitte verwende §e/chattools [ID]", true);
        I18N.setDefaultByLang("de_DE", "core.chat.tools.msgnotfound", "§cDiese Nachricht wurde nicht gefunden", true);
        I18N.setDefaultByLang("de_DE", "core.chat.tools.themessage", "§eNachricht: §7");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.delete", "§eNachricht löschen");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.undelete", "§eLöschen rückgängig machen");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.confirm", "§eAktion ausgeführt!", true);
        I18N.setDefaultByLang("de_DE", "core.chat.tools.name", "§6ChatTools§8 «");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.copy", "§ein die Zwischenablage kopieren");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.copy.website", "Kopieren");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.haste", "§eSpeicher alle Nachrichten ab dieser");
        I18N.setDefaultByLang("de_DE", "core.chat.tools.haste.confirm", "§aFertig! Hier ist dein Link: ", true);
        I18N.setDefaultByLang("de_DE", "core.chat.clear.placeholder", "%rank_color%%player%§e hat den Chat gecleared.", true);
        I18N.setDefaultByLang("de_DE", "core.event.join", "§7[§a+§7] %rank_prefix%%player%");
        I18N.setDefaultByLang("de_DE", "core.event.leave", "§7[§c-§7] %rank_prefix%%player%");
        I18N.setDefaultByLang("de_DE", "core.error", "§4Es ist ein Fehler aufgetreten!", true);
        I18N.setDefaultByLang("de_DE", "core.nopermissions", "§4Hierfür hast du keine Rechte!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.usage", "§cBitte nutze /coins [pay|set|add|remove|bal] [player] [amount]", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.player404", "§cDieser Spieler wurde nicht gefunden.", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.bal", "§cKontostand: %d", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.bal.others", "§c%player%'s Kontostand: %d", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.bal.usage", "§cBitte verwende /bal <player>", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.nonumber", "§cDies ist keine Zahl", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.notenoughcoins", "§cDu hast nicht genügend Coins", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.notenoughcoins.other", "§cDieser Spieler hat nicht genügend Coins", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.pay.success", "§cDu hast %player% %d Coins bezahlt!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.pay.success.other", "§cDu hast von %player% %d Coins bekommen!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.pay.usage", "§cBitte verwende /pay <player> <amount>", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.set.success", "§cDu hast %player% %d Coins gegeben!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.set.success.other", "§cDu hast nun %d Coins!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.add.success", "§cDu hast %player% %d Coins gegeben!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.add.success.other", "§cDu hast %d Coins bekommen!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.remove.success", "§cDu hast %player% %d Coins entfernt!", true);
        I18N.setDefaultByLang("de_DE", "core.command.coins.remove.success.other", "§cDu hast %d Coins abgezogen bekommen!", true);

        I18N.setDefaultByLang("en_GB", "core.chat.tools.open", "§eOpen the Chattools");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.commandfail", "§cPlease Use §e/chattools [ID]", true);
        I18N.setDefaultByLang("en_GB", "core.chat.tools.msgnotfound", "§cMessage not found!", true);
        I18N.setDefaultByLang("en_GB", "core.chat.tools.themessage", "§eMessage: §7");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.delete", "§eDelete Message");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.undelete", "§eUndo Deletion");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.confirm", "§eDone!", true);
        I18N.setDefaultByLang("en_GB", "core.chat.tools.name", "§6ChatTools§8 «");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.copy", "§eCopy To Clipboard");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.copy.website", "Copy");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.haste", "§eSave all messages from this on");
        I18N.setDefaultByLang("en_GB", "core.chat.tools.haste.confirm", "§aDone! Here's your link: ", true);
        I18N.setDefaultByLang("en_GB", "core.chat.clear.placeholder", "%rank_color%%player%§e cleared the chat.", true);
        I18N.setDefaultByLang("en_GB", "core.event.join", "§7[§a+§7] %rank_prefix%%player%");
        I18N.setDefaultByLang("en_GB", "core.event.leave", "§7[§c-§7] %rank_prefix%%player%");
        I18N.setDefaultByLang("en_GB", "core.error", "§4Oh no! There was an internal Error!", true);
        I18N.setDefaultByLang("en_GB", "core.nopermissions", "§4You don't have Permissions to use this!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.usage", "§cPlease use /coins [pay|set|add|remove|bal] <playername> [amount]", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.player404", "§cThe player was not found.", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.bal", "§cCoins: %d", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.bal.others", "§c%player%'s Coins: %d", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.bal.usage", "§cPlease use /bal <player>", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.nonumber", "§cThis is not a number", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.notenoughcoins", "§cYou don't have enough Coins", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.notenoughcoins.other", "§cThis player doesn't have enough Coins", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.pay.success", "§cYou payed %player% %d Coins!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.pay.success.other", "§cYou got %d Coins from %player%!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.pay.usage", "§cPlease use /pay <player> <amount>", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.set.success", "§cYou set %player%'s Coins to %d!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.set.success.other", "§cYou have not %d Coins!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.add.success", "§cYou set %player%'s Coins to %d!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.add.success.other", "§cYou got %d Coins added!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.remove.success", "§cYou removed %player% %d Coins!", true);
        I18N.setDefaultByLang("en_GB", "core.command.coins.remove.success.other", "§cYou got %d Coins removed!", true);

        // FORMATTING //
        chatFormatter = new ChatFormatter(this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);

        // CHATLOGS //
        chatLog = new ChatLog(this);
        coreNamespace.setDefault("chatlog.webhook_url", "https://discord.com/api/webhooks/882263428591419442/eTztbTcJ5TvZMJJhLC5Q__dTqwLHe91ryfL5TGdmOhdNRj_j47N4GMeMwIguM15syQ1M");

        // TPS WARN //
        tpsWarn = new TPSWarn(this);
        coreNamespace.setDefault("tpswarn.webhook_url", "https://discord.com/api/webhooks/882263428591419442/eTztbTcJ5TvZMJJhLC5Q__dTqwLHe91ryfL5TGdmOhdNRj_j47N4GMeMwIguM15syQ1M");
        coreNamespace.setDefault("tpswarn.minimal_allowed_tps", "15");

        // TABLIST SORT AND NAMETAGS AND SCOREBOAD //
        ScoreBoardController.setScoreBoardCallback(new ScoreBoardController());

        sortTablist = new SortTablist(this);

        pluginManager.registerEvents(chatFormatter, this);
        pluginManager.registerEvents(chatLog, this);

        // BLOCK TAB COMPLETION //
        playerTabCompleteListener = new PlayerTabCompleteListener(this);

        // SPECIAL EVENTS //
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getPluginManager().callEvent(new ServerTimePassedEvent()), 20, 5);

        // COINS //
        Objects.requireNonNull(getCommand("coins")).setExecutor(new CoinCommand());
        Objects.requireNonNull(getCommand("coins")).setTabCompleter(new CoinCommand());

        Objects.requireNonNull(getCommand("bal")).setTabCompleter(new CoinAliasCommand());
        Objects.requireNonNull(getCommand("bal")).setExecutor(new CoinAliasCommand());

        Objects.requireNonNull(getCommand("pay")).setTabCompleter(new CoinAliasCommand());
        Objects.requireNonNull(getCommand("pay")).setExecutor(new CoinAliasCommand());

        // ONLINE TIME //
        this.onlineTime = new OnlineTime(0, 0);
        pluginManager.registerEvents(this.onlineTime, this);

        // KMenu //
        Bukkit.getPluginManager().registerEvents(new KMenuListener(), this);
    }

    @Override
    public void onDisable() {
        tpsWarn.shutdown();

        sortTablist.shutdown();

        databaseConfiguration.disable();
    }
}
