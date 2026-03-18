package com.github.FlyBird.containerpreview.network.S2C;

import com.github.FlyBird.containerpreview.network.Packets;
import com.github.FlyBird.containerpreview.network.C2S.C2SInform;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;

public class S2CSyncEnderChest implements Packet {
    private final ItemStack[] syncedItems;

    public S2CSyncEnderChest(IInventory sourceInventory) {
        this.syncedItems = new ItemStack[27];
        int limit = Math.min(sourceInventory.getSizeInventory(), 27);
        for (int i = 0; i < limit; i++) {
            ItemStack stack = sourceInventory.getStackInSlot(i);
            this.syncedItems[i] = stack == null ? null : stack.copy();
        }
    }

    public S2CSyncEnderChest(PacketByteBuf packetByteBuf) {
        this.syncedItems = new ItemStack[27];
        for (int i = 0; i < 27; i++) {
            this.syncedItems[i] = packetByteBuf.readItemStack();
        }
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        for (int i = 0; i < 27; i++) {
            packetByteBuf.writeItemStack(this.syncedItems[i]);
        }
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote) {
            C2SInform.setInventoryEnderChest(this.syncedItems);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return Packets.SyncEnderChest;
    }
}
