package com.datecs.audioreader.emv;

import com.datecs.audioreader.AudioReader;
import com.datecs.audioreader.AudioReaderException;
import com.datecs.pinpad.Barcode;
import com.datecs.pinpad.Pinpad;
//import com.estel.AREMV.EMVTags;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.spec.RSAPrivateKeySpec;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.httpclient.HttpStatus;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.crypto.signers.ISO9796d2Signer;
import org.bouncycastle.crypto.signers.PSSSigner;

public class EMVProcessor {
    private AudioReader mReader;
    private byte[] mSessionKey;

//    class 1 implements Runnable {
//        private final /* synthetic */ EMVProcessorCallback val$callback;
//        private final /* synthetic */ byte[] val$data;
//        private final /* synthetic */ Object[] val$result;
//
//        1(Object[] objArr, EMVProcessorCallback eMVProcessorCallback, byte[] bArr) {
//            this.val$result = objArr;
//            this.val$callback = eMVProcessorCallback;
//            this.val$data = bArr;
//        }
//
//        public void run() {
//            this.val$result[0] = this.val$callback.onPanCheckingRequest(this.val$data);
//        }
//    }
//
//    class 2 implements Runnable {
//        private final /* synthetic */ EMVProcessorCallback val$callback;
//        private final /* synthetic */ byte[] val$data;
//        private final /* synthetic */ Object[] val$result;
//
//        2(Object[] objArr, EMVProcessorCallback eMVProcessorCallback, byte[] bArr) {
//            this.val$result = objArr;
//            this.val$callback = eMVProcessorCallback;
//            this.val$data = bArr;
//        }
//
//        public void run() {
//            this.val$result[0] = this.val$callback.onCardHolderSelectionRequest(this.val$data);
//        }
//    }
//
//    class 3 implements Runnable {
//        private final /* synthetic */ EMVProcessorCallback val$callback;
//        private final /* synthetic */ byte[] val$data;
//        private final /* synthetic */ Object[] val$result;
//
//        3(Object[] objArr, EMVProcessorCallback eMVProcessorCallback, byte[] bArr) {
//            this.val$result = objArr;
//            this.val$callback = eMVProcessorCallback;
//            this.val$data = bArr;
//        }
//
//        public void run() {
//            this.val$result[0] = this.val$callback.onOnlineProcessingRequest(this.val$data);
//        }
//    }
//
//    class 4 implements Runnable {
//        private final /* synthetic */ EMVProcessorCallback val$callback;
//        private final /* synthetic */ byte[] val$data;
//
//        4(EMVProcessorCallback eMVProcessorCallback, byte[] bArr) {
//            this.val$callback = eMVProcessorCallback;
//            this.val$data = bArr;
//        }
//
//        public void run() {
//            this.val$callback.onConfirmOrReverseOnlineRequest(this.val$data);
//        }
//    }

//    public static class ConfigrationParametersResponse {
//        public int maxSize;
//        public int result;
//        public int state;
//        public int version;
//
//        public ConfigrationParametersResponse() {
//            this.version = -1;
//            this.maxSize = -1;
//        }
//
//        public String toString() {
//            return "ConfigrationParameters [result=" + this.result + ", state=" + this.state + ", version=" + this.version + ", maxSize=" + this.maxSize + "]";
//        }
//    }
//
//    public static class LoadParametersResponse {
//        public int dataReceived;
//        public int result;
//        public int state;
//
//        public String toString() {
//            return String.format(Locale.US, "LoadParametersResponse [result=0x%X, state=0x%X, dataReceived=%d]", new Object[]{Integer.valueOf(this.result), Integer.valueOf(this.state), Integer.valueOf(this.dataReceived)});
//        }
//    }
//
//    private static class Message {
//        public final byte[] data;
//        public final boolean encrypted;
//
//        public Message(boolean encrypted, boolean rfu, byte[] data) {
//            this.encrypted = encrypted;
//            this.data = data;
//        }
//
//        public byte[] toArray() {
//            int i;
//            byte[] buffer = new byte[(this.data.length + 2)];
//            if (this.encrypted) {
//                i = ReasonFlags.unused;
//            } else {
//                i = 0;
//            }
//            buffer[0] = (byte) (i | (this.data.length >> 8));
//            buffer[1] = (byte) (this.data.length & Pinpad.EMV2_MESSAGE_UI_NA);
//            System.arraycopy(this.data, 0, buffer, 2, this.data.length);
//            return buffer;
//        }
//
//        public static Message parse(byte[] data) {
//            boolean encrypted = true;
//            if (data.length < 2 || ((data[0] & 63) << 8) + (data[1] & Pinpad.EMV2_MESSAGE_UI_NA) != data.length - 2) {
//                throw new IllegalArgumentException("Invalid data content length");
//            }
//            if ((data[0] & ReasonFlags.unused) == 0) {
//                encrypted = false;
//            }
//            byte[] buffer = new byte[(data.length - 2)];
//            System.arraycopy(data, 2, buffer, 0, buffer.length);
//            return new Message(encrypted, false, buffer);
//        }
//    }
//
//    public static class ResetParametersResponse {
//        public final int maxSize;
//        public final int result;
//        public final int state;
//
//        private ResetParametersResponse(byte[] data) {
//            this.result = EMVProcessor.byteArrayToInt(data, 0, 2);
//            this.state = EMVProcessor.byteArrayToInt(data, 2, 1);
//            this.maxSize = EMVProcessor.byteArrayToInt(data, 3, 2);
//        }
//
//        public static ResetParametersResponse parse(byte[] data) {
//            return new ResetParametersResponse(data);
//        }
//
//        public String toString() {
//            return "ResetParametersResponse [result=" + this.result + ", state=" + this.state + ", maxSize=" + this.maxSize + "]";
//        }
//    }
//
//    public static class Response {
//        public final int cid;
//        public final byte[] data;
//        public final int reserved;
//
//        private Response(int cid, int reserved, byte[] data) {
//            this.cid = cid;
//            this.reserved = reserved;
//            this.data = data;
//        }
//
//        static Response parse(byte[] input) {
//            return new Response(EMVProcessor.byteArrayToInt(input, 0, 1), EMVProcessor.byteArrayToInt(input, 1, 1), EMVProcessor.byteArrayToSubArray(input, 4, EMVProcessor.byteArrayToInt(input, 2, 2)));
//        }
//
//        public String toString() {
//            return "Response [cid=" + this.cid + ", data(" + this.data.length + ")=" + EMVProcessor.byteArrayToHexString(this.data) + "]";
//        }
//    }
//
//    public static class TransactionResponse {
//        public static final int ABORTED = 2;
//        public static final int AUTHORIZED = 1;
//        public static final int AUTHORIZED_SIGNATURE = 129;
//        public static final int DECLINED = 0;
//        public static final int NOT_ACCEPTED = 3;
//        public final Map<Tag, byte[]> tags;
//
//        TransactionResponse(Map<Tag, byte[]> tags) {
//            this.tags = tags;
//        }
//
//        public byte[] getValue(Tag tag) {
//            if (this.tags.containsKey(tag)) {
//                return (byte[]) this.tags.get(tag);
//            }
//            return null;
//        }
//
//        public byte[] getValue(int tag) {
//            return getValue(new Tag(tag));
//        }
//
//        public int getProcessingResult() {
//            byte[] value = getValue((int) PrivateTags.TAG_C1_PROCESSING_RESULT);
//            if (value != null) {
//                return EMVProcessor.byteArrayToInt(value, DECLINED, value.length);
//            }
//            return -1;
//        }
//
//        public int getTransactonResult() {
//            byte[] value = getValue((int) HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
//            if (value != null) {
//                return EMVProcessor.byteArrayToInt(value, DECLINED, value.length);
//            }
//            return -1;
//        }
//
//        public String getTransactonResultDescription() {
//            int value = getTransactonResult();
//            switch (value) {
//                case DECLINED /*0*/:
//                    return "Declined";
//                case AUTHORIZED /*1*/:
//                    return "Authorized, no signature required";
//                case ABORTED /*2*/:
//                    return "Aborted";
//                case NOT_ACCEPTED /*3*/:
//                    return "Not Accepted";
//                case AUTHORIZED_SIGNATURE /*129*/:
//                    return "Authorized, signature required";
//                default:
//                    return "Unknown transaction result " + value;
//            }
//        }
//
//        public boolean isSignatureRequired() {
//            if (getTransactonResult() == AUTHORIZED_SIGNATURE) {
//                return true;
//            }
//            return false;
//        }
//
//        public String toString() {
//            return "TransactionResponse [processingResult=" + getProcessingResult() + ", transactionResult=" + getTransactonResultDescription();
//        }
//    }
//
    public EMVProcessor(AudioReader reader) throws AudioReaderException, IOException {
        this.mReader = reader;
//        this.mSessionKey = loadSessionKey();
    }
//
//    private static int byteArrayToInt(byte[] buf, int offset, int length) {
//        int result = 0;
//        int length2 = length;
//        int offset2 = offset;
//        while (true) {
//            length = length2 - 1;
//            if (length2 <= 0) {
//                return result;
//            }
//            result = (result << 8) + (buf[offset2] & Pinpad.EMV2_MESSAGE_UI_NA);
//            length2 = length;
//            offset2++;
//        }
//    }
//
//    private static byte[] byteArrayToSubArray(byte[] buf, int offset, int length) {
//        byte[] result = new byte[length];
//        System.arraycopy(buf, offset, result, 0, length);
//        return result;
//    }
//
//    private static final String byteArrayToHexString(byte[] data, int offset, int length) {
//        char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//        char[] buf = new char[(length * 2)];
//        int offs = 0;
//        for (int i = 0; i < length; i++) {
//            int i2 = offs + 1;
//            buf[offs] = hex[(data[offset + i] >> 4) & 15];
//            offs = i2 + 1;
//            buf[i2] = hex[(data[offset + i] >> 0) & 15];
//        }
//        return new String(buf, 0, offs);
//    }
//
//    private static final String byteArrayToHexString(byte[] data) {
//        return byteArrayToHexString(data, 0, data.length);
//    }
//
//    private static final byte[] encryptWithAES(byte[] keyValue, byte[] data) {
//        try {
//            SecretKey key = new SecretKeySpec(keyValue, "AES");
//            IvParameterSpec iv = new IvParameterSpec(new byte[16]);
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(1, key, iv);
//            return cipher.doFinal(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static final byte[] decryptWithAES(byte[] keyValue, byte[] data) {
//        try {
//            SecretKey key = new SecretKeySpec(keyValue, "AES");
//            IvParameterSpec iv = new IvParameterSpec(new byte[16]);
//            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            cipher.init(2, key, iv);
//            return cipher.doFinal(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static final byte[] decryptRSABlock(byte[] data) {
//        byte[] modBytes = new byte[PKIFailureInfo.unacceptedPolicy];
//        modBytes[0] = (byte) 119;
//        modBytes[1] = (byte) 108;
//        modBytes[2] = (byte) -47;
//        modBytes[3] = (byte) -17;
//        modBytes[4] = (byte) 98;
//        modBytes[5] = (byte) -23;
//        modBytes[6] = (byte) -115;
//        modBytes[7] = (byte) -113;
//        modBytes[8] = (byte) 25;
//        modBytes[9] = (byte) -77;
//        modBytes[10] = (byte) 79;
//        modBytes[11] = (byte) -38;
//        modBytes[12] = (byte) -46;
//        modBytes[13] = (byte) 65;
//        modBytes[14] = (byte) 28;
//        modBytes[15] = (byte) 122;
//        modBytes[16] = (byte) -57;
//        modBytes[17] = (byte) -24;
//        modBytes[18] = (byte) -39;
//        modBytes[19] = (byte) 93;
//        modBytes[20] = Tnaf.POW_2_WIDTH;
//        modBytes[21] = (byte) -44;
//        modBytes[22] = (byte) -12;
//        modBytes[23] = (byte) -91;
//        modBytes[24] = (byte) 104;
//        modBytes[25] = (byte) 38;
//        modBytes[26] = (byte) -86;
//        modBytes[27] = (byte) 44;
//        modBytes[28] = (byte) -50;
//        modBytes[29] = (byte) -114;
//        modBytes[30] = (byte) -93;
//        modBytes[31] = (byte) 60;
//        modBytes[32] = (byte) 5;
//        modBytes[33] = (byte) -22;
//        modBytes[34] = (byte) 27;
//        modBytes[35] = (byte) -127;
//        modBytes[36] = (byte) 106;
//        modBytes[37] = (byte) 57;
//        modBytes[38] = (byte) -34;
//        modBytes[39] = (byte) 52;
//        modBytes[40] = (byte) 123;
//        modBytes[41] = (byte) 35;
//        modBytes[42] = (byte) -59;
//        modBytes[43] = (byte) -26;
//        modBytes[44] = (byte) 37;
//        modBytes[45] = (byte) 80;
//        modBytes[46] = (byte) 115;
//        modBytes[47] = (byte) 85;
//        modBytes[48] = (byte) -40;
//        modBytes[49] = (byte) 63;
//        modBytes[50] = (byte) 63;
//        modBytes[51] = (byte) 51;
//        modBytes[52] = (byte) 46;
//        modBytes[53] = (byte) 91;
//        modBytes[54] = (byte) 40;
//        modBytes[55] = (byte) -71;
//        modBytes[56] = (byte) -2;
//        modBytes[57] = (byte) 76;
//        modBytes[58] = (byte) 64;
//        modBytes[59] = (byte) -86;
//        modBytes[60] = (byte) -46;
//        modBytes[61] = (byte) 64;
//        modBytes[62] = (byte) 26;
//        modBytes[63] = (byte) -28;
//        modBytes[64] = (byte) 55;
//        modBytes[65] = (byte) -123;
//        modBytes[66] = (byte) 51;
//        modBytes[67] = (byte) -121;
//        modBytes[68] = (byte) 50;
//        modBytes[69] = (byte) 70;
//        modBytes[70] = (byte) -82;
//        modBytes[71] = (byte) -50;
//        modBytes[72] = (byte) 84;
//        modBytes[73] = (byte) 3;
//        modBytes[74] = (byte) -46;
//        modBytes[75] = (byte) -83;
//        modBytes[76] = (byte) -118;
//        modBytes[77] = (byte) -32;
//        modBytes[78] = (byte) -81;
//        modBytes[79] = (byte) 39;
//        modBytes[80] = (byte) -58;
//        modBytes[81] = (byte) 3;
//        modBytes[82] = (byte) 124;
//        modBytes[83] = (byte) -49;
//        modBytes[84] = (byte) 120;
//        modBytes[85] = (byte) -106;
//        modBytes[86] = (byte) 23;
//        modBytes[87] = (byte) -11;
//        modBytes[88] = (byte) 90;
//        modBytes[89] = (byte) 45;
//        modBytes[90] = (byte) 56;
//        modBytes[91] = (byte) -108;
//        modBytes[92] = (byte) 40;
//        modBytes[93] = (byte) 43;
//        modBytes[94] = (byte) 111;
//        modBytes[95] = (byte) -39;
//        modBytes[96] = (byte) -20;
//        modBytes[97] = (byte) -96;
//        modBytes[98] = (byte) 124;
//        modBytes[99] = (byte) 95;
//        modBytes[100] = (byte) -34;
//        modBytes[HttpStatus.SC_SWITCHING_PROTOCOLS] = (byte) 32;
//        modBytes[HttpStatus.SC_PROCESSING] = (byte) -24;
//        modBytes[103] = (byte) 47;
//        modBytes[104] = (byte) -90;
//        modBytes[105] = (byte) 81;
//        modBytes[106] = (byte) 7;
//        modBytes[107] = (byte) -51;
//        modBytes[108] = (byte) -43;
//        modBytes[109] = (byte) -80;
//        modBytes[110] = (byte) -56;
//        modBytes[111] = (byte) -80;
//        modBytes[112] = (byte) 68;
//        modBytes[113] = (byte) -80;
//        modBytes[114] = (byte) 74;
//        modBytes[115] = (byte) -30;
//        modBytes[116] = (byte) 91;
//        modBytes[117] = (byte) -41;
//        modBytes[118] = (byte) -60;
//        modBytes[119] = (byte) -103;
//        modBytes[120] = (byte) 34;
//        modBytes[121] = (byte) -104;
//        modBytes[122] = (byte) -84;
//        modBytes[123] = (byte) -107;
//        modBytes[124] = (byte) 117;
//        modBytes[125] = (byte) -103;
//        modBytes[126] = (byte) 93;
//        modBytes[127] = (byte) -21;
//        modBytes[ReasonFlags.unused] = (byte) -69;
////        modBytes[EMVTags.TAG_81_AMOUNT_AUTHORISED_BINARY] = (byte) -105;
////        modBytes[EMVTags.TAG_82_AIP] = (byte) 34;
////        modBytes[AudioReader.KEY_TYPE_DES_KEK] = (byte) -126;
////        modBytes[EMVTags.TAG_84_DF_NAME] = (byte) -64;
////        modBytes[133] = (byte) -12;
////        modBytes[134] = (byte) 106;
////        modBytes[EMVTags.TAG_87_APP_PRIORITY_INDICATOR] = (byte) 78;
////        modBytes[136] = (byte) 14;
////        modBytes[EMVTags.TAG_89_AUTH_CODE] = (byte) 116;
////        modBytes[EMVTags.TAG_8A_AUTH_RESP_CODE] = (byte) -29;
////        modBytes[139] = (byte) -88;
////        modBytes[EMVTags.TAG_8C_CDOL_1] = (byte) 17;
////        modBytes[EMVTags.TAG_8D_CDOL_2] = (byte) 23;
////        modBytes[EMVTags.TAG_8E_CVM_LIST] = (byte) -70;
////        modBytes[EMVTags.TAG_8F_CA_PK_INDEX] = (byte) 15;
////        modBytes[EMVTags.TAG_90_ISSUER_PK_CERTIFICATE] = (byte) -47;
////        modBytes[EMVTags.TAG_91_ISSUER_AUTH_DAT] = (byte) 71;
////        modBytes[EMVTags.TAG_92_ISSUER_PK_REMAINDER] = (byte) 126;
////        modBytes[EMVTags.TAG_93_SIGNED_STA_APP_DAT] = (byte) 56;
////        modBytes[EMVTags.TAG_94_AFL] = (byte) -106;
////        modBytes[EMVTags.TAG_95_TVR] = (byte) -96;
////        modBytes[150] = (byte) -38;
////        modBytes[EMVTags.TAG_97_TDOL] = (byte) 95;
////        modBytes[EMVTags.TAG_98_TC_HASH_VALUE] = (byte) -103;
////        modBytes[153] = (byte) 27;
////        modBytes[EMVTags.TAG_9A_TRANSACTION_DATE] = (byte) 107;
////        modBytes[EMVTags.TAG_9B_TSI] = (byte) 104;
////        modBytes[EMVTags.TAG_9C_TRANSACTION_TYPE] = (byte) 118;
//        modBytes[157] = (byte) 70;
//        modBytes[158] = (byte) -100;
//        modBytes[159] = (byte) -19;
//        modBytes[160] = (byte) 106;
//        modBytes[161] = (byte) 95;
//        modBytes[162] = (byte) -29;
//        modBytes[163] = (byte) 58;
//        modBytes[164] = (byte) -96;
//        modBytes[165] = (byte) 3;
//        modBytes[166] = (byte) 93;
//        modBytes[167] = PSSSigner.TRAILER_IMPLICIT;
//        modBytes[168] = (byte) 39;
//        modBytes[169] = (byte) 43;
//        modBytes[170] = (byte) 69;
//        modBytes[171] = (byte) -63;
//        modBytes[172] = (byte) 41;
//        modBytes[173] = (byte) -70;
//        modBytes[174] = (byte) 109;
//        modBytes[175] = (byte) 107;
//        modBytes[176] = (byte) -16;
//        modBytes[177] = (byte) -65;
//        modBytes[178] = (byte) -118;
//        modBytes[179] = (byte) -109;
//        modBytes[180] = (byte) -69;
//        modBytes[181] = (byte) -100;
//        modBytes[182] = (byte) 52;
//        modBytes[183] = (byte) -74;
//        modBytes[184] = (byte) -79;
//        modBytes[185] = (byte) -55;
//        modBytes[186] = (byte) 51;
//        modBytes[187] = (byte) -56;
//        modBytes[ISO9796d2Signer.TRAILER_IMPLICIT] = (byte) 59;
//        modBytes[189] = (byte) 83;
//        modBytes[190] = (byte) -30;
//        modBytes[191] = (byte) -25;
//        modBytes[192] = (byte) 64;
//        modBytes[PrivateTags.TAG_C1_PROCESSING_RESULT] = (byte) -9;
//        modBytes[PrivateTags.TAG_C2_APPLICATION_LABEL_DEFAULT] = (byte) 48;
//        modBytes[PrivateTags.TAG_C3_EMV_LIB_RESULT] = (byte) 116;
//        modBytes[PrivateTags.TAG_C4_INITIATE_PROCESSING_FLAGS] = (byte) -104;
//        modBytes[PrivateTags.TAG_C5_SELECTED_INDEX] = (byte) -15;
//        modBytes[PrivateTags.TAG_C6_READER_STATE] = (byte) 125;
//        modBytes[PrivateTags.TAG_C7_ONLINE_AUTHORIZATION_PROCESSING_RESULT] = (byte) -75;
//        modBytes[DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE] = (byte) 96;
//        modBytes[HttpStatus.SC_CREATED] = (byte) 124;
//        modBytes[HttpStatus.SC_ACCEPTED] = (byte) 85;
//        modBytes[HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION] = (byte) 40;
//        modBytes[HttpStatus.SC_NO_CONTENT] = (byte) 115;
//        modBytes[HttpStatus.SC_RESET_CONTENT] = (byte) 25;
//        modBytes[HttpStatus.SC_PARTIAL_CONTENT] = (byte) 92;
//        modBytes[HttpStatus.SC_MULTI_STATUS] = (byte) 116;
//        modBytes[208] = (byte) 34;
//        modBytes[209] = (byte) -73;
//        modBytes[210] = (byte) -72;
//        modBytes[211] = (byte) 101;
//        modBytes[212] = (byte) -4;
//        modBytes[213] = (byte) -95;
//        modBytes[214] = (byte) -70;
//        modBytes[215] = (byte) 58;
//        modBytes[216] = (byte) -61;
//        modBytes[217] = (byte) 77;
//        modBytes[218] = (byte) 112;
//        modBytes[219] = (byte) 117;
//        modBytes[220] = (byte) -2;
//        modBytes[221] = (byte) -107;
//        modBytes[222] = (byte) 106;
//        modBytes[223] = (byte) -106;
//        modBytes[224] = (byte) 15;
//        modBytes[PrivateTags.TAG_E1_LIST_OF_CANDIDATES] = (byte) -62;
//        modBytes[PrivateTags.TAG_E2_BLOCKED_LIST] = (byte) 117;
//        modBytes[PrivateTags.TAG_E3_ISSUER_SCRIPTS] = (byte) -122;
//        modBytes[PrivateTags.TAG_E4_LIST_OF_CANDIDATES_ENTRY] = (byte) -79;
//        modBytes[229] = (byte) 38;
//        modBytes[231] = (byte) 7;
//        modBytes[232] = (byte) 32;
//        modBytes[233] = (byte) 2;
//        modBytes[234] = (byte) 53;
//        modBytes[235] = (byte) 80;
//        modBytes[236] = (byte) 35;
//        modBytes[237] = (byte) -96;
//        modBytes[238] = (byte) -108;
//        modBytes[239] = (byte) 71;
//        modBytes[240] = (byte) -57;
//        modBytes[241] = (byte) 29;
//        modBytes[242] = (byte) 79;
//        modBytes[243] = (byte) 114;
//        modBytes[244] = (byte) 119;
//        modBytes[245] = (byte) -66;
//        modBytes[246] = (byte) -86;
//        modBytes[247] = (byte) 107;
//        modBytes[248] = (byte) -86;
//        modBytes[249] = (byte) -5;
//        modBytes[250] = (byte) -36;
//        modBytes[251] = (byte) 40;
//        modBytes[252] = (byte) -74;
//        modBytes[253] = (byte) 72;
//        modBytes[254] = (byte) -31;
//        modBytes[Pinpad.EMV2_MESSAGE_UI_NA] = (byte) -57;
//        byte[] expBytes = new byte[PKIFailureInfo.unacceptedPolicy];
//        expBytes[0] = (byte) 80;
//        expBytes[1] = (byte) -101;
//        expBytes[2] = (byte) -9;
//        expBytes[3] = (byte) 40;
//        expBytes[4] = (byte) 42;
//        expBytes[5] = (byte) 15;
//        expBytes[6] = (byte) -109;
//        expBytes[7] = (byte) 41;
//        expBytes[8] = (byte) 96;
//        expBytes[9] = (byte) 35;
//        expBytes[10] = (byte) -108;
//        expBytes[11] = (byte) 103;
//        expBytes[12] = (byte) 19;
//        expBytes[13] = (byte) 60;
//        expBytes[14] = (byte) 55;
//        expBytes[15] = (byte) -56;
//        expBytes[16] = (byte) -8;
//        expBytes[17] = (byte) 94;
//        expBytes[18] = (byte) -57;
//        expBytes[19] = (byte) 56;
//        expBytes[20] = (byte) -10;
//        expBytes[21] = (byte) 63;
//        expBytes[22] = (byte) -121;
//        expBytes[23] = (byte) -46;
//        expBytes[24] = (byte) -115;
//        expBytes[25] = (byte) -10;
//        expBytes[26] = (byte) 107;
//        expBytes[27] = (byte) 47;
//        expBytes[28] = (byte) 75;
//        expBytes[29] = (byte) 77;
//        expBytes[30] = (byte) 36;
//        expBytes[31] = (byte) 9;
//        expBytes[32] = (byte) 67;
//        expBytes[33] = (byte) -60;
//        expBytes[34] = (byte) -67;
//        expBytes[35] = (byte) 68;
//        expBytes[36] = (byte) 33;
//        expBytes[37] = (byte) 59;
//        expBytes[38] = (byte) 102;
//        expBytes[39] = (byte) 44;
//        expBytes[40] = (byte) -18;
//        expBytes[41] = (byte) 97;
//        expBytes[42] = (byte) 59;
//        expBytes[43] = (byte) 23;
//        expBytes[44] = (byte) 25;
//        expBytes[45] = (byte) 96;
//        expBytes[46] = (byte) -80;
//        expBytes[47] = (byte) 56;
//        expBytes[48] = (byte) -27;
//        expBytes[49] = (byte) 121;
//        expBytes[50] = (byte) -21;
//        expBytes[51] = (byte) 98;
//        expBytes[52] = (byte) -44;
//        expBytes[53] = (byte) -117;
//        expBytes[54] = (byte) 91;
//        expBytes[55] = (byte) 118;
//        expBytes[56] = (byte) 15;
//        expBytes[57] = (byte) -101;
//        expBytes[58] = (byte) -48;
//        expBytes[59] = (byte) -102;
//        expBytes[60] = (byte) 124;
//        expBytes[61] = (byte) -56;
//        expBytes[62] = (byte) 32;
//        expBytes[63] = (byte) 94;
//        expBytes[64] = (byte) -94;
//        expBytes[65] = (byte) -53;
//        expBytes[66] = (byte) 25;
//        expBytes[67] = (byte) -8;
//        expBytes[68] = (byte) -53;
//        expBytes[69] = (byte) -118;
//        expBytes[70] = (byte) -62;
//        expBytes[71] = (byte) 59;
//        expBytes[72] = (byte) 42;
//        expBytes[73] = (byte) -94;
//        expBytes[74] = (byte) 89;
//        expBytes[75] = (byte) -10;
//        expBytes[76] = (byte) 33;
//        expBytes[77] = (byte) -93;
//        expBytes[78] = Byte.MAX_VALUE;
//        expBytes[79] = (byte) 22;
//        expBytes[80] = (byte) -51;
//        expBytes[81] = (byte) -91;
//        expBytes[82] = (byte) 84;
//        expBytes[83] = (byte) -3;
//        expBytes[84] = (byte) -123;
//        expBytes[85] = (byte) 91;
//        expBytes[86] = (byte) 106;
//        expBytes[87] = (byte) 88;
//        expBytes[88] = (byte) -123;
//        expBytes[89] = (byte) -63;
//        expBytes[90] = (byte) -72;
//        expBytes[91] = (byte) 74;
//        expBytes[92] = (byte) -24;
//        expBytes[93] = (byte) -62;
//        expBytes[94] = (byte) 73;
//        expBytes[95] = (byte) 1;
//        expBytes[96] = (byte) 67;
//        expBytes[97] = (byte) -93;
//        expBytes[98] = (byte) 31;
//        expBytes[99] = (byte) -48;
//        expBytes[100] = (byte) 101;
//        expBytes[HttpStatus.SC_SWITCHING_PROTOCOLS] = (byte) -46;
//        expBytes[HttpStatus.SC_PROCESSING] = (byte) -72;
//        expBytes[103] = (byte) 102;
//        expBytes[104] = (byte) 81;
//        expBytes[105] = (byte) 80;
//        expBytes[106] = (byte) -88;
//        expBytes[107] = Byte.MAX_VALUE;
//        expBytes[108] = (byte) -37;
//        expBytes[109] = (byte) 25;
//        expBytes[110] = (byte) 52;
//        expBytes[111] = (byte) -99;
//        expBytes[112] = (byte) 38;
//        expBytes[114] = (byte) 8;
//        expBytes[115] = (byte) -53;
//        expBytes[116] = (byte) -71;
//        expBytes[117] = (byte) 74;
//        expBytes[118] = (byte) 110;
//        expBytes[119] = (byte) -67;
//        expBytes[120] = (byte) 30;
//        expBytes[121] = (byte) -119;
//        expBytes[122] = (byte) 7;
//        expBytes[123] = (byte) 20;
//        expBytes[124] = (byte) -21;
//        expBytes[125] = (byte) 7;
//        expBytes[126] = (byte) -42;
//        expBytes[127] = (byte) 72;
//        expBytes[ReasonFlags.unused] = (byte) 109;
////        expBytes[EMVTags.TAG_81_AMOUNT_AUTHORISED_BINARY] = (byte) 17;
////        expBytes[EMVTags.TAG_82_AIP] = (byte) -88;
////        expBytes[AudioReader.KEY_TYPE_DES_KEK] = (byte) 20;
////        expBytes[EMVTags.TAG_84_DF_NAME] = (byte) -61;
////        expBytes[133] = (byte) -77;
////        expBytes[134] = (byte) 20;
////        expBytes[EMVTags.TAG_87_APP_PRIORITY_INDICATOR] = (byte) 42;
////        expBytes[136] = (byte) 99;
////        expBytes[EMVTags.TAG_89_AUTH_CODE] = (byte) 99;
////        expBytes[EMVTags.TAG_8A_AUTH_RESP_CODE] = (byte) 81;
////        expBytes[139] = (byte) 12;
////        expBytes[EMVTags.TAG_8C_CDOL_1] = (byte) -11;
////        expBytes[EMVTags.TAG_8D_CDOL_2] = (byte) -109;
////        expBytes[EMVTags.TAG_8E_CVM_LIST] = (byte) 28;
////        expBytes[EMVTags.TAG_8F_CA_PK_INDEX] = (byte) -108;
////        expBytes[EMVTags.TAG_90_ISSUER_PK_CERTIFICATE] = (byte) 115;
////        expBytes[EMVTags.TAG_91_ISSUER_AUTH_DAT] = (byte) -45;
////        expBytes[EMVTags.TAG_92_ISSUER_PK_REMAINDER] = (byte) 123;
////        expBytes[EMVTags.TAG_93_SIGNED_STA_APP_DAT] = (byte) 33;
////        expBytes[EMVTags.TAG_94_AFL] = (byte) -93;
////        expBytes[EMVTags.TAG_95_TVR] = (byte) -25;
////        expBytes[150] = (byte) 87;
////        expBytes[EMVTags.TAG_97_TDOL] = (byte) 34;
////        expBytes[EMVTags.TAG_98_TC_HASH_VALUE] = (byte) -63;
////        expBytes[153] = (byte) 96;
////        expBytes[EMVTags.TAG_9A_TRANSACTION_DATE] = (byte) 98;
////        expBytes[EMVTags.TAG_9B_TSI] = (byte) -71;
////        expBytes[EMVTags.TAG_9C_TRANSACTION_TYPE] = (byte) -70;
////        expBytes[157] = (byte) 46;
////        expBytes[158] = (byte) 64;
////        expBytes[159] = (byte) -4;
////        expBytes[160] = (byte) -13;
////        expBytes[161] = (byte) 53;
////        expBytes[162] = (byte) 31;
////        expBytes[163] = (byte) -20;
////        expBytes[164] = (byte) 124;
////        expBytes[165] = Tnaf.POW_2_WIDTH;
////        expBytes[166] = (byte) -25;
////        expBytes[167] = (byte) 39;
////        expBytes[168] = (byte) 19;
////        expBytes[169] = (byte) 28;
////        expBytes[170] = (byte) 50;
////        expBytes[171] = (byte) 76;
////        expBytes[172] = (byte) -95;
////        expBytes[173] = (byte) 88;
////        expBytes[174] = (byte) -123;
////        expBytes[175] = (byte) 93;
////        expBytes[176] = (byte) -21;
////        expBytes[177] = (byte) 13;
////        expBytes[178] = (byte) -4;
////        expBytes[179] = (byte) -111;
////        expBytes[180] = (byte) 91;
////        expBytes[181] = (byte) -44;
////        expBytes[182] = (byte) -1;
////        expBytes[183] = (byte) 70;
////        expBytes[184] = (byte) -12;
////        expBytes[185] = (byte) -113;
////        expBytes[186] = (byte) 42;
////        expBytes[187] = (byte) -104;
////        expBytes[ISO9796d2Signer.TRAILER_IMPLICIT] = (byte) 5;
////        expBytes[189] = (byte) 29;
////        expBytes[190] = (byte) -26;
////        expBytes[191] = (byte) -82;
////        expBytes[192] = (byte) -52;
////        expBytes[PrivateTags.TAG_C1_PROCESSING_RESULT] = (byte) 36;
////        expBytes[PrivateTags.TAG_C2_APPLICATION_LABEL_DEFAULT] = (byte) -2;
////        expBytes[PrivateTags.TAG_C3_EMV_LIB_RESULT] = (byte) -41;
////        expBytes[PrivateTags.TAG_C4_INITIATE_PROCESSING_FLAGS] = (byte) -52;
////        expBytes[PrivateTags.TAG_C5_SELECTED_INDEX] = (byte) 64;
////        expBytes[PrivateTags.TAG_C6_READER_STATE] = (byte) -25;
////        expBytes[PrivateTags.TAG_C7_ONLINE_AUTHORIZATION_PROCESSING_RESULT] = (byte) -7;
////        expBytes[DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE] = (byte) 34;
////        expBytes[HttpStatus.SC_CREATED] = (byte) -48;
////        expBytes[HttpStatus.SC_ACCEPTED] = (byte) 2;
////        expBytes[HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION] = (byte) 41;
////        expBytes[HttpStatus.SC_NO_CONTENT] = (byte) -91;
////        expBytes[HttpStatus.SC_RESET_CONTENT] = (byte) 101;
////        expBytes[HttpStatus.SC_PARTIAL_CONTENT] = (byte) 122;
////        expBytes[HttpStatus.SC_MULTI_STATUS] = (byte) 84;
//        expBytes[208] = (byte) -102;
//        expBytes[209] = (byte) -37;
//        expBytes[210] = (byte) -52;
//        expBytes[211] = (byte) 76;
//        expBytes[212] = (byte) -125;
//        expBytes[213] = (byte) 89;
//        expBytes[214] = (byte) -42;
//        expBytes[215] = (byte) -37;
//        expBytes[216] = (byte) -24;
//        expBytes[217] = (byte) -116;
//        expBytes[218] = (byte) -39;
//        expBytes[219] = (byte) -31;
//        expBytes[220] = (byte) 117;
//        expBytes[221] = (byte) 87;
//        expBytes[222] = (byte) 67;
//        expBytes[223] = (byte) -85;
//        expBytes[224] = (byte) -36;
//        expBytes[PrivateTags.TAG_E1_LIST_OF_CANDIDATES] = (byte) 102;
//        expBytes[PrivateTags.TAG_E2_BLOCKED_LIST] = Byte.MIN_VALUE;
//        expBytes[PrivateTags.TAG_E3_ISSUER_SCRIPTS] = (byte) -95;
//        expBytes[PrivateTags.TAG_E4_LIST_OF_CANDIDATES_ENTRY] = (byte) -99;
//        expBytes[229] = (byte) -101;
//        expBytes[230] = (byte) 91;
//        expBytes[231] = PSSSigner.TRAILER_IMPLICIT;
//        expBytes[232] = (byte) -79;
//        expBytes[233] = (byte) 12;
//        expBytes[234] = (byte) -124;
//        expBytes[235] = (byte) 48;
//        expBytes[236] = (byte) -33;
//        expBytes[237] = (byte) -111;
//        expBytes[238] = (byte) 72;
//        expBytes[239] = (byte) -92;
//        expBytes[240] = (byte) -93;
//        expBytes[241] = (byte) 93;
//        expBytes[242] = (byte) -7;
//        expBytes[243] = (byte) -17;
//        expBytes[244] = (byte) -97;
//        expBytes[245] = (byte) -1;
//        expBytes[246] = (byte) 40;
//        expBytes[247] = (byte) -91;
//        expBytes[248] = (byte) -87;
//        expBytes[249] = (byte) 40;
//        expBytes[251] = (byte) -2;
//        expBytes[252] = (byte) -38;
//        expBytes[253] = (byte) -48;
//        expBytes[254] = (byte) 74;
//        expBytes[Pinpad.EMV2_MESSAGE_UI_NA] = (byte) -31;
//        byte[] result = null;
//        try {
//            BigInteger modulus = new BigInteger(1, modBytes);
//            BigInteger exponent = new BigInteger(1, expBytes);
//            KeyFactory factory = KeyFactory.getInstance("RSA");
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(2, factory.generatePrivate(new RSAPrivateKeySpec(modulus, exponent)));
//            result = cipher.doFinal(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private synchronized byte[] processMessage(byte[] data) throws AudioReaderException, IOException {
//        Throwable th;
//        try {
//            ByteBuffer buffer = ByteBuffer.allocate(data.length + 2);
//            int crc = AudioReader.crcccit(data, 0, data.length);
//            buffer.put(data);
//            buffer.put((byte) (crc >> 8));
//            buffer.put((byte) crc);
//            byte[] enc = encryptWithAES(this.mSessionKey, buffer.array());
//            if (enc == null) {
//                throw new RuntimeException("Error in data encryption");
//            }
//            Message input = new Message(true, false, enc);
//            try {
//                Message output = Message.parse(this.mReader.processMessage(input.toArray()));
//                if (output.encrypted != input.encrypted) {
//                    throw new RuntimeException("Invalid response encryption mode");
//                }
//                byte[] buffer2;
//                if (output.encrypted) {
//                    byte[] dec = decryptWithAES(this.mSessionKey, output.data);
//                    int length = ((dec[2] & Pinpad.EMV2_MESSAGE_UI_NA) << 8) + (dec[3] & Pinpad.EMV2_MESSAGE_UI_NA);
//                    if (dec.length < (length + 4) + 2) {
//                        throw new RuntimeException("Invalid content length");
//                    } else if (AudioReader.crcccit(dec, 0, length + 4) != ((dec[length + 4] & Pinpad.EMV2_MESSAGE_UI_NA) << 8) + (dec[length + 5] & Pinpad.EMV2_MESSAGE_UI_NA)) {
//                        throw new RuntimeException("Invalid content checksum");
//                    } else {
//                        buffer2 = new byte[(length + 4)];
//                        System.arraycopy(dec, 0, buffer2, 0, buffer2.length);
//                    }
//                } else {
//                    buffer2 = output.data;
//                }
//                return buffer2;
//            } catch (Throwable th2) {
//                th = th2;
//                Message message = input;
//                throw th;
//            }
//        } catch (Throwable th3) {
//            th = th3;
//            throw th;
//        }
//    }
//
//    private byte[] loadSessionKey() throws AudioReaderException, IOException {
//        byte[] buffer = this.mReader.enterProtectedMode();
//        if (byteArrayToInt(buffer, 0, 2) != 0) {
//            throw new RuntimeException("Error on session key loading");
//        }
//        byte[] dec = decryptRSABlock(byteArrayToSubArray(buffer, 26, PKIFailureInfo.unacceptedPolicy));
//        if (dec == null) {
//            throw new RuntimeException("Error in RSA block decryption");
//        }
//        byte[] key = new byte[16];
//        System.arraycopy(dec, HttpStatus.SC_MULTI_STATUS, key, 0, key.length);
//        return key;
//    }

//    private synchronized Response transmit(boolean check, int cid, byte[] data, int offset, int length) throws AudioReaderException, IOException {
//        Response result;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(cid);
//        o.write(0);
//        o.write(length >> 8);
//        o.write(length);
//        o.write(data, offset, length);
//        result = Response.parse(processMessage(o.toByteArray()));
//        if (check && result.cid != cid) {
//            throw new RuntimeException("Response with wrong cid: " + result.cid + " (must be " + cid + ")");
//        }
//        return result;
//    }
//
//    private synchronized Response transmit(boolean check, int cid, byte[] data) throws AudioReaderException, IOException {
//        return transmit(check, cid, data, 0, data.length);
//    }
//
//    private synchronized Response transmit(boolean check, int cid) throws AudioReaderException, IOException {
//        return transmit(check, cid, new byte[0]);
//    }
//
//    public synchronized ResetParametersResponse resetParameters() throws AudioReaderException, IOException {
//        return ResetParametersResponse.parse(transmit(true, 1).data);
//    }
//
//    private synchronized LoadParametersResponse loadParametersChunk(int msgControl, int configSize, int offset, byte[] data) throws AudioReaderException, IOException {
//        LoadParametersResponse result;
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        o.write(msgControl);
//        o.write(configSize >> 8);
//        o.write(configSize);
//        o.write(offset >> 8);
//        o.write(offset);
//        o.write(data);
//        Response response = transmit(true, 2, o.toByteArray());
//        result = new LoadParametersResponse();
//        result.result = byteArrayToInt(response.data, 0, 2);
//        result.state = byteArrayToInt(response.data, 2, 1);
//        result.dataReceived = byteArrayToInt(response.data, 3, 2);
//        return result;
//    }
//
//    public synchronized LoadParametersResponse loadParameters(byte[] data, int chunkSize) throws AudioReaderException, IOException {
//        LoadParametersResponse result;
//        int offset = 0;
//        do {
//            result = loadParametersChunk(data.length - offset <= chunkSize ? 1 : 2, data.length, offset, byteArrayToSubArray(data, offset, Math.min(data.length - offset, chunkSize)));
//            offset = result.dataReceived;
//            if (result.result != 0) {
//                break;
//            }
//        } while (offset < data.length);
//        return result;
//    }
//
//    public synchronized ConfigrationParametersResponse getConfigrationParameters() throws AudioReaderException, IOException {
//        ConfigrationParametersResponse result;
//        List<BerTlv> tlvs = BerTlv.createList(this.mReader.getConfigurationParameters());
//        result = new ConfigrationParametersResponse();
//        for (BerTlv tlv : tlvs) {
//            if (tlv.getTag().toIntValue() == PrivateTags.TAG_C1_PROCESSING_RESULT) {
//                result.result = byteArrayToInt(tlv.getValue(), 0, tlv.getValue().length);
//            }
//            if (tlv.getTag().toIntValue() == PrivateTags.TAG_C6_READER_STATE) {
//                result.state = byteArrayToInt(tlv.getValue(), 0, tlv.getValue().length);
//            }
//            if (tlv.getTag().toIntValue() == PrivateTags.TAG_DF10_CONFIGURATION_DATA_SET_VERSION_NUMBER) {
//                result.version = byteArrayToInt(tlv.getValue(), 0, tlv.getValue().length);
//            }
//            if (tlv.getTag().toIntValue() == PrivateTags.TAG_DF11_MAX_CONFIGURATION_MESSAGE_SIZE) {
//                result.maxSize = byteArrayToInt(tlv.getValue(), 0, tlv.getValue().length);
//            }
//        }
//        return result;
//    }
//
//    private void startThreadAndWaitToFinish(Thread t) throws AudioReaderException, IOException {
//        t.start();
//        do {
//            try {
//                t.join(250);
//            } catch (InterruptedException e) {
//            }
//        } while (t.isAlive());
//    }
//
//    private Response responseToCardHolderSelection(byte[] data) throws AudioReaderException, IOException {
//        return transmit(false, 16, data);
//    }
//
//    private Response responseToCheckForPAN(byte[] data) throws AudioReaderException, IOException {
//        return transmit(false, 32, data);
//    }
//
//    private Response responseToOnlineProcessing(byte[] data) throws AudioReaderException, IOException {
//        return transmit(false, 48, data);
//    }
//
//    private void responseToConfigOrReject(byte[] data) {
//    }
//
//    private Response processCheckForPAN(byte[] data, EMVProcessorCallback callback) throws AudioReaderException, IOException {
//        Object[] result = new Object[1];
//        startThreadAndWaitToFinish(new Thread(new 1(result, callback, data)));
//        return responseToCheckForPAN((byte[]) result[0]);
//    }
//
//    private Response processCardHolderSelection(byte[] data, EMVProcessorCallback callback) throws AudioReaderException, IOException {
//        Object[] result = new Object[1];
//        startThreadAndWaitToFinish(new Thread(new 2(result, callback, data)));
//        return responseToCardHolderSelection((byte[]) result[0]);
//    }
//
//    private Response processOnline(byte[] data, EMVProcessorCallback callback) throws AudioReaderException, IOException {
//        Object[] result = new Object[1];
//        startThreadAndWaitToFinish(new Thread(new 3(result, callback, data)));
//        Response response = responseToOnlineProcessing((byte[]) result[0]);
//        processConfirmOrReject(response.data, callback);
//        return response;
//    }
//
//    private void processConfirmOrReject(byte[] data, EMVProcessorCallback callback) throws AudioReaderException, IOException {
//        startThreadAndWaitToFinish(new Thread(new 4(callback, data)));
//        responseToConfigOrReject(new byte[0]);
//    }
//
//    public TransactionResponse initEMVProcessing(byte[] data, EMVProcessorCallback callback) throws AudioReaderException, IOException {
//        Response response = transmit(false, 4, data);
//        Map<Tag, byte[]> tags = BerTlv.createMap(data);
//        while (response.cid != 4) {
//            try {
//                tags.putAll(BerTlv.createMap(response.data));
//                switch (response.cid) {
//                    case ReasonFlags.affiliationChanged /*16*/:
//                        response = processCardHolderSelection(response.data, callback);
//                        break;
//                    case ReasonFlags.cACompromise /*32*/:
//                        response = processCheckForPAN(response.data, callback);
//                        break;
//                    case Barcode.BARCODE_CODE39_FULL /*48*/:
//                        response = processOnline(response.data, callback);
//                        break;
//                    default:
//                        throw new RuntimeException("Response with invalid cid: " + Integer.toHexString(response.cid));
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to parse TLV data", e);
//            }
//        }
//        tags.putAll(BerTlv.createMap(response.data));
//        return new TransactionResponse(tags);
//    }
}
