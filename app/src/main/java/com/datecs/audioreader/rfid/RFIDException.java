package com.datecs.audioreader.rfid;

import java.io.IOException;

public class RFIDException extends IOException {
    public static final int ERROR_FAILED = -20;
    public static final int ERROR_MIFARE_EACCESS = -10013;
    public static final int ERROR_MIFARE_EAUTHENTICATION = -10010;
    public static final int ERROR_MIFARE_EBASE = -10000;
    public static final int ERROR_MIFARE_EBIT = -10012;
    public static final int ERROR_MIFARE_ECODE = -10011;
    public static final int ERROR_MIFARE_ECOLLISION = -10002;
    public static final int ERROR_MIFARE_ECRC = -10005;
    public static final int ERROR_MIFARE_EEEPROM = -10007;
    public static final int ERROR_MIFARE_EFIFO = -10006;
    public static final int ERROR_MIFARE_EFRAME = -10004;
    public static final int ERROR_MIFARE_EGENERIC = -10009;
    public static final int ERROR_MIFARE_EKEY = -10008;
    public static final int ERROR_MIFARE_EPARITY = -10003;
    public static final int ERROR_MIFARE_ETIMEOUT = -10001;
    public static final int ERROR_MIFARE_EVALUE = -10014;
    public static final int ERROR_TIMEOUT = -6;
    private static final long serialVersionUID = 1;
    private int mErrorCode;

    public RFIDException(int errorCode) {
        super(getErrorAsString(errorCode));
        this.mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    private static String getErrorAsString(int errorCode) {
        switch (errorCode) {
            case ERROR_MIFARE_EVALUE /*-10014*/:
                return "Mifare value error";
            case ERROR_MIFARE_EACCESS /*-10013*/:
                return "Mifare access error";
            case ERROR_MIFARE_EBIT /*-10012*/:
                return "Mifare bit count error";
            case ERROR_MIFARE_ECODE /*-10011*/:
                return "Mifare code error";
            case ERROR_MIFARE_EAUTHENTICATION /*-10010*/:
                return "Mifare authentication error";
            case ERROR_MIFARE_EGENERIC /*-10009*/:
                return "Mifare generic error";
            case ERROR_MIFARE_EKEY /*-10008*/:
                return "Mifare invalid key";
            case ERROR_MIFARE_EEEPROM /*-10007*/:
                return "Mifare EEPROM error";
            case ERROR_MIFARE_EFIFO /*-10006*/:
                return "Mifare FIFO overflow";
            case ERROR_MIFARE_ECRC /*-10005*/:
                return "Mifare CRC error";
            case ERROR_MIFARE_EFRAME /*-10004*/:
                return "Mifare frame error";
            case ERROR_MIFARE_EPARITY /*-10003*/:
                return "Mifare parity error";
            case ERROR_MIFARE_ECOLLISION /*-10002*/:
                return "Mifare collision error";
            case ERROR_MIFARE_ETIMEOUT /*-10001*/:
                return "Mifare timeout error";
            case ERROR_MIFARE_EBASE /*-10000*/:
                return "Mifare operation successful";
            case ERROR_FAILED /*-20*/:
                return "Command failed";
            case ERROR_TIMEOUT /*-6*/:
                return "Timeout expired";
            default:
                return "Unspecified error code " + errorCode;
        }
    }
}
