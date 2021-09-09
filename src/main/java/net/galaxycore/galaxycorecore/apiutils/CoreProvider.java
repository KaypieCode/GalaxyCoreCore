package net.galaxycore.galaxycorecore.apiutils;

import lombok.Setter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.utils.IProvider;

public class CoreProvider implements IProvider<GalaxyCoreCore> {
    @Setter
    private static GalaxyCoreCore core;


    @Override
    public GalaxyCoreCore get() {
        return core;
    }
}
