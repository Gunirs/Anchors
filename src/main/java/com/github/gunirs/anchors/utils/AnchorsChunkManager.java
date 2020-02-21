package com.github.gunirs.anchors.utils;

import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import com.github.gunirs.anchors.utils.enums.FieldType;
import com.github.gunirs.anchors.utils.enums.LoadingMode;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.*;

public class AnchorsChunkManager {
    public static List<BlockPos> anchorsList = new ArrayList<>();
    public static boolean rendered = false;

    public static Set<ChunkCoordIntPair> getLoadingChunksInRadius(World world, double x, double z) {
        return getLoadingChunksInRadius(world, x, z, 1024);
    }

    public static Set<ChunkCoordIntPair> getLoadingChunksInRadius(World world, double x, double z, int radius) {
        Set<ChunkCoordIntPair> chunks = new HashSet<>();
        for(BlockPos a : anchorsList) {
            if(Utils.getDistance(x, a.getPosY(), z, a.getPosX(), a.getPosY(), a.getPosZ()) < radius) {
                TileWorldAnchor te = (TileWorldAnchor) world.getTileEntity(a.getPosX(), a.getPosY(), a.getPosZ());
                if(te != null) {
                    LoadingMode mode = (LoadingMode) te.getField(FieldType.MODE);
                    for(int i = -mode.ordinal(); i < mode.ordinal() + 1; i++) {
                        for(int j = -mode.ordinal(); j < mode.ordinal() + 1; j++) {
                            chunks.add(new ChunkCoordIntPair((a.getPosX() >> 4) + i, (a.getPosZ() >> 4) + j));
                        }
                    }
                }
            }
        }
        return chunks;
    }

    public static List<ChunkCoordIntPair> getLoadArea(LoadingMode mode, int x, int z) {
        List<ChunkCoordIntPair> loadArea = new LinkedList<>();

        for(int i = -mode.ordinal(); i < mode.ordinal() + 1; i++) {
            for(int j = -mode.ordinal(); j < mode.ordinal() + 1; j++) {
                loadArea.add(new ChunkCoordIntPair((x >> 4) + i, (z >> 4) + j));
            }
        }
        return loadArea;
    }
}
