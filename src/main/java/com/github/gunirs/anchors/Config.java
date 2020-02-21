package com.github.gunirs.anchors;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class Config {
    public static String fuelItem = Registry.Items.itemTritiumCell.getUnlocalizedName();
    public static int fuelTime = 86400; // 86400
    public static int multiplier = 2;
    public static boolean enabledChunkLoader = true;

    public static void init() {
        try {
            File dir = Loader.instance().getConfigDir();
            Configuration cfg = new Configuration(new File(dir, "anchors.cfg"));
            String c = Configuration.CATEGORY_GENERAL;

            enabledChunkLoader = cfg.getBoolean("enabledChunkLoader", c, enabledChunkLoader, "Enable the chunk loader");
            fuelItem = cfg.getString("fuelItem", c, fuelItem, "Item used as fuel for the world anchor");

            fuelTime = cfg.getInt("fuelTime", c + ".time", fuelTime, 1, Integer.MAX_VALUE, "Time of operation of the world anchor for 1 unit of fuel [in seconds]");
            multiplier = cfg.getInt("multiplier", c + ".time", multiplier, 0, Integer.MAX_VALUE, "Fuel multiplier depending on the mode.\nExample: For 1 unit. fuel you get 24 hours 3x3 anchors.\nIf the player sets the 5x5 mode, then the time will be divided by a multiplier");

            cfg.save();
        }
        catch (Throwable throwable) {
            System.err.println("Failed load config. Use default values.");
            throwable.printStackTrace();
        }
    }

    static {
        init();
    }
}