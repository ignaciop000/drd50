package com.datecs.audioreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

import com.datecs.audioreader.AudioReaderRecorder.RecorderListener;
import com.datecs.audioreader.rfid.ContactlessCard;
import com.datecs.audioreader.rfid.RC663;
import com.datecs.model.CardInfo;
import com.datecs.model.FinancialCard;
import com.estel.AREMV.IAudioMSR;
//import com.datecs.pinpad.FinancialCard;
//import com.datecs.audioreader.rfid.ContactlessCard;
//import com.datecs.audioreader.rfid.RC663;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.commons.httpclient.HttpStatus;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.x509.DisplayText;

public class AudioReader {
    private static final int BAUD_RATE = 2004;
    public static final int CARD_TYPE_MAGNETIC = 0;
    public static final int CARD_TYPE_RFID = 2;
    public static final int CARD_TYPE_SMART_CARD = 1;
    private static final int CKM_ENTER_READER_ID = 8;
    private static final int CKM_GET_IPEK_VERSION = 1;
    private static final int CKM_GET_KEK_VERSION = 2;
    private static final int CKM_GET_KSN = 5;
    private static final int CKM_LOAD_3DES_KEK = 4;
    private static final int CKM_LOAD_DUKPT_3DES_IPEK = 3;
    private static final int CKM_SET_INITIAL_VECTOR = 6;
    private static final int CKM_SET_READER_ID = 7;
    private static final int COMMAND_KEY_MANAGEMENT = 3;
    private static final int COMMAND_RFID = 4;
    private static final int COMMAND_SMART_CARD = 2;
    private static final int COMMAND_SYSTEM = 1;
    private static final int CRFID_TRANSMIT = 1;
    private static final int CSC_CARD_PACKET = 6;
    private static final int CSC_CARD_PRESENT = 5;
    private static final int CSC_ENABLE = 1;
    private static final int CSC_MASK = 8;
    private static final int CSC_MSR = 9;
    private static final int CSC_POWER_OFF = 4;
    private static final int CSC_POWER_ON = 3;
    private static final int CSC_START_CARD_READING = 16;
    private static final int CSC_TRACKS = 7;
    private static final int CSC_TRANSMIT_ENCRYPTED_MESSAGE = 32;
    private static final int CSC_TRANSMIT_MESSAGE = 33;
    private static final int CSYS_GET_BATTERY = 4;
    private static final int CSYS_GET_IDENT = 1;
    private static final int CSYS_GET_KEY_VERSION = 11;
    private static final int CSYS_GET_RSA_BLOCK = 10;
    private static final int CSYS_GET_SERIAL = 9;
    private static final int CSYS_RANDOM = 250;
    private static final int CSYS_READ_DATA = 17;
    private static final int CSYS_SCR_RANDOM = 254;
    private static final int CSYS_SYSTEM_POWER_OFF = 3;
    private static final int CSYS_TEST = 255;
    private static final int CSYS_UPDATE_ENTER = 5;
    private static final int CSYS_UPDATE_FINISH = 7;
    private static final int CSYS_UPDATE_WRITE = 6;
    private static final int CSYS_WRITE_DATA = 16;
    private static boolean D = false;
    private static final int DEFAULT_READ_TIMEOUT = 1500;
    private static final int DEFAULT_RETRIES = 5;
    public static final int ENCRYPTION_TYPE_AES256 = 2;
    public static final int ENCRYPTION_TYPE_IDTECH = 3;
    public static final int ENCRYPTION_TYPE_OLD_RSA = 0;
    public static final int ENCRYPTION_TYPE_RSA = 5;
    public static final int KEY_AES_AUTHENTICATION = 0;
    public static final int KEY_AES_DATA_ENCRYPTION = 1;
    public static final int KEY_AES_KEK = 2;
    public static final int KEY_DUKPT_MASTER = 32;
    public static final int KEY_NO_KEY = 255;
    public static final int KEY_TMK_AES = 16;
    public static final int KEY_TYPE_3DES_DATA = 4;
    public static final int KEY_TYPE_3DES_KEK = 132;
    public static final int KEY_TYPE_AES128_DATA = 1;
    public static final int KEY_TYPE_AES128_KEK = 129;
    public static final int KEY_TYPE_AES256_DATA = 2;
    public static final int KEY_TYPE_AES256_KEK = 130;
    public static final int KEY_TYPE_DES_DATA = 3;
    public static final int KEY_TYPE_DES_KEK = 131;
    public static final int KEY_TYPE_DUKPT_3DES_DATA = 5;
    public static final int KEY_TYPE_RSA2048_DATA = 6;
    private static final String LOG_TAG = "AudioReader";
    private static final int SOH = 1;
    private static final int START_PACKET = 172;
    public static final int STATUS_CARD_INTERNAL_ERROR = 8;
    public static final int STATUS_CARD_NOT_ENABLED = 5;
    public static final int STATUS_CARD_NOT_POWERED = 6;
    public static final int STATUS_CARD_NOT_PRESENT = 7;
    public static final int STATUS_EMSR_BAD_ARRAY = 1016;
    public static final int STATUS_EMSR_BARCODE_MISHMASH = 1018;
    public static final int STATUS_EMSR_BARCODE_NO_READER = 1019;
    public static final int STATUS_EMSR_CARD_ERROR = 1003;
    public static final int STATUS_EMSR_HARD = 1023;
    public static final int STATUS_EMSR_INVALID_COMMAND = 1001;
    public static final int STATUS_EMSR_INVALID_LENGTH = 1020;
    public static final int STATUS_EMSR_INVALID_SIGNATURE = 1022;
    public static final int STATUS_EMSR_NO_BARCODE_READ = 1017;
    public static final int STATUS_EMSR_NO_DATA = 1006;
    public static final int STATUS_EMSR_NO_PERMITION = 1002;
    public static final int STATUS_EMSR_NO_RESPONSE = 1005;
    private static final int STATUS_EMSR_OFFSET = 1000;
    public static final int STATUS_EMSR_SYNTAX_ERROR = 1004;
    public static final int STATUS_EMSR_TAMPERED = 1021;
    public static final int STATUS_ENCRYPTION_ERROR = 14;
    public static final int STATUS_GENERAL_ERROR = 1;
    public static final int STATUS_INVALID_ADDRESS = 13;
    public static final int STATUS_INVALID_COMMAND_DATA = 4;
    public static final int STATUS_INVALID_COMMAND_FORMAT = 12;
    public static final int STATUS_INVALID_COMMAND_LENGTH = 10;
    public static final int STATUS_INVALID_COMMAND_NUMBER = 3;
    public static final int STATUS_INVALID_COMMAND_SEQUENCE = 9;
    public static final int STATUS_INVALID_COMMAND_TYPE = 2;
    public static final int STATUS_INVALID_KEY = 19;
    public static final int STATUS_INVALID_NAME = 11;
    public static final int STATUS_INVALID_VERSION = 18;
    public static final int STATUS_LOW_BATTERY = 16;
    public static final int STATUS_OK = 0;
    public static final int STATUS_SIGNATURE_ERROR = 15;
    public static final int STATUS_TAMPER = 17;
    private static final int SYN = 17;
    private static final int TOGGLE = 128;
    public static final int TRACK_READ_MODE_ALLOWED = 1;
    public static final int TRACK_READ_MODE_NOT_ALLOWED = 0;
    public static final int TRACK_READ_MODE_REQUIRED = 2;
    private byte[] mCachedCardData;
    private FinancialCard mFinancialCard;
    private KeepAliveThread mKeepAlive;
    private long mLastRecvTime;
    private int mLastStatus;
    private List<byte[]> mPackets;
    private AudioReaderPlayer mPlayer;
    private SharedPreferences mPrefs;
    private AudioReaderRecorder mRecorder;
    private RecorderListener mRecorderListener = null;
    private int mRetries;
    private LocalSettings mSettings = null;
    private int mToggle;
    private String externalPath;
    private String poweringToneFile;
    private MediaPlayer mediaPlayer;

    public static class Battery {
        public static final int STATUS_CHARGE_DONE = 2;
        public static final int STATUS_CHARGING = 0;
        public static final int STATUS_NOT_CHARGING = 1;
        public final int level;
        public final int status;
        public final int voltage;

        Battery(int voltage, int level, int status) {
            this.voltage = voltage;
            this.level = level;
            this.status = status;
        }

        @Override
        public String toString() {
            return "Battery{" +
                    "level=" + level +
                    ", status=" + status +
                    ", voltage=" + voltage +
                    '}';
        }
    }

    public static class CardStatusResponse {
        public static final int CARD_AVAILABLE = 256;
        public static final int CARD_POWERED = 512;
        public static final int UNKNOWN_PROTOCOL = 16;
        public final int result;
        public final int status;

        private CardStatusResponse(int result, int status) {
            this.result = result;
            this.status = status;
        }

        static CardStatusResponse parse(byte[] input) {
            return new CardStatusResponse(AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_NOT_ALLOWED, AudioReader.TRACK_READ_MODE_REQUIRED), AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_REQUIRED, AudioReader.TRACK_READ_MODE_REQUIRED));
        }

        public boolean isCardAvailable() {
            return (this.status & CARD_AVAILABLE) != 0;
        }

        public boolean isCardPowered() {
            return (this.status & CARD_POWERED) != 0;
        }

        public boolean isUnknownProtocol() {
            return (this.status & UNKNOWN_PROTOCOL) != 0;
        }

        public int getProtocol() {
            return this.status & AudioReader.STATUS_SIGNATURE_ERROR;
        }

        public String toString() {
            Object[] objArr = new Object[AudioReader.TRACK_READ_MODE_REQUIRED];
            objArr[AudioReader.TRACK_READ_MODE_NOT_ALLOWED] = Integer.valueOf(this.result);
            objArr[AudioReader.TRACK_READ_MODE_ALLOWED] = Integer.valueOf(this.status);
            return String.format("CardStatus [result=0x%X, status=0x%X]", objArr);
        }
    }

    public static class DeviceKey {
        public final int index;
        public final String name;
        public final int type;
        public final int version;

        DeviceKey(int keyType, int keyIndex, int keyVersion) {
            this.type = keyType;
            this.index = keyIndex;
            this.version = keyVersion;
            this.name = getKeyName(keyType);
        }

        private static String getKeyName(int keyType) {
            switch (keyType) {
                case AudioReader.TRACK_READ_MODE_ALLOWED /*1*/:
                    return "AES128 data encryption key";
                case AudioReader.TRACK_READ_MODE_REQUIRED /*2*/:
                    return "AES256 data encryption key";
                case AudioReader.STATUS_INVALID_COMMAND_NUMBER /*3*/:
                    return "DES data encryption key";
                case AudioReader.STATUS_INVALID_COMMAND_DATA /*4*/:
                    return "3DES data encryption key";
                case AudioReader.STATUS_CARD_NOT_ENABLED /*5*/:
                    return "DUKPT/3DES data encryption key";
                case AudioReader.STATUS_CARD_NOT_POWERED /*6*/:
                    return "RSA2048 data encryption key";
                case AudioReader.KEY_TYPE_AES128_KEK /*129*/:
                    return "AES128 key encryption key";
                case AudioReader.KEY_TYPE_AES256_KEK /*130*/:
                    return "AES256 key encryption key";
                case AudioReader.KEY_TYPE_DES_KEK /*131*/:
                    return "DES key encryption key";
                case AudioReader.KEY_TYPE_3DES_KEK /*132*/:
                    return "3DES key encryption key";
                default:
                    return "Unknown";
            }
        }
    }

    public static class DeviceKeysInfo {
        Map<Integer, DeviceKey> mKeys;
        private boolean mTampered;

        DeviceKeysInfo(byte[] input) {
            this.mKeys = new HashMap();
            if (input.length >= AudioReader.STATUS_CARD_NOT_POWERED) {
                for (int i = AudioReader.TRACK_READ_MODE_NOT_ALLOWED; i < input.length; i += AudioReader.STATUS_CARD_NOT_POWERED) {
                    int keyType = input[i + AudioReader.TRACK_READ_MODE_NOT_ALLOWED] & AudioReader.KEY_NO_KEY;
                    int keyIndex = input[i + AudioReader.TRACK_READ_MODE_ALLOWED] & AudioReader.KEY_NO_KEY;
                    int keyId = (keyIndex << AudioReader.STATUS_LOW_BATTERY) | keyType;
                    this.mKeys.put(Integer.valueOf(keyId), new DeviceKey(keyType, keyIndex, ((((input[i + AudioReader.TRACK_READ_MODE_REQUIRED] & AudioReader.KEY_NO_KEY) << 24) + ((input[i + AudioReader.STATUS_INVALID_COMMAND_NUMBER] & AudioReader.KEY_NO_KEY) << AudioReader.STATUS_LOW_BATTERY)) + ((input[i + AudioReader.STATUS_INVALID_COMMAND_DATA] & AudioReader.KEY_NO_KEY) << AudioReader.STATUS_CARD_INTERNAL_ERROR)) + ((input[i + AudioReader.STATUS_CARD_NOT_ENABLED] & AudioReader.KEY_NO_KEY) << AudioReader.TRACK_READ_MODE_NOT_ALLOWED)));
                }
            }
            this.mTampered = false;
        }

        public boolean isTampered() {
            return this.mTampered;
        }

        public DeviceKey[] getKeys() {
            DeviceKey[] keys = new DeviceKey[this.mKeys.size()];
            int idx = AudioReader.TRACK_READ_MODE_NOT_ALLOWED;
            for (DeviceKey key : this.mKeys.values()) {
                int idx2 = idx + AudioReader.TRACK_READ_MODE_ALLOWED;
                keys[idx] = key;
                idx = idx2;
            }
            return keys;
        }

        public DeviceKey getDeviceKey(int keyType, int keyIndex) {
            return (DeviceKey) this.mKeys.get(Integer.valueOf((keyIndex << AudioReader.STATUS_LOW_BATTERY) | keyType));
        }
    }

    public static class EMSRInformation {
        public final String name;
        public final String version;

        EMSRInformation(String name, String version) {
            this.name = name;
            this.version = version;
        }
    }

    public static class EMSRKeyInformation {
        public final boolean tampered;
        public final int version;

        EMSRKeyInformation(boolean tampered, int version) {
            this.tampered = tampered;
            this.version = version;
        }
    }

    //    public static class EMVChecksumResponse {
//        public final byte[] checksum;
//        public final int result;
//
//        private EMVChecksumResponse(int result, byte[] checksum) {
//            this.result = result;
//            this.checksum = checksum;
//        }
//
//        static EMVChecksumResponse parse(byte[] input) {
//            return new EMVChecksumResponse(AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_NOT_ALLOWED, AudioReader.TRACK_READ_MODE_REQUIRED), AudioReader.byteArrayToSubArray(input, AudioReader.TRACK_READ_MODE_REQUIRED, input.length - 2));
//        }
//
//        public String toString() {
//            return "EMVChecksumResponse [result=" + this.result + ", checksum(" + this.checksum.length + ")=" + AudioReader.byteArrayToHexString(this.checksum) + "]";
//        }
//    }
//
//
    public static class FirmwareInformation {
        public static final int ENCRYPTED = 1;
        public static final int NOENCRYPTED = 0;
        public final int encryption;
        public final byte[] firmware;
        public final byte[] header;
        public final String name;
        public final byte[] signature;
        public final String version;

        public FirmwareInformation(byte[] header, int encryption, String version, String name, byte[] signature, byte[] firmware) {
            this.header = header;
            this.encryption = encryption;
            this.version = version;
            this.name = name;
            this.signature = signature;
            this.firmware = firmware;
        }

        @Override
        public String toString() {
            return "FirmwareInformation{" +
                    "encryption=" + encryption +
                    ", firmware=" + Arrays.toString(firmware) +
                    ", header=" + Arrays.toString(header) +
                    ", name='" + name + '\'' +
                    ", signature=" + Arrays.toString(signature) +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
//
//    public static class Identification {
//        public final String name;
//        public final String version;
//
//        public Identification(String name, String version) {
//            this.name = name;
//            this.version = version;
//        }
//    }
//
    private final class KeepAliveThread extends Thread {
        private volatile boolean mActive;
        private volatile long mStartTime;

        public KeepAliveThread() {
            reset();
        }

        public void run() {
            byte[] keepAliveData = new byte[2];
            this.mActive = true;
            while (this.mActive) {
                try {
                    synchronized (this) {
                        wait(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.mActive && System.currentTimeMillis() - this.mStartTime > 1000) {
                    try {
                        AudioReader.this.sendData(keepAliveData);
                    } catch (IOException e2) {
                        return;
                    }
                }
            }
        }

        public void reset() {
            this.mStartTime = System.currentTimeMillis();
        }

        public void finish() {
            if (isAlive()) {
                this.mActive = false;
                synchronized (this) {
                    notify();
                }
            }
        }
    }

    private static final class LocalSettings {
        int msEncryption;
        int msTrack1Mode;
        int msTrack2Mode;
        int msTrack3Mode;
        int rfSupportedCards;
        boolean showExpiration;
        int unmaskedDigitsAtEnd;
        int unmaskedDigitsAtStart;

        private LocalSettings() {
            this.msEncryption = AudioReader.TRACK_READ_MODE_REQUIRED;
            this.msTrack1Mode = AudioReader.TRACK_READ_MODE_ALLOWED;
            this.msTrack2Mode = AudioReader.TRACK_READ_MODE_ALLOWED;
            this.msTrack3Mode = AudioReader.TRACK_READ_MODE_ALLOWED;
            this.showExpiration = false;
            this.unmaskedDigitsAtStart = AudioReader.STATUS_INVALID_COMMAND_DATA;
            this.unmaskedDigitsAtEnd = AudioReader.STATUS_INVALID_COMMAND_DATA;
            this.rfSupportedCards = -1;
        }
    }
//
//    public interface MessageProcessingResults {
//        public static final int ABORTED = 13;
//        public static final int CANCELLED = 1;
//        public static final int CARD_BLOCKED = 2;
//        public static final int CARD_MISSING = 3;
//        public static final int CHIP_ERROR = 4;
//        public static final int CONFIGURATION_ERROR = 15;
//        public static final int DATA_ERROR = 5;
//        public static final int EMPTY_LIST = 6;
//        public static final int EMV_LIB = 32769;
//        public static final int FALLBACK_PROHIBITED = 14;
//        public static final int FLOW_CONTROL = 32770;
//        public static final int GPO6985 = 7;
//        public static final int INPUT_DATA_ERROR = 32774;
//        public static final int INTERNAL_ERROR = 32771;
//        public static final int MISSING_DATA = 8;
//        public static final int NOT_ACCEPTED = 11;
//        public static final int NO_CARD_INSERTED = 9;
//        public static final int NO_PROFILE = 10;
//        public static final int OK = 0;
//        public static final int OUT_OF_MEMORY = 32775;
//        public static final int RESELECT = 32772;
//        public static final int SECURITY = 32773;
//        public static final int TIMEOUT = 12;
//    }
//
//    public static class MessageResponse {
//        public final int cid;
//        public final byte[] data;
//        public final int reserved;
//
//        MessageResponse(byte[] input) {
//            this.cid = AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_NOT_ALLOWED, AudioReader.TRACK_READ_MODE_ALLOWED);
//            this.reserved = AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_ALLOWED, AudioReader.TRACK_READ_MODE_ALLOWED);
//            this.data = AudioReader.byteArrayToSubArray(input, AudioReader.STATUS_INVALID_COMMAND_DATA, AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_REQUIRED, AudioReader.TRACK_READ_MODE_REQUIRED));
//        }
//
//        public String toString() {
//            return "MessageResponse [cid=" + this.cid + ", data(" + this.data.length + ")=" + AudioReader.byteArrayToHexString(this.data) + "]";
//        }
//    }
//
//    public interface OnUpdateFirmwareListener {
//        boolean onDataSend(int i, int i2);
//    }
//
//    public static class Transaction {
//        public final byte[] rsaBlock;
//        public final int rsaKeyVersion;
//
//        Transaction(int rsaKeyVersion, byte[] rsaBlock) {
//            this.rsaKeyVersion = rsaKeyVersion;
//            this.rsaBlock = rsaBlock;
//        }
//    }
//
//
//    public static class CardResetResponse extends CardStatusResponse {
//        public final byte[] atr;
//
//        private CardResetResponse(int result, int status, byte[] atr) {
//            super(status, null);
//            this.atr = atr;
//        }
//
//        public static CardResetResponse parse(byte[] input) {
//            return new CardResetResponse(AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_NOT_ALLOWED, AudioReader.TRACK_READ_MODE_REQUIRED), AudioReader.byteArrayToInt(input, AudioReader.TRACK_READ_MODE_REQUIRED, AudioReader.TRACK_READ_MODE_REQUIRED), AudioReader.byteArrayToSubArray(input, AudioReader.STATUS_INVALID_COMMAND_DATA, input.length - 4));
//        }
//
//        public String toString() {
//            Object[] objArr = new Object[AudioReader.STATUS_INVALID_COMMAND_NUMBER];
//            objArr[AudioReader.TRACK_READ_MODE_NOT_ALLOWED] = Integer.valueOf(this.result);
//            objArr[AudioReader.TRACK_READ_MODE_ALLOWED] = Integer.valueOf(this.status);
//            objArr[AudioReader.TRACK_READ_MODE_REQUIRED] = AudioReader.byteArrayToHexString(this.atr);
//            return String.format("ResetResponse [result=0x%X, status=0x%X, atr=%s]", objArr);
//        }
//    }

    public AudioReader(Context context) {
        this.mSettings = new LocalSettings();
        this.mRecorderListener = new RecorderListener() {
            public void OnPacketReceived(int[] data, int length) {
                byte[] buffer = new byte[length];
                for (int i = AudioReader.TRACK_READ_MODE_NOT_ALLOWED; i < buffer.length; i += AudioReader.TRACK_READ_MODE_ALLOWED) {
                    buffer[i] = (byte) data[i];
                }
                System.out.println("Recv(" + buffer.length + "): " + Arrays.toString(buffer));
                if (length >= AudioReader.TRACK_READ_MODE_REQUIRED) {
                    AudioReader.this.mPackets.add(buffer);
                    synchronized (AudioReader.this.mPackets) {
                        AudioReader.this.mPackets.notifyAll();
                    }
                }
            }

            public void OnReceiving(int samples) {
                System.out.println("Receiving(" + samples + ")...");
                AudioReader.this.mLastRecvTime = System.currentTimeMillis();
            }
        };
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPackets = Collections.synchronizedList(new ArrayList());
        this.mToggle = TOGGLE;
        this.mPlayer = new AudioReaderPlayer(context, BAUD_RATE, STATUS_INVALID_COMMAND_NUMBER);
        this.mPlayer.start();
        this.mRecorder = new AudioReaderRecorder(this.mRecorderListener);
        this.mRecorder.setEndLength(TRACK_READ_MODE_ALLOWED);
        this.mRecorder.start();
        this.externalPath = getExternalStoragePath();
    }

    public static String getExternalStoragePath() {
        String path = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            path = Environment.getExternalStorageDirectory().toString();
        }
        return path;
    }


    public static void setDebug(boolean on) {
        D = on;
    }

    protected static void logD(String text) {
        if (D) {
            Log.d(LOG_TAG, text);
        }
    }

    protected static void logD(String text, byte[] data) {
        if (D) {
            StringBuilder sb = new StringBuilder(text);
            int length = data.length;
            for (int i = TRACK_READ_MODE_NOT_ALLOWED; i < length; i += TRACK_READ_MODE_ALLOWED) {
                Object[] objArr = new Object[TRACK_READ_MODE_ALLOWED];
                objArr[TRACK_READ_MODE_NOT_ALLOWED] = Integer.valueOf(data[i] & KEY_NO_KEY);
                sb.append(String.format("%02X ", objArr));
            }
            logD(sb.toString());
        }
    }

    protected static void logW(String text) {
        if (D) {
            Log.w(LOG_TAG, text);
        }
    }

    protected static void logI(String text) {
        if (D) {
            Log.i(LOG_TAG, text);
        }
    }

    private static int crcccit(int paramInt1, int paramInt2) {
//        crc = (((crc >> 8) & KEY_NO_KEY) | ((crc << 8) & 65535)) ^ (b & KEY_NO_KEY);
//        crc ^= (crc & KEY_NO_KEY) >> 4;
//        crc ^= ((crc << 8) << 4) & 65535;
//        return (crc ^ (((crc & KEY_NO_KEY) << 4) << 1)) & 65535;
        paramInt1 = (paramInt1 >> 8 & 0xFF | paramInt1 << 8 & 0xFFFF) ^ paramInt2 & 0xFF;
        paramInt1 ^= (paramInt1 & 0xFF) >> 4;
        paramInt1 ^= paramInt1 << 8 << 4 & 0xFFFF;
        return (paramInt1 ^ (paramInt1 & 0xFF) << 4 << 1) & 0xFFFF;
    }

    public static int crcccit(byte[] buf, int offset, int length) {
        int crc = 0;
        for (int i = 0; i < length; i++) {
            crc = crcccit(crc, buf[offset + i] & KEY_NO_KEY);
        }
        return crc;
    }

    private static int byteArrayToInt(byte[] buf, int offset, int length) {
        int result = TRACK_READ_MODE_NOT_ALLOWED;
        int length2 = length;
        int offset2 = offset;
        while (true) {
            length = length2 - 1;
            if (length2 <= 0) {
                return result;
            }
            result = (result << STATUS_CARD_INTERNAL_ERROR) + (buf[offset2] & KEY_NO_KEY);
            length2 = length;
            offset2 += TRACK_READ_MODE_ALLOWED;
        }
    }

    private static byte[] byteArrayToSubArray(byte[] buf, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(buf, offset, result, TRACK_READ_MODE_NOT_ALLOWED, length);
        return result;
    }

    private static final String byteArrayToHexString(byte[] data, int offset, int length) {
        char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] buf = new char[(length * TRACK_READ_MODE_REQUIRED)];
        int offs = TRACK_READ_MODE_NOT_ALLOWED;
        for (int i = TRACK_READ_MODE_NOT_ALLOWED; i < length; i += TRACK_READ_MODE_ALLOWED) {
            int i2 = offs + TRACK_READ_MODE_ALLOWED;
            buf[offs] = hex[(data[offset + i] >> STATUS_INVALID_COMMAND_DATA) & STATUS_SIGNATURE_ERROR];
            offs = i2 + TRACK_READ_MODE_ALLOWED;
            buf[i2] = hex[(data[offset + i] >> TRACK_READ_MODE_NOT_ALLOWED) & STATUS_SIGNATURE_ERROR];
        }
        return new String(buf, TRACK_READ_MODE_NOT_ALLOWED, offs);
    }

    private static final String byteArrayToHexString(byte[] data) {
        return byteArrayToHexString(data, TRACK_READ_MODE_NOT_ALLOWED, data.length);
    }

    public static final byte[] hexStringToByteArray(char[] src, int off, int len) {
        if ((len & TRACK_READ_MODE_ALLOWED) != 0) {
            throw new IllegalArgumentException("The argument 'len' can not be odd value");
        }
        byte[] buffer = new byte[(len / TRACK_READ_MODE_REQUIRED)];
        for (int i = TRACK_READ_MODE_NOT_ALLOWED; i < len; i += TRACK_READ_MODE_ALLOWED) {
            int nib = src[off + i];
            if (48 <= nib && nib <= 57) {
                nib -= 48;
            } else if (65 <= nib && nib <= 70) {
                nib = (nib - 65) + STATUS_INVALID_COMMAND_LENGTH;
            } else if (97 > nib || nib > 102) {
                throw new IllegalArgumentException("The argument 'src' can contains only HEX characters");
            } else {
                nib = (nib - 97) + STATUS_INVALID_COMMAND_LENGTH;
            }
            if ((i & TRACK_READ_MODE_ALLOWED) != 0) {
                int i2 = i / TRACK_READ_MODE_REQUIRED;
                buffer[i2] = (byte) (buffer[i2] + nib);
            } else {
                buffer[i / TRACK_READ_MODE_REQUIRED] = (byte) (nib << STATUS_INVALID_COMMAND_DATA);
            }
        }
        return buffer;
    }

    public static final byte[] hexStringToByteArray(char[] src) {
        return hexStringToByteArray(src, TRACK_READ_MODE_NOT_ALLOWED, src.length);
    }

    public static final byte[] hexStringToByteArray(String s) {
        return hexStringToByteArray(s.toCharArray());
    }

    private synchronized void enableKeepAlive(boolean enable) {
        if (this.mKeepAlive != null) {
            this.mKeepAlive.finish();
        }
        if (enable) {
            this.mKeepAlive = new KeepAliveThread();
            this.mKeepAlive.start();
        }
    }


    private byte[] createPacket(int cmd, int subCmd, byte[] data) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(START_PACKET);
        o.write(TRACK_READ_MODE_ALLOWED);
        o.write(this.mToggle | cmd);
        o.write(subCmd);
        if (data != null) {
            o.write(data.length >> STATUS_CARD_INTERNAL_ERROR);
            o.write(data.length);
            o.write(data);
        } else {
            o.write(TRACK_READ_MODE_NOT_ALLOWED);
            o.write(TRACK_READ_MODE_NOT_ALLOWED);
        }
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        byte[] result = o.toByteArray();
        int crc = crcccit(result, TRACK_READ_MODE_NOT_ALLOWED, result.length - 3);
        result[result.length - 3] = (byte) (crc >> STATUS_CARD_INTERNAL_ERROR);
        result[result.length - 2] = (byte) crc;
        return result;
    }

    private void clearPacketBuffer() {
        this.mPackets.clear();
    }

    private byte[] waitForData(int timeout) throws IOException {
        this.mLastRecvTime = System.currentTimeMillis();
        while (this.mPackets.isEmpty()) {
            if (!this.mRecorder.isActive()) {
                throw new IOException("Recorder is not active");
            } else if (this.mLastRecvTime + ((long) timeout) < System.currentTimeMillis()) {
                throw new IOException("Timeout");
            } else {
                synchronized (this.mPackets) {
                    try {
                        this.mPackets.wait(10);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return (byte[]) this.mPackets.remove(TRACK_READ_MODE_NOT_ALLOWED);
    }

    private void waitForSYN() throws IOException {
        while (true) {
            byte[] data = waitForData(2000);
            if (data.length >= 2 && data[0] == -84 && data[1] == 17) {
                return;
            }
        }
    }

    private byte[] decodePacket(byte[] data) throws IOException {
        System.out.println("Decode Packet: ("+data.length+"):" + Arrays.toString(data));
        if (data.length < 8) {
            throw new IOException("Insufficient packet content");
        }
        if (data[0] != -84) {
            throw new IOException("Inconsistent packet content");
        }
        if (data[1] != 1) {
            throw new IOException("Wrong packet content");
        }
        int status = data[3];
        int offset = 1;
        int offset2 = offset + 1;
        offset2 += 1;
        offset = offset2 + 1;
        offset2 = offset + 1;
        offset = offset2 + 1;
        int length = (data[4] << 8) | data[5];
//        int length = ((data[6] & KEY_NO_KEY) << 8) + (data[5] & KEY_NO_KEY);
        if (data.length < length + 8) {
            throw new IOException("Insufficient packet content");
        }
        offset2 = length + 6;
        offset = offset2 + 1;
        offset2 = offset + 1;
        int chksum = (((data[length + 6] & KEY_NO_KEY) << 8) | (data[length + 6 + 1] & KEY_NO_KEY)) - crcccit(data, 0, length + 6);
        System.out.println("Packet checksum result: " + chksum);
        if ((65532 & chksum) != 0) {
            throw new IOException("Wrong packet CRC");
        }
        byte[] result = new byte[length];
        System.arraycopy(data, 6, result, 0, length);
        this.mLastStatus = status;
        return result;
    }

    private byte[] waitForPacket() throws IOException {
        while (true) {
            byte[] data = waitForData(DEFAULT_READ_TIMEOUT);
            if (data.length >= 2 && data[0] == -84 && data[1] == (byte) 1) {
                return decodePacket(data);
            }
        }
    }

    private void sendData(byte[] data) throws IOException {
        System.out.println("Send(" + data.length + "): " + Arrays.toString(data));
        synchronized (this.mPlayer) {
            this.mPlayer.playBytes(data);
            this.mPlayer.flush();
            this.mKeepAlive.reset();
        }
    }

    private synchronized byte[] transmit(int cmd, int subCmd, byte[] data, int maxRetries) throws AudioReaderException, IOException {
        byte[] answer;
        byte[] packet = createPacket(cmd, subCmd, data);
        answer = null;
        this.mRetries = 0;
        while (this.mRetries < maxRetries && answer == null) {
            sendData(packet);
            System.out.println("Wait for responce...");
            try {
                answer = waitForPacket();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            this.mRetries += 1;
        }
        if (answer == null) {
            throw new IOException("Response timeout");
        }
        this.mToggle ^= TOGGLE;
        if (this.mLastStatus != 0) {
            throw new AudioReaderException(this.mLastStatus);
        }
        return answer;
    }

    public synchronized byte[] transmit(int cmd, int subCmd, byte[] data) throws AudioReaderException, IOException {
        return transmit(cmd, subCmd, data, 5);
    }

    private synchronized byte[] transmit(int cmd, int subCmd) throws AudioReaderException, IOException {
        return transmit(cmd, subCmd, null);
    }

    public int getLastStatus() {
        return this.mLastStatus;
    }

    public int getLastRetries() {
        return this.mRetries;
    }

    public synchronized void close() {
        this.mPlayer.stop();
        this.mRecorder.stop();
        enableKeepAlive(false);
    }

    public synchronized void powerOn() throws IOException {
        System.out.println("Power on");
        this.mPlayer.playTone(8000, 25, 100);
        this.mPlayer.flush();
        SystemClock.sleep(125);
        System.out.println("Wait for SYN...");
        waitForSYN();
        clearPacketBuffer();
        enableKeepAlive(true);
    }

    //
//    public synchronized void powerOff() throws IOException {
//        sendData(createPacket(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_NUMBER, null));
//        enableKeepAlive(false);
//    }
//
//    public synchronized Identification getIdent() throws AudioReaderException, IOException {
//        byte[] buf;
//        String name;
//        int offs;
//        String version;
//        buf = transmit(TRACK_READ_MODE_ALLOWED, TRACK_READ_MODE_ALLOWED);
//        name = "";
//        int offs2 = TRACK_READ_MODE_NOT_ALLOWED;
//        while (buf[offs2] != null) {
//            offs = offs2 + TRACK_READ_MODE_ALLOWED;
//            name = new StringBuilder(String.valueOf(name)).append((char) buf[offs2]).toString();
//            offs2 = offs;
//        }
//        offs = offs2 + TRACK_READ_MODE_ALLOWED;
//        offs += TRACK_READ_MODE_ALLOWED;
//        version = (buf[offs] & KEY_NO_KEY) + ((buf[offs] & KEY_NO_KEY) + "");
//        offs += TRACK_READ_MODE_ALLOWED;
//        return new Identification(name, (buf[offs + TRACK_READ_MODE_ALLOWED] & KEY_NO_KEY) + ("." + (buf[offs] & KEY_NO_KEY) + version));
//    }

    public synchronized Battery getBattery() throws AudioReaderException, IOException {
        byte[] buf;
        buf = transmit(1, 4);
        if (buf.length != 5) {
            throw new AudioReaderException(2);
        }
        return new Battery(((buf[0] & KEY_NO_KEY) * 1000) + ((buf[1] & KEY_NO_KEY) * 100), buf[2] & KEY_NO_KEY, buf[3] & KEY_NO_KEY);
    }

    public synchronized String getSerialNumber() throws IOException {
        StringBuffer s;
        byte[] buf = transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_SEQUENCE);
        s = new StringBuffer();
        int length = buf.length;
        for (int i = 0; i < length; i += 1) {
            byte b = buf[i];
//            if (b == null) {
//                break;
//            }
            s.append((char) b);
        }
        return s.toString();
    }

    //
//    public void writeNVRAM(int address, byte[] data) throws AudioReaderException, IOException {
//        if (address % TOGGLE != 0) {
//            throw new IllegalArgumentException("The address must be multiple of 128");
//        } else if (data.length != TOGGLE) {
//            throw new IllegalArgumentException("The data.length must be 128 bytes long");
//        } else {
//            ByteArrayOutputStream o = new ByteArrayOutputStream();
//            o.write(address >> STATUS_CARD_INTERNAL_ERROR);
//            o.write(address);
//            o.write(data);
//            transmit(TRACK_READ_MODE_ALLOWED, STATUS_LOW_BATTERY, o.toByteArray());
//        }
//    }
//
//    public byte[] readNVRAM(int address) throws AudioReaderException, IOException {
//        if (address % TOGGLE != 0) {
//            throw new IllegalArgumentException("The address must be multiple of 128");
//        }
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(address >> STATUS_CARD_INTERNAL_ERROR);
//        o.write(address);
//        return transmit(TRACK_READ_MODE_ALLOWED, SYN, o.toByteArray());
//    }
//
    public synchronized FinancialCard getFinancialCard(int wait) throws AudioReaderException, IOException {
        byte[] masked;
        byte[] data;
        byte[] tracksBuf;
        byte[] rsaBuf;
        String key = "com.datecs.audioreader.preferences.rsa_key";
        if (this.mCachedCardData != null) {
            tracksBuf = this.mCachedCardData;
            this.mCachedCardData = null;
        } else {
            try {
                byte[] tmp = new byte[TRACK_READ_MODE_REQUIRED];
                tmp[TRACK_READ_MODE_NOT_ALLOWED] = (byte) (wait >> STATUS_CARD_INTERNAL_ERROR);
                tmp[TRACK_READ_MODE_ALLOWED] = (byte) wait;
                tracksBuf = transmit(TRACK_READ_MODE_REQUIRED, STATUS_CARD_NOT_PRESENT, tmp);
            } catch (AudioReaderException e) {
                tracksBuf = transmit(TRACK_READ_MODE_REQUIRED, STATUS_CARD_NOT_PRESENT);
            }
        }
        String pattern = byteArrayToHexString(tracksBuf, TRACK_READ_MODE_ALLOWED, STATUS_LOW_BATTERY);
        String rsa = this.mPrefs.getString("com.datecs.audioreader.preferences.rsa_key", null);
        masked = null;
        try {
            masked = transmit(TRACK_READ_MODE_REQUIRED, STATUS_CARD_INTERNAL_ERROR);
        } catch (AudioReaderException e2) {
        }
        if (rsa != null) {
            if (pattern.equals(rsa.substring(TRACK_READ_MODE_NOT_ALLOWED, KEY_DUKPT_MASTER))) {
                rsaBuf = hexStringToByteArray(rsa.substring(KEY_DUKPT_MASTER));
                data = new byte[((rsaBuf.length - 2) + tracksBuf.length)];
                data[TRACK_READ_MODE_NOT_ALLOWED] = tracksBuf[TRACK_READ_MODE_NOT_ALLOWED];
                System.arraycopy(rsaBuf, TRACK_READ_MODE_REQUIRED, data, TRACK_READ_MODE_ALLOWED, rsaBuf.length - 2);
                System.arraycopy(tracksBuf, TRACK_READ_MODE_ALLOWED, data, (rsaBuf.length + TRACK_READ_MODE_ALLOWED) - 2, tracksBuf.length - 1);
            }
        }
        rsaBuf = transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_LENGTH);
        this.mPrefs.edit().putString("com.datecs.audioreader.preferences.rsa_key", new StringBuilder(String.valueOf(pattern)).append(byteArrayToHexString(rsaBuf, TRACK_READ_MODE_NOT_ALLOWED, rsaBuf.length)).toString()).commit();
        data = new byte[((rsaBuf.length - 2) + tracksBuf.length)];
        data[TRACK_READ_MODE_NOT_ALLOWED] = tracksBuf[TRACK_READ_MODE_NOT_ALLOWED];
        System.arraycopy(rsaBuf, TRACK_READ_MODE_REQUIRED, data, TRACK_READ_MODE_ALLOWED, rsaBuf.length - 2);
        System.arraycopy(tracksBuf, TRACK_READ_MODE_ALLOWED, data, (rsaBuf.length + TRACK_READ_MODE_ALLOWED) - 2, tracksBuf.length - 1);
        return new FinancialCard(masked, data);
    }

    public synchronized FinancialCard getFinancialCard() throws AudioReaderException, IOException {
        return getFinancialCard(STATUS_INVALID_COMMAND_LENGTH);
    }


    public static FirmwareInformation getFirmwareFileInformation(InputStream in) throws IOException {
        int i;
        byte[] header = new byte[36];
        for (i = TRACK_READ_MODE_NOT_ALLOWED; i < header.length; i += TRACK_READ_MODE_ALLOWED) {
            header[i] = (byte) in.read();
        }
        int encryption = ((((header[STATUS_INVALID_COMMAND_DATA] & KEY_NO_KEY) << 24) + ((header[STATUS_CARD_NOT_ENABLED] & KEY_NO_KEY) << STATUS_LOW_BATTERY)) + ((header[STATUS_CARD_NOT_POWERED] & KEY_NO_KEY) << STATUS_CARD_INTERNAL_ERROR)) + (header[STATUS_CARD_NOT_PRESENT] & KEY_NO_KEY);
        String version = (header[STATUS_INVALID_NAME] & KEY_NO_KEY) + ("." + (header[STATUS_INVALID_COMMAND_LENGTH] & KEY_NO_KEY) + ((header[STATUS_INVALID_COMMAND_SEQUENCE] & KEY_NO_KEY) + ((header[STATUS_CARD_INTERNAL_ERROR] & KEY_NO_KEY) + "")));
        String name = "";
        for (i = TRACK_READ_MODE_NOT_ALLOWED; i < STATUS_LOW_BATTERY; i += TRACK_READ_MODE_ALLOWED) {
            char c = (char) header[i + STATUS_INVALID_COMMAND_FORMAT];
            if (c != '\u0000') {
                name = new StringBuilder(String.valueOf(name)).append(c).toString();
            }
        }
        int signatureLength = (((TRACK_READ_MODE_NOT_ALLOWED + (header[28] & KEY_NO_KEY)) + ((header[29] & KEY_NO_KEY) << STATUS_CARD_INTERNAL_ERROR)) + ((header[30] & KEY_NO_KEY) << STATUS_LOW_BATTERY)) + ((header[31] & KEY_NO_KEY) << 24);
        int firmwareLength = (((TRACK_READ_MODE_NOT_ALLOWED + (header[KEY_DUKPT_MASTER] & KEY_NO_KEY)) + ((header[CSC_TRANSMIT_MESSAGE] & KEY_NO_KEY) << STATUS_CARD_INTERNAL_ERROR)) + ((header[34] & KEY_NO_KEY) << STATUS_LOW_BATTERY)) + ((header[35] & KEY_NO_KEY) << 24);
        byte[] signature = new byte[signatureLength];
        for (i = TRACK_READ_MODE_NOT_ALLOWED; i < signatureLength; i += TRACK_READ_MODE_ALLOWED) {
            signature[i] = (byte) in.read();
        }
        byte[] firmware = new byte[firmwareLength];
        for (i = TRACK_READ_MODE_NOT_ALLOWED; i < firmwareLength; i += TRACK_READ_MODE_ALLOWED) {
            firmware[i] = (byte) in.read();
        }
        return new FirmwareInformation(header, encryption, version, name, signature, firmware);
    }
//
//    public synchronized void updateFirmware(FirmwareInformation firmwareInfo, OnUpdateFirmwareListener listener) throws AudioReaderException, IOException {
//        byte[] data = new byte[(firmwareInfo.header.length + firmwareInfo.signature.length)];
//        System.arraycopy(firmwareInfo.header, TRACK_READ_MODE_NOT_ALLOWED, data, TRACK_READ_MODE_NOT_ALLOWED, firmwareInfo.header.length);
//        if ("1.930".compareTo(firmwareInfo.version) <= 0) {
//            byte[] buf = "DATECS Audio-SCR".getBytes();
//            System.arraycopy(buf, TRACK_READ_MODE_NOT_ALLOWED, data, STATUS_INVALID_COMMAND_FORMAT, buf.length);
//        }
//        System.arraycopy(firmwareInfo.signature, TRACK_READ_MODE_NOT_ALLOWED, data, firmwareInfo.header.length, firmwareInfo.signature.length);
//        transmit(TRACK_READ_MODE_ALLOWED, STATUS_CARD_NOT_ENABLED, data);
//        data = new byte[KEY_TYPE_3DES_KEK];
//        int i = TRACK_READ_MODE_NOT_ALLOWED;
//        while (i < firmwareInfo.firmware.length) {
//            int length = Math.min(firmwareInfo.firmware.length - i, TOGGLE);
//            data[TRACK_READ_MODE_NOT_ALLOWED] = (byte) (i >> 24);
//            data[TRACK_READ_MODE_ALLOWED] = (byte) (i >> STATUS_LOW_BATTERY);
//            data[TRACK_READ_MODE_REQUIRED] = (byte) (i >> STATUS_CARD_INTERNAL_ERROR);
//            data[STATUS_INVALID_COMMAND_NUMBER] = (byte) i;
//            if (listener != null && listener.onDataSend(i, firmwareInfo.firmware.length)) {
//                break;
//            }
//            System.arraycopy(firmwareInfo.firmware, i, data, STATUS_INVALID_COMMAND_DATA, length);
//            transmit(TRACK_READ_MODE_ALLOWED, STATUS_CARD_NOT_POWERED, data);
//            i += TOGGLE;
//        }
//        transmit(TRACK_READ_MODE_ALLOWED, STATUS_CARD_NOT_PRESENT);
//        SystemClock.sleep(2000);
//    }
//
//    public synchronized Transaction startTransaction() throws AudioReaderException, IOException {
//        int rsaKeyVersion;
//        byte[] rsaBlock;
//        byte[] buf = transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_LENGTH);
//        rsaKeyVersion = buf[TRACK_READ_MODE_NOT_ALLOWED] & KEY_NO_KEY;
//        rsaBlock = new byte[(buf.length - 2)];
//        System.arraycopy(buf, TRACK_READ_MODE_REQUIRED, rsaBlock, TRACK_READ_MODE_NOT_ALLOWED, rsaBlock.length);
//        return new Transaction(rsaKeyVersion, rsaBlock);
//    }
//
//    public synchronized byte[] powerOnSmartCard() throws AudioReaderException, IOException {
//        transmit(TRACK_READ_MODE_REQUIRED, TRACK_READ_MODE_ALLOWED);
//        try {
//        } catch (AudioReaderException e) {
//            transmit(TRACK_READ_MODE_REQUIRED, STATUS_INVALID_COMMAND_DATA);
//            throw e;
//        }
//        return transmit(TRACK_READ_MODE_REQUIRED, STATUS_INVALID_COMMAND_NUMBER);
//    }
//
//    public synchronized void powerOffSmartCard() throws AudioReaderException, IOException {
//        transmit(TRACK_READ_MODE_REQUIRED, STATUS_INVALID_COMMAND_DATA);
//    }
//
//    public synchronized boolean isCardPresent() throws AudioReaderException, IOException {
//        boolean z = false;
//        synchronized (this) {
//            if (transmit(TRACK_READ_MODE_REQUIRED, STATUS_CARD_NOT_ENABLED)[TRACK_READ_MODE_NOT_ALLOWED] != null) {
//                z = true;
//            }
//        }
//        return z;
//    }
//
    public synchronized byte[] random(int size) throws AudioReaderException, IOException {
        byte[] bArr;
        bArr = new byte[TRACK_READ_MODE_ALLOWED];
        bArr[TRACK_READ_MODE_NOT_ALLOWED] = (byte) size;
        return transmit(TRACK_READ_MODE_ALLOWED, CSYS_RANDOM, bArr);
    }

    public synchronized byte[] randomSCR() throws AudioReaderException, IOException {
        return transmit(TRACK_READ_MODE_ALLOWED, CSYS_SCR_RANDOM);
    }

//    public synchronized boolean test(byte[] b) throws AudioReaderException, IOException {
//        boolean res;
//        byte[] buf = transmit(TRACK_READ_MODE_ALLOWED, KEY_NO_KEY, b, TRACK_READ_MODE_ALLOWED);
//        if (b.length == buf.length) {
//            res = true;
//        } else {
//            res = false;
//        }
//        int i = TRACK_READ_MODE_NOT_ALLOWED;
//        while (res && i < buf.length) {
//            if ((b[i] & KEY_NO_KEY) == ((buf[i] & KEY_NO_KEY) ^ KEY_NO_KEY)) {
//                res = true;
//            } else {
//                res = false;
//            }
//            i += TRACK_READ_MODE_ALLOWED;
//        }
//        return res;
//    }
//
//    public void cancel() throws AudioReaderException, IOException {
//        byte[] bArr = new byte[TRACK_READ_MODE_ALLOWED];
//        bArr[TRACK_READ_MODE_NOT_ALLOWED] = (byte) -61;
//        sendData(bArr);
//    }
//
    public void setMagneticCardMode(int encryption, int track1Mode, int track2Mode, int track3Mode) {
        this.mSettings.msEncryption = encryption;
        this.mSettings.msTrack1Mode = track1Mode;
        this.mSettings.msTrack2Mode = track2Mode;
        this.mSettings.msTrack3Mode = track3Mode;
    }

    public void setMagneticCardMaskMode(boolean showExpiration, int unmaskedDigitsAtStart, int unmaskedDigitsAtEnd) {
        this.mSettings.showExpiration = showExpiration;
        this.mSettings.unmaskedDigitsAtStart = unmaskedDigitsAtStart;
        this.mSettings.unmaskedDigitsAtEnd = unmaskedDigitsAtEnd;
    }


    public void setRFIDCardMode(int supportedCards) {
        this.mSettings.rfSupportedCards = supportedCards;
    }

    public CardInfo waitForCard(int timeout) throws AudioReaderException, IOException {
        long endTime = System.currentTimeMillis() + ((long) timeout);
        ByteArrayOutputStream tmpBuf = new ByteArrayOutputStream();
        tmpBuf.write(81);
        tmpBuf.write(0);
        tmpBuf.write(0);
        tmpBuf.write(4);
        tmpBuf.write(52);
        tmpBuf.write(this.mSettings.showExpiration ? 1 : 0);
        tmpBuf.write(this.mSettings.unmaskedDigitsAtStart);
        tmpBuf.write(this.mSettings.unmaskedDigitsAtEnd);
        try {
            transmitEMSR(tmpBuf.toByteArray());
        } catch (AudioReaderException e) {
        }
        tmpBuf = new ByteArrayOutputStream();
        tmpBuf.write(TRACK_READ_MODE_NOT_ALLOWED);
        tmpBuf.write(STATUS_LOW_BATTERY);
        tmpBuf.write(TRACK_READ_MODE_NOT_ALLOWED);
        tmpBuf.write(STATUS_INVALID_COMMAND_DATA);
        tmpBuf.write(this.mSettings.rfSupportedCards >> STATUS_CARD_INTERNAL_ERROR);
        tmpBuf.write(this.mSettings.rfSupportedCards);
        tmpBuf.write(TRACK_READ_MODE_NOT_ALLOWED);
        tmpBuf.write(DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE);
        try {
            transmitRFID(tmpBuf.toByteArray());
        } catch (AudioReaderException e2) {
        }
        while (System.currentTimeMillis() < endTime) {
            int timeLeft = (int) (endTime - System.currentTimeMillis());
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            o.write(TRACK_READ_MODE_NOT_ALLOWED);
            o.write(TRACK_READ_MODE_NOT_ALLOWED);
            o.write(timeLeft >> STATUS_CARD_INTERNAL_ERROR);
            o.write(timeLeft);
            o.write(this.mSettings.msEncryption);
            o.write((this.mSettings.msTrack3Mode != 0 ? STATUS_INVALID_COMMAND_DATA : TRACK_READ_MODE_NOT_ALLOWED) + (((this.mSettings.msTrack1Mode != 0 ? TRACK_READ_MODE_ALLOWED : TRACK_READ_MODE_NOT_ALLOWED) + 160) + (this.mSettings.msTrack2Mode != 0 ? TRACK_READ_MODE_REQUIRED : TRACK_READ_MODE_NOT_ALLOWED)));
            o.write(TRACK_READ_MODE_ALLOWED);
            try {
                byte[] result = transmit(TRACK_READ_MODE_REQUIRED, STATUS_LOW_BATTERY, o.toByteArray());
                int cardType = result[TRACK_READ_MODE_NOT_ALLOWED] & KEY_NO_KEY;
                boolean track1Read = false;
                boolean track2Read = false;
                boolean track3Read = false;
                byte[] atr = null;
                ContactlessCard contactlessCard = null;
                if (cardType == 0) {
                    track1Read = (result[STATUS_INVALID_COMMAND_DATA] & TRACK_READ_MODE_ALLOWED) != 0;
                    track2Read = (result[STATUS_INVALID_COMMAND_DATA] & TRACK_READ_MODE_REQUIRED) != 0;
                    track3Read = (result[STATUS_INVALID_COMMAND_DATA] & STATUS_INVALID_COMMAND_DATA) != 0;
                    int i = this.mSettings.msTrack1Mode;
                    int r0 = 0;
                    if (r0 != TRACK_READ_MODE_REQUIRED || track1Read) {
                        i = this.mSettings.msTrack2Mode;
                        if (r0 != TRACK_READ_MODE_REQUIRED || track2Read) {
                            i = this.mSettings.msTrack3Mode;
                            if (r0 == TRACK_READ_MODE_REQUIRED && !track3Read) {
                            }
                        }
                    }
                } else if (cardType == TRACK_READ_MODE_ALLOWED) {
                    atr = new byte[(result.length - 4)];
                    System.arraycopy(result, STATUS_INVALID_COMMAND_DATA, atr, TRACK_READ_MODE_NOT_ALLOWED, atr.length);
                } else if (cardType == TRACK_READ_MODE_REQUIRED) {
                    RC663 module = new RC663(this);
                    byte[] packet = new byte[(result.length - 4)];
                    System.arraycopy(result, STATUS_INVALID_COMMAND_DATA, packet, TRACK_READ_MODE_NOT_ALLOWED, packet.length);
                    contactlessCard = module.initCard(packet);
                    if (contactlessCard == null) {
                    }
                } else {
                    throw new RuntimeException("Illegal card type");
                }
                return new CardInfo(cardType, atr, track1Read, track2Read, track3Read, contactlessCard);
            } catch (AudioReaderException e3) {
                if (e3.getStatusCode() == STATUS_INVALID_COMMAND_NUMBER) {
                    this.mFinancialCard = getFinancialCard(timeout / STATUS_EMSR_OFFSET);
                    return new CardInfo(TRACK_READ_MODE_NOT_ALLOWED, null, true, true, false, null);
                }
                throw e3;
            }
        }
        throw new AudioReaderException(STATUS_CARD_NOT_PRESENT);
    }


    public byte[] getCardData() throws AudioReaderException, IOException {
        if (this.mFinancialCard != null) {
            FinancialCard card = this.mFinancialCard;
            this.mFinancialCard = null;
            return card.data;
        }
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        byte[] res = transmit(TRACK_READ_MODE_REQUIRED, STATUS_LOW_BATTERY, o.toByteArray());
        byte[] tmp = new byte[(res.length - 4)];
        System.arraycopy(res, STATUS_INVALID_COMMAND_DATA, tmp, TRACK_READ_MODE_NOT_ALLOWED, tmp.length);
        return tmp;
    }

    public FinancialCard getFinancialCardData() throws AudioReaderException, IOException {
        if (this.mFinancialCard != null) {
            FinancialCard card = this.mFinancialCard;
            this.mFinancialCard = null;
            return card;
        }
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        o.write(TRACK_READ_MODE_NOT_ALLOWED);
        byte[] data = getCardData();
        if (this.mSettings.msEncryption == STATUS_CARD_NOT_ENABLED) {
            this.mCachedCardData = data;
            return getFinancialCard();
        }
        byte[] masked = null;
        try {
            masked = transmit(TRACK_READ_MODE_REQUIRED, STATUS_CARD_INTERNAL_ERROR);
        } catch (AudioReaderException e) {
        }
        return new FinancialCard(masked, data);
    }

    //    public synchronized byte[] transmitEncryptedMessage(byte[] data) throws AudioReaderException, IOException {
//        return transmit(TRACK_READ_MODE_REQUIRED, KEY_DUKPT_MASTER, data);
//    }
//
//    public synchronized byte[] transmitMessage(byte[] data) throws AudioReaderException, IOException {
//        return transmit(TRACK_READ_MODE_REQUIRED, CSC_TRANSMIT_MESSAGE, data);
//    }
//
//    public synchronized MessageResponse transmitMessage(int cid, byte[] data) throws AudioReaderException, IOException {
//        MessageResponse msgResponse;
//        byte[] input = new byte[(data.length + STATUS_INVALID_COMMAND_DATA)];
//        input[TRACK_READ_MODE_NOT_ALLOWED] = (byte) cid;
//        input[TRACK_READ_MODE_REQUIRED] = (byte) (data.length >> STATUS_CARD_INTERNAL_ERROR);
//        input[STATUS_INVALID_COMMAND_NUMBER] = (byte) data.length;
//        System.arraycopy(data, TRACK_READ_MODE_NOT_ALLOWED, input, STATUS_INVALID_COMMAND_DATA, data.length);
//        msgResponse = new MessageResponse(transmitMessage(input));
//        if (cid != msgResponse.cid) {
//            throw new IOException("Invalid command response data");
//        }
//        return msgResponse;
//    }
//
//    public synchronized MessageResponse transmitMessage(int cid) throws AudioReaderException, IOException {
//        return transmitMessage(cid, new byte[TRACK_READ_MODE_NOT_ALLOWED]);
//    }
//
//    public synchronized byte[] processMessage(byte[] data) throws AudioReaderException, IOException {
//        byte[] output;
//        boolean encrypted = true;
//        int i = TRACK_READ_MODE_NOT_ALLOWED;
//        synchronized (this) {
//            if (data.length < TRACK_READ_MODE_REQUIRED || ((data[TRACK_READ_MODE_NOT_ALLOWED] & 63) << STATUS_CARD_INTERNAL_ERROR) + (data[TRACK_READ_MODE_ALLOWED] & KEY_NO_KEY) != data.length - 2) {
//                throw new IllegalArgumentException("Invalid data content length");
//            }
//            byte[] result;
//            if ((data[TRACK_READ_MODE_NOT_ALLOWED] & TOGGLE) == 0) {
//                encrypted = false;
//            }
//            byte[] input = new byte[(data.length - 2)];
//            System.arraycopy(data, TRACK_READ_MODE_REQUIRED, input, TRACK_READ_MODE_NOT_ALLOWED, input.length);
//            if (encrypted) {
//                result = transmitEncryptedMessage(input);
//            } else {
//                result = transmitMessage(input);
//            }
//            output = new byte[(result.length + TRACK_READ_MODE_REQUIRED)];
//            if (encrypted) {
//                i = TOGGLE;
//            }
//            output[TRACK_READ_MODE_NOT_ALLOWED] = (byte) (i | (result.length >> STATUS_CARD_INTERNAL_ERROR));
//            output[TRACK_READ_MODE_ALLOWED] = (byte) (result.length & KEY_NO_KEY);
//            System.arraycopy(result, TRACK_READ_MODE_NOT_ALLOWED, output, TRACK_READ_MODE_REQUIRED, result.length);
//        }
//        return output;
//    }
//
//    public synchronized byte[] getConfigurationParameters() throws AudioReaderException, IOException {
//        return transmitMessage((int) STATUS_INVALID_COMMAND_NUMBER).data;
//    }
//
//    public synchronized CardStatusResponse getCardStatus() throws AudioReaderException, IOException {
//        return CardStatusResponse.parse(transmitMessage((int) STATUS_CARD_NOT_ENABLED).data);
//    }
//
//    public synchronized CardResetResponse performCardEMVReset() throws AudioReaderException, IOException {
//        return CardResetResponse.parse(transmitMessage((int) STATUS_CARD_NOT_POWERED).data);
//    }
//
//    public synchronized CardResetResponse performCardReset() throws AudioReaderException, IOException {
//        return CardResetResponse.parse(transmitMessage((int) STATUS_CARD_NOT_PRESENT).data);
//    }
//
//    public synchronized CardStatusResponse performCardPowerOff() throws AudioReaderException, IOException {
//        return CardStatusResponse.parse(transmitMessage((int) STATUS_CARD_INTERNAL_ERROR).data);
//    }
//
//    public synchronized CardResetResponse waitForCardAndEMVReset(int timeout) throws AudioReaderException, IOException {
//        byte[] data;
//        data = new byte[STATUS_INVALID_COMMAND_DATA];
//        data[TRACK_READ_MODE_NOT_ALLOWED] = (byte) (timeout >> 24);
//        data[TRACK_READ_MODE_ALLOWED] = (byte) (timeout >> STATUS_LOW_BATTERY);
//        data[TRACK_READ_MODE_REQUIRED] = (byte) (timeout >> STATUS_CARD_INTERNAL_ERROR);
//        data[STATUS_INVALID_COMMAND_NUMBER] = (byte) timeout;
//        return CardResetResponse.parse(transmitMessage(STATUS_INVALID_COMMAND_SEQUENCE, data).data);
//    }
//
//    public synchronized EMVChecksumResponse getEMVChecksum(String name) throws AudioReaderException, IOException {
//        return EMVChecksumResponse.parse(transmitMessage(STATUS_INVALID_COMMAND_LENGTH, new StringBuilder(String.valueOf(name)).append("\u0000").toString().getBytes()).data);
//    }
//
    public synchronized byte[] enterProtectedMode() throws AudioReaderException, IOException {
        ByteArrayOutputStream out;
        String prefKey = "com.datecs.audioreader.preferences.session_key_data";
        String sessionKeyData = this.mPrefs.getString("com.datecs.audioreader.preferences.session_key_data", null);
        byte[] serial = transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_SEQUENCE);
        byte[] version = transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_NAME);
        byte[] pattern = new byte[24];
        System.arraycopy(serial, TRACK_READ_MODE_NOT_ALLOWED, pattern, TRACK_READ_MODE_NOT_ALLOWED, serial.length);
        System.arraycopy(version, TRACK_READ_MODE_NOT_ALLOWED, pattern, STATUS_LOW_BATTERY, version.length);
        out = new ByteArrayOutputStream();
        byte[] rsaBlock;
        if (sessionKeyData == null || serial[TRACK_READ_MODE_NOT_ALLOWED] == -1 || !sessionKeyData.startsWith(byteArrayToHexString(pattern))) {
            byte[] result = transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_LENGTH);
            if (result.length == TRACK_READ_MODE_REQUIRED) {
                out.write(result);
                this.mPrefs.edit().remove("com.datecs.audioreader.preferences.session_key_data").commit();
            } else {
                rsaBlock = byteArrayToSubArray(result, TRACK_READ_MODE_REQUIRED, result.length - 2);
                out.write(TRACK_READ_MODE_NOT_ALLOWED);
                out.write(TRACK_READ_MODE_NOT_ALLOWED);
                out.write(pattern);
                out.write(rsaBlock);
                this.mPrefs.edit().putString("com.datecs.audioreader.preferences.session_key_data", byteArrayToHexString(pattern) + byteArrayToHexString(rsaBlock)).commit();
            }
        } else {
            rsaBlock = hexStringToByteArray(sessionKeyData.substring(pattern.length * TRACK_READ_MODE_REQUIRED));
            out.write(TRACK_READ_MODE_NOT_ALLOWED);
            out.write(TRACK_READ_MODE_NOT_ALLOWED);
            out.write(pattern);
            out.write(rsaBlock);
        }
        return out.toByteArray();
    }

    //
//    public synchronized int kmGetIPEKVersion() throws AudioReaderException, IOException {
//        byte[] res;
//        res = transmit(STATUS_INVALID_COMMAND_NUMBER, TRACK_READ_MODE_ALLOWED);
//        return ((((res[TRACK_READ_MODE_NOT_ALLOWED] & KEY_NO_KEY) << 24) + ((res[TRACK_READ_MODE_ALLOWED] & KEY_NO_KEY) << STATUS_LOW_BATTERY)) + ((res[TRACK_READ_MODE_REQUIRED] & KEY_NO_KEY) << STATUS_CARD_INTERNAL_ERROR)) + (res[STATUS_INVALID_COMMAND_NUMBER] & KEY_NO_KEY);
//    }
//
//    public synchronized int kmGetKEKVersion() throws AudioReaderException, IOException {
//        byte[] res;
//        res = transmit(STATUS_INVALID_COMMAND_NUMBER, TRACK_READ_MODE_REQUIRED);
//        return ((((res[TRACK_READ_MODE_NOT_ALLOWED] & KEY_NO_KEY) << 24) + ((res[TRACK_READ_MODE_ALLOWED] & KEY_NO_KEY) << STATUS_LOW_BATTERY)) + ((res[TRACK_READ_MODE_REQUIRED] & KEY_NO_KEY) << STATUS_CARD_INTERNAL_ERROR)) + (res[STATUS_INVALID_COMMAND_NUMBER] & KEY_NO_KEY);
//    }
//
//    public synchronized void kmLoadIPEK(int rfu, byte[] data) throws AudioReaderException, IOException {
//        byte[] buffer = new byte[(data.length + TRACK_READ_MODE_REQUIRED)];
//        System.arraycopy(data, TRACK_READ_MODE_NOT_ALLOWED, buffer, TRACK_READ_MODE_REQUIRED, data.length);
//        transmit(STATUS_INVALID_COMMAND_NUMBER, STATUS_INVALID_COMMAND_NUMBER, buffer);
//    }
//
//    public synchronized void kmLoadKEK(byte[] data) throws AudioReaderException, IOException {
//        transmit(STATUS_INVALID_COMMAND_NUMBER, STATUS_INVALID_COMMAND_DATA, data);
//    }
//
//    public synchronized byte[] kmGetKSN() throws AudioReaderException, IOException {
//        return transmit(STATUS_INVALID_COMMAND_NUMBER, STATUS_CARD_NOT_ENABLED);
//    }
//
//    public synchronized byte[] kmSetInitialVector(byte[] data) throws AudioReaderException, IOException {
//        if (data.length != STATUS_CARD_INTERNAL_ERROR) {
//            throw new IllegalArgumentException("The initial vactor must be 8 bytes long");
//        }
//        return transmit(STATUS_INVALID_COMMAND_NUMBER, STATUS_CARD_NOT_POWERED, data);
//    }
//
//    public void kmSetId(byte[] id) throws AudioReaderException, IOException {
//        if (id.length > 50) {
//            throw new IllegalArgumentException("The id data length must be up to 50 bytes long");
//        }
//        transmit(STATUS_INVALID_COMMAND_NUMBER, STATUS_CARD_NOT_PRESENT, id);
//    }
//
//    public void kmEnterId(byte[] id) throws AudioReaderException, IOException {
//        if (id.length > 50) {
//            throw new IllegalArgumentException("The id data length must be up to 50 bytes long");
//        }
//        transmit(STATUS_INVALID_COMMAND_NUMBER, STATUS_CARD_INTERNAL_ERROR, id);
//    }
//
//    public void kmLoadRSAKey(byte[] keyBlock, int index) throws AudioReaderException, IOException {
//        if (keyBlock == null) {
//            throw new NullPointerException("The parameter 'keyBlock' is null");
//        } else if (keyBlock.length != 516) {
//            throw new IllegalArgumentException("The parameter 'keyBlock' has invalid length");
//        } else {
//            byte[] signature = new byte[PKIFailureInfo.unacceptedPolicy];
//            System.arraycopy(keyBlock, TRACK_READ_MODE_NOT_ALLOWED, signature, TRACK_READ_MODE_NOT_ALLOWED, signature.length);
//            transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_ADDRESS, signature);
//            byte[] rsa = new byte[260];
//            System.arraycopy(keyBlock, signature.length, rsa, TRACK_READ_MODE_NOT_ALLOWED, rsa.length);
//            transmit(TRACK_READ_MODE_ALLOWED, STATUS_INVALID_COMMAND_FORMAT, rsa);
//        }
//    }
//
//    public void kmGenerateRSASymmetricKey(int keyType, int index) throws AudioReaderException, IOException {
//        byte[] bArr = new byte[TRACK_READ_MODE_ALLOWED];
//        bArr[TRACK_READ_MODE_NOT_ALLOWED] = (byte) keyType;
//        transmit(TRACK_READ_MODE_ALLOWED, STATUS_ENCRYPTION_ERROR, bArr);
//    }
//
//    public DeviceKeysInfo kmGetKeysInfo() throws AudioReaderException, IOException {
//        return new DeviceKeysInfo(transmit(TRACK_READ_MODE_ALLOWED, STATUS_SIGNATURE_ERROR));
//    }
//
//    public synchronized byte[] transmitAPDU(int rfu, byte[] input) throws AudioReaderException, IOException {
//        byte[] tmp;
//        byte[] buffer = new byte[(input.length + TRACK_READ_MODE_REQUIRED)];
//        System.arraycopy(input, TRACK_READ_MODE_NOT_ALLOWED, buffer, TRACK_READ_MODE_REQUIRED, input.length);
//        byte[] result = transmit(TRACK_READ_MODE_REQUIRED, STATUS_CARD_NOT_POWERED, buffer);
//        tmp = new byte[(result.length - 2)];
//        System.arraycopy(result, TRACK_READ_MODE_REQUIRED, tmp, TRACK_READ_MODE_NOT_ALLOWED, tmp.length);
//        return tmp;
//    }
//
    public synchronized byte[] transmitEMSR(byte[] input) throws AudioReaderException, IOException {
        return transmit(TRACK_READ_MODE_REQUIRED, STATUS_INVALID_COMMAND_SEQUENCE, input);
    }

    private byte[] transmitEMSR(int cmd, byte[] data) throws AudioReaderException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(cmd);
        out.write(TRACK_READ_MODE_NOT_ALLOWED);
        out.write(data.length >> STATUS_CARD_INTERNAL_ERROR);
        out.write(data.length);
        out.write(data);
        byte[] result = transmitEMSR(out.toByteArray());
        int status = result[TRACK_READ_MODE_ALLOWED] & KEY_NO_KEY;
        if (status != 0) {
            throw new AudioReaderException(status + STATUS_EMSR_OFFSET);
        }
        int length = ((result[TRACK_READ_MODE_REQUIRED] & KEY_NO_KEY) << STATUS_CARD_INTERNAL_ERROR) + (result[STATUS_INVALID_COMMAND_NUMBER] & KEY_NO_KEY);
        if (length != result.length - 4) {
            throw new IOException("Invalid packet content");
        }
        byte[] buffer = new byte[length];
        System.arraycopy(result, STATUS_INVALID_COMMAND_DATA, buffer, TRACK_READ_MODE_NOT_ALLOWED, buffer.length);
        return buffer;
    }

    private byte[] transmitEMSR(int cmd, int subCmd, byte[] data) throws AudioReaderException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(subCmd);
        out.write(data);
        return transmitEMSR(cmd, out.toByteArray());
    }

    private byte[] transmitEMSR(int cmd, int subCmd) throws AudioReaderException, IOException {
        return transmitEMSR(cmd, subCmd, new byte[TRACK_READ_MODE_NOT_ALLOWED]);
    }

    //
//    public synchronized EMSRInformation emsrGetInformation() throws AudioReaderException, IOException {
//        byte[] result;
//        result = transmitEMSR((int) TRACK_READ_MODE_ALLOWED, (int) TRACK_READ_MODE_ALLOWED);
//        return new EMSRInformation(new String(result, TRACK_READ_MODE_NOT_ALLOWED, STATUS_LOW_BATTERY).trim(), (result[STATUS_INVALID_VERSION] & KEY_NO_KEY) + "." + (result[STATUS_INVALID_KEY] & KEY_NO_KEY));
//    }
//
//    public synchronized EMSRKeyInformation emsrGetKeyInformation(int keyId) throws AudioReaderException, IOException {
//        EMSRKeyInformation keyInfo;
//        boolean tampered = true;
//        synchronized (this) {
//            byte[] bArr = new byte[TRACK_READ_MODE_ALLOWED];
//            bArr[TRACK_READ_MODE_NOT_ALLOWED] = (byte) keyId;
//            byte[] result = transmitEMSR(81, 40, bArr);
//            if (result[TRACK_READ_MODE_NOT_ALLOWED] == null) {
//                tampered = false;
//            }
//            keyInfo = new EMSRKeyInformation(tampered, (((result[TRACK_READ_MODE_REQUIRED] & -16777216) + (result[STATUS_INVALID_COMMAND_NUMBER] & 16711680)) + (result[STATUS_INVALID_COMMAND_DATA] & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) + (result[STATUS_CARD_NOT_ENABLED] & KEY_NO_KEY));
//        }
//        return keyInfo;
//    }
//
//    public synchronized void emsrLoadKey(byte[] keyData) throws AudioReaderException, IOException {
//        transmitEMSR(81, keyData);
//    }
//
//    public synchronized int emsrTest(int ms) throws AudioReaderException, IOException {
//        byte[] tmp;
//        tmp = null;
//        long endTime = System.currentTimeMillis() + ((long) ms);
//        byte[] bArr = new byte[TRACK_READ_MODE_ALLOWED];
//        bArr[TRACK_READ_MODE_NOT_ALLOWED] = (byte) 1;
//        transmitEMSR(81, TRACK_READ_MODE_ALLOWED, bArr);
//        while (endTime >= System.currentTimeMillis()) {
//            try {
//                transmitEMSR(81, (int) TRACK_READ_MODE_REQUIRED);
//                tmp = transmitEMSR(81, (int) STATUS_INVALID_COMMAND_DATA);
//                continue;
//            } catch (AudioReaderException e) {
//                if (e.getStatusCode() != STATUS_EMSR_NO_DATA) {
//                    throw e;
//                }
//            }
//            if (tmp != null) {
//                break;
//            }
//        }
//        transmitEMSR(81, TRACK_READ_MODE_ALLOWED, new byte[TRACK_READ_MODE_ALLOWED]);
//        if (tmp == null || tmp.length == 0) {
//            throw new AudioReaderException(STATUS_CARD_NOT_PRESENT);
//        }
//        return tmp[TRACK_READ_MODE_NOT_ALLOWED] % KEY_NO_KEY;
//    }
//
    public synchronized byte[] transmitRFID(byte[] input) throws AudioReaderException, IOException {
        return transmit(STATUS_INVALID_COMMAND_DATA, TRACK_READ_MODE_ALLOWED, input);
    }
}
