package com.datecs.pinpad;

import com.datecs.audioreader.emv.PrivateTags;
import com.datecs.pinpad.emv.EMVApplication;
import com.datecs.pinpad.emv.EMVCommonApplications;
import com.datecs.pinpad.emv.EMVKernelInfo;
import com.datecs.pinpad.emv.EMVTagDetails;
import com.datecs.pinpad.tlv.BerTlv;
//import com.estel.AREMV.EMVTags;
//import com.sun.mail.imap.IMAPStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//import org.apache.commons.httpclient.HttpStatus;

public class Pinpad {
    public static final int ALG_AES256 = 0;
    public static final int ALG_EH_3DES = 5;
    public static final int ALG_EH_AES256 = 2;
    public static final int ALG_EH_ECC = 1;
    public static final int ALG_EH_IDTECH = 3;
    public static final int ALG_EH_MAGTEK = 4;
    public static final int ANIM_ALL = -1;
    public static final int ANIM_BUSY = 2;
    public static final int ANIM_DROP = 3;
    public static final int ANIM_INSERT_CARD = 0;
    public static final int ANIM_INSERT_MAGNETIC_CARD = 5;
    public static final int ANIM_INSERT_SMARTCARD = 4;
    public static final int ANIM_REMOVE_CARD = 1;
    public static final int COLOR_BLACK = 1;
    public static final int COLOR_INVERTED = 2;
    public static final int COLOR_WHITE = 0;
    private static final int COMMAND_3DES = 49;
    private static final int COMMAND_BARCODE = 56;
    private static final int COMMAND_CA = 50;
    private static final int COMMAND_EMV2 = 55;
    private static final int COMMAND_IO = 53;
    private static final int COMMAND_MAGNETIC_CARD = 52;
    private static final int COMMAND_SMART_CARD = 51;
    private static final int COMMAND_SYSTEM = 54;
    public static final int CP_ARABIC = 5;
    public static final int CP_CYRILLIC = 4;
    public static final int CP_GREEK = 6;
    public static final int CP_HEBREW = 7;
    public static final int CP_LATIN1 = 0;
    public static final int CP_LATIN2 = 1;
    public static final int CP_LATIN3 = 2;
    public static final int CP_LATIN4 = 3;
    public static final int CP_LATIN5 = 8;
    public static final int CP_LATIN6 = 9;
    private static boolean DEBUG = false;
    private static final int DEFAULT_TIMEOUT = 5000;
    public static final int DUKPT = 2;
    private static final int EMV2_EVENT_COMPLETE = 131;
    private static final int EMV2_EVENT_ONLINE_AUTHORIZATION = 129;
    private static final int EMV2_EVENT_UPDATE_UI = 130;
    public static final int EMV2_MESSAGE_UI_APPROVED = 3;
    public static final int EMV2_MESSAGE_UI_CARD_COLLISION = 25;
    public static final int EMV2_MESSAGE_UI_CARD_READ_OK_REMOVE = 23;
    public static final int EMV2_MESSAGE_UI_CLEAR_DISPLAY = 30;
    public static final int EMV2_MESSAGE_UI_DECLINED = 7;
    public static final int EMV2_MESSAGE_UI_ERROR_PROCESSING = 15;
    public static final int EMV2_MESSAGE_UI_IDLE = 20;
    public static final int EMV2_MESSAGE_UI_INSERT_CARD = 29;
    public static final int EMV2_MESSAGE_UI_NA = 255;
    public static final int EMV2_MESSAGE_UI_NOT_WORKING = 0;
    public static final int EMV2_MESSAGE_UI_ONLINE_AUTHORISATION = 27;
    public static final int EMV2_MESSAGE_UI_PLEASE_ENTER_PIN = 9;
    public static final int EMV2_MESSAGE_UI_PRESENT_CARD = 21;
    public static final int EMV2_MESSAGE_UI_PRESENT_CARD_AGAIN = 33;
    public static final int EMV2_MESSAGE_UI_PROCESSING = 22;
    public static final int EMV2_MESSAGE_UI_REMOVE_CARD = 16;
    public static final int EMV2_MESSAGE_UI_SEE_PHONE = 32;
    public static final int EMV2_MESSAGE_UI_SIGN_APPROVED = 26;
    public static final int EMV2_MESSAGE_UI_TRY_OTHER_CARD = 28;
    public static final int EMV2_MESSAGE_UI_TRY_OTHER_INTERFACE = 24;
    public static final int EMV2_STATUS_UI_CARD_READ_SUCCESS = 4;
    public static final int EMV2_STATUS_UI_ERROR_PROCESSING = 5;
    public static final int EMV2_STATUS_UI_IDLE = 1;
    public static final int EMV2_STATUS_UI_NOT_READY = 0;
    public static final int EMV2_STATUS_UI_PROCESSING = 3;
    public static final int EMV2_STATUS_UI_READY_TO_READ = 2;
    public static final int EMV_AUTH_FAIL_PIN_ENTRY_NOT_DONE = 4;
    public static final int EMV_AUTH_FAIL_SEC_DEVICE_FAIL = 3;
    public static final int EMV_AUTH_FAIL_USER_CANCELLATION = 5;
    public static final int EMV_AUTH_RESULT_FAILURE = 2;
    public static final int EMV_AUTH_RESULT_SUCCESS = 1;
    public static final int EMV_BYPASS_ALL_METHODS_MODE = 1;
    public static final int EMV_BYPASS_CURRENT_METHOD_MODE = 0;
    public static final int EMV_CERTIFICATE_AAC = 0;
    public static final int EMV_CERTIFICATE_AC_TYPES = 3;
    public static final int EMV_CERTIFICATE_ARQC = 2;
    public static final int EMV_CERTIFICATE_TC = 1;
    public static final int EMV_RISK_CDOL_1 = 1;
    public static final int EMV_RISK_CDOL_2 = 2;
    public static final int ENABLE_BARCODE = 32;
    public static final int ENABLE_BUTTON1 = 6;
    public static final int ENABLE_BUTTON1_PRESS = 2;
    public static final int ENABLE_BUTTON1_RELEASE = 4;
    public static final int ENABLE_BUTTON2 = 24;
    public static final int ENABLE_BUTTON2_PRESS = 8;
    public static final int ENABLE_BUTTON2_RELEASE = 16;
    public static final int ENABLE_MAGNETIC_CARD = 64;
    public static final int ENABLE_SMART_CARD = 384;
    public static final int ENABLE_SMART_CARD_INSERT = 128;
    public static final int ENABLE_SMART_CARD_REMOVE = 256;
    public static final int ERROR_CANCEL = 18;
    public static final int ERROR_DUPLICATE_KEY = 34;
    public static final int ERROR_FLASH = 15;
    public static final int ERROR_GENERAL = 1;
    public static final int ERROR_HAL = 24;
    public static final int ERROR_HARDWARE = 16;
    public static final int ERROR_INVALID_ADDRESS = 4;
    public static final int ERROR_INVALID_COMMAND = 2;
    public static final int ERROR_INVALID_CRC = 17;
    public static final int ERROR_INVALID_DEVICE = 12;
    public static final int ERROR_INVALID_HEADER = 20;
    public static final int ERROR_INVALID_KEK = 33;
    public static final int ERROR_INVALID_KEY = 25;
    public static final int ERROR_INVALID_KEY_ATTRIBUTES = 11;
    public static final int ERROR_INVALID_KEY_FORMAT = 22;
    public static final int ERROR_INVALID_KEY_NUMBER = 10;
    public static final int ERROR_INVALID_LENGTH = 6;
    public static final int ERROR_INVALID_PARAM = 3;
    public static final int ERROR_INVALID_PASSWORD = 21;
    public static final int ERROR_INVALID_REMINDER = 27;
    public static final int ERROR_INVALID_SEQUENCE = 30;
    public static final int ERROR_INVALID_SIGNATURE = 19;
    public static final int ERROR_INVALID_VALUE = 5;
    public static final int ERROR_LIMIT = 29;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_NOT_INIT = 28;
    public static final int ERROR_NOT_PERMIT = 7;
    public static final int ERROR_NOT_SUPPORT = 13;
    public static final int ERROR_NO_DATA = 8;
    public static final int ERROR_NO_PERMITION = 31;
    public static final int ERROR_NO_PIN_DATA = 26;
    public static final int ERROR_NO_TMK = 32;
    public static final int ERROR_PIN_LIMIT = 14;
    public static final int ERROR_SCR = 23;
    public static final int ERROR_TIMEOUT = 9;
    private static final int EVENT_BARCODE = 5;
    private static final int EVENT_BLUETOOTH_DATA = 9;
    private static final int EVENT_EMV2 = 11;
    private static final int EVENT_MAGNETIC_CARD = 6;
    private static final int EVENT_SMART_CARD_IN = 7;
    private static final int EVENT_SMART_CARD_OUT = 8;
    private static final int EVENT_SWITCH1_OFF = 2;
    private static final int EVENT_SWITCH1_ON = 1;
    private static final int EVENT_SWITCH2_OFF = 4;
    private static final int EVENT_SWITCH2_ON = 3;
    public static final int FIXED = 4;
    public static final int FONT_4X6 = 2;
    public static final int FONT_6X8 = 0;
    public static final int FONT_8X16 = 1;
    public static final int HASH_HDES = 4;
    public static final int HASH_MD5 = 3;
    public static final int HASH_RIPEMD = 5;
    public static final int HASH_SHA1 = 1;
    public static final int HASH_SHA256 = 2;
    public static final int ISO0 = 4;
    public static final int ISO1 = 5;
    public static final int ISO3 = 13;
    public static final int KEY_AUTHENTICATION = 0;
    public static final int KEY_DATA_DEC = 19;
    public static final int KEY_DATA_ENC = 18;
    public static final int KEY_DUKPT = 24;
    public static final int KEY_EH_AES128_ENCRYPTION1 = 9;
    public static final int KEY_EH_AES128_ENCRYPTION2 = 11;
    public static final int KEY_EH_AES128_ENCRYPTION3 = 12;
    public static final int KEY_EH_AES256_ENCRYPTION1 = 1;
    public static final int KEY_EH_AES256_ENCRYPTION2 = 3;
    public static final int KEY_EH_AES256_ENCRYPTION3 = 4;
    public static final int KEY_EH_AES256_LOADING = 2;
    public static final int KEY_EH_DUKPT_MASTER1 = 32;
    public static final int KEY_EH_DUKPT_MASTER2 = 33;
    public static final int KEY_EH_DUKPT_MASTER3 = 34;
    public static final int KEY_EH_TMK_AES = 16;
    public static final int KEY_KEK_DATA_DEC = 3;
    public static final int KEY_KEK_DATA_ENC = 2;
    public static final int KEY_KEK_DUKPT = 8;
    public static final int KEY_KEK_MAC1 = 5;
    public static final int KEY_KEK_MAC1A = 7;
    public static final int KEY_KEK_MAC3 = 6;
    public static final int KEY_KEK_PIN = 4;
    public static final int KEY_MAC1 = 21;
    public static final int KEY_MAC1A = 23;
    public static final int KEY_MAC3 = 22;
    public static final int KEY_PIN = 20;
    public static final int KEY_TR31 = 0;
    public static final int LANG_BULGARIAN = 1;
    public static final int LANG_ENGLISH = 0;
    public static final int LANG_ESPANIOL = 2;
    public static final int LANG_FINISH = 6;
    public static final int LANG_FRENCH = 5;
    public static final int LANG_ROMANIAN = 4;
    public static final int LANG_RUSSIAN = 3;
    public static final int LANG_SWEDISH = 7;
    public static final int MAC_MODE_1 = 1;
    public static final int MAC_MODE_3 = 3;
    public static final int MAC_MODE_B = 11;
    private static final int MAX_PACKET_SIZE = 2056;
    public static final int MKSK = 3;
    public static final int MS_ENCRYPTION_3DES = 1;
    public static final int MS_ENCRYPTION_NONE = 0;
    public static final int SC_PROTOCOL_T0 = 0;
    public static final int SC_PROTOCOL_T1 = 1;
    public static final int SC_SLOT_MAIN = 0;
    public static final int SC_SLOT_SAM = 1;
    public static final int SC_VOLTAGE_3 = 3;
    public static final int SC_VOLTAGE_5 = 5;
    public static final int TRACKS_PREFIX = 128;
    public static final int TRACK_1 = 1;
    public static final int TRACK_2 = 2;
    public static final int TRACK_3 = 4;
    public static final int TYPE_ICC = 0;
    public static final int TYPE_PIN = 1;
//    private BarcodeListener mBarcodeListener;
//    private final InputStream mBaseInputStream;
//    private final OutputStream mBaseOutputStream;
//    private int mDisplayHeight;
//    private int mDisplayWidth;
//    private int mEMVLastStatus;
//    private Emv2Listener mEmv2Listener;
//    private boolean mEnterPinActive;
//    private Thread mEnterPinThread;
//    private IOException mLastError;
//    private MagstripeListener mMagstripeListener;
//    private byte[] mPacketBuffer;
//    private int mPacketBufferLen;
//    private PinpadListener mPinpadListener;
//    private ReceiverThread mReceiver;
//    private int mRecvTimeout;
//    private SmartCardListener mSmartCardListener;

//    class 1 implements Runnable {
//        private final /* synthetic */ byte[] val$data;
//        private final /* synthetic */ int val$event;
//
//        1(int i, byte[] bArr) {
//            this.val$event = i;
//            this.val$data = bArr;
//        }
//
//        public void run() {
//            switch (this.val$event) {
//                case Pinpad.SC_VOLTAGE_5 /*5*/:
//                    Pinpad.this.raiseOnBarcodeRead();
//                case Pinpad.LANG_FINISH /*6*/:
//                    Pinpad.this.raiseOnMagstripeRead();
//                case Pinpad.LANG_SWEDISH /*7*/:
//                    Pinpad.this.raiseOnSmartCardInserted();
//                case Pinpad.KEY_KEK_DUKPT /*8*/:
//                    Pinpad.this.raiseOnSmartCardRemoved();
//                case Pinpad.MAC_MODE_B /*11*/:
//                    Pinpad.this.translateContactlessEvent(this.val$data);
//                default:
//            }
//        }
//    }
//
//    class 2 implements Runnable {
//        private final /* synthetic */ PINEntryCompleteListener val$callback;
//
//        class 1 implements Runnable {
//            private final /* synthetic */ PINEntryCompleteListener val$callback;
//            private final /* synthetic */ int val$v;
//
//            1(PINEntryCompleteListener pINEntryCompleteListener, int i) {
//                this.val$callback = pINEntryCompleteListener;
//                this.val$v = i;
//            }
//
//            public void run() {
//                this.val$callback.onPINEntryComplete(this.val$v);
//            }
//        }
//
//        2(PINEntryCompleteListener pINEntryCompleteListener) {
//            this.val$callback = pINEntryCompleteListener;
//        }
//
//        public void run() {
//            int errorCode = Pinpad.KEY_DATA_ENC;
//            try {
//                ByteArrayOutputStream o = new ByteArrayOutputStream();
//                o.write(Pinpad.KEY_EH_DUKPT_MASTER2);
//                while (Pinpad.this.mEnterPinActive) {
//                    Pinpad.this.executeCmd(Pinpad.COMMAND_IO, o.toByteArray());
//                    errorCode = Pinpad.TYPE_ICC;
//                    break;
//                }
//                if (!Pinpad.this.mEnterPinActive) {
//                    o.reset();
//                    o.write(Pinpad.KEY_EH_DUKPT_MASTER3);
//                    Pinpad.this.executeCmd(Pinpad.COMMAND_IO, o.toByteArray());
//                }
//            } catch (PinpadException e) {
//                if (e.getErrorCode() != Pinpad.KEY_KEK_DUKPT) {
//                    throw e;
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e2) {
//                }
//            } catch (IOException e3) {
//                errorCode = Pinpad.ANIM_ALL;
//            } catch (PinpadException e4) {
//                errorCode = e4.getErrorCode();
//            }
//            new Thread(new 1(this.val$callback, errorCode)).start();
//        }
//    }

//    public interface BarcodeListener {
//        void onBarcodeRead();
//    }
//
//    public interface Emv2Listener {
//        void onOnlineAuthorisationRequest(byte[] bArr);
//
//        void onTransactionFinish(byte[] bArr);
//
//        void onUpdateUserInterface(int i, int i2, int i3);
//    }
//
//    public interface MagstripeListener {
//        void onMagstripeRead();
//    }
//
//    public interface PINEntryCompleteListener {
//        void onPINEntryComplete(int i);
//    }
//
//    public interface PinpadListener {
//        void onPinpadRelease();
//    }
//
//    private class ReceiverThread extends Thread {
//        private ReceiverThread() {
//        }
//
//        public void run() {
//            byte[] buffer = new byte[Pinpad.MAX_PACKET_SIZE];
//            int nbr = Pinpad.TYPE_ICC;
//            while (Pinpad.this.mLastError == null) {
//                if (nbr < Pinpad.LANG_FINISH) {
//                    try {
//                        nbr += Pinpad.this.read(buffer, nbr, buffer.length - nbr);
//                    } catch (Exception e) {
//                        if (Pinpad.this.mLastError == null) {
//                            if (Pinpad.DEBUG) {
//                                e.printStackTrace();
//                            }
//                            Pinpad.this.mLastError = new IOException(e.getMessage());
//                        }
//                    }
//                } else {
//                    int packetSize = Pinpad.LANG_FINISH + ((buffer[Pinpad.SC_VOLTAGE_3] & Pinpad.EMV2_MESSAGE_UI_NA) + (buffer[Pinpad.TRACK_3] & Pinpad.EMV2_MESSAGE_UI_NA));
//                    if (nbr < packetSize) {
//                        nbr += Pinpad.this.read(buffer, nbr, buffer.length - nbr);
//                    } else {
//                        int crc = Pinpad.TYPE_ICC;
//                        for (int i = Pinpad.TYPE_ICC; i < packetSize + Pinpad.ANIM_ALL; i += Pinpad.TYPE_PIN) {
//                            crc ^= buffer[i] & Pinpad.EMV2_MESSAGE_UI_NA;
//                        }
//                        if (crc == (buffer[packetSize + Pinpad.ANIM_ALL] & Pinpad.EMV2_MESSAGE_UI_NA)) {
//                            int event = buffer[Pinpad.TYPE_PIN] & Pinpad.EMV2_MESSAGE_UI_NA;
//                            if (event != 0) {
//                                byte[] data = new byte[(packetSize - 6)];
//                                System.arraycopy(buffer, Pinpad.SC_VOLTAGE_5, data, Pinpad.TYPE_ICC, data.length);
//                                if (Pinpad.DEBUG) {
//                                    System.out.println("event " + event + "(" + data.length + "): " + Pinpad.byteArrayToHexString(data, Pinpad.TYPE_ICC, data.length));
//                                }
//                                Pinpad.this.postEvent(event, data);
//                            } else {
//                                if (Pinpad.DEBUG) {
//                                    System.out.println("packet(" + packetSize + "): " + Pinpad.byteArrayToHexString(buffer, Pinpad.TYPE_ICC, packetSize));
//                                }
//                                synchronized (Pinpad.this.mPacketBuffer) {
//                                    System.arraycopy(buffer, Pinpad.TYPE_ICC, Pinpad.this.mPacketBuffer, Pinpad.TYPE_ICC, packetSize);
//                                    Pinpad.this.mPacketBufferLen = packetSize;
//                                    Pinpad.this.mPacketBuffer.notify();
//                                }
//                            }
//                            System.arraycopy(buffer, packetSize, buffer, Pinpad.TYPE_ICC, nbr - packetSize);
//                            nbr -= packetSize;
//                        } else {
//                            throw new IOException("Invalid packet CRC");
//                        }
//                    }
//                }
//            }
//            Pinpad.this.raiseOnPinpadRelease();
//        }
//    }
//
//    public interface SmartCardListener {
//        void onSmartCardInserted();
//
//        void onSmartCardRemoved();
//    }
//
//    static {
//        DEBUG = true;
//    }
//
//    public Pinpad(InputStream in, OutputStream out) {
//        this.mEMVLastStatus = TYPE_ICC;
//        if (in == null) {
//            throw new IllegalArgumentException("The argument 'in' is null");
//        } else if (out == null) {
//            throw new IllegalArgumentException("The argument 'out' is null");
//        } else {
//            this.mBaseInputStream = in;
//            this.mBaseOutputStream = out;
//            this.mRecvTimeout = DEFAULT_TIMEOUT;
//            this.mPacketBuffer = new byte[MAX_PACKET_SIZE];
//            this.mPacketBufferLen = TYPE_ICC;
//            this.mReceiver = new ReceiverThread();
//            this.mReceiver.start();
//            try {
//                init();
//            } catch (IOException e) {
//            }
//        }
//    }
//
//    private void init() throws IOException {
//        DeviceInfo info = getIdentification();
//        this.mDisplayWidth = TRACKS_PREFIX;
//        this.mDisplayHeight = ENABLE_MAGNETIC_CARD;
//        switch (info.getDeviceType()) {
//            case TRACK_2 /*2*/:
//                this.mDisplayHeight = KEY_EH_DUKPT_MASTER1;
//            default:
//        }
//    }
//
//    public static void setDebug(boolean on) {
//        DEBUG = on;
//    }
//
//    public void setRecvTimeout(int ms) {
//        this.mRecvTimeout = ms;
//    }
//
//    public void setPinpadListener(PinpadListener listener) {
//        this.mPinpadListener = listener;
//    }
//
//    private void raiseOnPinpadRelease() {
//        PinpadListener l = this.mPinpadListener;
//        if (l != null) {
//            l.onPinpadRelease();
//        }
//    }
//
//    public void setSmartCardListener(SmartCardListener listener) {
//        this.mSmartCardListener = listener;
//    }
//
//    private void raiseOnSmartCardInserted() {
//        SmartCardListener l = this.mSmartCardListener;
//        if (l != null) {
//            l.onSmartCardInserted();
//        }
//    }
//
//    private void raiseOnSmartCardRemoved() {
//        SmartCardListener l = this.mSmartCardListener;
//        if (l != null) {
//            l.onSmartCardRemoved();
//        }
//    }
//
//    public void setBarcodeListener(BarcodeListener listener) {
//        this.mBarcodeListener = listener;
//    }
//
//    private void raiseOnBarcodeRead() {
//        BarcodeListener l = this.mBarcodeListener;
//        if (l != null) {
//            l.onBarcodeRead();
//        }
//    }
//
//    public void setMagstripeListener(MagstripeListener listener) {
//        this.mMagstripeListener = listener;
//    }
//
//    private void raiseOnMagstripeRead() {
//        MagstripeListener l = this.mMagstripeListener;
//        if (l != null) {
//            l.onMagstripeRead();
//        }
//    }
//
//    public void setEmv2Listener(Emv2Listener listener) {
//        this.mEmv2Listener = listener;
//    }
//
//    private void raiseOnEmv2OnlineAuthorize(byte[] data) {
//        Emv2Listener l = this.mEmv2Listener;
//        if (l != null) {
//            l.onOnlineAuthorisationRequest(data);
//        }
//    }
//
//    private void raiseOnEmv2UpdateUserInterface(int message, int status, int hold) {
//        Emv2Listener l = this.mEmv2Listener;
//        if (l != null) {
//            l.onUpdateUserInterface(message, status, hold);
//        }
//    }
//
//    private void raiseOnEmv2TransactionFinish(byte[] data) {
//        Emv2Listener l = this.mEmv2Listener;
//        if (l != null) {
//            l.onTransactionFinish(data);
//        }
//    }
//
//    public int getDisplayWidth() {
//        return this.mDisplayWidth;
//    }
//
//    public int getDisplayHeight() {
//        return this.mDisplayHeight;
//    }
//
//    public synchronized void release() {
//        this.mLastError = new IOException("The object is released");
//        try {
//            this.mBaseInputStream.close();
//        } catch (IOException e) {
//        }
//        try {
//            this.mBaseOutputStream.close();
//        } catch (IOException e2) {
//        }
//    }
//
//    private static final String byteArrayToHexString(byte[] data, int offset, int length) {
//        char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//        char[] buf = new char[(length * SC_VOLTAGE_3)];
//        int i = TYPE_ICC;
//        int offs = TYPE_ICC;
//        while (i < length) {
//            int i2 = offs + TYPE_PIN;
//            buf[offs] = hex[(data[offset + i] >> TRACK_3) & ERROR_FLASH];
//            offs = i2 + TYPE_PIN;
//            buf[i2] = hex[(data[offset + i] >> TYPE_ICC) & ERROR_FLASH];
//            i2 = offs + TYPE_PIN;
//            buf[offs] = ' ';
//            i += TYPE_PIN;
//            offs = i2;
//        }
//        return new String(buf, TYPE_ICC, offs);
//    }
//
//    private static final String byteArrayToHexString(byte[] data) {
//        return byteArrayToHexString(data, TYPE_ICC, data.length);
//    }
//
//    private int read(byte[] b, int off, int len) throws IOException {
//        int nbr = this.mBaseInputStream.read(b, off, len);
//        if (nbr < 0) {
//            throw new IOException("The end of stream has been reached.");
//        }
//        if (DEBUG) {
//            System.out.println("read(" + nbr + "): " + byteArrayToHexString(b, off, nbr));
//        }
//        return nbr;
//    }
//
//    private void write(byte[] data) throws IOException {
//        this.mBaseOutputStream.write(data);
//        this.mBaseOutputStream.flush();
//        if (DEBUG) {
//            System.out.println("write(" + data.length + "): " + byteArrayToHexString(data));
//        }
//    }
//
//    private void writePacket(int command, byte[] data, int offset, int length) throws IOException {
//        byte[] buffer = new byte[((length + SC_VOLTAGE_5) + TYPE_PIN)];
//        int crc = TYPE_ICC;
//        int i = TYPE_ICC + TYPE_PIN;
//        buffer[TYPE_ICC] = (byte) 62;
//        int i2 = i + TYPE_PIN;
//        buffer[i] = (byte) command;
//        i = i2 + TYPE_PIN;
//        buffer[i2] = (byte) 0;
//        i2 = i + TYPE_PIN;
//        buffer[i] = (byte) (length >> KEY_KEK_DUKPT);
//        i = i2 + TYPE_PIN;
//        buffer[i2] = (byte) length;
//        if (data == null || length <= 0) {
//            i2 = i;
//        } else {
//            System.arraycopy(data, offset, buffer, i, length);
//            i2 = length + SC_VOLTAGE_5;
//        }
//        for (int i3 = TYPE_ICC; i3 < i2; i3 += TYPE_PIN) {
//            crc ^= buffer[i3] & EMV2_MESSAGE_UI_NA;
//        }
//        buffer[i2] = (byte) (crc & EMV2_MESSAGE_UI_NA);
//        write(buffer);
//    }
//
//    private void writePacket(int command, byte[] data) throws IOException {
//        writePacket(command, data, TYPE_ICC, data.length);
//    }
//
//    private byte[] readPacket(long ms) throws PinpadException, IOException {
//        byte[] tmp;
//        long endTime = System.currentTimeMillis() + ms;
//        while (this.mPacketBufferLen == 0) {
//            if (endTime < System.currentTimeMillis()) {
//                throw new IOException("Operation timeout expired");
//            }
//            synchronized (this.mPacketBuffer) {
//                try {
//                    this.mPacketBuffer.wait(10);
//                } catch (InterruptedException e) {
//                }
//            }
//        }
//        synchronized (this.mPacketBuffer) {
//            try {
//                int errCode = this.mPacketBuffer[TRACK_2] & EMV2_MESSAGE_UI_NA;
//                if (errCode != 0) {
//                    PinpadException e2 = new PinpadException(errCode);
//                    if (DEBUG) {
//                        System.out.println("throw PinpadException('" + e2.getMessage() + "')");
//                    }
//                    throw e2;
//                }
//                int length = (this.mPacketBuffer[SC_VOLTAGE_3] & EMV2_MESSAGE_UI_NA) + (this.mPacketBuffer[TRACK_3] & EMV2_MESSAGE_UI_NA);
//                tmp = new byte[length];
//                System.arraycopy(this.mPacketBuffer, SC_VOLTAGE_5, tmp, TYPE_ICC, length);
//                this.mPacketBufferLen = TYPE_ICC;
//            } catch (Throwable th) {
//                this.mPacketBufferLen = TYPE_ICC;
//            }
//        }
//        return tmp;
//    }
//
//    private void clearPacketBuffer() {
//        synchronized (this.mPacketBuffer) {
//            this.mPacketBufferLen = TYPE_ICC;
//        }
//    }
//
//    private synchronized byte[] executeCmd(int command, byte[] data, int offset, int length, int ms) throws PinpadException, IOException {
//        clearPacketBuffer();
//        writePacket(command, data, offset, length);
//        return readPacket((long) ms);
//    }
//
//    private byte[] executeCmd(int command, byte[] data, int ms) throws PinpadException, IOException {
//        return executeCmd(command, data, TYPE_ICC, data.length, ms);
//    }
//
//    private byte[] executeCmd(int command, byte[] data) throws PinpadException, IOException {
//        return executeCmd(command, data, this.mRecvTimeout);
//    }
//
//    private void postEvent(int event, byte[] data) {
//        new Thread(new 1(event, data)).start();
//    }
//
//    private void translateContactlessEvent(byte[] packet) {
//        if (packet.length >= TRACK_2) {
//            int event = packet[TYPE_ICC] & EMV2_MESSAGE_UI_NA;
//            byte[] data = new byte[(packet.length - 2)];
//            System.arraycopy(packet, TRACK_2, data, TYPE_ICC, data.length);
//            System.out.println("Translate event: " + event);
//            switch (event) {
//                case EMV2_EVENT_ONLINE_AUTHORIZATION /*129*/:
//                    raiseOnEmv2OnlineAuthorize(data);
//                case EMV2_EVENT_UPDATE_UI /*130*/:
//                    int message = TYPE_ICC;
//                    int status = ANIM_ALL;
//                    int hold = ANIM_ALL;
//                    if (data.length >= TRACK_2) {
//                        message = ((data[TYPE_ICC] & EMV2_MESSAGE_UI_NA) << KEY_KEK_DUKPT) + (data[TYPE_PIN] & EMV2_MESSAGE_UI_NA);
//                    }
//                    if (data.length >= SC_VOLTAGE_3) {
//                        status = data[TRACK_2] & EMV2_MESSAGE_UI_NA;
//                    }
//                    if (data.length >= SC_VOLTAGE_5) {
//                        hold = ((data[SC_VOLTAGE_3] & EMV2_MESSAGE_UI_NA) << KEY_KEK_DUKPT) + (data[TRACK_3] & EMV2_MESSAGE_UI_NA);
//                    }
//                    raiseOnEmv2UpdateUserInterface(message, status, hold);
//                case EMV2_EVENT_COMPLETE /*131*/:
//                    raiseOnEmv2TransactionFinish(data);
//                default:
//            }
//        }
//    }
//
//    public byte[] cryptoHash(int type, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        o.write(type);
//        o.write(data);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public void cryptoSaveKeysToFlash() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_5);
//        executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public byte[] crypt3DESECB(boolean decrypt, int key, byte[] vector, byte[] variant, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        o.write((decrypt ? TYPE_PIN : TYPE_ICC) + TYPE_ICC);
//        o.write(key);
//        o.write(vector);
//        o.write(variant);
//        o.write(data);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public byte[] crypt3DESCBC(boolean decrypt, int key, byte[] vector, byte[] variant, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        o.write((decrypt ? TYPE_PIN : TYPE_ICC) + TRACK_2);
//        o.write(key);
//        o.write(vector);
//        o.write(variant);
//        o.write(data);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public void cryptoDeleteKey(int key) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION1);
//        o.write(key);
//        executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public byte[] cryptoCheckKey(int key) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        o.write(key);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public byte[] crypto3DESCBCMAK(int mode, int key, byte[] vector, byte[] variant, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION3);
//        o.write(mode);
//        o.write(key);
//        o.write(vector);
//        o.write(variant);
//        o.write(data);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public byte[] cryptoExchangeECBKey(int kek, int key, int usage, int version, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ISO3);
//        o.write(kek);
//        o.write(key);
//        o.write(usage);
//        if (usage == 0) {
//            version = TYPE_ICC;
//        }
//        o.write(version);
//        o.write(TYPE_ICC);
//        o.write(data);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public byte[] cryptoExchangeCBCKey(int kek, int key, int usage, int version, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ISO3);
//        o.write(kek);
//        o.write(key);
//        o.write(usage);
//        if (usage == 0) {
//            version = TYPE_ICC;
//        }
//        o.write(version);
//        o.write(TYPE_PIN);
//        o.write(data);
//        return executeCmd(COMMAND_3DES, o.toByteArray());
//    }
//
//    public void caImportKey(int key, byte[] ridi, byte[] exponent, byte[] module) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_ICC);
//        o.write(key);
//        o.write(ridi);
//        o.write(exponent.length);
//        o.write(module.length);
//        o.write(exponent);
//        o.write(module);
//        executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public void caWriteKeysToFlash() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] caGetKeysData() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] caImportIssuerKey(int key, byte[] exponent, byte[] reminder, byte[] certificate) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        o.write(key);
//        o.write(exponent.length);
//        o.write(reminder.length);
//        o.write(certificate.length);
//        o.write(exponent);
//        o.write(reminder);
//        o.write(certificate);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] caImportICCKey(int type, byte[] exponent, byte[] reminder, byte[] certificate) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        o.write(type);
//        o.write(exponent.length);
//        o.write(reminder.length);
//        o.write(certificate.length);
//        o.write(exponent);
//        o.write(reminder);
//        o.write(certificate);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] caRSAVerify(int type, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_5);
//        o.write(data.length);
//        o.write(data);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] dukptGenerateKeyOnline(int key) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        o.write(key);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] dukptCalculateMACOnline(byte[] vector, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_SWEDISH);
//        o.write(vector);
//        o.write(data);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] dukptGetCurrentSerialOnline(int key) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_KEK_DUKPT);
//        o.write(key);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] dukptEncryptECBOnline(byte[] vector, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION1);
//        o.write(TYPE_ICC);
//        o.write(vector);
//        o.write(data);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public byte[] dukptEncryptCBCOnline(byte[] vector, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION1);
//        o.write(TRACK_2);
//        o.write(vector);
//        o.write(data);
//        return executeCmd(COMMAND_CA, o.toByteArray());
//    }
//
//    public void scInit(int slot, int voltage, int protocol) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        o.write(slot);
//        o.write(voltage);
//        o.write(protocol);
//        executeCmd(COMMAND_SMART_CARD, o.toByteArray());
//    }
//
//    public byte[] scCardPowerOn(int slot) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        o.write(slot);
//        return executeCmd(COMMAND_SMART_CARD, o.toByteArray());
//    }
//
//    public void scCardPowerOff(int slot) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        o.write(slot);
//        executeCmd(COMMAND_SMART_CARD, o.toByteArray());
//    }
//
//    public void scCheckCard(int slot) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        o.write(slot);
//        executeCmd(COMMAND_SMART_CARD, o.toByteArray());
//    }
//
//    public void scShutdown(int slot) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        executeCmd(COMMAND_SMART_CARD, o.toByteArray());
//    }
//
//    public void msStart() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//    }
//
//    public void msPull() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//    }
//
//    public byte[] msGetCardData3DESCBC(int keyId, int sessionId) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        o.write(EMVTags.TAG_87_APP_PRIORITY_INDICATOR);
//        o.write(keyId);
//        o.write(sessionId >> KEY_DUKPT);
//        o.write(sessionId >> KEY_EH_TMK_AES);
//        o.write(sessionId >> KEY_KEK_DUKPT);
//        o.write(sessionId);
//        return executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//    }
//
//    public byte[] msGetCardData3DESCBC(int mask, int keyId, int sessionId) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        o.write(mask);
//        o.write(keyId);
//        o.write(sessionId >> KEY_DUKPT);
//        o.write(sessionId >> KEY_EH_TMK_AES);
//        o.write(sessionId >> KEY_KEK_DUKPT);
//        o.write(sessionId);
//        return executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//    }
//
//    public byte[] msGetCardData3DESCBC(boolean smart, int keyId) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_PIN_LIMIT);
//        o.write(smart ? TYPE_PIN : TYPE_ICC);
//        o.write(keyId);
//        return executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//    }
//
//    public void msStop() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//    }
//
//    public String msGetCardholder() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ISO3);
//        return new String(executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray()));
//    }
//
//    public byte[] msGetMaskedCardData(int unmaskedDigitsAtStart, int unmaskedDigitsAtEnd, int unmaskedDigitsAfter) throws PinpadException, IOException {
//        if (unmaskedDigitsAtStart < 0 || unmaskedDigitsAtStart > LANG_FINISH) {
//            throw new IllegalArgumentException("The argument 'unmaskedDigitsAtStart' must be between 0 and 6");
//        } else if (unmaskedDigitsAtEnd < 0 || unmaskedDigitsAtEnd > TRACK_3) {
//            throw new IllegalArgumentException("The argument 'unmaskedDigitsAtEnd' must be between 0 and 4");
//        } else if (unmaskedDigitsAfter < 0 || unmaskedDigitsAfter > LANG_SWEDISH) {
//            throw new IllegalArgumentException("The argument 'unmaskedDigitsAfter' must be between 0 and 7");
//        } else {
//            ByteArrayOutputStream o = new ByteArrayOutputStream();
//            o.write(MAC_MODE_B);
//            o.write(unmaskedDigitsAtStart);
//            o.write(unmaskedDigitsAtEnd);
//            o.write(unmaskedDigitsAfter);
//            return executeCmd(COMMAND_MAGNETIC_CARD, o.toByteArray());
//        }
//    }
//
//    public void uiDrawString(byte[] text) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        o.write(text);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiDrawString(String text) throws PinpadException, IOException {
//        uiDrawString(text.getBytes());
//    }
//
//    public int uiReadKey() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        return executeCmd(COMMAND_IO, o.toByteArray())[TYPE_ICC] & EMV2_MESSAGE_UI_NA;
//    }
//
//    public void uiEnableNumericMode(int x, int y, int language, byte[] text) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        o.write(x);
//        o.write(y);
//        o.write(language);
//        o.write(text);
//        o.write(TYPE_ICC);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiDisableNumericMode() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiOpenTextWindow(int x, int y, int charsX, int charsY, int font, int codepage) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_5);
//        o.write(x);
//        o.write(y);
//        o.write(charsX);
//        o.write(charsY);
//        o.write(font);
//        o.write(codepage);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiKeyboardControl(boolean capture) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        o.write(capture ? TYPE_PIN : TYPE_ICC);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiSetKeyTone(int frequency, int duration, int volume) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_SWEDISH);
//        o.write(frequency >> KEY_KEK_DUKPT);
//        o.write(frequency);
//        o.write(duration >> KEY_KEK_DUKPT);
//        o.write(duration);
//        o.write(volume);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiEnterPin(boolean encrypted) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_KEK_DUKPT);
//        o.write(encrypted ? TYPE_PIN : TYPE_ICC);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiEnterPin(int x, int y, int timeout, char echo, byte[] message) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION1);
//        o.write(x);
//        o.write(y);
//        o.write(timeout);
//        o.write(echo);
//        o.write(message);
//        o.write(TYPE_ICC);
//        executeCmd(COMMAND_IO, o.toByteArray(), (timeout + TYPE_PIN) * IMAPStore.RESPONSE);
//    }
//
//    public void uiEnterPin(int x, int y, int timeout, char echo, String message) throws PinpadException, IOException {
//        uiEnterPin(x, y, timeout, echo, message.getBytes());
//    }
//
//    public void uiEnterPinAsync(int x, int y, int timeout, char echo, byte[] message, PINEntryCompleteListener l) throws PinpadException, IOException {
//        if (l == null) {
//            throw new NullPointerException("The parameter 'l' is null");
//        }
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_DUKPT_MASTER1);
//        o.write(x);
//        o.write(y);
//        o.write(timeout);
//        o.write(echo);
//        o.write(message);
//        o.write(TYPE_ICC);
//        executeCmd(COMMAND_IO, o.toByteArray());
//        this.mEnterPinActive = true;
//        this.mEnterPinThread = new Thread(new 2(l));
//        this.mEnterPinThread.start();
//    }
//
//    public void uiEnterPinAsync(int x, int y, int timeout, char echo, String message, PINEntryCompleteListener l) throws PinpadException, IOException {
//        uiEnterPinAsync(x, y, timeout, echo, message.getBytes(), l);
//    }
//
//    public void uiEnterPINCancel() throws PinpadException, IOException {
//        this.mEnterPinActive = false;
//    }
//
//    public int uiVerifyPINOffline(boolean encrypted) throws PinpadException, IOException {
//        int i;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        if (encrypted) {
//            i = TYPE_PIN;
//        } else {
//            i = TYPE_ICC;
//        }
//        o.write(i);
//        byte[] buf = executeCmd(COMMAND_IO, o.toByteArray());
//        return ((buf[TYPE_ICC] & EMV2_MESSAGE_UI_NA) << KEY_KEK_DUKPT) + (buf[TYPE_PIN] & EMV2_MESSAGE_UI_NA);
//    }
//
//    public byte[] uiVerifyPINOnline(int mode, int format, byte[] variant, int key, byte[] pan) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        o.write(TRACK_2);
//        o.write(mode);
//        o.write(format);
//        o.write(variant);
//        o.write(key);
//        if (format == SC_VOLTAGE_5) {
//            o.write("0000000000000".getBytes());
//        } else if (pan != null) {
//            o.write(pan);
//        }
//        return executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiFillScreen(int color) throws PinpadException, IOException {
//        uiFillRectangle(TYPE_ICC, TYPE_ICC, getDisplayWidth(), getDisplayHeight(), color);
//    }
//
//    public void uiFillRectangle(int x, int y, int width, int height, int color) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(MAC_MODE_B);
//        o.write(x);
//        o.write(y);
//        o.write(width);
//        o.write(height);
//        o.write(color);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiSetContrast(int contrast) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION3);
//        o.write(contrast);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiPutPixel(int x, int y, int color) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION3);
//        o.write(x);
//        o.write(y);
//        o.write(color);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiDisplayImage(int x, int y, int width, int height, int[] argb) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        byte[] buf = new byte[((getDisplayWidth() * (getDisplayHeight() + LANG_SWEDISH)) / KEY_KEK_DUKPT)];
//        int argbIndex = TYPE_ICC;
//        int j = TYPE_ICC;
//        int offs = -width;
//        while (j < height) {
//            int vbit = j % KEY_KEK_DUKPT;
//            int vbitMask = TYPE_PIN << vbit;
//            if (vbit == 0) {
//                offs += width;
//            }
//            int i = TYPE_ICC;
//            int argbIndex2 = argbIndex;
//            while (i < width) {
//                argbIndex = argbIndex2 + TYPE_PIN;
//                if ((16777215 & argb[argbIndex2]) == 0) {
//                    int i2 = offs + i;
//                    buf[i2] = (byte) (buf[i2] | vbitMask);
//                }
//                i += TYPE_PIN;
//                argbIndex2 = argbIndex;
//            }
//            j += TYPE_PIN;
//            argbIndex = argbIndex2;
//        }
//        o.write(ERROR_PIN_LIMIT);
//        o.write(x);
//        o.write(y);
//        o.write(width);
//        o.write(height);
//        o.write(buf);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiStartAnimation(int x, int y, int animation, boolean repeated) throws PinpadException, IOException {
//        int i = TYPE_PIN;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_TMK_AES);
//        o.write(TYPE_PIN);
//        o.write(animation);
//        o.write(x);
//        o.write(y);
//        if (!repeated) {
//            i = TYPE_ICC;
//        }
//        o.write(i);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiStopAnimation(int animation) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_TMK_AES);
//        if (animation == ANIM_ALL) {
//            o.write(SC_VOLTAGE_3);
//        } else {
//            o.write(TYPE_ICC);
//        }
//        o.write(animation);
//        o.write(TYPE_ICC);
//        o.write(TYPE_ICC);
//        o.write(TYPE_ICC);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiSetButtonCaption(int button, byte[] caption) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_CRC);
//        o.write(button);
//        o.write(caption);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public void uiSetButtonCaption(int button, String caption) throws PinpadException, IOException {
//        uiSetButtonCaption(button, caption.getBytes());
//    }
//
//    public void uiInitScreen() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(35);
//        executeCmd(COMMAND_IO, o.toByteArray());
//    }
//
//    public int uiEnterGratuity(int timeout, boolean decimalSeparator, int maxValue, int defValue) throws PinpadException, IOException {
//        int i;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        int timeoutInSeconds = (timeout + 999) / IMAPStore.RESPONSE;
//        o.write(37);
//        o.write(timeoutInSeconds);
//        if (decimalSeparator) {
//            i = TYPE_PIN;
//        } else {
//            i = TYPE_ICC;
//        }
//        o.write(i);
//        o.write(maxValue);
//        o.write(maxValue >> KEY_KEK_DUKPT);
//        o.write(maxValue >> KEY_EH_TMK_AES);
//        o.write(maxValue >> KEY_DUKPT);
//        o.write(defValue);
//        o.write(defValue >> KEY_KEK_DUKPT);
//        o.write(defValue >> KEY_EH_TMK_AES);
//        o.write(defValue >> KEY_DUKPT);
//        byte[] tmp = executeCmd(COMMAND_IO, o.toByteArray(), timeout + HttpStatus.SC_INTERNAL_SERVER_ERROR);
//        return (((tmp[TYPE_ICC] & EMV2_MESSAGE_UI_NA) + ((tmp[TYPE_PIN] & EMV2_MESSAGE_UI_NA) << KEY_KEK_DUKPT)) + ((tmp[TRACK_2] & EMV2_MESSAGE_UI_NA) << KEY_EH_TMK_AES)) + ((tmp[SC_VOLTAGE_3] & EMV2_MESSAGE_UI_NA) << KEY_DUKPT);
//    }
//
//    public Calendar getDate() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        byte[] buf = executeCmd(COMMAND_SYSTEM, o.toByteArray());
//        int year = (buf[TYPE_ICC] & EMV2_MESSAGE_UI_NA) + 2000;
//        int month = buf[TYPE_PIN] & EMV2_MESSAGE_UI_NA;
//        int day = buf[TRACK_2] & EMV2_MESSAGE_UI_NA;
//        int hour = buf[SC_VOLTAGE_3] & EMV2_MESSAGE_UI_NA;
//        int minute = buf[TRACK_3] & EMV2_MESSAGE_UI_NA;
//        int second = buf[SC_VOLTAGE_5] & EMV2_MESSAGE_UI_NA;
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, day, hour, minute, second);
//        return calendar;
//    }
//
//    public void sysSetDate(Calendar date) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        o.write(date.get(TYPE_PIN) - 2000);
//        o.write(date.get(TRACK_2) + TYPE_PIN);
//        o.write(date.get(SC_VOLTAGE_5));
//        o.write(date.get(MAC_MODE_B));
//        o.write(date.get(KEY_EH_AES128_ENCRYPTION3));
//        o.write(date.get(ISO3));
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysBeep(int frequency, int duration, int volume) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        o.write(frequency);
//        o.write(frequency >> KEY_KEK_DUKPT);
//        o.write(duration);
//        o.write(duration >> KEY_KEK_DUKPT);
//        o.write(volume / TRACK_2);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysLEDControl(int led, byte[] pattern) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        o.write(led);
//        o.write(pattern);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysStatusLine(boolean enable) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_5);
//        o.write(enable ? TYPE_PIN : TYPE_ICC);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysMelody(byte[] notes) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        o.write(notes);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysEraseFlashPage(int address) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_SWEDISH);
//        o.write(address);
//        o.write(address >> KEY_KEK_DUKPT);
//        o.write(address >> KEY_EH_TMK_AES);
//        o.write(address >> KEY_DUKPT);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysWriteFlashPage(int address, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_KEK_DUKPT);
//        o.write(address);
//        o.write(address >> KEY_KEK_DUKPT);
//        o.write(address >> KEY_EH_TMK_AES);
//        o.write(address >> KEY_DUKPT);
//        o.write(TYPE_ICC);
//        o.write(data);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public DeviceInfo getIdentification() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION1);
//        return DeviceInfo.parse(executeCmd(COMMAND_SYSTEM, o.toByteArray()));
//    }
//
//    public HardwareInfo sysGetHardwareInfo() throws PinpadException, IOException {
//        boolean charging;
//        boolean low;
//        boolean veryLow;
//        boolean external;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        byte[] buf = executeCmd(COMMAND_SYSTEM, o.toByteArray());
//        int capacity = buf[TYPE_ICC] & EMV2_MESSAGE_UI_NA;
//        if ((buf[TYPE_PIN] & TYPE_PIN) > 0) {
//            charging = true;
//        } else {
//            charging = false;
//        }
//        if ((buf[TYPE_PIN] & TRACK_2) > 0) {
//            low = true;
//        } else {
//            low = false;
//        }
//        if ((buf[TYPE_PIN] & TRACK_3) > 0) {
//            veryLow = true;
//        } else {
//            veryLow = false;
//        }
//        if ((buf[TYPE_PIN] & KEY_KEK_DUKPT) > 0) {
//            external = true;
//        } else {
//            external = false;
//        }
//        return new HardwareInfo(capacity, charging, low, veryLow, external, buf[TRACK_2] & EMV2_MESSAGE_UI_NA);
//    }
//
//    public void sysTurnOff() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(MAC_MODE_B);
//        writePacket(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    public void sysEnableEvents(int mask) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_TMK_AES);
//        o.write(mask);
//        o.write(mask >> KEY_KEK_DUKPT);
//        o.write(mask >> KEY_EH_TMK_AES);
//        o.write(mask >> KEY_DUKPT);
//        executeCmd(COMMAND_SYSTEM, o.toByteArray());
//    }
//
//    private byte[] executeEmvCmd(byte[] data) throws PinpadException, IOException {
//        byte[] buf = executeCmd(COMMAND_EMV2, data);
//        if (buf.length < TRACK_2) {
//            throw new PinpadException(TYPE_PIN);
//        }
//        this.mEMVLastStatus = (buf[TYPE_ICC] & EMV2_MESSAGE_UI_NA) + ((buf[TYPE_PIN] & EMV2_MESSAGE_UI_NA) >> KEY_KEK_DUKPT);
//        byte[] tmpBuf = new byte[(buf.length - 2)];
//        System.arraycopy(buf, TRACK_2, tmpBuf, TYPE_ICC, tmpBuf.length);
//        return tmpBuf;
//    }
//
//    public int emvGetLastStatus() {
//        return this.mEMVLastStatus;
//    }
//
//    public void emvInit() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvDeinitialise() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public EMVKernelInfo emvGetVersion() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        byte[] res = executeEmvCmd(o.toByteArray());
//        if (this.mEMVLastStatus != 0 || res.length < ERROR_PIN_LIMIT) {
//            return null;
//        }
//        String version = new String(res, TYPE_ICC, TRACK_3);
//        String release = new String(res, SC_VOLTAGE_5, TRACK_2);
//        int day = Integer.parseInt(new String(res, KEY_KEK_DUKPT, TRACK_2));
//        int month = Integer.parseInt(new String(res, ERROR_INVALID_KEY_NUMBER, TRACK_2));
//        int year = Integer.parseInt(new String(res, KEY_EH_AES128_ENCRYPTION3, TRACK_2));
//        Calendar date = Calendar.getInstance();
//        date.set(year, month, day);
//        return new EMVKernelInfo(version, release, date);
//    }
//
//    public void emvATRValidation(boolean warmReset, byte[] atr) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        o.write(warmReset ? TYPE_PIN : TYPE_ICC);
//        o.write(atr);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvLoadAppList(int method, boolean includeBlocked, EMVApplication[] applications) throws PinpadException, IOException {
//        int i;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_5);
//        o.write(method);
//        if (includeBlocked) {
//            i = TYPE_PIN;
//        } else {
//            i = TYPE_ICC;
//        }
//        o.write(i);
//        o.write(applications.length);
//        for (int i2 = TYPE_ICC; i2 < applications.length; i2 += TYPE_PIN) {
//            EMVApplication app = applications[i2];
//            o.write(app.getAID().length);
//            o.write(app.getAID());
//            o.write(app.getMatchCriteria());
//            o.write(app.getLabel());
//            o.write(TYPE_ICC);
//        }
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public EMVCommonApplications emvGetCommonAppList() throws PinpadException, IOException {
//        boolean confirmationRequired = true;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_FINISH);
//        byte[] res = executeEmvCmd(o.toByteArray());
//        if (this.mEMVLastStatus != TYPE_PIN && this.mEMVLastStatus != TRACK_2) {
//            return null;
//        }
//        if (res.length < TRACK_2) {
//            return null;
//        }
//        if (res[TYPE_ICC] == null) {
//            confirmationRequired = false;
//        }
//        List<EMVApplication> list = new ArrayList();
//        int offs = TRACK_2;
//        while (offs + TRACK_2 < res.length) {
//            int aidLength = res[offs] & EMV2_MESSAGE_UI_NA;
//            offs += TYPE_PIN;
//            byte[] aid = new byte[aidLength];
//            System.arraycopy(res, offs, aid, TYPE_ICC, aidLength);
//            offs += aidLength;
//            int labelLength = res[offs] & EMV2_MESSAGE_UI_NA;
//            offs += TYPE_PIN;
//            byte[] label = new byte[labelLength];
//            System.arraycopy(res, offs, label, TYPE_ICC, labelLength);
//            offs += labelLength;
//            list.add(new EMVApplication(aid, label, (int) TYPE_ICC));
//        }
//        EMVApplication[] applications = new EMVApplication[list.size()];
//        list.toArray(applications);
//        return new EMVCommonApplications(confirmationRequired, applications);
//    }
//
//    public void emvInitialAppProcessing(byte[] aid) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(LANG_SWEDISH);
//        o.write(aid);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvReadAppData(int[] tags) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_KEK_DUKPT);
//        o.write(TYPE_ICC);
//        for (int i = TYPE_ICC; i < tags.length; i += TYPE_PIN) {
//            int tag = tags[i];
//            o.write(tag);
//            o.write(tag >> KEY_KEK_DUKPT);
//        }
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvAuthentication(boolean checkAmount) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION1);
//        o.write(checkAmount ? TYPE_PIN : TYPE_ICC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvProcessRestrictions() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY_NUMBER);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvTerminalRisk(boolean forceProcessing) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(MAC_MODE_B);
//        o.write(forceProcessing ? TYPE_PIN : TYPE_ICC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvGetAuthenticationMethod() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_AES128_ENCRYPTION3);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvSetAuthenticationResult(int authResult) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ISO3);
//        o.write(authResult);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvVerifyPinOffline() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_PIN_LIMIT);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvGenerateCertificate(int type, int risk) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_FLASH);
//        o.write(type);
//        o.write(risk);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvMakeTransactionDecision() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_EH_TMK_AES);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvMakeDefaultDecision() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_CRC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvAuthenticateIssuer() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_DATA_ENC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvScriptProcessing(int script) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_DATA_DEC);
//        o.write(script);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvUpdateTVR(int byteIndex, int bitIndex, boolean set) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_PIN);
//        o.write(byteIndex);
//        o.write(bitIndex);
//        o.write(set ? TYPE_PIN : TYPE_ICC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvUpdateTSI(int byteIndex, int bitIndex, boolean set) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_MAC1);
//        o.write(byteIndex);
//        o.write(bitIndex);
//        o.write(set ? TYPE_PIN : TYPE_ICC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvCheckTVR(int byteIndex, int bitIndex) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_MAC3);
//        o.write(byteIndex);
//        o.write(bitIndex);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvCheckTSI(int byteIndex, int bitIndex) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_MAC1A);
//        o.write(byteIndex);
//        o.write(bitIndex);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvLoadPublicKey(int rid, int index, int method, int algorithm, byte[] module, byte[] exponent) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(KEY_DUKPT);
//        o.write(rid);
//        o.write(index);
//        o.write(method);
//        o.write(algorithm);
//        o.write(module.length);
//        o.write(module);
//        o.write(exponent.length);
//        o.write(exponent);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvRemovePublicKey(int rid, int caIndex) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_KEY);
//        o.write(rid);
//        o.write(caIndex);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvSetDataAsBinary(int tag, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_NO_PIN_DATA);
//        o.write(tag);
//        o.write(tag >> KEY_KEK_DUKPT);
//        o.write(data);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvSetDataAsString(int tag, byte[] data) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_REMINDER);
//        o.write(tag);
//        o.write(tag >> KEY_KEK_DUKPT);
//        o.write(data);
//        o.write(TYPE_ICC);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvSetDataAsString(int tag, String data) throws PinpadException, IOException {
//        emvSetDataAsString(tag, data.getBytes());
//    }
//
//    public byte[] emvGetDataAsBinary(int tag) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_NOT_INIT);
//        o.write(tag);
//        o.write(tag >> KEY_KEK_DUKPT);
//        byte[] res = executeEmvCmd(o.toByteArray());
//        if (this.mEMVLastStatus != 0) {
//            return null;
//        }
//        return res;
//    }
//
//    public String emvGetDataAsString(int tag) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_LIMIT);
//        o.write(tag);
//        o.write(tag >> KEY_KEK_DUKPT);
//        byte[] res = executeEmvCmd(o.toByteArray());
//        if (this.mEMVLastStatus != 0) {
//            return null;
//        }
//        return new String(res);
//    }
//
//    public EMVTagDetails emvGetDataDetails(int tag) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_INVALID_SEQUENCE);
//        o.write(tag);
//        o.write(tag >> KEY_KEK_DUKPT);
//        byte[] res = executeEmvCmd(o.toByteArray());
//        if (this.mEMVLastStatus == 0 && res.length >= LANG_FINISH) {
//            return new EMVTagDetails(res[TYPE_ICC] & EMV2_MESSAGE_UI_NA, (res[TRACK_2] & EMV2_MESSAGE_UI_NA) + ((res[SC_VOLTAGE_3] & EMV2_MESSAGE_UI_NA) << KEY_KEK_DUKPT), (res[TRACK_3] & EMV2_MESSAGE_UI_NA) + ((res[SC_VOLTAGE_5] & EMV2_MESSAGE_UI_NA) << KEY_KEK_DUKPT));
//        }
//        return null;
//    }
//
//    public void emvSetBypassMode(int mode) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(ERROR_NO_PERMITION);
//        o.write(mode);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public void emvSetTags(byte[] TLVList) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACKS_PREFIX);
//        o.write(TLVList);
//        executeEmvCmd(o.toByteArray());
//    }
//
//    public byte[] emvGetTags(byte[] tagList) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(EMV2_EVENT_ONLINE_AUTHORIZATION);
//        return executeEmvCmd(o.toByteArray());
//    }
//
//    public byte[] emvGetTags3DESCBC(int keyId, int id, byte[] tagList) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(EMV2_EVENT_UPDATE_UI);
//        o.write(keyId);
//        o.write(id >> KEY_DUKPT);
//        o.write(id >> KEY_EH_TMK_AES);
//        o.write(id >> KEY_KEK_DUKPT);
//        o.write(id);
//        o.write(tagList);
//        return executeEmvCmd(o.toByteArray());
//    }
//
//    public void barStart() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TYPE_PIN);
//        executeCmd(COMMAND_BARCODE, o.toByteArray());
//    }
//
//    public void barPull() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_2);
//        executeCmd(COMMAND_BARCODE, o.toByteArray());
//    }
//
//    public Barcode barGetBarcodeData() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(SC_VOLTAGE_3);
//        return Barcode.parsePinpad(executeCmd(COMMAND_BARCODE, o.toByteArray()));
//    }
//
//    public void barStop() throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(TRACK_3);
//        executeCmd(COMMAND_BARCODE, o.toByteArray());
//    }
//
//    private byte[] emv2Transmit(int command, byte[] input) throws PinpadException, IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(command);
//        o.write(TYPE_ICC);
//        o.write(input);
//        byte[] res = executeCmd(60, o.toByteArray());
//        byte[] tmp = new byte[(res.length - 2)];
//        System.arraycopy(res, TRACK_2, tmp, TYPE_ICC, tmp.length);
//        return tmp;
//    }
//
//    public void emv2LoadConfiguration(byte[] input) throws PinpadException, IOException {
//        emv2Transmit(TYPE_PIN, input);
//    }
//
//    public byte[] emv2GetEMVEngineInfo() throws PinpadException, IOException {
//        return emv2Transmit(TRACK_2, new byte[TYPE_ICC]);
//    }
//
//    public void emv2StartTransaction(byte[] input) throws PinpadException, IOException {
//        emv2Transmit(SC_VOLTAGE_3, input);
//    }
//
//    public void emv2SetOnlineResult(byte[] tags) throws PinpadException, IOException {
//        emv2Transmit(TRACK_3, tags);
//    }
//
//    public void emv2CancelTransaction() throws PinpadException, IOException {
//        emv2Transmit(SC_VOLTAGE_5, new byte[TYPE_ICC]);
//    }
//
//    public void emv2EnableDebug(boolean on) throws PinpadException, IOException {
//        if (on) {
//            emv2Transmit(113, new byte[TYPE_ICC]);
//        } else {
//            emv2Transmit(114, new byte[TYPE_ICC]);
//        }
//    }
//
//    public void emv2Initialize() throws PinpadException, IOException {
//        emv2Transmit(97, new byte[TYPE_ICC]);
//    }
//
//    public void emv2Deinitialise() throws PinpadException, IOException {
//        emv2Transmit(98, new byte[TYPE_ICC]);
//    }
//
//    public byte[] emv2GetTrackData(int format, int keyID) throws PinpadException, IOException {
//        byte[] bArr = new byte[TRACK_3];
//        bArr[TYPE_ICC] = (byte) (format >> KEY_DUKPT);
//        bArr[TYPE_PIN] = (byte) (format >> KEY_EH_TMK_AES);
//        bArr[TRACK_2] = (byte) (format >> KEY_KEK_DUKPT);
//        bArr[SC_VOLTAGE_3] = (byte) (format >> TYPE_ICC);
//        BerTlv berC1 = new BerTlv((int) PrivateTags.TAG_C1_PROCESSING_RESULT, bArr);
//        bArr = new byte[TRACK_3];
//        bArr[TYPE_ICC] = (byte) (keyID >> KEY_DUKPT);
//        bArr[TYPE_PIN] = (byte) (keyID >> KEY_EH_TMK_AES);
//        bArr[TRACK_2] = (byte) (keyID >> KEY_KEK_DUKPT);
//        bArr[SC_VOLTAGE_3] = (byte) (keyID >> TYPE_ICC);
//        BerTlv berC2 = new BerTlv((int) PrivateTags.TAG_C1_PROCESSING_RESULT, bArr);
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(berC1.toByteArray());
//        o.write(berC2.toByteArray());
//        return emv2Transmit(LANG_FINISH, o.toByteArray());
//    }
//
//    public byte[] emv2GetTagsEncrypted(int format, int keyID, byte[] tags) throws PinpadException, IOException {
//        byte[] bArr = new byte[TRACK_3];
//        bArr[TYPE_ICC] = (byte) (format >> KEY_DUKPT);
//        bArr[TYPE_PIN] = (byte) (format >> KEY_EH_TMK_AES);
//        bArr[TRACK_2] = (byte) (format >> KEY_KEK_DUKPT);
//        bArr[SC_VOLTAGE_3] = (byte) (format >> TYPE_ICC);
//        BerTlv berC1 = new BerTlv((int) PrivateTags.TAG_C1_PROCESSING_RESULT, bArr);
//        bArr = new byte[TRACK_3];
//        bArr[TYPE_ICC] = (byte) (keyID >> KEY_DUKPT);
//        bArr[TYPE_PIN] = (byte) (keyID >> KEY_EH_TMK_AES);
//        bArr[TRACK_2] = (byte) (keyID >> KEY_KEK_DUKPT);
//        bArr[SC_VOLTAGE_3] = (byte) (keyID >> TYPE_ICC);
//        BerTlv berC2 = new BerTlv((int) PrivateTags.TAG_C1_PROCESSING_RESULT, bArr);
//        BerTlv berC3 = new BerTlv((int) PrivateTags.TAG_C3_EMV_LIB_RESULT, tags);
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(berC1.toByteArray());
//        o.write(berC2.toByteArray());
//        o.write(berC3.toByteArray());
//        return emv2Transmit(LANG_SWEDISH, o.toByteArray());
//    }
//
//    public byte[] emv2GetTagsPlain(byte[] tags) throws PinpadException, IOException {
//        return emv2Transmit(KEY_KEK_DUKPT, new BerTlv((int) PrivateTags.TAG_C3_EMV_LIB_RESULT, tags).toByteArray());
//    }
}
