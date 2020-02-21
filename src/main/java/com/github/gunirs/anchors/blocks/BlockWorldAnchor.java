package com.github.gunirs.anchors.blocks;

import com.github.gunirs.anchors.Anchors;
import com.github.gunirs.anchors.tileentity.TileWorldAnchor;
import com.github.gunirs.anchors.utils.Logger;
import com.github.gunirs.anchors.utils.enums.FieldType;
import com.github.gunirs.anchors.utils.enums.LoadingMode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BlockWorldAnchor extends BlockContainer {
    public BlockWorldAnchor() {
        super(Material.rock);
        setUnlocalizedName(Anchors.MODID + ":blockWorldAnchor");
        setTextureName(Anchors.MODID + ":blockWorldAnchor");
        setHardness(2.0f);
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileWorldAnchor();
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side,
                                    float subX, float subY, float subZ) {
        if(!worldIn.isRemote)
            player.openGui(Anchors.INSTANCE, 0, worldIn, x, y, z);

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        if(!worldIn.isRemote) {
            System.out.println("[Anchors] Placed (" + x + ", " + y + ", " + z +") world anchor by " + placer.getCommandSenderName());
            try {
                Logger.log("Placed (" + x + ", " + y + ", " + z +") world anchor by " + placer.getCommandSenderName());
                Logger.Active.add(placer.getCommandSenderName(), x, y, z);
            } catch (IOException e) {
                e.printStackTrace();
            }

            NBTTagCompound nbtTagCompound = itemIn.getTagCompound();
            if(nbtTagCompound != null) {
                int chunkLoadingTime = nbtTagCompound.hasKey("chunkLoadingTime") ?
                        nbtTagCompound.getInteger("chunkLoadingTime") : 0;
                LoadingMode mode = LoadingMode.fromInteger(nbtTagCompound.hasKey("mode") ? nbtTagCompound.getShort("mode") : 0);

                TileWorldAnchor te = (TileWorldAnchor) worldIn.getTileEntity(x, y, z);
                te.setField(FieldType.CHUNKLOADINGTIME, chunkLoadingTime);
                te.setField(FieldType.MODE, mode);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if(!worldIn.isRemote) {
            System.out.println("[Anchors] Broken (" + x + ", " + y + ", " + z +") world anchor");
            try {
                Logger.log("Broken (" + x + ", " + y + ", " + z +") world anchor");
                Logger.Active.remove(x, y, z);
            } catch (IOException e) {
                e.printStackTrace();
            }

            TileWorldAnchor te = (TileWorldAnchor) worldIn.getTileEntity(x, y, z);

            Random rand = new Random();
            float randX = rand.nextFloat() * 0.8F + 0.1F;
            float randY = rand.nextFloat() * 0.8F + 0.1F;
            float randZ = rand.nextFloat() * 0.8F + 0.1F;

            ItemStack item = new ItemStack(Item.getItemFromBlock(this), 1, 0);
            EntityItem entityItem = new EntityItem(worldIn, x + randX, y + randY, z + randZ, item.copy());

            int time = (int) te.getField(FieldType.CHUNKLOADINGTIME);
            LoadingMode mode = (LoadingMode) te.getField(FieldType.MODE);
            ItemStack itemStackInFirstSlot = (ItemStack) te.getField(FieldType.FIRSTSLOT);

            if(time != 0) {
                NBTTagCompound nbtTagCompound = entityItem.getEntityItem().getTagCompound();
                if (nbtTagCompound == null)
                    nbtTagCompound = new NBTTagCompound();

                nbtTagCompound.setInteger("chunkLoadingTime", time);
                nbtTagCompound.setShort("mode", (short) mode.ordinal());
                entityItem.getEntityItem().setTagCompound(nbtTagCompound);
            }

            float factor = 0.05F;
            entityItem.motionX = rand.nextGaussian() * factor;
            entityItem.motionY = 0;
            entityItem.motionZ = rand.nextGaussian() * factor;

            if(itemStackInFirstSlot != null)
            {
                EntityItem entityFuelItem = new EntityItem(worldIn, x + randX, y + randY, z + randZ, itemStackInFirstSlot);
                entityFuelItem.motionX = entityItem.motionX;
                entityFuelItem.motionY = 0;
                entityFuelItem.motionZ = entityItem.motionZ;
                worldIn.spawnEntityInWorld(entityFuelItem);
            }
            worldIn.spawnEntityInWorld(entityItem);
        }
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z) {
        return Item.getItemFromBlock(this);
    }
}
