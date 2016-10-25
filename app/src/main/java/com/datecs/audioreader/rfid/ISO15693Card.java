package com.datecs.audioreader.rfid;

import com.datecs.pinpad.Pinpad;
import java.io.IOException;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.x509.ReasonFlags;

public class ISO15693Card extends ContactlessCard {
    private static final int ISO15_FLAG_CARD_ADDRESSED = 32;
    private static final int ISO15_FLAG_LOW_DATA_RATE = 2;
    private static final int ISO15_FLAG_OPTIONS = 64;
    private static final int ISO15_GET_MULTIPLE_BLOCK_SECURITY_STATUS = 44;
    private static final int ISO15_GET_SYSTEM_INFO = 43;
    private static final int ISO15_HALT = 2;
    private static final int ISO15_LOCK_AFI = 40;
    private static final int ISO15_LOCK_BLOCK = 34;
    private static final int ISO15_LOCK_DSFID = 42;
    private static final int ISO15_MAX_BLOCKS = 1;
    private static final int ISO15_READ_BLOCK = 32;
    private static final int ISO15_RESET = 38;
    private static final int ISO15_SELECT = 37;
    private static final int ISO15_WRITE_AFI = 39;
    private static final int ISO15_WRITE_BLOCK = 33;
    private static final int ISO15_WRITE_DSFID = 41;

    public ISO15693Card(RC663 module) {
        super(module);
    }

    protected boolean deinitialize() throws IOException {
        while (true) {
            try {
                getSystemInformation();
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
        try {
            getSystemInformation();
            return true;
        } catch (RFIDException e) {
            return false;
        }
    }

    public void lockBlock(int block) throws IOException, RFIDException {
        byte[] cmd = new byte[ISO15_READ_BLOCK];
        int index = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index2 = index + ISO15_MAX_BLOCKS;
        cmd[index] = (byte) 34;
        System.arraycopy(this.uid, 0, cmd, index2, this.uid.length);
        index2 = this.uid.length + ISO15_HALT;
        index = index2 + ISO15_MAX_BLOCKS;
        cmd[index2] = (byte) block;
        this.mModule.transmitCard(this.channel, cmd, index);
    }

    public byte[] read(int startBlock, int blocks) throws IOException, RFIDException {
        byte[] cmd = new byte[ISO15_READ_BLOCK];
        int index = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index2 = index + ISO15_MAX_BLOCKS;
        cmd[index] = (byte) 32;
        System.arraycopy(this.uid, 0, cmd, index2, this.uid.length);
        index2 = this.uid.length + ISO15_HALT;
        index = index2 + ISO15_MAX_BLOCKS;
        cmd[index2] = (byte) startBlock;
        byte[] result = this.mModule.transmitCard(this.channel, cmd, index);
        byte[] tmp = new byte[(result.length - 1)];
        System.arraycopy(result, ISO15_MAX_BLOCKS, tmp, 0, tmp.length);
        return tmp;
    }

    public int write(int startBlock, byte[] data) throws IOException, RFIDException {
//        byte[] tmp;
//        int i;
//        int blocks = data.length / 4;
//        byte[] cmd = new byte[ISO15_READ_BLOCK];
        int written = 0;
//        byte flags = (byte) 34;
//        int dataIndex = 0;
//        while (true) {
//            int toWrite = blocks - written;
//            if (toWrite > ISO15_MAX_BLOCKS) {
//                toWrite = ISO15_MAX_BLOCKS;
//            }
//            if (this.hyatt64Card) {
//                flags = (byte) (flags | ISO15_FLAG_OPTIONS);
//            }
//            int i2 = 0 + ISO15_MAX_BLOCKS;
//            cmd[0] = flags;
//            int index = i2 + ISO15_MAX_BLOCKS;
//            cmd[i2] = (byte) 33;
//            System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
//            index = this.uid.length + ISO15_HALT;
//            i2 = index + ISO15_MAX_BLOCKS;
//            cmd[index] = (byte) startBlock;
//            System.arraycopy(data, dataIndex, cmd, i2, this.blockSize * toWrite);
//            try {
//                this.mModule.transmitCard(this.channel, cmd, i2 + (this.blockSize * toWrite));
//                dataIndex += this.blockSize * toWrite;
//                written += toWrite;
//                startBlock += toWrite;
//                if (written >= blocks) {
//                    break;
//                }
//            } catch (RFIDException e) {
//                if (this.hyatt64Card) {
//                    break;
//                    tmp = read(startBlock, ISO15_MAX_BLOCKS);
//                    while (i < 4) {
//                        if (tmp[i] == data[dataIndex + i]) {
//                        } else {
//                            throw new RFIDException(-20);
//                        }
//                    }
//                }
//                this.hyatt64Card = true;
//            }
//        }
//        if (e.getErrorCode() == -6 && this.hyatt64Card) {
//            tmp = read(startBlock, ISO15_MAX_BLOCKS);
//            for (i = 0; i < 4; i += ISO15_MAX_BLOCKS) {
//                if (tmp[i] == data[dataIndex + i]) {
//                    throw new RFIDException(-20);
//                }
//            }
//        }
        return written;
    }

    private void select() throws IOException, RFIDException {
        byte[] cmd = new byte[ISO15_READ_BLOCK];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 37;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        this.mModule.transmitCard(this.channel, cmd, this.uid.length + ISO15_HALT);
    }

    private void resetToReady() throws IOException, RFIDException {
        byte[] cmd = new byte[ReasonFlags.unused];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 38;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        this.mModule.transmitCard(this.channel, cmd, this.uid.length + ISO15_HALT);
    }

    private void halt() throws IOException, RFIDException {
        byte[] cmd = new byte[ReasonFlags.unused];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 2;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        this.mModule.transmitCard(this.channel, cmd, this.uid.length + ISO15_HALT);
    }

    public void writeAFI(byte afi) throws IOException, RFIDException {
        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 39;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        index = this.uid.length + ISO15_HALT;
        i = index + ISO15_MAX_BLOCKS;
        cmd[index] = afi;
        this.mModule.transmitCard(this.channel, cmd, i);
        this.afi = afi;
    }

    public void lockAFI() throws IOException, RFIDException {
        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 40;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        this.mModule.transmitCard(this.channel, cmd, this.uid.length + ISO15_HALT);
    }

    public void writeDSFID(byte dsfid) throws IOException, RFIDException {
        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 41;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        index = this.uid.length + ISO15_HALT;
        i = index + ISO15_MAX_BLOCKS;
        cmd[index] = dsfid;
        this.mModule.transmitCard(this.channel, cmd, i);
        this.dsfid = dsfid;
    }

    public void lockDSFID() throws IOException, RFIDException {
        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 42;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        this.mModule.transmitCard(this.channel, cmd, this.uid.length + ISO15_HALT);
    }

    public byte[] getBlocksSecurityStatus(int startBlock, int blocks) throws IOException, RFIDException {
        byte[] cmd = new byte[ReasonFlags.unused];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 44;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        index = this.uid.length + ISO15_HALT;
        i = index + ISO15_MAX_BLOCKS;
        cmd[index] = (byte) startBlock;
        index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) (blocks - 1);
        byte[] result = this.mModule.transmitCard(this.channel, cmd, index);
        byte[] tmp = new byte[(result.length - 1)];
        System.arraycopy(result, ISO15_MAX_BLOCKS, tmp, 0, tmp.length);
        return tmp;
    }

    private void getSystemInformation() throws IOException, RFIDException {
        byte[] cmd = new byte[PKIFailureInfo.unacceptedPolicy];
        int i = 0 + ISO15_MAX_BLOCKS;
        cmd[0] = (byte) 34;
        int index = i + ISO15_MAX_BLOCKS;
        cmd[i] = (byte) 43;
        System.arraycopy(this.uid, 0, cmd, index, this.uid.length);
        byte[] result = this.mModule.transmitCard(this.channel, cmd, this.uid.length + ISO15_HALT);
        int sysflags = result[ISO15_MAX_BLOCKS] & Pinpad.EMV2_MESSAGE_UI_NA;
        index = (ISO15_MAX_BLOCKS + ISO15_MAX_BLOCKS) + 8;
        if ((sysflags & ISO15_MAX_BLOCKS) != 0) {
            i = index + ISO15_MAX_BLOCKS;
            this.dsfid = result[index];
        } else {
            this.dsfid = (byte) 0;
            i = index;
        }
        if ((sysflags & ISO15_HALT) != 0) {
            index = i + ISO15_MAX_BLOCKS;
            this.afi = result[i];
            i = index;
        } else {
            this.afi = (byte) 0;
        }
        if ((sysflags & 4) != 0) {
            index = i + ISO15_MAX_BLOCKS;
            this.maxBlocks = (result[i] & Pinpad.EMV2_MESSAGE_UI_NA) + ISO15_MAX_BLOCKS;
            i = index + ISO15_MAX_BLOCKS;
            this.blockSize = (result[index] & 31) + ISO15_MAX_BLOCKS;
            index = i;
            return;
        }
        this.blockSize = 4;
        this.maxBlocks = 0;
        index = i;
    }
}
