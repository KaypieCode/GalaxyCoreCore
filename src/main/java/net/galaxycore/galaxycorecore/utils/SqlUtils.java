package net.galaxycore.galaxycorecore.utils;

import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;

import java.sql.SQLException;

public class SqlUtils {
    public static void runScript(DatabaseConfiguration databaseConfiguration, String scope, String name){
        try {
            for (String query : FileUtils.readSqlScript(scope, name, databaseConfiguration.getInternalConfiguration().getConnection().equals("sqlite") ? "sqlite" : "mysql").split(";")) {
                query = query.replace("\n", "");
                databaseConfiguration.getConnection().prepareStatement(query).executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
