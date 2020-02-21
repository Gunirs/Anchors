package com.github.gunirs.anchors.packets;

import com.github.gunirs.anchors.Config;
import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import com.github.gunirs.anchors.utils.enums.FieldType;
import com.github.gunirs.anchors.utils.enums.LoadingMode;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketClickButton implements IMessage {
    public PacketClickButton() {}

    private int buttonId, posX, posY, posZ, dimensionId;

    public PacketClickButton(int buttonId, int posX, int posY, int posZ, int dimensionId) {
        this.buttonId = buttonId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.dimensionId = dimensionId;
    }

    public static class Handler implements IMessageHandler<PacketClickButton, IMessage> {
        @Override
        public IMessage onMessage(PacketClickButton message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            TileWorldAnchor te = (TileWorldAnchor) player.worldObj.getTileEntity(message.posX, message.posY, message.posZ);

            switch(message.buttonId) {
                case 1:
                    LoadingMode mode = (LoadingMode) te.getField(FieldType.MODE);
                    int time = (int) te.getField(FieldType.CHUNKLOADINGTIME);

                    if(mode == LoadingMode.SMALL) {
                        te.setField(FieldType.MODE, LoadingMode.NORMAL);
                        te.setField(FieldType.CHUNKLOADINGTIME, time / Config.multiplier);
                    }
                    else if(mode == LoadingMode.NORMAL) {
                        te.setField(FieldType.MODE, LoadingMode.LARGE);
                        te.setField(FieldType.CHUNKLOADINGTIME, time / Config.multiplier);
                    }
                    else {
                        te.setField(FieldType.MODE, LoadingMode.SMALL);
                        te.setField(FieldType.CHUNKLOADINGTIME, time * (Config.multiplier * Config.multiplier));
                    }
                    te.getWorld().markBlockForUpdate(message.posX, message.posY, message.posZ);
                    break;
                case 2:
                    boolean paused = (boolean) te.getField(FieldType.ISPAUSED);

                    if(paused) {
                        te.forceChunkLoading();
                        te.setField(FieldType.ISPAUSED, false);
                    }
                    else {
                        te.releaseTicket();
                        te.setField(FieldType.ISPAUSED, true);
                    }
                    te.getWorld().markBlockForUpdate(message.posX, message.posY, message.posZ);
                    break;
            }
            return null;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.buttonId);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeInt(this.dimensionId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.buttonId = buf.readInt();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.dimensionId = buf.readInt();
    }
}
