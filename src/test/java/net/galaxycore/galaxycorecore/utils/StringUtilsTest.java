package net.galaxycore.galaxycorecore.utils;

import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StringUtilsTest {

    @Test
    void replaceRelevant() {
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

        String testString = "%player% - %rank_displayname% - %rank_color% - %rank_prefix% - %rank_name% - %chat_important%";
        String expected = "Deppelopfer - testname - testcolor - testprefix - testrawname - §c";

        String actual = StringUtils.replaceRelevant(testString, mockWrapper);

        assertEquals(expected, actual);
    }

    @Test
    void replaceRelevantNoPermissions() {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn("thomas");

        String testString = "My Name is %player%";
        String expected = "My Name is thomas";

        String actual = StringUtils.replaceRelevantNoPermissions(testString, mockPlayer);

        assertEquals(expected, actual);
    }
}
