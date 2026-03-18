package com.github.FlyBird.containerpreview;


import com.github.FlyBird.containerpreview.network.Packets;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;

public class ContainerPreviewMod implements ModInitializer {

    public static final String MOD_ID = "containerpreview";

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);
        MITEEvents.MITE_EVENT_BUS.register(new CPFMLEvents());
        Packets.init();
    }
}
