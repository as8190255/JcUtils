package org.jc.jcutils.javabean;

/**
 * h.264数据
 * Created by a3552 on 2017/4/22.
 */

public class H264_Send extends SocketWrite{

    public H264_Send(byte[] data) {
        super("192.168.61.247", data);
    }
}
