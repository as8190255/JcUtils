package org.jc.jcutils.javabean;

import org.jc.jcutils.utils.ShellUtils;

/**
 * 菜单键点击 65,3 接收
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_3Result {
    public static final byte[] rule = new byte[]{65, 3};
    private static final String actionMenu="input keyevent 1";
    /** 执行命令*/
    public static void run(){
        ShellUtils.execShellCmd(actionMenu);
    }
}
