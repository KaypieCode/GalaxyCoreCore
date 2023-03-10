package net.galaxycore.galaxycorecore.configuration;

import lombok.Getter;
import net.galaxycore.galaxycorecore.utils.SqlUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    @Getter private final InternalConfiguration internalConfiguration;
    @Getter public Connection connection;

    public DatabaseConfiguration(InternalConfiguration internalConfiguration){
        this.internalConfiguration = internalConfiguration;
        try {
            if (internalConfiguration.getConnection().equals("sqlite")){
                connection = DriverManager.getConnection(
                        "jdbc:sqlite:" + internalConfiguration.getDataFolder().getAbsolutePath() + "/"
                                + internalConfiguration.getSqlite_name(),
                        "sa",
                        "");
            } else {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + internalConfiguration.getMysql_host() + ":"
                                + internalConfiguration.getMysql_port() + "/"
                                + internalConfiguration.getMysql_database() + "?autoReconnect=true",
                        internalConfiguration.getMysql_user(),
                        internalConfiguration.getMysql_password());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        SqlUtils.runScript(this, "core", "create");
    }

    public ConfigNamespace getNamespace(String name){
        return new ConfigNamespace("config_" + name, this);
    }

    public void disable(){
        try {
            if (connection!=null && !connection.isClosed()){
                connection.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
