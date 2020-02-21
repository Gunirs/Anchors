package com.github.gunirs.anchors.events;

import com.github.gunirs.anchors.utils.AnchorsChunkManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class EventVisualRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        if(AnchorsChunkManager.rendered) {
            for(ChunkCoordIntPair pair : AnchorsChunkManager.getLoadingChunksInRadius(mc.thePlayer.worldObj, mc.thePlayer.posX, mc.thePlayer.posZ)) {
                renderVisualChunk(pair.chunkXPos, pair.chunkZPos, event.partialTicks);
            }
        }
    }

    private void renderVisualChunk(int chunkX, int chunkZ, float partialTicks) {
        double playerX = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * partialTicks;
        double playerY = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * partialTicks;
        double playerZ = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * partialTicks;

        float x = chunkX * 16;
        float y = (float) (mc.thePlayer.posY + 70);
        float z = chunkZ * 16;

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glTranslated(-playerX, -playerY, -playerZ);
        GL11.glColor3ub((byte) 255, (byte) 255, (byte) 0);

        GL11.glLineWidth(3.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x + 16, y, z);

        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x, y, z + 16);

        GL11.glVertex3f(x, y, z + 16);
        GL11.glVertex3f(x + 16, y, z + 16);

        GL11.glVertex3f(x + 16, y, z);
        GL11.glVertex3f(x + 16, y, z + 16);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glPopMatrix();
    }
}
