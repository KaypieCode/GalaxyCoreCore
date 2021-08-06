package net.galaxycore.galaxycorecore.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class FileUtils {

    public static String readSqlScript(String scope, String fileName, String dialect) {
        String name = "sql/" + scope + "." + fileName + "." + dialect + ".sql";

        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(name);
        assert inputStream != null;
        try {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
