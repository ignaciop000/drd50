package com.datecs.pinpad;

import com.datecs.pinpad.emv.EMVStatusCodes;
import java.io.IOException;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.bouncycastle.asn1.DERTags;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.crypto.params.DESedeParameters;

public class PinpadException extends IOException {
    private static final long serialVersionUID = -7567878786989356262L;
    private int mErrorCode;

    public PinpadException(int errorCode) {
        super(getErrorDesc(errorCode));
        this.mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    private static final String getErrorDesc(int errorCode) {
        switch (errorCode) {
            case CRLReason.UNSPECIFIED /*0*/:
                return "No error";
            case ReasonFlags.privilegeWithdrawn /*1*/:
                return "General error";
            case ReasonFlags.certificateHold /*2*/:
                return "Not valid command or sub command code";
            case CRLReason.affiliationChanged /*3*/:
                return "Invalid parameter";
            case ReasonFlags.cessationOfOperation /*4*/:
                return "The address is outside limits";
            case CRLReason.cessationOfOperation /*5*/:
                return "The value is outside limits";
            case CRLReason.certificateHold /*6*/:
                return "The length is outside limits";
            case GeneralName.iPAddress /*7*/:
                return "The action is not permit in current state";
            case ReasonFlags.superseded /*8*/:
                return "There is no data to be returned";
            case CRLReason.privilegeWithdrawn /*9*/:
                return "Timeout occurs";
            case CRLReason.aACompromise /*10*/:
                return "Invalid key number";
            case 11 /*11*/:
                return "Invalid key attributes(usage)";
            case 12 /*12*/:
                return "Calling of non-existing device";
            case EMVStatusCodes.EMV_TRANSACTION_ONLINE /*13*/:
                return "(Not used in this FW version)";
            case EMVStatusCodes.EMV_TRANSACTION_APPROVED /*14*/:
                return "Pin entering limit exceed";
            case EMVStatusCodes.EMV_TRANSACTION_DENIED /*15*/:
                return "General in flash commands";
            case ReasonFlags.affiliationChanged /*16*/:
                return "General hardware error";
            case DERTags.SET_OF /*17*/:
                return "(Not used in this FW version)";
            case DERTags.NUMERIC_STRING /*18*/:
                return "The button \"CANCEL\" is pressed";
            case DERTags.PRINTABLE_STRING /*19*/:
                return "Invalid signature";
            case 20 /*20*/:
                return "Invalid data in header";
            case DERTags.VIDEOTEX_STRING /*21*/:
                return "Incorrect password";
            case DERTags.IA5_STRING /*22*/:
                return "Invalid key format";
            case DERTags.UTC_TIME /*23*/:
                return "General error in smart card reader";
            case DESedeParameters.DES_EDE_KEY_LENGTH /*24*/:
                return "Error code returned from HAL functions";
            case DERTags.GRAPHIC_STRING /*25*/:
                return "Invalid key (may be no present)";
            case DERTags.VISIBLE_STRING /*26*/:
                return "The PIN length is <4 or >12";
            case DERTags.GENERAL_STRING /*27*/:
                return "Issuer or ICC key invalid remainder length";
            case DERTags.UNIVERSAL_STRING /*28*/:
                return "(Not used in this FW version)";
            case Pinpad.ERROR_LIMIT /*29*/:
                return "(Not used in this FW version)";
            case DERTags.BMP_STRING /*30*/:
                return "(Not used in this FW version)";
            case Pinpad.ERROR_NO_PERMITION /*31*/:
                return "The action is not permitted";
            case ReasonFlags.cACompromise /*32*/:
                return "TMK is not loaded. The action cannot be executed";
            case Pinpad.KEY_EH_DUKPT_MASTER2 /*33*/:
                return "Wrong key format";
            case Pinpad.KEY_EH_DUKPT_MASTER3 /*34*/:
                return "Duplicated key";
            default:
                return "Unspecified error code";
        }
    }
}
