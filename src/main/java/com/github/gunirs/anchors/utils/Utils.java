package com.github.gunirs.anchors.utils;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static String getMode(int mode) {
        if(mode == 0)
            return I18n.format("gui.loadingModeSmall.text");
        else if(mode == 1)
            return I18n.format("gui.loadingModeNormal.text");
        else
            return I18n.format("gui.loadingModeLarge.text");
    }

    public static String getPaused(boolean paused) {
        return paused ? I18n.format("gui.pausedOFF.text") : I18n.format("gui.pausedON.text");
    }

    public static String getChunkLoadingTime(int ticks) {
        return ticks == 0 ? "0" + getI18n("day") + " 0" + getI18n("hour") + " 0" + getI18n("second") :
                Utils.convertSecToDHMS(ticks / 20);
    }

    public static String convertSecToDHMS(int sec) {
        int day = (int) TimeUnit.SECONDS.toDays(sec);
        long hours = TimeUnit.SECONDS.toHours(sec) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(sec) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);

        return day > 0 ? day + "" + getI18n("day") + " " + hours + "" + getI18n("hour") + " " + minute + getI18n("minute") :
                    hours + "" + getI18n("hour") + " " + minute + "" + getI18n("minute") + " " + second + getI18n("second");
    }

    private static String getI18n(String s) {
        return I18n.format("gui." + s + "Short.text");
    }

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double dx = x - x1;
        double dy = y - y1;
        double dz = z - z1;
        return MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    }
}
