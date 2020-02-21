package com.github.gunirs.anchors.handlers;

import com.github.gunirs.anchors.Anchors;
import com.github.gunirs.anchors.packets.PacketClickButton;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Anchors.MODID);

    public static void init() {
        INSTANCE.registerMessage(PacketClickButton.Handler.class, PacketClickButton.class, 0, Side.SERVER);
    }
}
