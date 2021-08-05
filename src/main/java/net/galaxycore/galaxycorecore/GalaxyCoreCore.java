package net.galaxycore.galaxycorecore;

import lombok.Getter;
import net.galaxycore.galaxycorecore.chattools.ChatBuffer;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;
import net.galaxycore.galaxycorecore.configuration.InternalConfiguration;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.playerFormatting.ChatFormatter;
import net.galaxycore.galaxycorecore.playerFormatting.FormatRoutine;
import net.galaxycore.galaxycorecore.playerFormatting.TablistFormatter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class GalaxyCoreCore extends JavaPlugin {
    // CONFIGURATION //
    private DatabaseConfiguration databaseConfiguration;
    private ConfigNamespace coreNamespace;

    // FORMATTING //
    private ChatFormatter chatFormatter;
    private TablistFormatter tablistFormatter;
    private FormatRoutine formatRoutine;
    private ChatBuffer chatBuffer;

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

        // DEFAULT CONFIG VALUES //
        coreNamespace.setDefault("chat.format", "%rank_displayname% §8| %rank_color%%player% §8» §7%chat_important%");
        coreNamespace.setDefault("tablist.format", "%rank_prefix%%rank_color% %player%");
        coreNamespace.setDefault("chat.maxbufferlength", "100");

        // ChatTools //
        chatBuffer = new ChatBuffer(this);

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
