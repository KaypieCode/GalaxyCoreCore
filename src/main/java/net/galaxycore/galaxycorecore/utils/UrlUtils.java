package net.galaxycore.galaxycorecore.utils;

import net.galaxycore.galaxycorecore.chattools.ChatToolsCommand;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
    public static String getCpUrl(String msg, String cpy){
        URLBuilder urlBuilder = new URLBuilder("ctc.galaxycore.net");
        urlBuilder.setConnectionType("https");

        msg = ChatToolsCommand.replaceColorCodes(msg);
        msg = msg.replace("»", ">");

        urlBuilder.addParameter("cp", msg);
        urlBuilder.addParameter("btntext", cpy);

        try {
            return urlBuilder.getURL();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        return "";
    }

    static class URLBuilder {
        private final StringBuilder folders;
        private final StringBuilder params;
        private String connType, host;

        void setConnectionType(String conn) {
            connType = conn;
        }

        URLBuilder() {
            folders = new StringBuilder();
            params = new StringBuilder();
        }

        URLBuilder(String host) {
            this();
            this.host = host;
        }

        void addSubfolder(String folder) {
            folders.append("/");
            folders.append(folder);
        }

        void addParameter(String parameter, String value) {
            if (params.toString().length() > 0) {
                params.append("&");
            }
            params.append(parameter);
            params.append("=");
            params.append(value);
        }

        String getURL() throws URISyntaxException, MalformedURLException {
            URI uri = new URI(connType, host, folders.toString(),
                    params.toString(), null);
            return uri.toURL().toString();
        }

        String getRelativeURL() throws URISyntaxException {
            URI uri = new URI(null, null, folders.toString(), params.toString(), null);
            return uri.toString();
        }
    }
}