package com.github.FlyBird.containerpreview.network.S2C;

import com.github.FlyBird.containerpreview.network.Packets;
import com.github.FlyBird.containerpreview.network.C2S.C2SInform;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;

import java.io.IOException;

public class S2CSyncEnderChest implements Packet {
    private final int slotCount;
    private final ItemStack[] syncedItems;

    public S2CSyncEnderChest(IInventory sourceInventory) {
        this.slotCount = Math.max(0, sourceInventory.getSizeInventory());
        this.syncedItems = new ItemStack[this.slotCount];
        for (int i = 0; i < this.slotCount; i++) {
            ItemStack stack = sourceInventory.getStackInSlot(i);
            this.syncedItems[i] = stack == null ? null : stack.copy();
        }
    }

    public S2CSyncEnderChest(PacketByteBuf packetByteBuf) {
        int parsedSlotCount;
        boolean useLegacyLayout = false;
        packetByteBuf.getInputStream().mark(4);
        try {
            parsedSlotCount = packetByteBuf.readInt();
            if (parsedSlotCount <= 0 || parsedSlotCount > 4096) {
                useLegacyLayout = true;
                parsedSlotCount = 27;
            }
        } catch (RuntimeException e) {
            useLegacyLayout = true;
            parsedSlotCount = 27;
        }

        if (useLegacyLayout) {
            try {
                packetByteBuf.getInputStream().reset();
            } catch (IOException e) {
                throw new RuntimeException("Failed to reset packet stream for legacy ender chest preview format", e);
            }
        }

        this.slotCount = parsedSlotCount;
        this.syncedItems = new ItemStack[this.slotCount];
        for (int i = 0; i < this.slotCount; i++) {
            this.syncedItems[i] = packetByteBuf.readItemStack();
        }
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.slotCount);
        for (int i = 0; i < this.slotCount; i++) {
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
