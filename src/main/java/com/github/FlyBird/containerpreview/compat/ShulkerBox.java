package com.github.FlyBird.containerpreview.compat;

import limingzxc.shulkerbox.config.Configs;
import limingzxc.shulkerbox.tileentity.TileEntityShulkerBox;
import limingzxc.shulkerbox.utils.Utils;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagList;

public class ShulkerBox {
    public static ItemStack[] getItemsFromItemStack(ItemStack stack){
        if (stack.hasTagCompound()) {
            ItemStack[] chestContents = new ItemStack[TileEntityShulkerBox.ShulkerBoxType.VANILLA.getSize()];
            NBTTagList nbttaglist = stack.getTagCompound().getTagList("Items");
            Utils.loadItemStacksFromNBT(nbttaglist, chestContents);
            return chestContents;
        }
        return null;
    }

    public static int getShulkerBoxID()
    {
        return Configs.SHULKERBOX_BLOCKID.get();
    }
}
