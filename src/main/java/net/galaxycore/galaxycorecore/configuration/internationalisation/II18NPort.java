package net.galaxycore.galaxycorecore.configuration.internationalisation;

public interface II18NPort {
    void setDefault(String lang, String key, String value);
    String get(String lang, String key);
}
