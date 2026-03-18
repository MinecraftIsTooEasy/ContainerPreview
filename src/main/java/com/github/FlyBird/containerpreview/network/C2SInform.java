package com.github.FlyBird.containerpreview.network;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.InventoryEnderChest;
import net.minecraft.ResourceLocation;

public class C2SInform implements Packet {
    public static InventoryEnderChest inventoryEnderChest;
    public void write(PacketByteBuf packetByteBuf) {

    }


    public void apply(EntityPlayer player) {
        if(!player.worldObj.isRemote) {
            inventoryEnderChest = player.getInventoryEnderChest();
        }
    }

    public static InventoryEnderChest getInventoryEnderChest(){
        return inventoryEnderChest;
    }

    public ResourceLocation getChannel() {
        return Packets.Inform;
    }
}
