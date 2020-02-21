package com.github.gunirs.anchors.containers;

import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWorldAnchor extends Container {
    private TileWorldAnchor te;

    public ContainerWorldAnchor(TileWorldAnchor te, EntityPlayer player) {
        this.te = te;

        // Storage slot
        addSlotToContainer(new Slot(te, 0, 80, 17));

        // Player inventory slots
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 89 + i * 18));
            }
        }

        // Hotbar slots
        for(int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 147));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStack = itemStackInSlot.copy();

            if(index < 3 * 9) {
                if(!mergeItemStack(itemStackInSlot, 3 * 9, inventorySlots.size(), true))
                    return null;
            }
            else if(!mergeItemStack(itemStackInSlot, 0, 3 * 9, false)) {
                return null;
            }

            if(itemStackInSlot.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return te.isUseableByPlayer(player);
    }
}
