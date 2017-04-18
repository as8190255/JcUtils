package org.jc.jcutils;

import org.jc.jcutils.utils.ByteUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jc on 2017/4/18.
 */

public class ByteUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println();
//        byteDisplay();

        //协议测试

        byte[] rule1_1=new byte[6];
        rule1_1[0]=1;

        rule1_1[1]=1;

        rule1_1=ByteUtils.int16s2byteArrays(4518, rule1_1, 2);
        rule1_1=ByteUtils.int16s2byteArrays(6536, rule1_1, 4);
        System.out.println(ByteUtils.byteArrays2int16s(rule1_1, 2));
        System.out.println(ByteUtils.byteArrays2int16s(rule1_1, 4));

        System.out.println(ByteUtils.byteArrays2string(rule1_1[0]));
        System.out.println(ByteUtils.int8_2byteArrays(rule1_1[0]));

        for (byte item: rule1_1) {
            System.out.println(ByteUtils.byteArrays2string(item));
        }
        //结束
        assertEquals(4, 2 + 2);
    }

    /**位处理 */
    public void byteDisplay(){
        byte b = 0;// 0000 0000
        System.out.println(byte2string(b));

        b = (byte)(15 << 4);// 1111 0000
        System.out.println(byte2string(b));

//        b |= (byte)(1 << 3);// 1111 1000
        b |= 0x8;// 1111 1000 = 1111 0000 | 0000 1000(0x8)
        System.out.println(byte2string(b));

        b = (byte)(b | (byte)(1 << 2));
        System.out.println(byte2string(b));

        b = (byte) 255;
        System.out.println(String.valueOf((b&0xff)));

    }

    /** byte 转 二进制字符串*/
    public String byte2string(byte b){
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }
}


/**
 byte b = 0;
 b = (byte) (fixedHeader.getMessageType().value() << 4);
 b |= fixedHeader.isDup() ? 0x8 : 0x0;
 b |= fixedHeader.getQos().value() << 1;
 b |= fixedHeader.isRetain() ? 0x1 : 0;

 byte[] bArray = new byte[]{b};
 */