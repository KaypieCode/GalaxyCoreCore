package net.galaxycore.galaxycorecore.utils;

import lombok.NonNull;
import net.galaxycore.galaxycorecore.utils.servername.ServerNameProviderCloudNet;
import net.galaxycore.galaxycorecore.utils.servername.ServerNameProviderStatic;

public class ServerNameUtil {
    public static @NonNull String getName() {
        String name = new ServerNameProviderStatic().getName();

        try{
            name = new ServerNameProviderCloudNet().getName();
        }catch (Exception ignored){/* This happens if the CloudNetApi cannot be found*/}

        return name;
    }
}
