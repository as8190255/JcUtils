package org.jc.jcutils.javabean;

import org.jc.jcutils.utils.ShellUtils;

/**
 * 返回键点击 65,5 接收
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_5Result {
    public static final byte[] rule = new byte[]{65, 5};
    private static final String actionBack="input keyevent 4";

    /** 执行命令*/
    public static void run(){
        ShellUtils.execShellCmd(actionBack);
    }
}
