package com.github.gunirs.anchors.utils.enums;

public enum LoadingMode {
    SMALL,
    NORMAL,
    LARGE;

    public static LoadingMode fromInteger(int x) {
        switch(x) {
            case 0:
                return SMALL;
            case 1:
                return NORMAL;
            case 2:
                return LARGE;
        }
        return null;
    }
}
