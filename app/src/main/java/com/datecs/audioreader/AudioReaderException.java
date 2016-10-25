package com.datecs.audioreader;

//import com.datecs.pinpad.emv.EMVStatusCodes;
import java.io.IOException;
import org.bouncycastle.asn1.DERTags;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.ReasonFlags;

public class AudioReaderException extends IOException {
    private static final long serialVersionUID = 1;
    private int mStatusCode;

    public AudioReaderException(int status) {
        super(getDescription(status));
        this.mStatusCode = status;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }

    private static final String getDescription(int statusCode) {
        switch (statusCode) {
            case ReasonFlags.privilegeWithdrawn /*1*/:
                return "General error";
            case 2:
                return "Invalid command type";
            case CRLReason.affiliationChanged /*3*/:
                return "Invalid command number";
            case ReasonFlags.cessationOfOperation /*4*/:
                return "Invalid command data";
            case CRLReason.cessationOfOperation /*5*/:
                return "Card not enabled";
            case CRLReason.certificateHold /*6*/:
                return "Card not powered";
            case GeneralName.iPAddress /*7*/:
                return "Card not present";
            case ReasonFlags.superseded /*8*/:
                return "Card internal error";
            case CRLReason.privilegeWithdrawn /*9*/:
                return "Invalid command sequence";
            case CRLReason.aACompromise /*10*/:
                return "Invalid command length";
//            case CertStatus.UNREVOKED /*11*/:
//                return "Invalid name";
//            case CertStatus.UNDETERMINED /*12*/:
//                return "Invalid command format";
//            case EMVStatusCodes.EMV_TRANSACTION_ONLINE /*13*/:
//                return "Invalid address";
//            case EMVStatusCodes.EMV_TRANSACTION_APPROVED /*14*/:
//                return "Encryption error";
//            case EMVStatusCodes.EMV_TRANSACTION_DENIED /*15*/:
//                return "Signature error";
            case ReasonFlags.affiliationChanged /*16*/:
                return "Low battery";
            case DERTags.SET_OF /*17*/:
                return "Tamper";
            case DERTags.NUMERIC_STRING /*18*/:
                return "Invalid version";
            case DERTags.PRINTABLE_STRING /*19*/:
                return "Invalid key";
            default:
                return "Unknown status code " + statusCode;
        }
    }
}
