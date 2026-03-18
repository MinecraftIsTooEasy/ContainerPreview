package com.github.FlyBird.containerpreview.network.C2S;

import com.github.FlyBird.containerpreview.network.Packets;
import com.github.FlyBird.containerpreview.network.S2C.S2CSyncEnderChest;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;

public class C2SInform implements Packet {

    private static InventoryEnderChest inventoryEnderChest;

    public C2SInform() {}

    public C2SInform(PacketByteBuf packetByteBuf) {}

    @Override
    public void write(PacketByteBuf packetByteBuf) {}

    @Override
    public void apply(EntityPlayer player)
    {
        if (!player.worldObj.isRemote)
        {
            inventoryEnderChest = player.getInventoryEnderChest();
            Network.sendToClient((ServerPlayer) player, new S2CSyncEnderChest(inventoryEnderChest));
        }
    }

    public static InventoryEnderChest getInventoryEnderChest() {
        return inventoryEnderChest;
    }

    public static void setInventoryEnderChest(ItemStack[] syncedItems)
    {
        if (syncedItems == null)
        {
            inventoryEnderChest = null;
            return;
        }

        InventoryEnderChest synced = new InventoryEnderChest();
        int limit = Math.min(synced.getSizeInventory(), syncedItems.length);

        for (int i = 0; i < limit; i++)
        {
            synced.setInventorySlotContents(i, syncedItems[i]);
        }
        inventoryEnderChest = synced;
    }

    @Override
    public ResourceLocation getChannel() {
        return Packets.Inform;
    }
}
