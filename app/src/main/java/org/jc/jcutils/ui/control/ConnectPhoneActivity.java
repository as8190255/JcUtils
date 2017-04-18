package org.jc.jcutils.ui.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.jc.jcutils.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 连接被控端手机界面
 */
public class ConnectPhoneActivity extends AppCompatActivity {
    private static final int SERVER_PORT = 16050;
    private DatagramSocket dSocket = null;
    InetAddress address = null;
    private String msg="0x1_0300.00000_0300.00000_0600.00000_0600.00000";
    String addre = "192.168.61.164";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_phone);

    }

    public void send(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dSocket = new DatagramSocket();
                    address = InetAddress.getByName("192.168.61.164");
                    DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(), msg.length() ,address, SERVER_PORT);
                    dSocket.send(datagramPacket);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void send2(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName(addre);
                    DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(), msg.length() ,address, SERVER_PORT);

                    dSocket = new DatagramSocket();
                    dSocket.send(datagramPacket);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (dSocket!=null){
                        if (dSocket.isConnected()){
                            dSocket.close();
                        }
                    }
                }
            }
        }).start();
    }

    public void send3(View view)  {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    DatagramSocket client = new DatagramSocket();

                    String sendStr = "0x1_0300.00000_0300.00000_0600.00000_0600.00000";
                    byte[] sendBuf;
                    sendBuf = sendStr.getBytes();
                    InetAddress addr = InetAddress.getByName("192.168.61.164");
                    int port = 16050;
                    DatagramPacket sendPacket
                            = new DatagramPacket(sendBuf ,sendBuf.length , addr , port);
                    client.send(sendPacket);
//            byte[] recvBuf = new byte[100];
//            DatagramPacket recvPacket
//                    = new DatagramPacket(recvBuf , recvBuf.length);
//            client.receive(recvPacket);
//            String recvStr = new String(recvPacket.getData() , 0 ,recvPacket.getLength());
//            System.out.println("收到:" + recvStr);
//                    client.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ConnectPhoneActivity.this,"ok",Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
