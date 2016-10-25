package com.datecs.audioreader.rfid;

import com.datecs.audioreader.AudioReader;
import java.io.IOException;

public abstract class RFID {
    public static final int CARD_SUPPORT_FELICA = 4;
    public static final int CARD_SUPPORT_ISO15 = 32;
    public static final int CARD_SUPPORT_JEWEL = 16;
    public static final int CARD_SUPPORT_NFC = 8;
    public static final int CARD_SUPPORT_STSRI = 64;
    public static final int CARD_SUPPORT_TYPE_A = 1;
    public static final int CARD_SUPPORT_TYPE_B = 2;
    private AudioReader mAudioReader;

    RFID(AudioReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("The argument 'reader' is null");
        }
        this.mAudioReader = reader;
    }

    protected byte[] transmit(byte[] b) throws IOException {
        return this.mAudioReader.transmitRFID(b);
    }
}
