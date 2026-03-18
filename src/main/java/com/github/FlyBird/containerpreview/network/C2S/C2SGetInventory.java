package com.github.FlyBird.containerpreview.network.C2S;

import com.github.FlyBird.containerpreview.network.S2C.S2CSyncInventory;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;

import static com.github.FlyBird.containerpreview.network.Packets.GetInventory;

public class C2SGetInventory implements Packet {
    private static IInventory inventory;
    private static int inventoryX;
    private static int inventoryY;
    private static int inventoryZ;

    private final int x;
    private final int y;
    private final int z;

    public C2SGetInventory(PacketByteBuf packetByteBuf) {
        this(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt());
    }

    public C2SGetInventory(int x, int y, int z) {
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
        if (entityPlayer.worldObj.isRemote) {
            return;
        }

        TileEntity tileEntity = entityPlayer.worldObj.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof IInventory target) {
            inventory = target;
            inventoryX = x;
            inventoryY = y;
            inventoryZ = z;
            Network.sendToClient((ServerPlayer) entityPlayer, new S2CSyncInventory(x, y, z, target));
        }
    }

    public static IInventory getInventory(int x, int y, int z) {
        if (inventoryX != x || inventoryY != y || inventoryZ != z) {
            return null;
        }
        return inventory;
    }

    public static void setInventory(int x, int y, int z, ItemStack[] syncedItems) {
        InventoryBasic synced = new InventoryBasic("preview_remote_inventory", true, 27);
        for (int i = 0; i < Math.min(27, syncedItems.length); i++) {
            synced.setInventorySlotContents(i, syncedItems[i]);
        }

        inventory = synced;
        inventoryX = x;
        inventoryY = y;
        inventoryZ = z;
    }

    @Override
    public ResourceLocation getChannel() {
        return GetInventory;
    }
}
