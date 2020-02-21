package com.github.gunirs.anchors.tileentity;

import com.github.gunirs.anchors.Anchors;
import com.github.gunirs.anchors.Config;
import com.github.gunirs.anchors.utils.AnchorsChunkManager;
import com.github.gunirs.anchors.utils.BlockPos;
import com.github.gunirs.anchors.utils.enums.FieldType;
import com.github.gunirs.anchors.utils.enums.LoadingMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.Constants;

public class TileWorldAnchor extends TileEntity implements IInventory, ISidedInventory {
    private ItemStack itemsInFirstSlot;
    private int chunkLoadingTime;
    private Ticket ticket;
    private boolean isPaused = false;
    private LoadingMode mode = LoadingMode.NORMAL;

    public void setField(FieldType type, Object value) {
        if(type == FieldType.FIRSTSLOT) {
            if(value instanceof ItemStack) {
                this.itemsInFirstSlot = (ItemStack) value;
            }
        }
        else if(type == FieldType.CHUNKLOADINGTIME) {
            if(value instanceof Integer)
                this.chunkLoadingTime = (int) value;
        }
        else if(type == FieldType.ISPAUSED) {
            if(value instanceof Boolean) {
                this.isPaused = (boolean) value;
            }
        }
        else if(type == FieldType.MODE) {
            if(value instanceof LoadingMode) {
                this.mode = (LoadingMode) value;
            }
        }
    }

    public Object getField(FieldType type) {
        if(type == FieldType.FIRSTSLOT)
            return this.itemsInFirstSlot;
        else if(type == FieldType.CHUNKLOADINGTIME)
            return this.chunkLoadingTime;
        else if(type == FieldType.ISPAUSED)
            return this.isPaused;
        else if(type == FieldType.MODE)
            return this.mode;

        return null;
    }

    public void forceChunkLoadingCallback(Ticket ticket) {
        if(!Config.enabledChunkLoader)
            return;

        if (this.ticket == null)
            this.ticket = ticket;

        for (ChunkCoordIntPair coord : AnchorsChunkManager.getLoadArea(this.mode, xCoord, zCoord))
            ForgeChunkManager.forceChunk(this.ticket, coord);
    }

    public void forceChunkLoading() {
        if(!Config.enabledChunkLoader)
            return;

        if(this.ticket == null)
            this.ticket = ForgeChunkManager.requestTicket(Anchors.INSTANCE, worldObj, ForgeChunkManager.Type.NORMAL);

        if(this.ticket == null)
            System.out.println("[Anchors] Ticket could not be reserved [" + xCoord + ", " + yCoord + ", " + zCoord + "]");
        else {
            this.ticket.getModData().setInteger("coreX", xCoord);
            this.ticket.getModData().setInteger("coreY", yCoord);
            this.ticket.getModData().setInteger("coreZ", zCoord);

            for(ChunkCoordIntPair coord : AnchorsChunkManager.getLoadArea(this.mode, xCoord, zCoord))
                ForgeChunkManager.forceChunk(this.ticket, coord);
        }
    }

    public void releaseTicket() {
        if(this.ticket != null) {
            ForgeChunkManager.releaseTicket(this.ticket);
            this.ticket = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList items = new NBTTagList();

        if(this.itemsInFirstSlot != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setByte("Slot", (byte) 0);
            this.itemsInFirstSlot.writeToNBT(tagCompound);
            items.appendTag(tagCompound);
        }

        compound.setInteger("chunkLoadingTime", this.chunkLoadingTime);
        compound.setShort("mode", (short) this.mode.ordinal());
        compound.setBoolean("isPaused", this.isPaused);
        compound.setTag("Items", items);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.chunkLoadingTime = compound.getInteger("chunkLoadingTime");
        this.mode = LoadingMode.fromInteger(compound.getShort("mode"));
        this.isPaused = compound.getBoolean("isPaused");

        NBTTagList items = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        NBTTagCompound nbtTagCompound = items.getCompoundTagAt(0);
        this.itemsInFirstSlot = ItemStack.loadItemStackFromNBT(nbtTagCompound);
    }

    @Override
    public void updateEntity() {
        if (this.chunkLoadingTime > 0 && !this.isPaused)
            --this.chunkLoadingTime;

        if (this.itemsInFirstSlot != null && this.itemsInFirstSlot.getItem().getUnlocalizedName().equals(Config.fuelItem) && this.chunkLoadingTime == 0) {
            if (this.itemsInFirstSlot.stackSize > 1)
                --this.itemsInFirstSlot.stackSize;
            else
                this.itemsInFirstSlot = null;

            if (this.mode == LoadingMode.SMALL)
                this.chunkLoadingTime = (20 * Config.fuelTime) * Config.multiplier;
            else if (this.mode == LoadingMode.NORMAL)
                this.chunkLoadingTime = 20 * Config.fuelTime;
            else
                this.chunkLoadingTime = (20 * Config.fuelTime) / Config.multiplier;

            if (!worldObj.isRemote) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                if(!this.isPaused)
                    forceChunkLoading();
            }
        }

        if (!worldObj.isRemote && this.chunkLoadingTime == 0)
            releaseTicket();

        this.markDirty();
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(worldObj.isRemote) {
            AnchorsChunkManager.anchorsList.removeIf(
                    obj -> obj.getPosX() == xCoord && obj.getPosY() == yCoord && obj.getPosZ() == zCoord);
        }

        if(!worldObj.isRemote)
            releaseTicket();
    }

    @Override
    public void validate() {
        super.validate();

        if(worldObj.isRemote) {
            AnchorsChunkManager.anchorsList.add(new BlockPos(xCoord, yCoord, zCoord));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return this.itemsInFirstSlot;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(this.itemsInFirstSlot != null) {
            ItemStack itemStack;

            if(this.itemsInFirstSlot.stackSize == count) {
                itemStack = this.itemsInFirstSlot;
                this.itemsInFirstSlot = null;
            }
            else {
                itemStack = this.itemsInFirstSlot.splitStack(count);
                if(this.itemsInFirstSlot.stackSize == 0)
                    this.itemsInFirstSlot = null;

            }
            this.markDirty();
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if(this.itemsInFirstSlot != null) {
            ItemStack itemStack = this.itemsInFirstSlot;
            this.itemsInFirstSlot = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.itemsInFirstSlot = stack;
        if(stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "inventory.worldAnchor.name";
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq((double) xCoord + 0.5D,
                (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem().getUnlocalizedName().equals(Config.fuelItem);
    }

    @Override
    public int[] getSlotsForFace(int slot) {
        return new int[slot];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return false;
    }
}
