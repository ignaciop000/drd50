package com.datecs.pinpad.emv;

public class EMVTagDetails {
    public static final int TAG_ASCIIZ = 2;
    public static final int TAG_BCD = 1;
    public static final int TAG_BINARY = 0;
    private final int mLength;
    private final int mMaxLength;
    private final int mType;

    public EMVTagDetails(int type, int maxLength, int length) {
        this.mType = type;
        this.mMaxLength = maxLength;
        this.mLength = length;
    }

    public int getType() {
        return this.mType;
    }

    public int getMaxLength() {
        return this.mMaxLength;
    }

    public int getLength() {
        return this.mLength;
    }
}
