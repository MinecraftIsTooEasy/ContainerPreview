package com.github.FlyBird.containerpreview.compat;

import com.github.flybird.backpack.BPRegistryInit;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.minecraft.NBTTagList;

public class Backpack {

    public static int getBackpackID(){
        return BPRegistryInit.backpack.blockID;
    }

    public static ItemStack[] getItemStackfromNBT(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.stackTagCompound;
        ItemStack[] itemStacks = new ItemStack[27];

        if (nbt!=null&&nbt.hasKey("Items")) {
            NBTTagList nbtTagList = (NBTTagList) nbt.getTag("Items");
            if (nbtTagList != null) {
                for (int i = 0; i < nbtTagList.tagCount(); i++) {
                    NBTTagCompound itemStackTag = (NBTTagCompound) nbtTagList.tagAt(i);
                    int slot = itemStackTag.getInteger("Slot");
                    itemStacks[slot] = ItemStack.loadItemStackFromNBT(itemStackTag);
                }
            }
        }
        return itemStacks;
    }
}
