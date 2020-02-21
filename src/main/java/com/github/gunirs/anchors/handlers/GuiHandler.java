package com.github.gunirs.anchors.handlers;

import com.github.gunirs.anchors.containers.ContainerWorldAnchor;
import com.github.gunirs.anchors.gui.GuiWorldAnchor;
import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);

        if(te != null) {
            if(ID == 0)
                return new ContainerWorldAnchor((TileWorldAnchor) te, player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);

        if(te != null) {
            if(ID == 0)
                return new GuiWorldAnchor((TileWorldAnchor) te, player);
        }

        return null;
    }
}
