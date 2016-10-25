package com.datecs.pinpad.emv;

import java.util.Calendar;

public class EMVKernelInfo {
    private final Calendar mDate;
    private final String mRelease;
    private final String mVersion;

    public EMVKernelInfo(String version, String release, Calendar date) {
        this.mVersion = version;
        this.mRelease = release;
        this.mDate = date;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public String getRelease() {
        return this.mRelease;
    }

    public Calendar getDate() {
        return this.mDate;
    }
}
