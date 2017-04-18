package org.jc.jcutils.utils;

/**
 * 字节工具箱
 * Created by Jc on 2017/4/18.
 */

public class ByteUtils {

    /**
     * 整型转字节数组
     * @param i
     * @return
     */
    public byte[] int2byteArrays(int i){
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
//        result[3] = (byte)((i >> 0) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    public int byteArrays2int32(byte[]b){
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            int shift = (b.length - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public int byteArrays2int16(byte[]b){
        return (b[0] & 0xFF) | ((b[1] << 8) & 0xff00);
    }

    public String byteArrays2string(byte b){
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }
}
