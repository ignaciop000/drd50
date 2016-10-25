package com.datecs.pinpad;

public final class FinancialCard {
    private int mExpMonth;
    private int mExpYear;
    private String mName;
    private String mNumber;
    private String mServiceCode;

    public FinancialCard(String data) {
        if (data == null) {
            throw new NullPointerException("The data is null");
        } else if (data.startsWith("%B")) {
            int fi = data.indexOf(94);
            int li = data.lastIndexOf(94);
            if (fi > 2) {
                this.mNumber = data.substring(2, fi);
            }
            if (fi < li) {
                this.mName = data.substring(fi + 1, li).trim();
            }
            if (li > 0 && li + 7 < data.length()) {
                this.mExpYear = ((data.charAt(li + 1) & 15) * 10) + (data.charAt(li + 2) & 15);
                this.mExpMonth = ((data.charAt(li + 3) & 15) * 10) + (data.charAt(li + 4) & 15);
                this.mServiceCode = data.substring(li + 5, li + 8);
            }
        } else if (data.startsWith(";")) {
            int li = data.indexOf(61);
            if (li > 0 && li + 7 < data.length()) {
                this.mNumber = data.substring(1, li);
                this.mExpYear = ((data.charAt(li + 1) & 15) * 10) + (data.charAt(li + 2) & 15);
                this.mExpMonth = ((data.charAt(li + 3) & 15) * 10) + (data.charAt(li + 4) & 15);
                this.mServiceCode = data.substring(li + 5, li + 8);
            }
        }
    }

    public static FinancialCard parsePinpad(byte[] data) {
        StringBuffer track1 = new StringBuffer();
        StringBuffer track2 = new StringBuffer();
        int index = -1;
        for (byte b : data) {
            int value = b & Pinpad.EMV2_MESSAGE_UI_NA;
            if (value < 241) {
                switch (index) {
                    case 241:
                        track1.append((char) value);
                        break;
                    case 242:
                        track2.append((char) value);
                        break;
                    default:
                        break;
                }
            }
            index = value;
        }
        if (track1.length() > 0) {
            return new FinancialCard(track1.toString());
        }
        if (track2.length() > 0) {
            return new FinancialCard(track2.toString());
        }
        return null;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public String getName() {
        return this.mName;
    }

    public int getExpiryMonth() {
        return this.mExpMonth;
    }

    public int getExpiryYear() {
        return this.mExpYear;
    }

    public String getServiceCode() {
        return this.mServiceCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FinancialCard ");
        sb.append("[Number=" + this.mNumber);
        sb.append(",Name=" + this.mName);
        sb.append(",ExpiryMonth=" + this.mExpMonth);
        sb.append(",ExpiryYear=" + this.mExpYear);
        sb.append(",ServiceCode=" + this.mServiceCode);
        sb.append("]");
        return sb.toString();
    }
}
