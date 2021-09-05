package net.galaxycore.galaxycorecore.tablist;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SortTablistTest {

    @Test
    public void calculatePlayerName() {

        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn("deppelopfer");

        GalaxyCoreCore mockCore = mock(GalaxyCoreCore.class);

        SortTablist tablist = new SortTablist(mockCore);

        String expected = "§9Developer §7| §9deppelopfer";
        String actual = tablist.calculatePlayerName(mockPlayer);

        assertEquals(expected, actual);

    }

}
