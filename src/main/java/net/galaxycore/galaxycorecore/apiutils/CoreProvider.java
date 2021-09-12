package net.galaxycore.galaxycorecore.apiutils;

import lombok.Getter;
import lombok.Setter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.utils.IProvider;

public class CoreProvider implements IProvider<GalaxyCoreCore> {
    @Setter
    @Getter
    private static GalaxyCoreCore core;


    @Override
    public GalaxyCoreCore get() {
        return core;
    }

    public void set(GalaxyCoreCore core) {
        CoreProvider.core = core;
    }
}
