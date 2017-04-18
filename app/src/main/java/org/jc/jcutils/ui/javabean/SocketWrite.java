package org.jc.jcutils.ui.javabean;

/**
 * Socket消息发送
 * Created by a3552 on 2017/4/18.
 */

public class SocketWrite {
    String address;
    byte [] data;
    int port = 35642;

    public SocketWrite(String address, byte[] data) {
        this.address = address;
        this.data = data;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getData() {
        return data;
    }
}
