package com.datecs.pinpad.tlv;

import com.datecs.pinpad.Pinpad;
import java.nio.ByteBuffer;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.ReasonFlags;

public class Tag {
    private static final int MASK_ANOTHER_BYTE = 128;
    private static final int MASK_CONSTRUCTED_DATA_OBJECT = 32;
    private static final int MASK_SUBSEQUENT_BYTES = 31;
    private byte[] mBytes;

    public Tag(byte[] bytes) {
        validate(bytes);
        this.mBytes = bytes;
    }

    public Tag(int tag) {
//        this(encodeTag(tag));
    }

//    private static byte[] encodeTag(int tag) {
//        byte b1 = (byte) (tag >> 16);
//        byte b2 = (byte) (tag >> 8);
//        byte b3 = (byte) tag;
//        if (((byte) (tag >> 24)) != null) {
//            return new byte[]{(byte) (tag >> 24), b1, b2, b3};
//        } else if (b1 != null) {
//            return new byte[]{b1, b2, b3};
//        } else if (b2 != null) {
//            return new byte[]{b2, b3};
//        } else if (b3 != null) {
//            return new byte[]{b3};
//        } else {
//            throw new IllegalArgumentException("The argument 'tag' can not be null");
//        }
//    }

    private void validate(byte[] b) {
        if (b == null || b.length == 0) {
            throw new IllegalArgumentException("Tag must be constructed with a non-empty byte array");
        } else if (b.length == 1) {
            if ((b[0] & MASK_SUBSEQUENT_BYTES) == MASK_SUBSEQUENT_BYTES) {
                throw new IllegalArgumentException("If first 5 bits are set tag must not be only one byte long");
            }
        } else if ((b[b.length - 1] & -128) != 0) {
            throw new IllegalArgumentException("For multibyte tag bit 8 of the final byte must be 0");
        } else if (b.length > 2) {
            for (int i = 1; i < b.length - 1; i++) {
                if ((b[i] & -128) != -128) {
                    throw new IllegalArgumentException("For multibyte tag bit 8 of the internal bytes must be 1");
                }
            }
        }
    }

    public TagClass getTagClass() {
        byte classValue = (byte) ((this.mBytes[0] >>> 6) & 3);
        switch (classValue) {
            case CRLReason.UNSPECIFIED /*0*/:
                return TagClass.UNIVERSAL;
            case ReasonFlags.privilegeWithdrawn /*1*/:
                return TagClass.APPLICATION;
            case ReasonFlags.certificateHold /*2*/:
                return TagClass.CONTEXT_SPECIFIC;
            case CRLReason.affiliationChanged /*3*/:
                return TagClass.PRIVATE;
            default:
                throw new RuntimeException("Tag has invalid class type: " + Integer.toHexString(classValue));
        }
    }

    public TagType getTagType() {
        if (isConstructed()) {
            return TagType.CONSTRUCTED;
        }
        return TagType.PRIMITIVE;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public int toIntValue() {
        int value = 0;
        for (byte b : this.mBytes) {
            value = (value << 8) + (b & Pinpad.EMV2_MESSAGE_UI_NA);
        }
        return value;
    }

    public String toHexValue() {
        return Integer.toHexString(toIntValue()).toUpperCase();
    }

    public boolean isConstructed() {
        return (this.mBytes[0] & MASK_CONSTRUCTED_DATA_OBJECT) != 0;
    }

    public boolean isPrimitive() {
        return !isConstructed();
    }

    public static Tag create(ByteBuffer buffer) {
        int len = 1;
        if ((buffer.get() & MASK_SUBSEQUENT_BYTES) == MASK_SUBSEQUENT_BYTES) {
            do {
                len++;
            } while ((buffer.get() & MASK_ANOTHER_BYTE) == MASK_ANOTHER_BYTE);
        }
        byte[] bytes = new byte[len];
        buffer.position(buffer.position() - len);
        buffer.get(bytes, 0, bytes.length);
        return new Tag(bytes);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) obj;
        if (this.mBytes.length != other.mBytes.length) {
            return false;
        }
        for (int i = 0; i < this.mBytes.length; i++) {
            if (this.mBytes[i] != other.mBytes[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = Tag.class.getName().hashCode() + 1;
        for (byte element : this.mBytes) {
            result = (result * MASK_SUBSEQUENT_BYTES) + element;
        }
        return result;
    }

    public String toString() {
        return "Tag [" + toHexValue() + ", Type=" + getTagType() + ", Class=" + getTagClass() + "]";
    }
}
