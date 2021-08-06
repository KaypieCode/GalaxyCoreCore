package net.galaxycore.galaxycorecore.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlUtilsTest {

    @Test
    void getCpUrl() {
        assertEquals("https://ctc.galaxycore.net?cp=Test%20Message&btntext=Copy", UrlUtils.getCpUrl("Test §bMessage", "Copy"));
    }
}