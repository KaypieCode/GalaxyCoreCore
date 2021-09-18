package net.galaxycore.galaxycorecore.configuration.internationalisation;

import org.bukkit.entity.Player;

import java.util.HashMap;

public interface II18NPort {
    void setDefault(String lang, String key, String value);
    String get(String lang, String key);
    String get(Player player, String key);
    void retrieve();
    String getLocale(Player player);
    HashMap<String, I18N.MinecraftLocale> getLanguages();
}
