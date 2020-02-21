package com.github.gunirs.anchors.handlers;

import com.github.gunirs.anchors.Registry;
import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.ArrayList;
import java.util.List;

public class ChunkLoadingHandler implements OrderedLoadingCallback {

    @Override
    public List<ForgeChunkManager.Ticket> ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world, int maxTicketCount) {
        List<Ticket> validTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            int coreX = ticket.getModData().getInteger("coreX");
            int coreY = ticket.getModData().getInteger("coreY");
            int coreZ = ticket.getModData().getInteger("coreZ");
            Block blockChunkLoader = world.getBlock(coreX, coreY, coreZ);
            if (blockChunkLoader == Registry.Blocks.blockWorldAnchor)
                validTickets.add(ticket);
        }
        return validTickets;
    }

    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
        for (Ticket ticket : tickets) {
            int coreX = ticket.getModData().getInteger("coreX");
            int coreY = ticket.getModData().getInteger("coreY");
            int coreZ = ticket.getModData().getInteger("coreZ");
            TileWorldAnchor te = (TileWorldAnchor) world.getTileEntity(coreX, coreY, coreZ);
            te.forceChunkLoadingCallback(ticket);
        }
    }
}
