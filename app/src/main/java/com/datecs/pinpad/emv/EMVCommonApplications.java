package com.datecs.pinpad.emv;

public class EMVCommonApplications {
    private final EMVApplication[] mApplications;
    private final boolean mConfirmationRequired;

    public EMVCommonApplications(boolean confirmationRequired, EMVApplication[] applications) {
        this.mConfirmationRequired = confirmationRequired;
        this.mApplications = applications;
    }

    public boolean isConfirmationRequired() {
        return this.mConfirmationRequired;
    }

    public EMVApplication[] getApplications() {
        return this.mApplications;
    }
}
