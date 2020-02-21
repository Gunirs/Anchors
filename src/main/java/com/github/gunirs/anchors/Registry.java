package com.github.gunirs.anchors;

import com.github.gunirs.anchors.blocks.BlockWorldAnchor;
import com.github.gunirs.anchors.handlers.GuiHandler;
import com.github.gunirs.anchors.items.ItemBlockWorldAnchor;
import com.github.gunirs.anchors.items.ItemTritiumCell;
import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class Registry {
    public static class Blocks {
        public static final Block blockWorldAnchor = new BlockWorldAnchor();

        public static void register() {
            GameRegistry.registerBlock(blockWorldAnchor, ItemBlockWorldAnchor.class, blockWorldAnchor.getUnlocalizedName());
        }
    }

    public static class Items {
        public static final Item itemTritiumCell = new ItemTritiumCell();

        public static void register() {
            GameRegistry.registerItem(itemTritiumCell, itemTritiumCell.getUnlocalizedName());
        }
    }

    public static class TileEntity {
        public static void register() {
            GameRegistry.registerTileEntity(TileWorldAnchor.class, "tileWorldAnchor");
        }
    }

    public static class Gui {
        public static void register() {
            NetworkRegistry.INSTANCE.registerGuiHandler(Anchors.INSTANCE, new GuiHandler());
        }
    }
}
