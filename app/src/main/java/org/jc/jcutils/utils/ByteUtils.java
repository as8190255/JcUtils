package org.jc.jcutils.utils;

/**
 * 字节工具箱
 * Created by Jc on 2017/4/18.
 */

public class ByteUtils {

    public static byte byteArrays2int8(int i){return (byte) i;}
    public static int int8_2byteArrays(byte b){return b & 0xFF;}//转成无符号

    public static int byteArrays2int16s(byte[]src){
        return byteArrays2int16s(src, 0);
    }
    public static int byteArrays2int16s(byte[]src, int offset){
        return src[0 + offset] << 8 | src[ 1 + offset] & 0xff;
    }
    public static byte[] int16s2byteArrays(int i){
        byte[] dest = new byte[2];
        dest[0] = (byte) (i >> 8);
        dest[1] = (byte) (i >> 0);
        return dest;
    }
    public static byte[] int16s2byteArrays(int i, byte[] scr, int offset){
        scr[offset] = (byte) (i >> 8);
        scr[offset + 1] = (byte) (i >> 0);
        return scr;
    }



    /**
     * 32整型转字节数组
     * @param i 值
     * @return 4字节长度数组
     */
    public static byte[] int32_2byteArrays(int i){
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
//        result[3] = (byte)((i >> 0) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    public static int byteArrays2int32(byte[]b){
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            int shift = (b.length - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static String byteArrays2string(byte b){
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }


    //待测试为什么错误
    @Deprecated
    public static byte[] int16_2byteArrays(int i){
        byte[] result = new byte[2];
        result[0] = (byte)((i >> 8) & 0xFF);
        result[1] = (byte)(i & 0xFF);
        return result;
    }
    @Deprecated
    public static int byteArrays2int16(byte[]b){
        return (b[0] & 0xFF) | ((b[1] << 8) & 0xff00);
    }
}
