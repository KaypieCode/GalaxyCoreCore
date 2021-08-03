package net.galaxycore.galaxycorecore.configuration.internationalisation;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;
import net.galaxycore.galaxycorecore.configuration.InternalConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class I18NTest {

    @Test
    void init() {
        File dataFolder = new File("run/tests/i18ntest");

        // if (dataFolder.exists()) {
        //     try {
        //         FileUtils.deleteDirectory(dataFolder);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }

        //noinspection ResultOfMethodCallIgnored
        dataFolder.mkdirs();

        InternalConfiguration internalConfigurationMock = mock(InternalConfiguration.class);
        when(internalConfigurationMock.getConnection()).thenReturn("sqlite");
        when(internalConfigurationMock.getSqlite_name()).thenReturn("TestDatabase.sqlite");
        when(internalConfigurationMock.getMysql_host()).thenReturn("");
        when(internalConfigurationMock.getMysql_port()).thenReturn(0);
        when(internalConfigurationMock.getMysql_user()).thenReturn("");
        when(internalConfigurationMock.getMysql_password()).thenReturn("");
        when(internalConfigurationMock.getMysql_database()).thenReturn("");
        when(internalConfigurationMock.getDataFolder()).thenReturn(dataFolder);

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(internalConfigurationMock);

        GalaxyCoreCore mainMock = mock(GalaxyCoreCore.class);
        when(mainMock.getDatabaseConfiguration()).thenReturn(databaseConfiguration);

        try {
            I18N.init(mainMock);
        }catch (Exception e) {e.printStackTrace();}

        I18N.setDefault("de_DE", "TestKey1", "Test Value1");
        I18N.setDefault("de_DE", "TestKey2", "Test Value2");
        I18N.setDefault("de_DE", "TestKey3", "Test Value3");
        I18N.setDefault("de_DE", "TestKey4", "Test Value4");
        I18N.setDefault("de_DE", "TestKey5", "Test Value5");
        I18N.setDefault("de_DE", "TestKey6", "Test Value6");

        I18N.retrieve();
    }
}