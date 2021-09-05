package net.galaxycore.galaxycorecore.chattools.haste;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class Haste {
    public static String hasteMessages(String... messages) throws IOException {
        Connection.Response response = Jsoup.connect("https://haste.galaxycore.net/documents")
                .requestBody(String.join("\n", messages))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .execute();

        return "https://haste.galaxycore.net/" + response.body().substring(8, 18);
    }
}
