package com.github.FlyBird.containerpreview.mixins;

import com.github.FlyBird.containerpreview.network.C2SInform;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.TileEntityChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityChest.class)
public class TileEntityChestMixin {
    @Inject(method = "closeChest",at ={@At(value = "HEAD")})
    public void closeChest(CallbackInfo ci){
        Network.sendToServer(new C2SInform());

    }
}
