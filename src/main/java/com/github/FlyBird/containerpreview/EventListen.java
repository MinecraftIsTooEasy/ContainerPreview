package com.github.FlyBird.containerpreview;

import com.github.FlyBird.containerpreview.network.C2SInform;
import com.google.common.eventbus.Subscribe;
import net.xiaoyu233.fml.reload.event.*;

public class EventListen {

    @Subscribe
    public void onItemRegister(ItemRegistryEvent event) {
        //物品被注册事件
    }

    @Subscribe
    public void onRecipeRegister(RecipeRegistryEvent event) {
        //合成方式被注册事件

    }

    //玩家登录事件
    @Subscribe
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        C2SInform.inventoryEnderChest=event.getPlayer().getAsPlayer().getInventoryEnderChest();
    }


    //指令事件
    @Subscribe
    public void handleChatCommand(HandleChatCommandEvent event) {

    }
}
