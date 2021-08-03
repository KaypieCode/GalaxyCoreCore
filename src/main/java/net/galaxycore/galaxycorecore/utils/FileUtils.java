package net.galaxycore.galaxycorecore.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class FileUtils {

    public static String readSqlScript(String scope, String fileName, String dialect){
        String name = "sql/" + scope + "." + fileName + "." + dialect + ".sql";

        File file = new File(Objects.requireNonNull(FileUtils.class.getClassLoader().getResource(name)).getFile());

        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
