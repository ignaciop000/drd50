package com.datecs.pinpad;

public class DeviceInfo {
    public static final int DEVICE_BP50 = 2;
    public static final int DEVICE_MPED400 = 1;
    public static final int DEVICE_PPAD1 = 3;
    public static final int DEVICE_UNKNOWN = 0;
    private String mApplicationName;
    private String mApplicationVersion;
    private String mCPULoaderVersion;
    private String mCPUSerialNumber;
    private String mDeviceSerialNumber;
    private String mEMVL1Version;
    private String mFirmwareName;
    private String mFirmwareVersion;
    private String mHardwareVersion;
    private String mLoaderName;
    private String mLoaderVersion;
    private String mPKAAVersion;
    private String mPKFAVersion;
    private String mPKKAVersion;
    private String mPromptVersion;

    private DeviceInfo() {
    }

    private static final String byteArrayToHexString(byte[] data, int offset, int length) {
        char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] buf = new char[(length * DEVICE_BP50)];
        int offs = 0;
        for (int i = 0; i < length; i += DEVICE_MPED400) {
            int i2 = offs + DEVICE_MPED400;
            buf[offs] = hex[(data[offset + i] >> 4) & 15];
            offs = i2 + DEVICE_MPED400;
            buf[i2] = hex[(data[offset + i] >> 0) & 15];
        }
        return new String(buf, 0, offs);
    }

    private static final String byteArrayToString(byte[] data, int offset, int length) {
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i += DEVICE_MPED400) {
            char c = (char) (data[offset + i] & Pinpad.EMV2_MESSAGE_UI_NA);
            if (c == '\u0000') {
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static final String byteArrayToStringVersion(byte[] data, int offset) {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + (data[offset + DEVICE_PPAD1] & Pinpad.EMV2_MESSAGE_UI_NA) + ".")).append(data[offset + DEVICE_BP50] & Pinpad.EMV2_MESSAGE_UI_NA).append(".").toString())).append(data[offset + DEVICE_MPED400] & Pinpad.EMV2_MESSAGE_UI_NA).append(".").toString())).append(data[offset] & Pinpad.EMV2_MESSAGE_UI_NA).toString();
    }

    static DeviceInfo parse(byte[] data) {
        DeviceInfo info = new DeviceInfo();
        info.mCPUSerialNumber = byteArrayToHexString(data, 0, 16);
        int pos = 0 + 16;
        info.mLoaderName = byteArrayToString(data, pos, 16);
        pos += 16;
        info.mFirmwareName = byteArrayToString(data, pos, 16);
        pos += 16;
        info.mApplicationName = byteArrayToString(data, pos, 16);
        pos += 16;
        info.mHardwareVersion = byteArrayToStringVersion(data, pos);
        pos = (pos + 4) + 4;
        info.mCPULoaderVersion = byteArrayToStringVersion(data, pos);
        pos = (pos + 4) + 4;
        info.mLoaderVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
        info.mFirmwareVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
        info.mApplicationVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
//        info.mDeviceSerialNumber = ((((0 + ((long) ((data[95] & Pinpad.EMV2_MESSAGE_UI_NA) << 24))) + ((long) ((data[94] & Pinpad.EMV2_MESSAGE_UI_NA) << 16))) + ((long) ((data[93] & Pinpad.EMV2_MESSAGE_UI_NA) << 8))) + ((long) (data[pos] & Pinpad.EMV2_MESSAGE_UI_NA)));
        pos = (((((pos + 4) + 4) + 4) + 4) + 4) + 4;
        info.mPromptVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
        info.mPKFAVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
        info.mPKAAVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
        info.mPKKAVersion = byteArrayToStringVersion(data, pos);
        pos += 4;
        return info;
    }

    public int getDeviceType() {
        String pattern = this.mFirmwareName.toLowerCase();
        if (pattern.startsWith("fw blupad")) {
            return DEVICE_BP50;
        }
        if (pattern.startsWith("fw mped")) {
            return DEVICE_MPED400;
        }
        if (pattern.startsWith("fw ppad")) {
            return DEVICE_PPAD1;
        }
        return 0;
    }

    public String getApplicationVersion() {
        return this.mApplicationVersion;
    }

    public String getApplicationName() {
        return this.mApplicationName;
    }

    public String getCPUSerialNumber() {
        return this.mCPUSerialNumber;
    }

    public String getDeviceSerialNumber() {
        return this.mDeviceSerialNumber;
    }

    public String getCPULoaderVersion() {
        return this.mCPULoaderVersion;
    }

    public String getLoaderName() {
        return this.mLoaderName;
    }

    public String getLoaderVersion() {
        return this.mLoaderVersion;
    }

    public String getFirmwareName() {
        return this.mFirmwareName;
    }

    public String getFirmwareVersion() {
        return this.mFirmwareVersion;
    }

    public String getHardwareVersion() {
        return this.mHardwareVersion;
    }

    public String getPromptVersion() {
        return this.mPromptVersion;
    }

    public String getEMVL1Version() {
        return this.mEMVL1Version;
    }

    public String getPKFAVersion() {
        return this.mPKFAVersion;
    }

    public String getPKAAVersion() {
        return this.mPKAAVersion;
    }

    public String getPKKAVersion() {
        return this.mPKKAVersion;
    }

    public String getKeyVersion() {
        return getPKAAVersion();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DeviceInfo ");
        sb.append("[DeviceType=" + Integer.toHexString(getDeviceType()));
        sb.append(",ApplicationVersion=" + getApplicationVersion());
        sb.append(",ApplicationVersionName=" + getApplicationName());
        sb.append(",CPUSerialNumber=" + getCPUSerialNumber());
        sb.append(",DeviceSerialNumber=" + getDeviceSerialNumber());
        sb.append(",KeyVersion=" + getKeyVersion());
        sb.append("]");
        return sb.toString();
    }
}
