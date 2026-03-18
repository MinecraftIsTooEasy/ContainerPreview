package com.github.FlyBird.containerpreview.network;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.ResourceLocation;
import net.minecraft.TileEntity;

import static com.github.FlyBird.containerpreview.network.Packets.GetInventory;

public class C2SGetInventory implements Packet {
    public static IInventory inventory ;
    private int x,y,z;

    public C2SGetInventory(PacketByteBuf packetByteBuf)
    {
        this(packetByteBuf.readInt(), packetByteBuf.readInt() ,packetByteBuf.readInt());
    }

    public C2SGetInventory(int x,int y,int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(x);
        packetByteBuf.writeInt(y);
        packetByteBuf.writeInt(z);
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        if(!entityPlayer.worldObj.isRemote)
        {
            TileEntity tileEntity =  entityPlayer.worldObj.getBlockTileEntity(x,y,z);
            if(tileEntity instanceof IInventory)
                inventory = (IInventory) entityPlayer.worldObj.getBlockTileEntity(x,y,z);
        }
    }

    public static IInventory getInventory(){
        return inventory;
    }

    @Override
    public ResourceLocation getChannel() {
        return GetInventory;
    }
}
