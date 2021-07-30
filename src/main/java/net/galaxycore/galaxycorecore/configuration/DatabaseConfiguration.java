package net.galaxycore.galaxycorecore.configuration;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    @Getter private Connection connection;

    public DatabaseConfiguration(InternalConfiguration internalConfiguration){
        try {
            if (internalConfiguration.getConnection().equals("sqlite")){
                connection = DriverManager.getConnection(
                        "jdbc:sqlite:" + internalConfiguration.getDataFolder().getAbsolutePath() + "/"
                                + internalConfiguration.getSqlite_name(),
                        "sa",
                        "");
                System.out.println(internalConfiguration.getDataFolder().getAbsolutePath() + "/"
                        + internalConfiguration.getSqlite_name());
            } else {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + internalConfiguration.getMysql_host() + ":"
                                + internalConfiguration.getMysql_port() + "/"
                                + internalConfiguration.getMysql_database(),
                        internalConfiguration.getMysql_user(),
                        internalConfiguration.getMysql_password());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConfigNamespace getNamespace(String name){
        return new ConfigNamespace(name, this);
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
