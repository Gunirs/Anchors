package com.github.gunirs.anchors.utils.enums;

public enum FieldType {
    FIRSTSLOT,
    CHUNKLOADINGTIME,
    ISPAUSED,
    MODE;

    public static FieldType fromInteger(int x) {
        switch(x) {
            case 0:
                return FIRSTSLOT;
            case 1:
                return CHUNKLOADINGTIME;
            case 2:
                return ISPAUSED;
            case 3:
                return MODE;
        }
        return null;
    }
}
