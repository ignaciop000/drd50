package com.datecs.pinpad;

public class HardwareInfo {
    private final int mCapacity;
    private final boolean mCharging;
    private final boolean mExternal;
    private final boolean mLow;
    private final boolean mVeryLow;
    private final int mVoltage;

    public HardwareInfo(int capacity, boolean charging, boolean low, boolean veryLow, boolean external, int voltage) {
        this.mCapacity = capacity;
        this.mCharging = charging;
        this.mLow = low;
        this.mVeryLow = veryLow;
        this.mExternal = external;
        this.mVoltage = voltage;
    }

    public int getVoltage() {
        return this.mVoltage;
    }

    public int getCapacity() {
        return this.mCapacity;
    }

    public boolean isCharging() {
        return this.mCharging;
    }

    public boolean isLowBattery() {
        return this.mLow;
    }

    public boolean isVeryLowBattery() {
        return this.mVeryLow;
    }

    public boolean isExternalPower() {
        return this.mExternal;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("HardwareInfo ");
        sb.append("[Voltage=" + getVoltage());
        sb.append(",Capacity=" + getCapacity());
        sb.append(",isCharging=" + isCharging());
        sb.append(",isLowBattery=" + isLowBattery());
        sb.append(",isVeryLowBattery=" + isVeryLowBattery());
        sb.append(",isExternalPower=" + isExternalPower());
        sb.append("]");
        return sb.toString();
    }
}
