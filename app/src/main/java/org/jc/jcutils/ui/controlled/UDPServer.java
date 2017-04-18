package org.jc.jcutils.ui.controlled;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Created by a3552 on 2017/3/30.
 */

public class UDPServer implements Runnable{

    private static final int PORT = 16050;
    private byte[] msg = new byte[1024];
    private boolean life = true;

    getResultMsg resultMsg;
    @Override
    public void run() {
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length);
        try {
            datagramSocket = new DatagramSocket(PORT);
            while (life){
                try {
                    datagramSocket.receive(datagramPacket);
                    Log.i("anan", "接受长度:" + datagramPacket.getLength());
                    if (resultMsg!=null && datagramPacket.getLength()>0){
                        byte[]result;
                        result = Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength());
                        resultMsg.onResult(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setResultMsg(getResultMsg resultMsg) {
        this.resultMsg = resultMsg;
    }

    public interface getResultMsg{
        void onResult(byte[]msgs);
    }
}
