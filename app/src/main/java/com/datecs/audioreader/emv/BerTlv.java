package com.datecs.audioreader.emv;

import com.datecs.pinpad.Pinpad;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.ReasonFlags;

public class BerTlv {
    private final Tag mTag;
    private final byte[] mValue;

    public BerTlv(Tag tag, byte[] value) {
        if (tag == null) {
            throw new IllegalArgumentException("The argument 'tag' can not be null");
        } else if (value == null) {
            throw new IllegalArgumentException("The argument 'value' can not be null");
        } else {
            this.mTag = tag;
            this.mValue = value;
        }
    }

    public BerTlv(int tag, byte[] value) {
        this(new Tag(tag), value);
    }

    public Tag getTag() {
        return this.mTag;
    }

    public byte[] getLengthBytes() {
        return encodeLength(this.mValue.length);
    }

    public byte[] getValue() {
        return this.mValue;
    }

    public String getValueAsHexString() {
        char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] buf = new char[(this.mValue.length * 2)];
        int j = 0;
        for (int i = 0; i < this.mValue.length; i++) {
            int i2 = j + 1;
            buf[j] = hex[(this.mValue[i] >> 4) & 15];
            j = i2 + 1;
            buf[i2] = hex[this.mValue[i] & 15];
        }
        return new String(buf);
    }

    public byte[] toByteArray() {
        byte[] tmpTag = this.mTag.getBytes();
        byte[] tmpLen = getLengthBytes();
        byte[] tmpVal = this.mValue;
        byte[] buffer = new byte[((tmpTag.length + tmpLen.length) + tmpVal.length)];
        System.arraycopy(tmpTag, 0, buffer, 0, tmpTag.length);
        System.arraycopy(tmpLen, 0, buffer, tmpTag.length, tmpLen.length);
        System.arraycopy(tmpVal, 0, buffer, tmpTag.length + tmpLen.length, tmpVal.length);
        return buffer;
    }

    public static byte[] encodeLength(int length) {
        if (length == 0) {
            return new byte[1];
        }
        if (length <= 127) {
            return new byte[]{(byte) length};
        }
        int numberOfBytes = 0;
        do {
            numberOfBytes++;
        } while (((8388607 << (numberOfBytes * 8)) & length) > 0);
        byte[] data = new byte[(numberOfBytes + 1)];
        data[0] = (byte) (numberOfBytes + ReasonFlags.unused);
        for (int i = 0; i < numberOfBytes; i++) {
            data[numberOfBytes - i] = (byte) ((length >> (i * 8)) & Pinpad.EMV2_MESSAGE_UI_NA);
        }
        return data;
    }

    public static int decodeLength(ByteBuffer data) {
        int length = data.get() & Pinpad.EMV2_MESSAGE_UI_NA;
        if ((length & ReasonFlags.unused) != 0) {
            length = 0;
            for (int numberOfBytes = length & 127; numberOfBytes > 0; numberOfBytes--) {
                length = (length << 8) + (data.get() & Pinpad.EMV2_MESSAGE_UI_NA);
            }
        }
        return length;
    }

    public static BerTlv create(Tag tag, byte[] value) {
        return new BerTlv(tag, value);
    }

    public static BerTlv create(Tag tag, List<BerTlv> values) {
        int i;
        byte[][] container = new byte[values.size()][];
        int totalDataLen = 0;
        for (i = 0; i < container.length; i++) {
            container[i] = ((BerTlv) values.get(i)).toByteArray();
            totalDataLen += container[i].length;
        }
        byte[] buffer = new byte[totalDataLen];
        int off = 0;
        for (i = 0; i < container.length; i++) {
            System.arraycopy(container[i], 0, buffer, off, container[i].length);
            off += container[i].length;
        }
        return new BerTlv(tag, buffer);
    }

    public static BerTlv create(ByteBuffer buffer) {
        Tag tag = Tag.create(buffer);
        byte[] val = new byte[decodeLength(buffer)];
        buffer.get(val);
        return new BerTlv(tag, val);
    }

    public static BerTlv create(byte[] src, int off, int len) {
        return create(ByteBuffer.wrap(src, off, len));
    }

    public static BerTlv create(byte[] src) {
        return create(ByteBuffer.wrap(src, 0, src.length));
    }

    public static List<BerTlv> createList(ByteBuffer buffer) {
        List<BerTlv> tlvList = new ArrayList();
        while (buffer.hasRemaining()) {
            tlvList.add(create(buffer));
        }
        return tlvList;
    }

    public static List<BerTlv> createList(byte[] array) {
        return createList(ByteBuffer.wrap(array));
    }

    public static Map<Tag, byte[]> createMap(ByteBuffer buffer) {
        Map<Tag, byte[]> tlvMap = new HashMap();
        while (buffer.hasRemaining()) {
            BerTlv tlv = create(buffer);
            tlvMap.put(tlv.getTag(), tlv.getValue());
        }
        return tlvMap;
    }

    public static Map<Tag, byte[]> createMap(byte[] array) {
        return createMap(ByteBuffer.wrap(array));
    }

    public static byte[] listToByteArray(List<BerTlv> input) {
        List<byte[]> dataList = new ArrayList();
        int totalLen = 0;
        for (BerTlv tlv : input) {
            byte[] tmp = tlv.toByteArray();
            dataList.add(tmp);
            totalLen += tmp.length;
        }
        byte[] buffer = new byte[totalLen];
        totalLen = 0;
        for (byte[] data : dataList) {
            System.arraycopy(data, 0, buffer, totalLen, data.length);
            totalLen += data.length;
        }
        return buffer;
    }

    public static byte[] mapToByteArray(Map<Tag, byte[]> input) {
        List<byte[]> dataList = new ArrayList();
        int totalLen = 0;
        for (Tag tag : input.keySet()) {
            byte[] tmpTag = tag.getBytes();
            byte[] tmpVal = (byte[]) input.get(tag);
            byte[] tmpLen = encodeLength(tmpVal.length);
            dataList.add(tmpTag);
            dataList.add(tmpLen);
            dataList.add(tmpVal);
            totalLen += (tmpTag.length + tmpLen.length) + tmpVal.length;
        }
        byte[] buffer = new byte[totalLen];
        totalLen = 0;
        for (byte[] data : dataList) {
            System.arraycopy(data, 0, buffer, totalLen, data.length);
            totalLen += data.length;
        }
        return buffer;
    }

    public static BerTlv find(ByteBuffer buffer, Tag tag) {
        while (buffer.hasRemaining()) {
            BerTlv tlv = create(buffer);
            if (tlv.getTag().equals(tag)) {
                return tlv;
            }
        }
        return null;
    }

    public static BerTlv find(ByteBuffer buffer, int tag) {
        return find(buffer, new Tag(tag));
    }

    public static BerTlv find(byte[] array, Tag tag) {
        return find(ByteBuffer.wrap(array), tag);
    }

    public static BerTlv find(byte[] array, int tag) {
        return find(ByteBuffer.wrap(array), tag);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Tag)) {
            return false;
        }
        BerTlv other = (BerTlv) obj;
        if (!this.mTag.equals(other.getTag()) || this.mValue.length != other.mValue.length) {
            return false;
        }
        for (int i = 0; i < this.mValue.length; i++) {
            if (this.mValue[i] != other.mValue[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        int result = BerTlv.class.getName().hashCode() + 1;
        for (byte element : this.mTag.getBytes()) {
            result = (result * 31) + element;
        }
        byte[] bArr = this.mValue;
        while (i < bArr.length) {
            result = (result * 31) + bArr[i];
            i++;
        }
        return result;
    }

    public String toString() {
        return "BerTlv [Tag=" + this.mTag.toHexValue() + ", Length=" + this.mValue.length + ", Value=" + getValueAsHexString() + "]";
    }
}
