package net.galaxycore.galaxycorecore.configuration.internationalisation;

import lombok.Setter;
import net.galaxycore.galaxycorecore.utils.IProvider;

public class I18NProvider implements IProvider<II18NPort> {
    @Setter
    private static I18N i18N;

    @Override
    public II18NPort get() {
        return i18N;
    }
}
