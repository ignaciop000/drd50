package com.datecs.pinpad.emv;

public class EMVApplication {
    public static final int FULL_MATCH = 1;
    public static final int PARTIAL_MATCH_EUROPAY = 3;
    public static final int PARTIAL_MATCH_VISA = 2;
    public static final int SELECTION_METHOD_AIDLIST = 1;
    public static final int SELECTION_METHOD_PSE = 0;
    private final byte[] mAID;
    private final byte[] mLabel;
    private final int mMatchCriteria;

    public EMVApplication(byte[] aid, byte[] label, int matchCriteria) {
        this.mAID = aid;
        this.mLabel = label;
        this.mMatchCriteria = matchCriteria;
    }

    public EMVApplication(byte[] aid, String label, int matchCriteria) {
        this(aid, label.getBytes(), matchCriteria);
    }

    public byte[] getAID() {
        return this.mAID;
    }

    public byte[] getLabel() {
        return this.mLabel;
    }

    public String getLabelString() {
        return new String(this.mLabel);
    }

    public int getMatchCriteria() {
        return this.mMatchCriteria;
    }
}
