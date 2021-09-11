package net.galaxycore.galaxycorecore.utils.servername;

import de.dytanic.cloudnet.driver.CloudNetDriver;

public class ServerNameProviderCloudNet {
    public String getName() {
        return CloudNetDriver.getInstance().getComponentName();
    }
}
