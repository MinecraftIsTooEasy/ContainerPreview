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
    public void apply(EntityPlayer entityPlayer)
    {
        if (entityPlayer.worldObj.isRemote)
        {
            return;
        }

        World world = entityPlayer.worldObj;
        int blockId = world.getBlockId(x, y, z);
        Block block = Block.blocksList[blockId];

        if (block instanceof BlockChest chestBlock)
        {
            IInventory mergedChest = chestBlock.getInventory(world, x, y, z);

            if (mergedChest != null)
            {
                Network.sendToClient((ServerPlayer) entityPlayer, new S2CSyncInventory(x, y, z, mergedChest));
            }
            return;
        }

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof IInventory target)
        {
            Network.sendToClient((ServerPlayer) entityPlayer, new S2CSyncInventory(x, y, z, target));
        }
    }

    public static IInventory getInventory(int x, int y, int z)
    {
        if (inventoryX != x || inventoryY != y || inventoryZ != z)
        {
            return null;
        }

        return inventory;
    }

    public static void setInventory(int x, int y, int z, ItemStack[] syncedItems)
    {
        if (syncedItems == null)
        {
            inventory = null;
            return;
        }

        int size = Math.max(0, syncedItems.length);
        InventoryBasic synced = new InventoryBasic("preview_remote_inventory", true, size);

        for (int i = 0; i < size; i++)
        {
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
