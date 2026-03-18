package com.github.FlyBird.containerpreview;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigDouble;

import java.util.ArrayList;
import java.util.List;

public class CPConfigs extends SimpleConfigs {

    public static final ConfigDouble previewScale = new ConfigDouble("containerpreview.previewScale", 1.0D, 0.5D, 4.0D, "containerpreview.previewScale");

    private static final CPConfigs INSTANCE;

    public static final List<ConfigBase<?>> MiscSettings;
    public static final List<ConfigBase<?>> Total;
    public static final List<ConfigTab> tabs;

    public CPConfigs(String name, List<ConfigBase<?>> values) {
        super(name, null, values);
    }

    @Override
    public List<ConfigTab> getConfigTabs() {
        return tabs;
    }

    public static CPConfigs getInstance() {
        return INSTANCE;
    }

    public static float getPreviewScale()
    {
        double value = previewScale.getDoubleValue();

        if (Double.isNaN(value) || Double.isInfinite(value))
        {
            return 0.5F;
        }
        return (float) Math.max(0.1D, value);
    }

    static {
        Total = new ArrayList<>();
        tabs = new ArrayList<>();

        MiscSettings = List.of(
                previewScale
        );

        Total.addAll(MiscSettings);
        tabs.add(new ConfigTab("misc", MiscSettings));

        INSTANCE = new CPConfigs("ContainerPreview", Total);
    }
}
