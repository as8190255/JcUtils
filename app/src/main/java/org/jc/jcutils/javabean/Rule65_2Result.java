package org.jc.jcutils.javabean;

import org.jc.jcutils.config.Config;
import org.jc.jcutils.utils.ByteUtils;
import org.jc.jcutils.utils.MyLog;
import org.jc.jcutils.utils.ShellUtils;

/**
 * 屏幕滑动 65,2 接收
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_2Result {
    public static final byte[] rule = new byte[]{65, 2};
    private static final String actionMove="input swipe %s %s %s %s";

    public static void run(byte[] data){
        int sxbfb = ByteUtils.byteArrays2int16s(data, 2);
        int sybfb = ByteUtils.byteArrays2int16s(data, 4);
        int exbfb = ByteUtils.byteArrays2int16s(data, 6);
        int eybfb = ByteUtils.byteArrays2int16s(data, 8);
        int sx = (int) (sxbfb * 1.0f / 10000 * Config.PHONE_WITH);
        int sy = (int) (sybfb * 1.0f / 10000 * Config.PHONE_HEIGHT);
        int ex = (int) (exbfb * 1.0f / 10000 * Config.PHONE_WITH);
        int ey = (int) (eybfb * 1.0f / 10000 * Config.PHONE_HEIGHT);

        MyLog.i("协议65,2 滑动:" + sx + "," + sy + "→" + ex + "," + ey);
        ShellUtils.execShellCmd(String.format(actionMove, sx, sy, ex, ey));
    }
}
