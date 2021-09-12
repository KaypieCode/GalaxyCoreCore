package net.galaxycore.galaxycorecore.utils.servername;

import org.bukkit.Bukkit;

public class ServerNameProviderStatic {
    public String getName() {
        return "TestServer running on " + Bukkit.getServer().getName();
    }
}
