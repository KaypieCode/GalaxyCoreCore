package net.galaxycore.galaxycorecore;

import lombok.Getter;
import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;
import net.galaxycore.galaxycorecore.configuration.InternalConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Galaxycorecore extends JavaPlugin {
    private DatabaseConfiguration databaseConfiguration;

    @Override
    public void onEnable() {
        InternalConfiguration internalConfiguration = new InternalConfiguration(getDataFolder());
        databaseConfiguration = new DatabaseConfiguration(internalConfiguration);
    }

    @Override
    public void onDisable() {
        databaseConfiguration.disable();
    }
}
