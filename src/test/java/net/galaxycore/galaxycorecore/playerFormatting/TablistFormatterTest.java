package net.galaxycore.galaxycorecore.playerFormatting;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TablistFormatterTest {

    @Test
    void calculatePlayerListName() {
        ConfigNamespace mockNamespace = mock(ConfigNamespace.class);
        when(mockNamespace.get("tablist.format")).thenReturn("%rank_prefix%%rank_color% %player%");

        GalaxyCoreCore mockMain = mock(GalaxyCoreCore.class);
        when(mockMain.getCoreNamespace()).thenReturn(mockNamespace);

        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn("Deppelopfer");
        when(mockPlayer.hasPermission("core.chat.important")).thenReturn(true);

        LuckPermsApiWrapper mockWrapper = mock(LuckPermsApiWrapper.class);
        when(mockWrapper.getPlayerName()).thenReturn("Deppelopfer");
        when(mockWrapper.isChatImportant()).thenReturn(true);
        when(mockWrapper.getPermissionsDisplayName()).thenReturn("testname");
        when(mockWrapper.getPermissionsColor()).thenReturn("testcolor");
        when(mockWrapper.getPermissionsPrefix()).thenReturn("testprefix");
        when(mockWrapper.getPermissionsGroupNameRaw()).thenReturn("testrawname");

        TablistFormatter tablistFormatter = new TablistFormatter(mockMain);

        String expected = "testprefixtestcolor Deppelopfer";
        String actual = tablistFormatter.calculatePlayerListName(mockWrapper);

        assertEquals(expected, actual);
    }
}