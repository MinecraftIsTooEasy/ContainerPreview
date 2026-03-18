package com.github.FlyBird.containerpreview;

import com.github.FlyBird.containerpreview.network.S2C.S2CSyncEnderChest;
import com.google.common.eventbus.Subscribe;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.ServerPlayer;
import net.xiaoyu233.fml.reload.event.*;

public class CPFMLEvents {

    @Subscribe
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (event.getPlayer().getAsPlayer() instanceof ServerPlayer player) {
            Network.sendToClient(player, new S2CSyncEnderChest(player.getInventoryEnderChest()));
        }
    }
}
