package com.github.FlyBird.containerpreview.mixins;

import com.github.FlyBird.containerpreview.PreviewGui;
import com.github.FlyBird.containerpreview.compat.Backpack;
import com.github.FlyBird.containerpreview.compat.ShulkerBox;
import com.github.FlyBird.containerpreview.network.C2SInform;
import net.minecraft.*;
import net.xiaoyu233.fml.FishModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @Shadow
    private Slot theSlot;

    @Unique
    private boolean flag;

    @Inject(method = {"drawScreen"}, at = {@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V")})
    private void renderItem(int par1, int par2, float par3, CallbackInfo ci) {
        ItemStack stack = this.theSlot == null ? null : this.theSlot.getStack();

        if (stack != null&&(GuiScreen.isCtrlKeyDown()||GuiScreen.isShiftKeyDown())) {
            flag = false;
            ItemStack[] chestContents = new ItemStack[27];
            if (FishModLoader.hasMod("shulkerbox")) {
                for (int i = 0; i < 16; i++) {
                    if (stack.itemID == ShulkerBox.getShulkerBoxID() + i) {
                        this.flag = true;
                        chestContents = ShulkerBox.getItemsFromItemStack(stack);
                        break;
                    }
                }
            }
            if (FishModLoader.hasMod("backpack")) {
                if (stack.itemID == Backpack.getBackpackID()) {
                    this.flag = true;
                    chestContents = Backpack.getItemStackfromNBT(stack);
                }
            }
            if (stack.itemID == Block.enderChest.blockID) {
                InventoryEnderChest inventory = C2SInform.getInventoryEnderChest();
                if (inventory != null) {
                    for (int i = 0; i < 27; i++) {
                        if (chestContents != null) {
                            chestContents[i] = inventory.getStackInSlot(i);
                        }
                    }
                }
                flag = true;
            }
            for (int i = 0; i < 27; i++) {
                if(chestContents == null){
                    flag = false;
                    break;
                }
                if (chestContents[i] == null) {
                    flag = false;
                }
                if (chestContents[i] != null) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                PreviewGui previewGui = new PreviewGui();
                previewGui.drawBackground(par1 - 115, par2 - 140);
                previewGui.drawItems(par1 - 115, par2 - 140, chestContents);
            }
        }
    }
}
