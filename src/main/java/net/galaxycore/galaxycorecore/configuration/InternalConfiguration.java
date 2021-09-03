package net.galaxycore.galaxycorecore.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class InternalConfiguration {

    private final String connection;
    private final String mysql_host;
    private final int mysql_port;
    private final String mysql_user;
    private final String mysql_password;
    private final String mysql_database;
    private final String sqlite_name;

    private final File dataFolder;

    public InternalConfiguration(File settingsDataFolder) {
        this.dataFolder = settingsDataFolder;


        if (!settingsDataFolder.exists())
            //noinspection ResultOfMethodCallIgnored
            settingsDataFolder.mkdirs();

        File configurationFile = new File(settingsDataFolder, "config.yml");

        if (!configurationFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                configurationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileConfiguration writeConfig = YamlConfiguration.loadConfiguration(configurationFile);

            writeConfig.set("connection", "h2");
            writeConfig.set("mysql.host", "host");
            writeConfig.set("mysql.port", 3306);
            writeConfig.set("mysql.user", "user");
            writeConfig.set("mysql.password", "password");
            writeConfig.set("mysql.database", "database");
            writeConfig.set("sqlite.name", "TestDatabase.sqlite");

            try {
                writeConfig.save(configurationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);

        connection = config.getString("connection");
        mysql_host = config.getString("mysql.host");
        mysql_port = config.getInt("mysql.port");
        mysql_user = config.getString("mysql.user");
        mysql_password = config.getString("mysql.password");
        mysql_database = config.getString("mysql.database");
        sqlite_name = config.getString("sqlite.name");
    }
}
