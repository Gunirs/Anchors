package com.github.gunirs.anchors.proxy;

import com.github.gunirs.anchors.Anchors;
import com.github.gunirs.anchors.Config;
import com.github.gunirs.anchors.Registry;
import com.github.gunirs.anchors.handlers.ChunkLoadingHandler;
import com.github.gunirs.anchors.handlers.PacketHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.ForgeChunkManager;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.init();
    }

    public void init(FMLInitializationEvent event) {
        Registry.Blocks.register();
        Registry.Items.register();
        Registry.TileEntity.register();
        Registry.Gui.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ForgeChunkManager.setForcedChunkLoadingCallback(Anchors.INSTANCE, new ChunkLoadingHandler());
        PacketHandler.init();
    }
}
