package net.galaxycore.galaxycorecore;

import lombok.Getter;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;
import net.galaxycore.galaxycorecore.configuration.InternalConfiguration;
import net.galaxycore.galaxycorecore.formatting.ChatFormatter;
import net.galaxycore.galaxycorecore.formatting.TablistFormatter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Galaxycorecore extends JavaPlugin {
    private DatabaseConfiguration databaseConfiguration;
    private ConfigNamespace coreNamespace;

    @Override
    public void onEnable() {
        InternalConfiguration internalConfiguration = new InternalConfiguration(getDataFolder());
        databaseConfiguration = new DatabaseConfiguration(internalConfiguration);

        coreNamespace = databaseConfiguration.getNamespace("core");

        coreNamespace.setDefault("chat.format", "%rank_displayname% §8| %rank_color%%player% §8» §7%chat_important%");
        coreNamespace.setDefault("tablist.format", "%rank_prefix% %rank_color%%player%");


        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChatFormatter(this), this);
        pluginManager.registerEvents(new TablistFormatter(this), this);
    }

    @Override
    public void onDisable() {
        databaseConfiguration.disable();
    }
}
