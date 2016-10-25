package com.datecs.audioreader.rfid;

import com.datecs.audioreader.AudioReader;
import com.datecs.pinpad.Pinpad;
import java.io.IOException;

public class RC663 extends RFID {
    public static final int CHANNEL_FELICA = 4;
    public static final int CHANNEL_ISO1443A = 1;
    public static final int CHANNEL_ISO1443B = 2;
    public static final int CHANNEL_ISO15693 = 3;
    public static final int CHANNEL_MAIN = 0;
    public static final int CHANNEL_STSRI = 5;
    private static final boolean DEBUG = false;
    private static final int EVENT_BIT = 128;
    private static final int PACKET_HEADER_SIZE = 4;
    private static final int RC663_COMMAND_CLOSE = 1;
    private static final int RC663_COMMAND_TRANSCEIVE = 2;

    public RC663(AudioReader reader) {
        super(reader);
    }

    private static final void log(String text) {
    }

    private static final void log(String text, byte[] b) {
        log(text, b, CHANNEL_MAIN, b.length);
    }

    private static final void log(String text, byte[] b, int offset, int length) {
    }

    private boolean isValidPacket(byte[] packet) {
        if (packet.length >= PACKET_HEADER_SIZE) {
            if (packet.length == (((packet[RC663_COMMAND_TRANSCEIVE] & Pinpad.EMV2_MESSAGE_UI_NA) << 8) | (packet[CHANNEL_ISO15693] & Pinpad.EMV2_MESSAGE_UI_NA)) + PACKET_HEADER_SIZE) {
                return true;
            }
        }
        return DEBUG;
    }

    private byte[] extractData(byte[] packet) {
        if (!isValidPacket(packet)) {
            return null;
        }
        byte[] tmp = new byte[(packet.length - 4)];
        System.arraycopy(packet, PACKET_HEADER_SIZE, tmp, CHANNEL_MAIN, tmp.length);
        return tmp;
    }

    private synchronized byte[] transmit(int channel, int command, byte[] data) throws IOException, RFIDException {
        byte[] output;
        byte[] input = new byte[(data.length + PACKET_HEADER_SIZE)];
        input[CHANNEL_MAIN] = (byte) channel;
        input[RC663_COMMAND_CLOSE] = (byte) command;
        input[RC663_COMMAND_TRANSCEIVE] = (byte) (data.length >> 8);
        input[CHANNEL_ISO15693] = (byte) data.length;
        System.arraycopy(data, CHANNEL_MAIN, input, PACKET_HEADER_SIZE, data.length);
        log("[RC663::transmit] send(" + input.length + "): ", input);
        output = transmit(input);
        log("[RC663::transmit] recv(" + output.length + "): ", output);
        int errorCode = -(output[RC663_COMMAND_CLOSE] & Pinpad.EMV2_MESSAGE_UI_NA);
        if (errorCode != 0) {
            log("[RC663::transmit] command failed with error " + errorCode);
            throw new RFIDException(errorCode);
        }
        return extractData(output);
    }

    private byte[] transmit(int channel, int command) throws IOException, RFIDException {
        return transmit(channel, command, new byte[CHANNEL_MAIN]);
    }

    public byte[] transmitCard(int channel, byte[] data) throws IOException, RFIDException {
        byte[] result = transmit(channel, RC663_COMMAND_TRANSCEIVE, data);
        if (channel != PACKET_HEADER_SIZE) {
            return result;
        }
        if (result.length < 9) {
            throw new RFIDException(-6);
        }
        byte[] tmp = new byte[(result.length - 1)];
        System.arraycopy(result, RC663_COMMAND_CLOSE, tmp, CHANNEL_MAIN, tmp.length);
        return tmp;
    }

    public byte[] transmitCard(int channel, byte[] data, int length) throws IOException, RFIDException {
        byte[] tmp = new byte[length];
        System.arraycopy(data, CHANNEL_MAIN, tmp, CHANNEL_MAIN, length);
        return transmitCard(channel, tmp);
    }

    public ContactlessCard initCard(byte[] packet) throws IOException {
        if (!isValidPacket(packet) || (packet[CHANNEL_MAIN] & EVENT_BIT) == 0) {
            return null;
        }
        return initCard(packet[RC663_COMMAND_CLOSE] & Pinpad.EMV2_MESSAGE_UI_NA, extractData(packet));
    }

    private ContactlessCard initCard(int channel, byte[] data) throws IOException {
        boolean cardDetected;
        ContactlessCard card = null;
        log("[RC663::initCard] channel=" + channel + ", data=", data);
        int uidLen;
        if (channel == RC663_COMMAND_CLOSE) {
            card = new ISO14443Card(this);
            card.channel = channel;
            card.atqa = (short) (((data[CHANNEL_MAIN] & Pinpad.EMV2_MESSAGE_UI_NA) << 8) | (data[RC663_COMMAND_CLOSE] & Pinpad.EMV2_MESSAGE_UI_NA));
            int index = CHANNEL_MAIN + RC663_COMMAND_TRANSCEIVE;
            card.sak = (short) (data[index] & Pinpad.EMV2_MESSAGE_UI_NA);
            index += RC663_COMMAND_CLOSE;
            uidLen = data.length - 3;
            card.uid = new byte[uidLen];
            System.arraycopy(data, index, card.uid, CHANNEL_MAIN, uidLen);
            cardDetected = card.initialize();
        } else if (channel == RC663_COMMAND_TRANSCEIVE) {
            cardDetected = DEBUG;
        } else if (channel == CHANNEL_ISO15693) {
            card = new ISO15693Card(this);
            card.channel = channel;
            card.type = 8;
            card.dsfid = data[RC663_COMMAND_CLOSE];
            uidLen = data.length - 2;
            card.uid = new byte[uidLen];
            System.arraycopy(data, RC663_COMMAND_TRANSCEIVE, card.uid, CHANNEL_MAIN, uidLen);
            cardDetected = card.initialize();
        } else if (channel == PACKET_HEADER_SIZE) {
            card = new FeliCaCard(this);
            card.channel = channel;
            card.type = 11;
            card.uid = new byte[8];
            System.arraycopy(data, RC663_COMMAND_TRANSCEIVE, card.uid, CHANNEL_MAIN, 8);
            cardDetected = card.initialize();
        } else if (channel == CHANNEL_STSRI) {
            card = new STSRICard(this);
            card.channel = channel;
            card.type = 12;
            card.uid = new byte[8];
            System.arraycopy(data, CHANNEL_MAIN, card.uid, CHANNEL_MAIN, 8);
            cardDetected = card.initialize();
        } else {
            cardDetected = DEBUG;
        }
        return cardDetected ? card : null;
    }

    void close() throws IOException, RFIDException {
        transmit(CHANNEL_MAIN, RC663_COMMAND_CLOSE);
    }
}
