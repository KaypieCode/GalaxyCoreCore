package net.galaxycore.galaxycorecore.configuration.internationalisation;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;
import net.galaxycore.galaxycorecore.utils.FileUtils;
import net.galaxycore.galaxycorecore.utils.SqlUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class I18N implements II18NPort {
    @Getter
    private static final I18NProvider instanceRef = new I18NProvider();

    private final DatabaseConfiguration databaseConfiguration;

    private final HashMap<String, I18N.MinecraftLocale> languages = new HashMap<>();
    private final HashMap<UUID, I18N.MinecraftLocale> playerLocales = new HashMap<>();
    private HashMap<I18N.MinecraftLocale, HashMap<String, String>> language_data = new HashMap<>();

    private I18N(GalaxyCoreCore core) {
        databaseConfiguration = core.getDatabaseConfiguration();
        Logger logger = Logger.getLogger(this.getClass().getName());


        SqlUtils.runScript(databaseConfiguration, "i18n", "initialize");

        try {
            if (!databaseConfiguration.getConnection().prepareStatement("SELECT id FROM I18N_languages;").executeQuery().next()) {
                databaseConfiguration.getConnection().prepareStatement("INSERT INTO I18N_languages (lang, head_data, english_name, local_name, date_fmt, time_fmt) VALUES ('de_DE', 'head_de_DE', 'German', 'Deutsch', 'DD.MM.YYYY', 'mm:ss');").executeUpdate();
                databaseConfiguration.getConnection().prepareStatement("INSERT INTO I18N_languages (lang, head_data, english_name, local_name, date_fmt, time_fmt) VALUES ('fr_FR', 'head_fr_FR', 'French', 'Francais', 'DD.MM.YYYY', 'mm:ss');").executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet resultSet =
                    databaseConfiguration.getConnection().
                            prepareStatement("SELECT * FROM `I18N_languages`;").executeQuery();

            while (resultSet.next()) {
                languages.put(resultSet.getString("lang"), new I18N.MinecraftLocale(
                        resultSet.getInt("id"),
                        resultSet.getString("lang"),
                        resultSet.getString("head_data"),
                        resultSet.getString("english_name"),
                        resultSet.getString("local_name"),
                        resultSet.getString("date_fmt"),
                        resultSet.getString("time_fmt")
                ));

                logger.info(String.format("Registered MinecraftLocale %s", resultSet.getString("lang")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void init(GalaxyCoreCore core) {
        if (instanceRef.get() == null) {
            I18NProvider.setI18N(new I18N(core));
        }
    }

    public static void setDefaultByLang(String lang, String key, String value) {
        instanceRef.get().setDefault(lang, key, value);
    }

    public static String getByLang(String lang, String key) {
        return instanceRef.get().get(lang, key);
    }

    public String get(String lang, String key) {
        return language_data.get(languages.get(lang)).get(key);
    }

    public void setDefault(String lang, String key, String value) {
        MinecraftLocale locale = languages.get(lang);

        language_data.computeIfAbsent(locale, k -> new HashMap<>());

        HashMap<String, String> localizedBundle = language_data.get(locale);
        localizedBundle.put(key, value);
    }

    public void retrieve() {
        language_data.forEach((minecraftLocale, localizedKV) -> localizedKV.forEach((key, value) -> {
            try {

                PreparedStatement isAvaiable = databaseConfiguration.getConnection().prepareStatement("SELECT id FROM I18N_language_data WHERE `language_id`=? AND `key`=?;");

                isAvaiable.setInt(1, minecraftLocale.id);
                isAvaiable.setString(2, key);

                if (!isAvaiable.executeQuery().next()) {
                    PreparedStatement stmt = databaseConfiguration.getConnection().prepareStatement(
                            "INSERT INTO `I18N_language_data` (`language_id`, `key`, `value`) VALUES (?, ?, ?)"
                    );

                    stmt.setInt(1, minecraftLocale.id);
                    stmt.setString(2, key);
                    stmt.setString(3, value);

                    stmt.executeUpdate();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }));

        try {
            ResultSet resultSet = databaseConfiguration.getConnection().prepareStatement(
                    FileUtils.readSqlScript(
                            "i18n",
                            "loadAll",
                            databaseConfiguration.getInternalConfiguration().getConnection()
                                    .equals("sqlite") ? "sqlite" : "mysql"
                    )
            ).executeQuery();

            language_data = new HashMap<>();

            while (resultSet.next()) {
                MinecraftLocale locale = languages.get(resultSet.getString("lang"));

                language_data.computeIfAbsent(locale, minecraftLocale -> new HashMap<>());

                language_data.get(locale).put(resultSet.getString("key"), resultSet.getString("value"));
            }

            resultSet.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet playerLocales = databaseConfiguration.getConnection().prepareStatement("SELECT core_playercache.uuid, I18N_languages.lang FROM core_playercache, I18N_player_data, I18N_languages WHERE core_playercache.id = I18N_player_data.id AND I18N_player_data.language_id = I18N_languages.id").executeQuery();

            while (playerLocales.next()) {
                UUID uuid = UUID.fromString(playerLocales.getString("uuid"));
                MinecraftLocale locale = languages.get(playerLocales.getString("lang"));

                this.playerLocales.put(uuid, locale);
            }

            playerLocales.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public MinecraftLocale getLocale(UUID uuid) {
        return playerLocales.get(uuid);
    }

    public static void load() {
        instanceRef.get().retrieve();
    }

    @Getter
    public static class MinecraftLocale {
        private final int id;
        private final String lang;
        private final String headData;
        private final String englishName;
        private final String localName;
        private final String dateFormat;
        private final String timeFormat;

        public MinecraftLocale(int id, String lang, String headData, String englishName, String localName, String dateFormat, String timeFormat) {
            this.id = id;
            this.lang = lang;
            this.headData = headData;
            this.englishName = englishName;
            this.localName = localName;
            this.dateFormat = dateFormat;
            this.timeFormat = timeFormat;
        }
    }
}
