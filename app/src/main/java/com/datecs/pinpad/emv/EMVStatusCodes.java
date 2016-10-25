package com.datecs.pinpad.emv;

public interface EMVStatusCodes {
    public static final int EMV_ABORT_TRANSACTION = 55;
    public static final int EMV_AMOUNT_NEEDED = 5;
    public static final int EMV_APPLICATION_AVAILABLE = 2;
    public static final int EMV_APPLICATION_NOT_FOUND = 56;
    public static final int EMV_AUTH_COMPLETED = 7;
    public static final int EMV_AUTH_NOT_DONE = 8;
    public static final int EMV_BLOCKED_APPLICATION = 12;
    public static final int EMV_CARD_BLOCKED = 59;
    public static final int EMV_CDA_FAILED = 16;
    public static final int EMV_DATA_FORMAT_ERROR = 53;
    public static final int EMV_EASY_ENTRY_APP = 4;
    public static final int EMV_ERROR_AC_DENIED = 68;
    public static final int EMV_ERROR_AC_PROCESS = 67;
    public static final int EMV_ERROR_IN_APPLICATION = 58;
    public static final int EMV_FAILURE = 50;
    public static final int EMV_INVALID_APPLICATION = 57;
    public static final int EMV_INVALID_ATR = 54;
    public static final int EMV_INVALID_CERTIFICATE = 84;
    public static final int EMV_INVALID_FOOTER = 82;
    public static final int EMV_INVALID_FORMAT = 83;
    public static final int EMV_INVALID_HASH = 64;
    public static final int EMV_INVALID_HEADER = 81;
    public static final int EMV_INVALID_KEY = 65;
    public static final int EMV_INVALID_LENGTH = 63;
    public static final int EMV_INVALID_PIN = 17;
    public static final int EMV_INVALID_PIN_LAST_ATTEMPT = 18;
    public static final int EMV_INVALID_REMAINDER = 80;
    public static final int EMV_INVALID_SIGNATURE = 85;
    public static final int EMV_INVALID_TAG = 62;
    public static final int EMV_LAST_EMVKERNEL_ERR_CODE = 70;
    public static final int EMV_LIST_AVAILABLE = 1;
    public static final int EMV_NO_COMMON_APPLICATION = 3;
    public static final int EMV_NO_CURRENT_METHOD = 69;
    public static final int EMV_NO_DATA_FOUND = 51;
    public static final int EMV_NO_MORE_KEYS = 66;
    public static final int EMV_NO_SCRIPT_LOADED = 60;
    public static final int EMV_OFFLINE_PIN_CIPHERED = 11;
    public static final int EMV_OFFLINE_PIN_PLAIN = 9;
    public static final int EMV_ONLINE_PIN = 10;
    public static final int EMV_RESULT_ALREADY_LOADED = 70;
    public static final int EMV_RESULT_NEEDED = 6;
    public static final int EMV_SUCCESS = 0;
    public static final int EMV_SYSTEM_ERROR = 52;
    public static final int EMV_TAG_NOT_FOUND = 61;
    public static final int EMV_TRANSACTION_APPROVED = 14;
    public static final int EMV_TRANSACTION_DENIED = 15;
    public static final int EMV_TRANSACTION_ONLINE = 13;
}
