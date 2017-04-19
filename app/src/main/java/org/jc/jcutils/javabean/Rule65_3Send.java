package org.jc.jcutils.javabean;

/**
 * 菜单键点击 65,3 发送
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_3Send extends SocketWrite{
    static byte[] data = new byte[]{65, 3};
    public Rule65_3Send(String address) {
        super(address, data);
    }
}
