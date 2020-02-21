package com.github.gunirs.anchors.proxy;

import com.github.gunirs.anchors.events.EventKeybinding;
import com.github.gunirs.anchors.events.EventVisualRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
    public static final KeyBinding visualRenderKeybind = new KeyBinding("key.chunkloading.view",
            Keyboard.KEY_F10, "key.chunkloading.category");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        FMLCommonHandler.instance().bus().register(new EventKeybinding());
        MinecraftForge.EVENT_BUS.register(new EventVisualRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ClientRegistry.registerKeyBinding(visualRenderKeybind);
    }
}
