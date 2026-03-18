package com.github.FlyBird.containerpreview.network.S2C;

import com.github.FlyBird.containerpreview.network.C2S.C2SGetInventory;
import com.github.FlyBird.containerpreview.network.Packets;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;

public class S2CSyncInventory implements Packet {
    private final int x;
    private final int y;
    private final int z;
    private final ItemStack[] syncedItems;

    public S2CSyncInventory(int x, int y, int z, IInventory sourceInventory) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.syncedItems = new ItemStack[27];

        int limit = Math.min(sourceInventory.getSizeInventory(), 27);
        for (int i = 0; i < limit; i++) {
            ItemStack stack = sourceInventory.getStackInSlot(i);
            this.syncedItems[i] = stack == null ? null : stack.copy();
        }
    }

    public S2CSyncInventory(PacketByteBuf packetByteBuf) {
        this.x = packetByteBuf.readInt();
        this.y = packetByteBuf.readInt();
        this.z = packetByteBuf.readInt();
        this.syncedItems = new ItemStack[27];
        for (int i = 0; i < 27; i++) {
            this.syncedItems[i] = packetByteBuf.readItemStack();
        }
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.x);
        packetByteBuf.writeInt(this.y);
        packetByteBuf.writeInt(this.z);
        for (int i = 0; i < 27; i++) {
            packetByteBuf.writeItemStack(this.syncedItems[i]);
        }
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote) {
            C2SGetInventory.setInventory(this.x, this.y, this.z, this.syncedItems);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return Packets.SyncInventory;
    }
}
