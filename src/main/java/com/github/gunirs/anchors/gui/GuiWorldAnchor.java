package com.github.gunirs.anchors.gui;

import com.github.gunirs.anchors.containers.ContainerWorldAnchor;
import com.github.gunirs.anchors.handlers.PacketHandler;
import com.github.gunirs.anchors.packets.PacketClickButton;
import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import com.github.gunirs.anchors.utils.Utils;
import com.github.gunirs.anchors.utils.enums.FieldType;
import com.github.gunirs.anchors.utils.enums.LoadingMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiWorldAnchor extends GuiContainer {
    public static final ResourceLocation RESOURCE_LOCATION_GUI =
            new ResourceLocation("anchors:textures/container/gui/guiChunkLoader.png");

    private EntityPlayer player;
    private TileWorldAnchor te;

    private GuiButton buttonModeSwitch;
    private GuiButton buttonPause;

    public GuiWorldAnchor(TileWorldAnchor te, EntityPlayer player) {
        super(new ContainerWorldAnchor(te, player));
        this.player = player;
        this.te = te;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(this.buttonModeSwitch = new GuiButton(1, width / 2 + 43, (height - ySize) / 2 + 20, 40, 20, Utils.getMode(((LoadingMode) te.getField(FieldType.MODE)).ordinal())));
        buttonList.add(this.buttonPause = new GuiButton(2, width / 2 + 43, (height - ySize) / 2 + 45, 40, 20, Utils.getPaused((boolean) te.getField(FieldType.ISPAUSED))));
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(RESOURCE_LOCATION_GUI);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(I18n.format(te.getInventoryName()), (xSize / 2) - (fontRendererObj.getStringWidth(I18n.format(te.getInventoryName())) / 2), 6, 4210752, false);

        fontRendererObj.drawString(I18n.format("gui.workTime.text"), (xSize / 2) - (fontRendererObj.getStringWidth(I18n.format("gui.workTime.text"))) / 2, 45, 4210752);
        fontRendererObj.drawString(Utils.getChunkLoadingTime((int) te.getField(FieldType.CHUNKLOADINGTIME)), (xSize / 2) - (fontRendererObj.getStringWidth(Utils.getChunkLoadingTime((int) te.getField(FieldType.CHUNKLOADINGTIME)))) / 2, 55, 4210752);

        fontRendererObj.drawString(I18n.format(player.inventory.getInventoryName()), 8, ySize - 96 + 8, 4210752);
    }

    @Override
    public void updateScreen() {
        if(mc.theWorld.getTileEntity(te.xCoord, te.yCoord, te.zCoord) != te) {
            mc.currentScreen = null;
            mc.setIngameFocus();
        }

        this.buttonModeSwitch.displayString = Utils.getMode(((LoadingMode) te.getField(FieldType.MODE)).ordinal());
        this.buttonPause.displayString = Utils.getPaused((boolean) te.getField(FieldType.ISPAUSED));

        super.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                PacketHandler.INSTANCE.sendToServer(new PacketClickButton(1, te.xCoord, te.yCoord, te.zCoord, player.dimension));
                break;
            case 2:
                PacketHandler.INSTANCE.sendToServer(new PacketClickButton(2, te.xCoord, te.yCoord, te.zCoord, player.dimension));
                break;
        }
    }
}
