package com.github.FlyBird.containerpreview.network;

import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;

public class Packets {
  public static final ResourceLocation Inform = new ResourceLocation("Preview", "Inform");
  public static final ResourceLocation GetInventory = new ResourceLocation("Preview", "GetInventory");
  public static void init() {
    if (!FishModLoader.isServer()) {
      initClient();
    }
    initServer();
  }

  private static void initClient() {

  }

  private static void initServer() {
    PacketReader.registerServerPacketReader(Inform, packetByteBuf -> new C2SInform());
    PacketReader.registerServerPacketReader(GetInventory, C2SGetInventory::new);
  }
}