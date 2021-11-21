package net.galaxycore.galaxycorecore.playerFormatting;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatFormatterTest {

    @Test
    void handleChatMessage() {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn("Deppelopfer");
        when(mockPlayer.hasPermission("core.chat.important")).thenReturn(true);
        when(mockPlayer.hasPermission("core.chat.color")).thenReturn(true);
        when(mockPlayer.hasPermission("core.chat.format")).thenReturn(false);

        ConfigNamespace mockNamespace = mock(ConfigNamespace.class);
        when(mockNamespace.get("chat.format")).thenReturn("%player%");

        GalaxyCoreCore mockMain = mock(GalaxyCoreCore.class);
        when(mockMain.getCoreNamespace()).thenReturn(mockNamespace);
        when(mockMain.getChatBuffer()).thenReturn(null);

        LuckPermsApiWrapper mockWrapper = mock(LuckPermsApiWrapper.class);
        when(mockWrapper.getPlayerName()).thenReturn("Deppelopfer");
        when(mockWrapper.isChatImportant()).thenReturn(true);
        when(mockWrapper.getPermissionsDisplayName()).thenReturn("testname");
        when(mockWrapper.getPermissionsColor()).thenReturn("testcolor");
        when(mockWrapper.getPermissionsPrefix()).thenReturn("testprefix");
        when(mockWrapper.getPermissionsGroupNameRaw()).thenReturn("testrawname");

        @SuppressWarnings("deprecation")
        AsyncPlayerChatEvent chatEvent = new AsyncPlayerChatEvent(false, mockPlayer, "&b&lTest", new HashSet<>());

        ChatFormatter chatFormatter = new ChatFormatter(mockMain);
        chatFormatter.handleChatMessage(chatEvent, mockWrapper);

        String expected = "Deppelopfer§b&lTest §f";
        String actual = chatEvent.getFormat();

        assertEquals(expected, actual);
    }

    @Test
    void lastCharPercentMessageTest(){
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn("Deppelopfer");
        when(mockPlayer.hasPermission("core.chat.important")).thenReturn(true);
        when(mockPlayer.hasPermission("core.chat.color")).thenReturn(true);
        when(mockPlayer.hasPermission("core.chat.format")).thenReturn(false);

        ConfigNamespace mockNamespace = mock(ConfigNamespace.class);
        when(mockNamespace.get("chat.format")).thenReturn("%player%");

        GalaxyCoreCore mockMain = mock(GalaxyCoreCore.class);
        when(mockMain.getCoreNamespace()).thenReturn(mockNamespace);
        when(mockMain.getChatBuffer()).thenReturn(null);

        LuckPermsApiWrapper mockWrapper = mock(LuckPermsApiWrapper.class);
        when(mockWrapper.getPlayerName()).thenReturn("Deppelopfer");
        when(mockWrapper.isChatImportant()).thenReturn(true);
        when(mockWrapper.getPermissionsDisplayName()).thenReturn("testname");
        when(mockWrapper.getPermissionsColor()).thenReturn("testcolor");
        when(mockWrapper.getPermissionsPrefix()).thenReturn("testprefix");
        when(mockWrapper.getPermissionsGroupNameRaw()).thenReturn("testrawname");

        @SuppressWarnings("deprecation")
        AsyncPlayerChatEvent percentLastChatMessage = new AsyncPlayerChatEvent(false, mockPlayer, "&b&lTest%", new HashSet<>());

        ChatFormatter chatFormatter = new ChatFormatter(mockMain);
        chatFormatter.handleChatMessage(percentLastChatMessage, mockWrapper);

        String expected = "Deppelopfer§b&lTest⁒ §f";
        String actual = percentLastChatMessage.getFormat();

        assertEquals(expected, actual);
    }
}