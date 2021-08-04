package net.galaxycore.galaxycorecore.configuration.internationalisation;

import net.galaxycore.galaxycorecore.utils.IProvider;

public class I18NProvider implements IProvider<II18NPort> {
    @Override
    public II18NPort get() {
        return I18N.getInstance();
    }
}
