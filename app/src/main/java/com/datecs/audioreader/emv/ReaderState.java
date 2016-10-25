package com.datecs.audioreader.emv;

public interface ReaderState {
    public static final int BORN = 0;
    public static final int CARD_HOLDER = 48;
    public static final int COMPLETION = 81;
    public static final int CONFIGURATION_IN_PROGRESS = 16;
    public static final int FINAL_SELECT = 34;
    public static final int IDLE = 32;
    public static final int ONLINE = 80;
    public static final int PAN_CHECK = 64;
    public static final int SELECTION = 33;
    public static final int TRANSACTION = 49;
}
