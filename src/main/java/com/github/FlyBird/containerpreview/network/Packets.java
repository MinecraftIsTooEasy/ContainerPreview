package com.github.FlyBird.containerpreview.network;

import com.github.FlyBird.containerpreview.network.C2S.C2SGetInventory;
import com.github.FlyBird.containerpreview.network.C2S.C2SInform;
import com.github.FlyBird.containerpreview.network.S2C.S2CSyncEnderChest;
import com.github.FlyBird.containerpreview.network.S2C.S2CSyncInventory;
import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;

public class Packets {

    public static final ResourceLocation Inform = new ResourceLocation("containerpreview", "Inform");
    public static final ResourceLocation GetInventory = new ResourceLocation("containerpreview", "GetInventory");
    public static final ResourceLocation SyncInventory = new ResourceLocation("containerpreview", "SyncInventory");
    public static final ResourceLocation SyncEnderChest = new ResourceLocation("containerpreview", "SyncEnderChest");

    public static void init() {
        if (!FishModLoader.isServer()) {
            initClient();
        }

        initServer();
    }

    private static void initClient() {
        PacketReader.registerClientPacketReader(SyncInventory, S2CSyncInventory::new);
        PacketReader.registerClientPacketReader(SyncEnderChest, S2CSyncEnderChest::new);
    }

    private static void initServer() {
        PacketReader.registerServerPacketReader(Inform, C2SInform::new);
        PacketReader.registerServerPacketReader(GetInventory, C2SGetInventory::new);
    }
}
