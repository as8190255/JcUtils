package org.jc.jcutils.javabean;

/**
 * 自定义Str内容发送
 * Created by Jc on 2017/4/20.
 */

public class CustomStr_Send extends SocketWrite{
    public CustomStr_Send(String address, String content) {
        super(address, content.getBytes());
    }
}
