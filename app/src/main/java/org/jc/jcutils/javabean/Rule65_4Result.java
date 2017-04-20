package org.jc.jcutils.javabean;

import org.jc.jcutils.utils.ShellUtils;

/**
 * HOME键点击 65,4 接收
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_4Result {
    public static final byte[] rule = new byte[]{65, 4};
    private static final String actionHome="input keyevent 3";

    /** 执行命令*/
    public static void run(){
        ShellUtils.execShellCmd(actionHome);
    }
}
