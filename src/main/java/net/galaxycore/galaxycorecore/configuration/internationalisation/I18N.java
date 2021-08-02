package net.galaxycore.galaxycorecore.configuration.internationalisation;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.DatabaseConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class I18N {
    private static I18N instance = null;
    private final Logger logger;
    private final DatabaseConfiguration databaseConfiguration;

    private HashMap<I18N.MinecraftLocale, HashMap<String, String>> language_data = new HashMap<>();
    private HashMap<String, I18N.MinecraftLocale> languages = new HashMap<>();

    private I18N(GalaxyCoreCore core) {
        databaseConfiguration = core.getDatabaseConfiguration();
        logger = Logger.getLogger(this.getClass().getName());

        try {
            Class.forName("net.galaxycore.galaxycorecore.configuration.internationalization.I18NTest");

            logger.setLevel(Level.OFF);
        } catch (ClassNotFoundException ignored) {
        } // Not a test environment

        try {
            if (databaseConfiguration.getInternalConfiguration().getConnection().equals("sqlite")) {
                databaseConfiguration.getConnection().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `I18N_language_data` " +
                                "(`id` INTEGER PRIMARY KEY, " +
                                "`language_id` INT NOT NULL DEFAULT '0', " +
                                "`key` VARCHAR NOT NULL, `value` LONGTEXT);"
                ).executeUpdate();

                databaseConfiguration.getConnection().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `I18N_languages` " +
                                "( `id` INTEGER PRIMARY KEY, " +
                                "`lang` VARCHAR NOT NULL, " +
                                "`head_data` TEXT NOT NULL, " +
                                "`english_name` VARCHAR NOT NULL, " +
                                "`local_name` VARCHAR NOT NULL, " +
                                "`date_fmt` VARCHAR NOT NULL, " +
                                "`time_fmt` VARCHAR NOT NULL);"
                ).executeUpdate();
            } else {
                databaseConfiguration.getConnection().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `I18N_language_data` " +
                                "(`id` INT NOT NULL AUTO_INCREMENT, " +
                                "`language_id` INT NOT NULL DEFAULT '0', " +
                                "`key` VARCHAR NOT NULL, `value` LONGTEXT, " +
                                "UNIQUE KEY `unique_key` (`key`) USING BTREE);"
                ).executeUpdate();

                databaseConfiguration.getConnection().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `I18N_languages` " +
                                "( `id` INT NOT NULL AUTO_INCREMENT, " +
                                "`lang` VARCHAR NOT NULL, " +
                                "`head_data` TEXT NOT NULL, " +
                                "`english_name` VARCHAR NOT NULL, " +
                                "`local_name` VARCHAR NOT NULL, " +
                                "`date_fmt` VARCHAR NOT NULL, " +
                                "`time_fmt` VARCHAR NOT NULL, " +
                                "UNIQUE KEY `unique_lang` (`lang`) USING BTREE);"
                ).executeUpdate();
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
        if (instance == null) {
            instance = new I18N(core);
        }
    }

    public static void setDefault(String lang, String key, String value) {
        MinecraftLocale locale = instance.languages.get(lang);

        instance.language_data.computeIfAbsent(locale, k -> new HashMap<>());

        HashMap<String, String> localizedBundle = instance.language_data.get(locale);
        localizedBundle.put(key, value);
    }

    public static String get(String lang, String key) {
        return instance.language_data.get(instance.languages.get(lang)).get(key);
    }

    public static void retrieve() {
        StringBuilder bobTheSqlBuilder = new StringBuilder();

        instance.language_data.forEach((minecraftLocale, localizedKV) -> localizedKV.forEach((key, value) ->
                bobTheSqlBuilder.append("INSERT INTO `I18N_language_data` (`language_id`, `key`, `value`) SELECT ('")
                        .append(minecraftLocale.id)
                        .append("', '")
                        .append(key)
                        .append("', '")
                        .append(value)
                        .append("') WHERE NOT EXISTS(SELECT * FROM `I18N_language_data` WHERE `key`='")
                        .append(key)
                        .append("'));\n")));

        System.out.println(bobTheSqlBuilder.toString());

        try {
            instance.databaseConfiguration.getConnection().prepareStatement(bobTheSqlBuilder.toString()).executeLargeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Getter
    private static class MinecraftLocale {
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
