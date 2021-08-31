package net.galaxycore.galaxycorecore.chattools.haste;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HasteTest {
    @Test
    public void testHaste(){
        try {
            String link = Haste.hasteMessages("Test1", "Test2");

            assertEquals(39, link.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}