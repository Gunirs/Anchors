package com.github.gunirs.anchors.events;

import com.github.gunirs.anchors.proxy.ClientProxy;
import com.github.gunirs.anchors.utils.AnchorsChunkManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class EventKeybinding {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public void keyInputEvent(KeyInputEvent event) {
        if(ClientProxy.visualRenderKeybind.isPressed())
            AnchorsChunkManager.rendered = !AnchorsChunkManager.rendered;
    }
}
