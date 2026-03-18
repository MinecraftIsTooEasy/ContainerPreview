package com.github.FlyBird.containerpreview.mixins;

import com.github.FlyBird.containerpreview.PreviewGui;
import com.github.FlyBird.containerpreview.compat.Backpack;
import com.github.FlyBird.containerpreview.compat.ShulkerBox;
import com.github.FlyBird.containerpreview.network.C2S.C2SInform;
import moddedmite.rustedironcore.network.Network;
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
    private boolean requestedEnderPreview;

    @Inject(method = {"drawScreen"}, at = {@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V")})
    private void renderItem(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ItemStack stack = this.theSlot == null ? null : this.theSlot.getStack();
        if (stack == null || !(GuiScreen.isCtrlKeyDown() || GuiScreen.isShiftKeyDown())) {
            requestedEnderPreview = false;
            return;
        }

        ItemStack[] previewStacks = null;

        if (FishModLoader.hasMod("shulkerbox")) {
            for (int i = 0; i < 16; i++) {
                if (stack.itemID == ShulkerBox.getShulkerBoxID() + i) {
                    previewStacks = ShulkerBox.getItemsFromItemStack(stack);
                    break;
                }
            }
        }
        if (previewStacks == null && FishModLoader.hasMod("backpack") && stack.itemID == Backpack.getBackpackID()) {
            previewStacks = Backpack.getItemStackfromNBT(stack);
        }

        if (stack.itemID == Block.enderChest.blockID) {
            InventoryEnderChest inventory = C2SInform.getInventoryEnderChest();
            if (inventory == null && !requestedEnderPreview) {
                Network.sendToServer(new C2SInform());
                requestedEnderPreview = true;
            }
            if (inventory != null) {
                requestedEnderPreview = false;
                previewStacks = PreviewGui.snapshot(inventory);
            }
        } else {
            requestedEnderPreview = false;
        }

        if (previewStacks == null || previewStacks.length == 0) {
            return;
        }

        ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int width = PreviewGui.getScaledPreviewWidth(previewStacks.length);
        int height = PreviewGui.getScaledPreviewHeight(previewStacks.length);

        int drawX = mouseX - width - 10;
        int drawY = mouseY - height / 2;
        drawX = Math.max(4, Math.min(drawX, scaled.getScaledWidth() - width - 4));
        drawY = Math.max(4, Math.min(drawY, scaled.getScaledHeight() - height - 4));

        PreviewGui previewGui = new PreviewGui();
        previewGui.drawPreview(drawX, drawY, previewStacks);
    }
}
