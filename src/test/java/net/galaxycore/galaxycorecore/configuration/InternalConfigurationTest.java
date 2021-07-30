package net.galaxycore.galaxycorecore.configuration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class InternalConfigurationTest {
    @Test
    public void newConfigurationTest(){
        File dataFolder = new File("run/tests/datafolder");

        if (dataFolder.exists()){
            try {
                FileUtils.deleteDirectory(dataFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        assertFalse(dataFolder.exists());

        InternalConfiguration testConfiguration = new InternalConfiguration(dataFolder);

        assertTrue(dataFolder.exists());
        assertEquals(testConfiguration.getConnection(), "h2");
        assertEquals(testConfiguration.getH2_name(), "default.h2");
        assertEquals(testConfiguration.getMysql_host(), "host");
        assertEquals(testConfiguration.getMysql_port(), 3306);
        assertEquals(testConfiguration.getMysql_user(), "user");
        assertEquals(testConfiguration.getMysql_password(), "password");
        assertEquals(testConfiguration.getMysql_database(), "database");
    }

    @Test
    public void loadConfigurationTest(){
        File dataFolder = new File("run/tests/datafolder");

        if (!dataFolder.exists()){
            //noinspection ResultOfMethodCallIgnored
            dataFolder.mkdirs();
        }

        File config = new File(dataFolder, "config.yml");

        if (config.exists()) {
            try {
                FileUtils.forceDelete(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter myWriter = new FileWriter(config);
            myWriter.write("connection: mysql\n");
            myWriter.write("mysql:\n");
            myWriter.write("  host: myhost\n");
            myWriter.write("  port: 3307\n");
            myWriter.write("  user: myuser\n");
            myWriter.write("  password: mypassword\n");
            myWriter.write("  database: mydatabase\n");
            myWriter.write("h2:\n");
            myWriter.write("  name: myconfig.h2\n");
            myWriter.write("\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InternalConfiguration testConfiguration = new InternalConfiguration(dataFolder);

        assertTrue(dataFolder.exists());
        assertEquals(testConfiguration.getConnection(), "mysql");
        assertEquals(testConfiguration.getH2_name(), "myconfig.h2");
        assertEquals(testConfiguration.getMysql_host(), "myhost");
        assertEquals(testConfiguration.getMysql_port(), 3307);
        assertEquals(testConfiguration.getMysql_user(), "myuser");
        assertEquals(testConfiguration.getMysql_password(), "mypassword");
        assertEquals(testConfiguration.getMysql_database(), "mydatabase");
    }

}