package com.datecs.audioreader.rfid;

import java.io.IOException;

public class STSRICard extends ContactlessCard {
    public STSRICard(RC663 module) {
        super(module);
    }

    protected boolean deinitialize() throws IOException {
        while (true) {
            try {
                getUID();
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
        this.blockSize = 4;
        this.maxBlocks = 0;
        return true;
    }

    public void completion() throws IOException, RFIDException {
        byte[] cmd = new byte[32];
        int index = 0 + 1;
        cmd[0] = (byte) 15;
        this.mModule.transmitCard(this.channel, cmd, index);
    }

    public void reset() throws IOException, RFIDException {
        byte[] cmd = new byte[32];
        int index = 0 + 1;
        cmd[0] = (byte) 12;
        this.mModule.transmitCard(this.channel, cmd, index);
    }

    public byte[] readBlock(int address) throws IOException, RFIDException {
        byte[] cmd = new byte[32];
        int i = 0 + 1;
        cmd[0] = (byte) 8;
        int index = i + 1;
        cmd[i] = (byte) address;
        byte[] result = this.mModule.transmitCard(this.channel, cmd, index);
        if (result.length == 4) {
            return result;
        }
        throw new RFIDException(-6);
    }

    public void writeBlock(int address, byte[] data) throws IOException, RFIDException {
        byte[] cmd = new byte[32];
        int i = 0 + 1;
        cmd[0] = (byte) 9;
        int index = i + 1;
        cmd[i] = (byte) address;
        System.arraycopy(data, 0, cmd, index, 4);
        this.mModule.transmitCard(this.channel, cmd, index + 4);
    }

    public byte[] getUID() throws IOException, RFIDException {
        byte[] cmd = new byte[32];
        int index = 0 + 1;
        cmd[0] = (byte) 11;
        byte[] result = this.mModule.transmitCard(this.channel, cmd, index);
        if (result.length == 8) {
            return result;
        }
        throw new RFIDException(-6);
    }
}
