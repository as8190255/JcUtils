package org.jc.jcutils.ui.test.rtmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jc.jcutils.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 视频推流显示界面
 */
public class VideoShow2Activity extends AppCompatActivity {

    DatagramSocket datagramSocket;
    DatagramPacket datagramPacket;//接收
    DatagramPacket datagramPacket2;//回发
    byte[] temp = new byte[60001];
    List<byte []> cache = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show2);

        try {
            datagramSocket = new DatagramSocket(35555);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        datagramPacket = new DatagramPacket(temp, temp.length);
        t.start();

    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    datagramSocket.receive(datagramPacket);
                    if (datagramPacket.getLength()>0){
                        cache.add(Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength()));
                        Log.i("anan", "byte[]集合数据+1,总数:" + cache.size());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public void send(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress address = null;
                try {
                    address = InetAddress.getByName("10.8.254.83");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                for (int i = cache.size()-1; i >= 0; i--) {
                    byte [] asd= cache.get(i);
                    datagramPacket2 = new DatagramPacket(asd, asd.length, address, 35555);
                    try {
                        datagramSocket.send(datagramPacket2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("anan","回发完毕");
            }
        }).start();
    }
}
