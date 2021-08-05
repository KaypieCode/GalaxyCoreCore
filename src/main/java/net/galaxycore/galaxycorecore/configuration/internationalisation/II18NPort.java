package net.galaxycore.galaxycorecore.configuration.internationalisation;

import java.util.UUID;

public interface II18NPort {
    void setDefault(String lang, String key, String value);
    String get(String lang, String key);
    void retrieve();
    I18N.MinecraftLocale getLocale(UUID uuid);
}
