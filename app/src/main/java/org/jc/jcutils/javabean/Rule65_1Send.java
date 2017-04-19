package org.jc.jcutils.javabean;

import org.jc.jcutils.utils.ByteUtils;

/**
 * 屏幕点击事件 65,1 发送
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_1Send extends SocketWrite{
    /**
     *
     * @param address 地址
     * @param x 屏幕x轴百分比 float
     * @param y 屏幕y轴百分比 float
     */
    public Rule65_1Send(String address, float x, float y) {
        super(address, display(new byte[]{65, 1, 0, 0, 0, 0}, x, y));
    }

    public static byte[] display(byte[]src, float x, float y){
        int xi = (int) (x * 10000);
        int yi = (int) (y * 10000);
        src = ByteUtils.int16s2byteArrays(xi, src, 2);
        src = ByteUtils.int16s2byteArrays(yi, src, 4);
        return src;
    }
}
