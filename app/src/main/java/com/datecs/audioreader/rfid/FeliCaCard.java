package com.datecs.audioreader.rfid;

import com.datecs.pinpad.Pinpad;
import java.io.IOException;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class FeliCaCard extends ContactlessCard {
    private static final int FELICA_MAX_READ = 4;
    private static final int FELICA_MAX_WRITE = 176;
    private static final int FELICA_MAX_WRITE_BLOCKS = 11;
    public static final int FELICA_SMARTTAG_BATTERY_LOW1 = 2;
    public static final int FELICA_SMARTTAG_BATTERY_LOW2 = 3;
    public static final int FELICA_SMARTTAG_BATTERY_NORMAL1 = 0;
    public static final int FELICA_SMARTTAG_BATTERY_NORMAL2 = 1;
    public static final int FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND = 1;
    public static final int FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND = 2;
    public static final int FELICA_SMARTTAG_DRAW_USE_LAYOUT = 3;
    public static final int FELICA_SMARTTAG_DRAW_WHITE_BACKGROUND = 0;
    private byte mFelicaSeq;
//
    public FeliCaCard(RC663 module) {
        super(module);
        this.mFelicaSeq = (byte) 1;
    }

    protected boolean deinitialize() throws IOException {
        while (true) {
            try {
                poll();
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
        this.blockSize = 16;
        return true;
    }

    private byte[] poll() throws IOException, RFIDException {
        byte[] cmd = new byte[6];
        cmd[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) 6;
        cmd[FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND] = (byte) -1;
        cmd[FELICA_SMARTTAG_DRAW_USE_LAYOUT] = (byte) -1;
        byte[] uuid = new byte[8];
        System.arraycopy(this.mModule.transmitCard(this.channel, cmd, cmd.length), FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND, uuid, FELICA_SMARTTAG_BATTERY_NORMAL1, uuid.length);
        return uuid;
    }
//
//    private byte[] transmitRawCommand(int command, byte[] data, int length) throws IOException, RFIDException {
//        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
//        int i = FELICA_SMARTTAG_BATTERY_NORMAL1 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        int index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) command;
//        System.arraycopy(this.uid, FELICA_SMARTTAG_BATTERY_NORMAL1, cmd, index, this.uid.length);
//        i = this.uid.length + FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND;
//        System.arraycopy(data, FELICA_SMARTTAG_BATTERY_NORMAL1, cmd, i, length);
//        i += length;
//        cmd[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) (i + FELICA_SMARTTAG_BATTERY_NORMAL1);
//        byte[] result = this.mModule.transmitCard(this.channel, cmd, i);
//        byte[] tmp = new byte[(result.length - 8)];
//        tmp[FELICA_SMARTTAG_BATTERY_NORMAL1] = result[FELICA_SMARTTAG_BATTERY_NORMAL1];
//        System.arraycopy(result, 9, tmp, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, result.length - 9);
//        return tmp;
//    }
//
//    public byte[] read(int serviceCode, int startBlock, int blocks) throws IOException, RFIDException {
//        if (blocks > FELICA_MAX_READ) {
//            throw new IllegalArgumentException("Too much blocks");
//        }
//        int i;
//        byte[] cmd = new byte[32];
//        int i2 = FELICA_SMARTTAG_BATTERY_NORMAL1 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) 1;
//        int i3 = i2 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i2] = (byte) (serviceCode >> 8);
//        i2 = i3 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i3] = (byte) serviceCode;
//        i3 = i2 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i2] = (byte) blocks;
//        i2 = i3;
//        for (i = FELICA_SMARTTAG_BATTERY_NORMAL1; i < blocks; i += FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND) {
//            i3 = i2 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//            cmd[i2] = Byte.MIN_VALUE;
//            i2 = i3 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//            cmd[i3] = (byte) (startBlock + i);
//        }
//        byte[] result = null;
//        i = FELICA_SMARTTAG_BATTERY_NORMAL1;
//        while (i < FELICA_SMARTTAG_DRAW_USE_LAYOUT) {
//            try {
//                result = transmitRawCommand(6, cmd, i2);
//                break;
//            } catch (RFIDException e) {
//                if (e.getErrorCode() == -6) {
//                    i += FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//                } else {
//                    throw e;
//                }
//            }
//        }
//        if (result == null) {
//            throw new RFIDException(-20);
//        } else if (result[FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND] == null && result[FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND] == null) {
//            byte[] tmp = new byte[(result.length - 4)];
//            System.arraycopy(result, FELICA_MAX_READ, tmp, FELICA_SMARTTAG_BATTERY_NORMAL1, tmp.length);
//            return tmp;
//        } else {
//            throw new RFIDException(-20);
//        }
//    }
//
//    public void write(int serviceCode, int startBlock, byte[] data) throws IOException, RFIDException {
//        if (data.length == 0 || data.length % this.blockSize != 0 || data.length / this.blockSize > FELICA_MAX_WRITE_BLOCKS) {
//            throw new IllegalArgumentException("Illegal data size");
//        }
//        int blocks = data.length / this.blockSize;
//        byte[] cmd = new byte[192];
//        int i = FELICA_SMARTTAG_BATTERY_NORMAL1 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) 1;
//        int i2 = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) (serviceCode >> 8);
//        i = i2 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i2] = (byte) serviceCode;
//        i2 = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) blocks;
//        i = i2;
//        for (int i3 = FELICA_SMARTTAG_BATTERY_NORMAL1; i3 < blocks; i3 += FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND) {
//            i2 = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//            cmd[i] = Byte.MIN_VALUE;
//            i = i2 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//            cmd[i2] = (byte) (startBlock + i3);
//        }
//        System.arraycopy(data, FELICA_SMARTTAG_BATTERY_NORMAL1, cmd, i, blocks * 16);
//        byte[] result = transmitRawCommand(8, cmd, i + (blocks * 16));
//        if (result[FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND] != null || result[FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND] != null) {
//            throw new RFIDException(-20);
//        }
//    }
//
//    /* JADX WARNING: inconsistent code. */
//    /* Code decompiled incorrectly, please refer to instructions dump. */
//    private byte[] smartTagGetStatus(boolean r13, boolean r14) throws IOException, com.datecs.audioreader.rfid.RFIDException {
//        /*
//        r12 = this;
//        r9 = 32;
//        r0 = new byte[r9];
//        r2 = 0;
//    L_0x0005:
//        r3 = 0;
//        r5 = 0;
//    L_0x0007:
//        r9 = r0.length;
//        if (r5 < r9) goto L_0x006d;
//    L_0x000a:
//        r4 = r3 + 1;
//        r9 = 1;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 9;
//        r0[r4] = r9;
//        r4 = r3 + 1;
//        r9 = 0;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 1;
//        r0[r4] = r9;
//        r4 = r3 + 1;
//        r9 = -128; // 0xffffffffffffff80 float:NaN double:NaN;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 0;
//        r0[r4] = r9;
//        r4 = r3 + 1;
//        r9 = -48;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 1;
//        r0[r4] = r9;
//        r4 = r3 + 1;
//        r9 = 1;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 0;
//        r0[r4] = r9;
//        r4 = r3 + 1;
//        r9 = 0;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 0;
//        r0[r4] = r9;
//        r4 = r3 + 1;
//        r9 = 0;
//        r0[r3] = r9;
//        r3 = r4 + 1;
//        r9 = 0;
//        r0[r4] = r9;
//        r3 = r3 + 8;
//        r9 = 8;
//        r7 = r12.transmitRawCommand(r9, r0, r3);
//        r9 = 1;
//        r9 = r7[r9];
//        if (r9 != 0) goto L_0x0065;
//    L_0x0060:
//        r9 = 2;
//        r9 = r7[r9];
//        if (r9 == 0) goto L_0x0073;
//    L_0x0065:
//        r9 = new com.datecs.audioreader.rfid.RFIDException;
//        r10 = -20;
//        r9.<init>(r10);
//        throw r9;
//    L_0x006d:
//        r9 = 0;
//        r0[r5] = r9;
//        r5 = r5 + 1;
//        goto L_0x0007;
//    L_0x0073:
//        r6 = 1;
//        r9 = 2304; // 0x900 float:3.229E-42 double:1.1383E-320;
//        r10 = 0;
//        r7 = r12.read(r9, r10, r6);
//        r9 = 4;
//        r9 = r7[r9];
//        r12.mFelicaSeq = r9;
//        r9 = 3;
//        r9 = r7[r9];
//        r10 = -16;
//        if (r9 == r10) goto L_0x0097;
//    L_0x0087:
//        r9 = 3;
//        r9 = r7[r9];
//        r10 = -15;
//        if (r9 == r10) goto L_0x0097;
//    L_0x008e:
//        if (r14 != 0) goto L_0x00a2;
//    L_0x0090:
//        r9 = 3;
//        r9 = r7[r9];
//        r10 = -14;
//        if (r9 != r10) goto L_0x00a2;
//    L_0x0097:
//        r9 = 16;
//        r8 = new byte[r9];
//        r9 = 0;
//        r10 = 0;
//        r11 = r8.length;
//        java.lang.System.arraycopy(r7, r9, r8, r10, r11);
//        return r8;
//    L_0x00a2:
//        r9 = 3;
//        r9 = r7[r9];
//        r10 = -14;
//        if (r9 != r10) goto L_0x00ad;
//    L_0x00a9:
//        r9 = 10;
//        if (r2 < r9) goto L_0x00b7;
//    L_0x00ad:
//        if (r13 != 0) goto L_0x00b7;
//    L_0x00af:
//        r9 = new com.datecs.audioreader.rfid.RFIDException;
//        r10 = -20;
//        r9.<init>(r10);
//        throw r9;
//    L_0x00b7:
//        r9 = 100;
//        java.lang.Thread.sleep(r9);	 Catch:{ InterruptedException -> 0x00c0 }
//    L_0x00bc:
//        r2 = r2 + 1;
//        goto L_0x0005;
//    L_0x00c0:
//        r1 = move-exception;
//        r1.printStackTrace();
//        goto L_0x00bc;
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.datecs.audioreader.rfid.FeliCaCard.smartTagGetStatus(boolean, boolean):byte[]");
//    }
//
//    private byte[] smartTagWaitCompletion() throws IOException, RFIDException {
//        return smartTagGetStatus(false, true);
//    }
//
//    private byte[] smartTagReadResponse(int blocks) throws IOException, RFIDException {
//        if (blocks <= 0) {
//            return new byte[FELICA_SMARTTAG_BATTERY_NORMAL1];
//        }
//        byte[] result = read(2304, blocks + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, FELICA_SMARTTAG_BATTERY_NORMAL1);
//        if (result[FELICA_SMARTTAG_DRAW_USE_LAYOUT] == -16 || result[FELICA_SMARTTAG_DRAW_USE_LAYOUT] == -15) {
//            byte[] tmp = new byte[(blocks * 16)];
//            System.arraycopy(result, 16, tmp, FELICA_SMARTTAG_BATTERY_NORMAL1, tmp.length);
//            return tmp;
//        }
//        throw new RFIDException(-20);
//    }
//
//    private void smartTagCommand(int command, int fsum, int fnum, byte[] param, int blocks, byte[] data) throws IOException, RFIDException {
//        byte[] cmd = new byte[1920];
//        int i = FELICA_SMARTTAG_BATTERY_NORMAL1 + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) 1;
//        int index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) 9;
//        i = index + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[index] = (byte) 0;
//        index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) (blocks + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND);
//        i = index;
//        for (int i2 = FELICA_SMARTTAG_BATTERY_NORMAL1; i2 < blocks + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND; i2 += FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND) {
//            index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//            cmd[i] = Byte.MIN_VALUE;
//            i = index + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//            cmd[index] = (byte) 0;
//        }
//        index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) command;
//        i = index + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[index] = (byte) fsum;
//        index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) fnum;
//        i = index + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[index] = (byte) (blocks * 16);
//        if (command == 208) {
//            this.mFelicaSeq = (byte) 0;
//        } else {
//            this.mFelicaSeq = (byte) (this.mFelicaSeq + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND);
//            if (this.mFelicaSeq == null) {
//                this.mFelicaSeq = (byte) 1;
//            }
//        }
//        index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = this.mFelicaSeq;
//        i = index + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[index] = (byte) 0;
//        index = i + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[i] = (byte) 0;
//        i = index + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        cmd[index] = (byte) 0;
//        if (param != null) {
//            System.arraycopy(param, FELICA_SMARTTAG_BATTERY_NORMAL1, cmd, i, 8);
//        }
//        index = i + 8;
//        if (data != null) {
//            System.arraycopy(data, FELICA_SMARTTAG_BATTERY_NORMAL1, cmd, index, blocks * 16);
//        }
//        byte[] result = transmitRawCommand(8, cmd, index + (blocks * 16));
//        if (result[FELICA_SMARTTAG_BATTERY_NORMAL1] != null || result[FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND] != null) {
//            throw new RFIDException(-20);
//        }
//    }
//
//    public int smartTagGetBatteryStatus() throws IOException, RFIDException {
//        return smartTagGetStatus(true, false)[6] & Pinpad.EMV2_MESSAGE_UI_NA;
//    }
//
//    public void smartTagClearScreen() throws IOException, RFIDException {
//        smartTagCommand(161, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, null, FELICA_SMARTTAG_BATTERY_NORMAL1, null);
//        smartTagGetStatus(false, false);
//    }
//
//    public void smartTagDrawImage(int x, int y, int width, int height, int drawMode, int layout, byte[] data) throws IOException, RFIDException {
//        int w = (width + 7) / 8;
//        int m1 = x % 8;
//        int m2 = 8 - ((x + width) % 8);
//        if (m2 == 8) {
//            m2 = FELICA_SMARTTAG_BATTERY_NORMAL1;
//        }
//        int nBlocks = ((w * height) + 15) / 16;
//        int nPackets = ((nBlocks + FELICA_MAX_WRITE_BLOCKS) - 1) / FELICA_MAX_WRITE_BLOCKS;
//        int i = FELICA_SMARTTAG_BATTERY_NORMAL1;
//        int packet = FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        while (i < nBlocks) {
//            int tosend = nBlocks - i;
//            if (tosend > FELICA_MAX_WRITE_BLOCKS) {
//                tosend = FELICA_MAX_WRITE_BLOCKS;
//            }
//            byte[] cmd = new byte[]{(byte) ((x / 8) + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND), (byte) (y + FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND), (byte) m1, (byte) m2, (byte) w, (byte) drawMode, (byte) layout, (byte) 3};
//            byte[] tmp = new byte[16];
//            System.arraycopy(data, i * 16, tmp, FELICA_SMARTTAG_BATTERY_NORMAL1, tmp.length);
//            smartTagCommand(160, nPackets, packet, cmd, tosend, tmp);
//            i += tosend;
//            packet += FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        }
//        smartTagGetStatus(false, false);
//    }
//
//    public void smartTagSaveLayout(int layout) throws IOException, RFIDException {
//        byte[] cmd = new byte[8];
//        cmd[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) layout;
//        smartTagCommand(178, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, cmd, FELICA_SMARTTAG_BATTERY_NORMAL1, null);
//    }
//
//    public void smartTagDisplayLayout(int layout) throws IOException, RFIDException {
//        byte[] cmd = new byte[8];
//        cmd[5] = (byte) 3;
//        cmd[6] = (byte) layout;
//        cmd[7] = (byte) 1;
//        smartTagCommand(160, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, cmd, FELICA_SMARTTAG_BATTERY_NORMAL1, null);
//    }
//
//    public void smartTagWrite(int address, byte[] data) throws IOException, RFIDException {
//        int length = data.length;
//        int nBlocks = (length + 15) / 16;
//        int nPackets = ((nBlocks + FELICA_MAX_WRITE_BLOCKS) - 1) / FELICA_MAX_WRITE_BLOCKS;
//        int i = FELICA_SMARTTAG_BATTERY_NORMAL1;
//        int packet = FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        while (i < nBlocks) {
//            int tosend = nBlocks - i;
//            if (tosend > FELICA_MAX_WRITE_BLOCKS) {
//                tosend = FELICA_MAX_WRITE_BLOCKS;
//            }
//            int toSendLength = tosend * 16;
//            if (toSendLength > length) {
//                toSendLength = length;
//            }
//            byte[] cmdWrite = new byte[8];
//            cmdWrite[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) (address >> 8);
//            cmdWrite[FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND] = (byte) address;
//            cmdWrite[FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND] = (byte) (toSendLength >> 8);
//            cmdWrite[FELICA_SMARTTAG_DRAW_USE_LAYOUT] = (byte) toSendLength;
//            byte[] tmp = new byte[16];
//            System.arraycopy(data, i * 16, tmp, FELICA_SMARTTAG_BATTERY_NORMAL1, tmp.length);
//            smartTagCommand(FELICA_MAX_WRITE, nPackets, packet, cmdWrite, tosend, tmp);
//            i += tosend;
//            address += toSendLength;
//            length -= toSendLength;
//            packet += FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND;
//        }
//        smartTagGetStatus(false, true);
//    }
//
//    public byte[] smartTagRead(int address, int length) throws IOException, RFIDException {
//        byte[] data = new byte[length];
//        int offset = FELICA_SMARTTAG_BATTERY_NORMAL1;
//        while (length > 0) {
//            int toReadBlocks = (length + 15) / 16;
//            if (toReadBlocks > FELICA_MAX_WRITE_BLOCKS) {
//                toReadBlocks = FELICA_MAX_WRITE_BLOCKS;
//            }
//            int toReadLength = toReadBlocks * 16;
//            byte[] cmdRead = new byte[8];
//            cmdRead[FELICA_SMARTTAG_BATTERY_NORMAL1] = (byte) (address >> 8);
//            cmdRead[FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND] = (byte) address;
//            cmdRead[FELICA_SMARTTAG_DRAW_KEEP_BACKGROUND] = (byte) (toReadLength >> 8);
//            cmdRead[FELICA_SMARTTAG_DRAW_USE_LAYOUT] = (byte) toReadLength;
//            smartTagCommand(192, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, FELICA_SMARTTAG_DRAW_BLACK_BACKGROUND, cmdRead, FELICA_SMARTTAG_BATTERY_NORMAL1, null);
//            byte[] result = smartTagReadResponse(toReadBlocks);
//            System.arraycopy(result, FELICA_SMARTTAG_BATTERY_NORMAL1, data, offset, result.length);
//            address += toReadLength;
//            offset += toReadLength;
//            length -= toReadLength;
//        }
//        return data;
//    }
}
