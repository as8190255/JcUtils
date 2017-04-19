package org.jc.jcutils.javabean;

import org.jc.jcutils.config.Config;
import org.jc.jcutils.utils.ByteUtils;

/**
 * 屏幕滑动事件 65,2 发送
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_2Send extends SocketWrite{
    /**
     *
     * @param address 地址
     * @param sx 屏幕开始x轴坐标
     * @param sy 屏幕开始y轴坐标
     * @param ex 屏幕结束y轴坐标
     * @param ey 屏幕结束y轴坐标
     */
    public Rule65_2Send(String address, int sx, int sy, int ex, int ey) {
        super(address, display(new byte[]{65, 1, 0, 0, 0, 0, 0, 0, 0, 0}, sx, sy, ex, ey));
    }
    public static byte[] display(byte[]src, int sx, int sy, int ex, int ey){
        int sxi = (int) (sx * 1.0f / Config.PHONE_WITH * 10000);
        int syi = (int) (sy * 1.0f / Config.PHONE_HEIGHT * 10000);
        int exi = (int) (ex * 1.0f / Config.PHONE_WITH * 10000);
        int eyi = (int) (ey * 1.0f / Config.PHONE_HEIGHT * 10000);

        src = ByteUtils.int16s2byteArrays(sxi, src, 2);
        src = ByteUtils.int16s2byteArrays(syi, src, 4);
        src = ByteUtils.int16s2byteArrays(exi, src, 6);
        src = ByteUtils.int16s2byteArrays(eyi, src, 8);
        return src;
    }

    /**
     *
     * @param address 地址
     * @param sx 屏幕开始x轴百分比 float
     * @param sy 屏幕开始y轴百分比 float
     * @param ex 屏幕结束y轴百分比 float
     * @param ey 屏幕结束y轴百分比 float
     */
    public Rule65_2Send(String address, float sx, float sy, float ex, float ey) {
        super(address, display(new byte[]{65, 1, 0, 0, 0, 0, 0, 0, 0, 0}, sx, sy, ex, ey));
    }

    public static byte[] display(byte[]src, float sx, float sy, float ex, float ey){
        int sxi = (int) (sx * 10000);
        int syi = (int) (sy * 10000);
        int exi = (int) (ex * 10000);
        int eyi = (int) (ey * 10000);

        src = ByteUtils.int16s2byteArrays(sxi, src, 2);
        src = ByteUtils.int16s2byteArrays(syi, src, 4);
        src = ByteUtils.int16s2byteArrays(exi, src, 6);
        src = ByteUtils.int16s2byteArrays(eyi, src, 8);
        return src;
    }
}
