package com.datecs.audioreader.rfid;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class ISO14443Card extends ContactlessCard {
    private static final int ISO14B_TRANSIEVE = 0;
    private static final int MAX_UID_LEN = 10;
    private static final int MF_AUTHENTA = 96;
    private static final int MF_AUTHENTB = 97;
    private static final int MF_HALT = 80;
    private static final int MF_READ16 = 48;
    private static final int MF_SELECT = 147;
    private static final int MF_SETKEY = 1;
    private static final int MF_WRITE16 = 160;
    private static final int MF_WRITE4 = 162;
    private boolean mSelectNeeded;
    private byte[] mUUIDSelected;

    public ISO14443Card(RC663 module) {
        super(module);
        this.mUUIDSelected = new byte[MAX_UID_LEN];
        this.mSelectNeeded = true;
    }

    protected boolean deinitialize() throws IOException {
        while (true) {
            try {
                select(true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (RFIDException e2) {
                return true;
            }
        }
    }

    public boolean initialize() throws IOException {
        this.type = ISO14B_TRANSIEVE;
        this.capacity = ISO14B_TRANSIEVE;
        if ((this.sak == (short) 36 || this.sak == (short) 32) && this.atqa == (short) 17411) {
            this.type = 9;
        }
        if (this.sak == (short) 0 && this.atqa == (short) 17408) {
            this.type = 4;
        }
        if (this.sak == (short) 9 && (this.atqa & 3840) == AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) {
            this.type = MF_SETKEY;
            this.capacity = 320;
        }
        if (this.sak == (short) 8 && (this.atqa & 3840) == AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) {
            this.type = 2;
            this.capacity = AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT;
        }
        if (this.sak == (short) 24 && (this.atqa & 3840) == PKIFailureInfo.timeNotAvailable) {
            this.type = 3;
            this.capacity = AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
        }
        if ((this.sak & 2) == 0 && (this.sak & 8) == 0 && (this.sak & 16) == 0 && (this.sak & 32) == MF_SETKEY) {
            this.type = 6;
        }
        return true;
    }

    private void selectCard() throws IOException, RFIDException {
        select(this.mSelectNeeded);
    }

    private void select(boolean force) throws IOException, RFIDException {
        if (!force) {
            boolean match = true;
            for (int i = ISO14B_TRANSIEVE; i < this.uid.length && match; i += MF_SETKEY) {
                if (this.uid[i] == this.mUUIDSelected[i]) {
                    match = true;
                } else {
                    match = false;
                }
            }
            if (match) {
                return;
            }
        }
        byte[] cmd = new byte[32];
        int index = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) -109;
        System.arraycopy(this.uid, ISO14B_TRANSIEVE, cmd, index, this.uid.length);
        this.mModule.transmitCard(this.channel, cmd, this.uid.length + MF_SETKEY);
        this.mSelectNeeded = false;
        System.arraycopy(this.uid, ISO14B_TRANSIEVE, this.mUUIDSelected, ISO14B_TRANSIEVE, this.uid.length);
    }

    public void loadKey(int keyIndex, char type, byte[] key) throws IOException, RFIDException {
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) 1;
        int i2 = i + MF_SETKEY;
        cmd[i] = (byte) keyIndex;
        i = i2 + MF_SETKEY;
        cmd[i2] = (byte) type;
        System.arraycopy(key, ISO14B_TRANSIEVE, cmd, i, key.length);
        this.mModule.transmitCard(MF_SETKEY, cmd, key.length + 3);
    }

    public void authenticate(char type, int address, byte[] key) throws IOException, RFIDException {
        selectCard();
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) (type == 'A' ? MF_AUTHENTA : MF_AUTHENTB);
        int index = i + MF_SETKEY;
        cmd[i] = (byte) address;
        System.arraycopy(this.uid, ISO14B_TRANSIEVE, cmd, index, this.uid.length);
        index = this.uid.length + 2;
        System.arraycopy(key, ISO14B_TRANSIEVE, cmd, index, key.length);
        try {
            this.mModule.transmitCard(this.channel, cmd, index + key.length);
        } catch (RFIDException e) {
            this.mSelectNeeded = true;
            throw e;
        }
    }

    public void authenticate(char type, int address, int keyIndex) throws IOException, RFIDException {
        selectCard();
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) (type == 'A' ? MF_AUTHENTA : MF_AUTHENTB);
        int i2 = i + MF_SETKEY;
        cmd[i] = (byte) address;
        i = i2 + MF_SETKEY;
        cmd[i2] = (byte) keyIndex;
        System.arraycopy(this.uid, ISO14B_TRANSIEVE, cmd, i, this.uid.length);
        try {
            this.mModule.transmitCard(this.channel, cmd, this.uid.length + 3);
        } catch (RFIDException e) {
            this.mSelectNeeded = true;
            throw e;
        }
    }

    public byte[] read16(int address) throws IOException, RFIDException {
        selectCard();
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) 48;
        int index = i + MF_SETKEY;
        cmd[i] = (byte) address;
        try {
            byte[] result = this.mModule.transmitCard(this.channel, cmd, index);
            if (result.length < 16) {
                throw new RFIDException(-6);
            } else if (result.length <= 16) {
                return result;
            } else {
                byte[] tmp = new byte[16];
                System.arraycopy(result, ISO14B_TRANSIEVE, tmp, ISO14B_TRANSIEVE, tmp.length);
                return tmp;
            }
        } catch (RFIDException e) {
            this.mSelectNeeded = true;
            throw e;
        }
    }

    public void write16(int address, byte[] data) throws IOException, RFIDException {
        selectCard();
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) -96;
        int index = i + MF_SETKEY;
        cmd[i] = (byte) address;
        System.arraycopy(data, ISO14B_TRANSIEVE, cmd, index, data.length);
        try {
            this.mModule.transmitCard(this.channel, cmd, data.length + 2);
        } catch (RFIDException e) {
            this.mSelectNeeded = true;
            throw e;
        }
    }

    private void write4(int address, byte[] data) throws IOException, RFIDException {
        selectCard();
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) -94;
        int index = i + MF_SETKEY;
        cmd[i] = (byte) address;
        System.arraycopy(data, ISO14B_TRANSIEVE, cmd, index, data.length);
        try {
            this.mModule.transmitCard(this.channel, cmd, data.length + 2);
        } catch (RFIDException e) {
            this.mSelectNeeded = true;
            throw e;
        }
    }

    public void ulcSetKey(byte[] key) throws IOException, RFIDException {
        int i;
        selectCard();
        byte[] cmd = new byte[16];
        for (i = ISO14B_TRANSIEVE; i < 8; i += MF_SETKEY) {
            cmd[i] = key[(8 - i) - 1];
            cmd[i + 8] = key[(16 - i) - 1];
        }
        for (i = ISO14B_TRANSIEVE; i < 4; i += MF_SETKEY) {
            byte[] tmp = new byte[4];
            System.arraycopy(cmd, i * 4, tmp, ISO14B_TRANSIEVE, tmp.length);
            write4(i + 44, tmp);
        }
    }

    private void halt() throws IOException, RFIDException {
        byte[] cmd = new byte[2];
        cmd[ISO14B_TRANSIEVE] = (byte) 80;
        this.mSelectNeeded = true;
        this.mModule.transmitCard(this.channel, cmd, cmd.length);
    }

    private static void rol(byte[] data, int offset, int len) {
        byte first = data[offset];
        for (int i = ISO14B_TRANSIEVE; i < len - 1; i += MF_SETKEY) {
            data[offset + i] = data[(offset + i) + MF_SETKEY];
        }
        data[(offset + len) - 1] = first;
    }

    private static byte[] decryptCBC3DES(byte[] key, byte[] iv, byte[] input, int offset, int length) {
        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(key, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(2, newKey, ivSpec);
            return cipher.doFinal(input, offset, length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encryptCBC3DES(byte[] key, byte[] iv, byte[] input, int offset, int length) {
        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(key, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(MF_SETKEY, newKey, ivSpec);
            return cipher.doFinal(input, offset, length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ulcAuthenticate(byte[] key) throws IOException, RFIDException {
        byte[] iv = new byte[8];
        byte[] cmd = new byte[32];
        int i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) 26;
        int index = i + MF_SETKEY;
        cmd[i] = (byte) 0;
        byte[] result = this.mModule.transmitCard(this.channel, cmd, index);
        byte[] random = new byte[8];
        new Random(System.currentTimeMillis()).nextBytes(random);
        i = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) -81;
        System.arraycopy(random, ISO14B_TRANSIEVE, cmd, i, random.length);
        index = random.length + MF_SETKEY;
        byte[] dec = decryptCBC3DES(key, iv, result, MF_SETKEY, 8);
        System.arraycopy(dec, ISO14B_TRANSIEVE, cmd, index, dec.length);
        rol(cmd, index, 8);
        index += 8;
        for (int n = ISO14B_TRANSIEVE; n < iv.length; n += MF_SETKEY) {
            iv[n] = (byte) 0;
        }
        byte[] enc = encryptCBC3DES(key, iv, cmd, MF_SETKEY, 16);
        System.arraycopy(enc, ISO14B_TRANSIEVE, cmd, MF_SETKEY, enc.length);
        this.mModule.transmitCard(this.channel, cmd, index);
    }

    private byte[] transieve(byte[] input) throws IOException, RFIDException {
        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
        int index = ISO14B_TRANSIEVE + MF_SETKEY;
        cmd[ISO14B_TRANSIEVE] = (byte) 0;
        System.arraycopy(this.uid, ISO14B_TRANSIEVE, cmd, index, this.uid.length);
        int index2 = this.uid.length + MF_SETKEY;
        System.arraycopy(input, ISO14B_TRANSIEVE, cmd, index2, input.length);
        return this.mModule.transmitCard(this.channel, cmd, index2 + input.length);
    }
}
