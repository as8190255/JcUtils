package org.jc.jcutils.javabean;

/**
 * 返回点击 65,5 发送
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_5Send extends SocketWrite{
    static byte[] data = new byte[]{65, 5};
    public Rule65_5Send(String address) {
        super(address, data);
    }
}
