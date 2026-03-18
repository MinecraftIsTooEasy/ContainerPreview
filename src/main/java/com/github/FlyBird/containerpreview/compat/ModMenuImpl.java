package com.github.FlyBird.containerpreview.compat;

import com.github.FlyBird.containerpreview.CPConfigs;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> CPConfigs.getInstance().getConfigScreen(parent);
    }
}
