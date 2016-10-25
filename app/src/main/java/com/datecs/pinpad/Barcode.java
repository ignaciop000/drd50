package com.datecs.pinpad;

import com.datecs.pinpad.emv.EMVStatusCodes;
//import com.estel.AREMV.EMVTags;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.HttpURL;

public class Barcode {
    public static final int BARCODE_ALL = 0;
    public static final int BARCODE_AZTEK = 31;
    public static final int BARCODE_CCA = 56;
    public static final int BARCODE_CCB = 57;
    public static final int BARCODE_CCC = 58;
    public static final int BARCODE_CODABAR = 2;
    public static final int BARCODE_CODABAR_ABC = 50;
    public static final int BARCODE_CODABAR_CX = 51;
    public static final int BARCODE_CODE11 = 8;
    public static final int BARCODE_CODE128 = 7;
    public static final int BARCODE_CODE25_I2OF5 = 4;
    public static final int BARCODE_CODE25_NI2OF5 = 3;
    public static final int BARCODE_CODE39 = 5;
    public static final int BARCODE_CODE39_FULL = 48;
    public static final int BARCODE_CODE93 = 6;
    public static final int BARCODE_CPCBINARY = 9;
    public static final int BARCODE_DATAMATRIX = 30;
    public static final int BARCODE_DUN14 = 10;
    public static final int BARCODE_EAN128 = 15;
    public static final int BARCODE_EAN13 = 14;
    public static final int BARCODE_EAN13_2 = 44;
    public static final int BARCODE_EAN13_5 = 45;
    public static final int BARCODE_EAN2 = 11;
    public static final int BARCODE_EAN5 = 12;
    public static final int BARCODE_EAN8 = 13;
    public static final int BARCODE_EAN8_2 = 46;
    public static final int BARCODE_EAN8_5 = 47;
    public static final int BARCODE_GS1DATABAR = 16;
    public static final int BARCODE_IATA = 54;
    public static final int BARCODE_INTELLIGENT_MAIL = 22;
    public static final int BARCODE_ITA_PHARMA = 49;
    public static final int BARCODE_ITF14 = 17;
    public static final int BARCODE_KOREAN_POSTAL = 55;
    public static final int BARCODE_LAST = 59;
    public static final int BARCODE_LATENT_IMAGE = 18;
    public static final int BARCODE_MATRIX_2OF5 = 53;
    public static final int BARCODE_MAXICODE = 33;
    public static final int BARCODE_MICROPDF417 = 29;
    public static final int BARCODE_MSI_PLESSEY = 23;
    public static final int BARCODE_PDF417 = 28;
    public static final int BARCODE_PHARMACODE = 19;
    public static final int BARCODE_PLANET = 20;
    public static final int BARCODE_POSTBAR = 24;
    public static final int BARCODE_POSTNET = 21;
    public static final int BARCODE_QRCODE = 32;
    public static final int BARCODE_RESERVED1 = 34;
    public static final int BARCODE_RESERVED2 = 35;
    public static final int BARCODE_RESERVED3 = 36;
    public static final int BARCODE_RESERVED4 = 37;
    public static final int BARCODE_RESERVED5 = 38;
    public static final int BARCODE_RM4SCC = 25;
    public static final int BARCODE_SCODE = 52;
    public static final int BARCODE_TELEPEN = 26;
    public static final int BARCODE_UK_PLESSEY = 27;
    public static final int BARCODE_UPCA = 1;
    public static final int BARCODE_UPCA_2 = 39;
    public static final int BARCODE_UPCA_5 = 40;
    public static final int BARCODE_UPCE = 41;
    public static final int BARCODE_UPCE_2 = 42;
    public static final int BARCODE_UPCE_5 = 43;
    private final byte[] mData;
    private final int mType;

    public Barcode(int type, byte[] data) {
        this.mType = type;
        this.mData = data;
    }

    public int getType() {
        return this.mType;
    }

    public String getName() {
        switch (this.mType) {
            case BARCODE_ALL /*0*/:
                return "ALL";
            case BARCODE_UPCA /*1*/:
                return "UPCA";
            case BARCODE_CODABAR /*2*/:
                return "CODABAR";
            case BARCODE_CODE25_NI2OF5 /*3*/:
                return "CODE25 NI2OF5";
            case BARCODE_CODE25_I2OF5 /*4*/:
                return "CODE25 I2OF5";
            case BARCODE_CODE39 /*5*/:
                return "CODE39";
            case BARCODE_CODE93 /*6*/:
                return "CODE93";
            case BARCODE_CODE128 /*7*/:
                return "CODE128";
            case BARCODE_CODE11 /*8*/:
                return "CODE11";
            case BARCODE_CPCBINARY /*9*/:
                return "CPC BINARY";
            case BARCODE_DUN14 /*10*/:
                return "CPC BINARY";
            case BARCODE_EAN2 /*11*/:
                return "EAN2";
            case BARCODE_EAN5 /*12*/:
                return "EAN5";
            case BARCODE_EAN8 /*13*/:
                return "EAN8";
            case BARCODE_EAN13 /*14*/:
                return "EAN13";
            case BARCODE_EAN128 /*15*/:
                return "EAN128";
            case BARCODE_GS1DATABAR /*16*/:
                return "GS1 DATABAR";
            case BARCODE_ITF14 /*17*/:
                return "ITF14";
            case BARCODE_LATENT_IMAGE /*18*/:
                return "LATENT IMAGE";
            case BARCODE_PHARMACODE /*19*/:
                return "PHARMA CODE";
            case BARCODE_PLANET /*20*/:
                return "PLANET";
            case BARCODE_POSTNET /*21*/:
                return "POSTNET";
            case BARCODE_INTELLIGENT_MAIL /*22*/:
                return "INTELLIGENT MAIL";
            case BARCODE_MSI_PLESSEY /*23*/:
                return "MSI PLESSEY";
            case BARCODE_POSTBAR /*24*/:
                return "POSTBAR";
            case BARCODE_RM4SCC /*25*/:
                return "RM4SCC";
            case BARCODE_TELEPEN /*26*/:
                return "TELEPEN";
            case BARCODE_UK_PLESSEY /*27*/:
                return "UK PLESSEY";
            case BARCODE_PDF417 /*28*/:
                return "PDF417";
            case BARCODE_MICROPDF417 /*29*/:
                return "MICRO PDF417";
            case BARCODE_DATAMATRIX /*30*/:
                return "DATAMATRIX";
            case BARCODE_AZTEK /*31*/:
                return "AZTEK";
            case BARCODE_QRCODE /*32*/:
                return "QRCODE";
            case BARCODE_MAXICODE /*33*/:
                return "MAXICODE";
            case BARCODE_RESERVED1 /*34*/:
                return "RESERVED1";
            case BARCODE_RESERVED2 /*35*/:
                return "RESERVED2";
            case BARCODE_RESERVED3 /*36*/:
                return "RESERVED3";
            case BARCODE_RESERVED4 /*37*/:
                return "RESERVED4";
            case BARCODE_RESERVED5 /*38*/:
                return "RESERVED5";
            case BARCODE_UPCA_2 /*39*/:
                return "UPCA 2";
            case BARCODE_UPCA_5 /*40*/:
                return "UPCA 5";
            case BARCODE_UPCE /*41*/:
                return "UPCE";
            case BARCODE_UPCE_2 /*42*/:
                return "UPCE 2";
            case BARCODE_UPCE_5 /*43*/:
                return "UPCE 5";
            case BARCODE_EAN13_2 /*44*/:
                return "EAN13 2";
            case BARCODE_EAN13_5 /*45*/:
                return "EAN13 5";
            case BARCODE_EAN8_2 /*46*/:
                return "EAN8 2";
            case BARCODE_EAN8_5 /*47*/:
                return "EAN8 5";
            case BARCODE_CODE39_FULL /*48*/:
                return "CODE39 FULL";
            case BARCODE_ITA_PHARMA /*49*/:
                return "ITA PHARMA";
            case BARCODE_CODABAR_ABC /*50*/:
                return "CODABAR ABC";
            case BARCODE_CODABAR_CX /*51*/:
                return "CODABAR CX";
            case BARCODE_SCODE /*52*/:
                return "SCODE";
            case BARCODE_MATRIX_2OF5 /*53*/:
                return "MATRIX 2OF5";
            case BARCODE_IATA /*54*/:
                return "IATA";
            case BARCODE_KOREAN_POSTAL /*55*/:
                return "KOREAN POSTAL";
            case BARCODE_CCA /*56*/:
                return "CCA";
            case BARCODE_CCB /*57*/:
                return "CCB";
            case BARCODE_CCC /*58*/:
                return "CCC";
            case BARCODE_LAST /*59*/:
                return "LAST";
            default:
                return "UNKNOWN";
        }
    }

    public byte[] getData() {
        return this.mData;
    }

    public String getDataString() {
        StringBuffer sb = new StringBuffer(this.mData.length);
        for (int i = BARCODE_ALL; i < this.mData.length; i += BARCODE_UPCA) {
            int value = this.mData[i] & Pinpad.EMV2_MESSAGE_UI_NA;
            if (Character.isISOControl((char) value)) {
                sb.append(".");
            } else {
                sb.append((char) value);
            }
        }
        return sb.toString();
    }

    public static Barcode parsePinpad(byte[] buf) {
        int type;
        switch (buf[BARCODE_ALL] & Pinpad.EMV2_MESSAGE_UI_NA) {
            case EMVStatusCodes.EMV_INVALID_KEY /*65*/:
                type = BARCODE_EAN8;
                break;
            case 66 /*66*/:
                type = BARCODE_EAN13;
                break;
            case EMVStatusCodes.EMV_ERROR_AC_PROCESS /*67*/:
                type = BARCODE_UPCA;
                break;
            case EMVStatusCodes.EMV_ERROR_AC_DENIED /*68*/:
                type = BARCODE_UPCE;
                break;
            case EMVStatusCodes.EMV_RESULT_ALREADY_LOADED /*70*/:
                type = BARCODE_UPCA_2;
                break;
            case 71:
                type = BARCODE_UPCA_5;
                break;
            case 72:
                type = BARCODE_UPCE_2;
                break;
            case 73:
                type = BARCODE_UPCE_5;
                break;
            case 74:
                type = BARCODE_EAN8_2;
                break;
            case 75:
                type = BARCODE_EAN8_5;
                break;
            case 76:
                type = BARCODE_EAN13_2;
                break;
            case 77:
                type = BARCODE_EAN13_5;
                break;
            case 78:
                type = BARCODE_CODE25_I2OF5;
                break;
            case 79 /*79*/:
                type = BARCODE_CODE25_NI2OF5;
                break;
            case 80 /*80*/:
                type = BARCODE_IATA;
                break;
            case EMVStatusCodes.EMV_INVALID_HEADER /*81*/:
                type = BARCODE_MATRIX_2OF5;
                break;
            case EMVStatusCodes.EMV_INVALID_FOOTER /*82*/:
                type = BARCODE_CODABAR;
                break;
            case EMVStatusCodes.EMV_INVALID_FORMAT /*83*/:
                type = BARCODE_CODABAR_ABC;
                break;
            case EMVStatusCodes.EMV_INVALID_CERTIFICATE /*84*/:
                type = BARCODE_CODE128;
                break;
            case EMVStatusCodes.EMV_INVALID_SIGNATURE /*85*/:
                type = BARCODE_CODE93;
                break;
            case 86:
                type = BARCODE_CODE39;
                break;
            case 87 /*87*/:
                type = BARCODE_CODE39_FULL;
                break;
            case 89:
                type = BARCODE_ITA_PHARMA;
                break;
            case 90/*90*/:
                type = BARCODE_MSI_PLESSEY;
                break;
            case 97:
                type = BARCODE_UK_PLESSEY;
                break;
            case 98:
                type = BARCODE_CODE11;
                break;
            case 99:
                type = BARCODE_KOREAN_POSTAL;
                break;
            case 100 /*100*/:
                type = BARCODE_TELEPEN;
                break;
            case 102 /*102*/:
                type = BARCODE_CODABAR_CX;
                break;
            case 103:
                type = BARCODE_SCODE;
                break;
            case 108:
                type = BARCODE_CCC;
                break;
            case 109:
                type = BARCODE_CCA;
                break;
            case 110:
                type = BARCODE_CCB;
                break;
            case 111:
                type = BARCODE_AZTEK;
                break;
            case 114:
                type = BARCODE_PDF417;
                break;
            case 115:
                type = BARCODE_MICROPDF417;
                break;
            case 116:
                type = BARCODE_DATAMATRIX;
                break;
            case 117:
                type = BARCODE_QRCODE;
                break;
            case 118:
                type = BARCODE_MAXICODE;
                break;
            case 121:
                type = BARCODE_GS1DATABAR;
                break;
            default:
                type = -1;
                break;
        }
        byte[] data = new byte[(buf.length - 1)];
        System.arraycopy(buf, BARCODE_UPCA, data, BARCODE_ALL, data.length);
        return new Barcode(type, data);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Barcode ");
        sb.append("[Name=" + getName());
        sb.append(",Data=" + getDataString());
        sb.append("]");
        return sb.toString();
    }
}
