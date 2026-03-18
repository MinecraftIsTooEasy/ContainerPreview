package com.github.FlyBird.containerpreview.mixins;

import com.github.FlyBird.containerpreview.network.C2S.C2SInform;
import moddedmite.rustedironcore.api.util.FabricUtil;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.InventoryEnderChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryEnderChest.class)
public class InventoryEnderChestMixin {

    @Inject(method = "closeChest", at = @At(value = "HEAD"))
    public void closeChest(CallbackInfo ci)
    {
        if (FabricUtil.isServer())
        {
            return;
        }
        Network.sendToServer(new C2SInform());
    }
}
