package net.galaxycore.galaxycorecore.configuration;

import lombok.Getter;
import lombok.Setter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;

public class CoreProvider {
    @Getter
    @Setter
    private static GalaxyCoreCore core;
}
