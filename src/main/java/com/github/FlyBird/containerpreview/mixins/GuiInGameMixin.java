package com.github.FlyBird.containerpreview.mixins;

import com.github.FlyBird.containerpreview.PreviewGui;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiInGameMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @Inject(method = {"renderGameOverlay(FZII)V"},
            at = {@At(value = "INVOKE",
                    target = "Lnet/minecraft/Minecraft;inDevMode()Z",
                    shift = At.Shift.BEFORE)})
    private void injectRenderExtraGuiIngame(float par1, boolean par2, int par3, int par4, CallbackInfo ci) {
        if (mc.gameSettings.gui_mode == 0) {
            PreviewGui previewGui = new PreviewGui();
            //mc.displayGuiScreen(previewGui);
            previewGui.renderAll();
        }
    }
}
