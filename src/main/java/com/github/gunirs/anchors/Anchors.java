package com.github.gunirs.anchors;

import com.github.gunirs.anchors.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Anchors.MODID, name = Anchors.NAME, version = Anchors.VERSION)
public class Anchors {
    public static final String
            MODID = "anchors",
            NAME = "Anchors",
            VERSION = "1.0beta";

    @Instance(MODID)
    public static Anchors INSTANCE;

    @SidedProxy(serverSide = "com.github.gunirs.anchors.proxy.CommonProxy",
            clientSide = "com.github.gunirs.anchors.proxy.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
