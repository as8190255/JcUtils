package org.jc.jcutils.javabean;

import org.jc.jcutils.config.Config;
import org.jc.jcutils.utils.ByteUtils;
import org.jc.jcutils.utils.MyLog;
import org.jc.jcutils.utils.ShellUtils;

/**
 * 屏幕点击 65,1 接收
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_1Result {
    public static final byte[] rule = new byte[]{65, 1};
    private static final String actionClick="input tap %s %s";

    /** 执行*/
    public static void run(byte[] data){
        int xbfb = ByteUtils.byteArrays2int16s(data, 2);
        int ybfb = ByteUtils.byteArrays2int16s(data, 4);
        int x = (int) (xbfb * 1.0f / 10000 * Config.PHONE_WITH);
        int y = (int) (ybfb * 1.0f / 10000 * Config.PHONE_HEIGHT);
        MyLog.i("协议65,1 点击:" + x + "," + y);
        ShellUtils.execShellCmd(String.format(actionClick, x, y));
    }
}
