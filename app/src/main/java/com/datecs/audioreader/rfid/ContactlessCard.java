package com.datecs.audioreader.rfid;

import java.io.IOException;

public abstract class ContactlessCard {
    public static final int CARD_FELICA = 11;
    public static final int CARD_ISO14443A = 6;
    public static final int CARD_ISO14443B = 10;
    public static final int CARD_ISO15693 = 8;
    public static final int CARD_MIFARE_CLASSIC_1K = 2;
    public static final int CARD_MIFARE_CLASSIC_4K = 3;
    public static final int CARD_MIFARE_DESFIRE = 9;
    public static final int CARD_MIFARE_MINI = 1;
    public static final int CARD_MIFARE_PLUS = 7;
    public static final int CARD_MIFARE_ULTRALIGHT = 4;
    public static final int CARD_MIFARE_ULTRALIGHT_C = 5;
    public static final int CARD_ST_SRI = 12;
    public static final int CARD_UNKNOWN = 0;
    public byte afi;
    public short atqa;
    public int blockSize;
    public int capacity;
    public int channel;
    public byte dsfid;
    public boolean hyatt64Card;
    protected RC663 mModule;
    public int maxBlocks;
    public short sak;
    public int type;
    public byte[] uid;

    protected abstract boolean deinitialize() throws IOException;

    protected abstract boolean initialize() throws IOException;

    public ContactlessCard(RC663 module) {
        this.mModule = module;
    }

    public void remove() throws IOException {
        this.mModule.close();
    }

    public RFID getModule() {
        return this.mModule;
    }
}
