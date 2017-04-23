package org.jc.jcutils.javabean;

/**
 * H.264数据接收
 * Created by a3552 on 2017/4/22.
 */

public class H264_Result {
    public static final byte[] rule = new byte[]{0, 0, 0, 1};
    byte[] data;

    public H264_Result(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
