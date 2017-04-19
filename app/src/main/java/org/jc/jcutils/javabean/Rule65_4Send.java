package org.jc.jcutils.javabean;

/**
 * HOME点击 65,4 发送
 * Created by a3552 on 2017/4/19.
 */

public class Rule65_4Send extends SocketWrite{
    static byte[] data = new byte[]{65, 4};
    public Rule65_4Send(String address) {
        super(address, data);
    }
}
