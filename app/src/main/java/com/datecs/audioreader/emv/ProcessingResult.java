package com.datecs.audioreader.emv;

public interface ProcessingResult {
    public static final int ABORTED = 13;
    public static final int CANCELLED = 1;
    public static final int CARD_BLOCKED = 2;
    public static final int CARD_MISSING = 3;
    public static final int CHIP_ERROR = 4;
    public static final int CONFIGURATION_ERROR = 15;
    public static final int DATA_ERROR = 5;
    public static final int EMPTY_LIST = 6;
    public static final int EMV_LIB = 32769;
    public static final int FALLBACK_PROHIBITED = 14;
    public static final int FLOW_CONTROL = 32770;
    public static final int GPO6985 = 7;
    public static final int INPUT_DATA_ERROR = 32774;
    public static final int INTERNAL_ERROR = 32771;
    public static final int MISSING_DATA = 8;
    public static final int NOT_ACCEPTED = 11;
    public static final int NO_CARD_INSERTED = 9;
    public static final int NO_PROFILE = 10;
    public static final int OK = 0;
    public static final int OUT_OF_MEMORY = 32775;
    public static final int RESELECT = 32772;
    public static final int SECURITY = 32773;
    public static final int TIMEOUT = 12;
}
