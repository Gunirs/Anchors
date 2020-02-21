package com.github.gunirs.anchors.items;

import com.github.gunirs.anchors.Anchors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTritiumCell extends Item {
    public ItemTritiumCell() {
        setUnlocalizedName(Anchors.MODID + ":itemTritiumCell");
        setTextureName(Anchors.MODID + ":itemTritiumCell");
        setCreativeTab(CreativeTabs.tabRedstone);
        setMaxStackSize(64);
    }
}
