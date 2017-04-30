package org.jc.jcutils.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jc.jcutils.javabean.SocketWrite;
import org.jc.jcutils.utils.MyLog;
import org.jc.jcutils.utils.RuleDisplayUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

public class MySocketService extends Service {
    public static final String PORT_KEY = "jc_socket_port";

    public static int port = 35642;
//    public static int port = 35555;
    private byte[] msg = new byte[1024];
    private boolean life = true;

    static DatagramSocket mSocket;//Socket连接
    static InetAddress socketAddress = null;//连接对方地址信息
    DatagramPacket acceptPacket;//接收信息端
    static SocketThread socketThread;//监听线程

    public MySocketService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.i("Service:onCreate()");

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.i("Service:onStartCommand()");
        if (mSocket == null){
            port = intent.getIntExtra(MySocketService.PORT_KEY, port);
            try {
                mSocket = new DatagramSocket(port);
            } catch (SocketException e) {
                MyLog.e("Socket:创建失败", e);
            }
        }
        connetSocket();

        if (aaa == null){
            aaa = new TheadLog();
            aaa.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    TheadLog aaa ;
    public class TheadLog extends Thread{
        @Override
        public void run() {
            super.run();
            SharedPreferences sharedPreferences = getSharedPreferences("applog", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("timeStart",new Date().toString());
            while (true){
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences2 = getSharedPreferences("applog", Context.MODE_PRIVATE);
                sharedPreferences2.edit().putString("timeLog",new Date().toString());
            }
        }
    }

    /** 连接*/
    private void connetSocket(){
        acceptPacket = new DatagramPacket(msg, msg.length);
        if (socketThread == null){
            socketThread = new SocketThread(mSocket, acceptPacket);
        }
        if (socketThread.isAlive()){
            socketThread.interrupt();
        }
        socketThread.start();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void writeData(SocketWrite bean){
        if (mSocket == null || mSocket.isClosed()){
            MyLog.e("Socket:不在运行中");
            return;
        }
        if (socketThread == null || !socketThread.isAlive()){
            MyLog.e("SocketThread:不在运行中");
            return;
        }
        if (socketAddress == null || !bean.getAddress().equals(socketAddress.getHostName())){
            try {
                socketAddress = InetAddress.getByName(bean.getAddress());
            } catch (UnknownHostException e) {
                MyLog.e("address:连接地址异常", e);
                return;
            }
        }
        socketThread.write(socketAddress, bean.getData());
    }

    /** 线程处理区*/
    class SocketThread extends Thread{
        DatagramSocket mSocket;
        DatagramPacket acceptPacket;

        public SocketThread(DatagramSocket mSocket, DatagramPacket acceptPacket){
            this.mSocket = mSocket;
            this.acceptPacket = acceptPacket;
        }

        @Override
        public void run() {
            super.run();
            while (life){
                try {
                    mSocket.receive(acceptPacket);
                    MyLog.i("Socket:接受到数据长度 " + acceptPacket.getLength()+" "+acceptPacket.getAddress().getHostName());
                    if (acceptPacket!=null && acceptPacket.getLength()>0){
                        byte[] result;
                        result = Arrays.copyOf(acceptPacket.getData(), acceptPacket.getLength());
                        //底层协议 byte[]前位数判断请求类型
                        RuleDisplayUtils.display(acceptPacket.getAddress().getAddress(), acceptPacket.getPort(), result);
                    }
                } catch (IOException e) {
                    MyLog.e("Socket:接收数据异常", e);
                }
            }
        }

        /** 发送消息*/
        public void write(InetAddress inetAddress, byte[] data){
            if (data.length <= 60000){
                MyLog.i("Socket:消息发送:"+ data.length);
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
                try {
                    this.mSocket.send(datagramPacket);
                } catch (IOException e) {
                    MyLog.e("Socket:消息发送失败", e);
                }
            }else {
                MyLog.i("Socket:消息发送:"+ data.length + " 分包发送");
                int readDataLength = data.length;
                int num = readDataLength / 60000 + ((readDataLength % 60000 ) > 0 ? 1 : 0);
                for (int i = 0; i < num; i++) {
                    byte [] b ;
                    if (i < (num-1)){
                        b = Arrays.copyOfRange(data, i * 60000, (i + 1) * 60000);
                    }else {
                        b = Arrays.copyOfRange(data, i * 60000, data.length);
                    }
                    DatagramPacket datagramPacket = new DatagramPacket(b, b.length, inetAddress, port);
                    try {
                        this.mSocket.send(datagramPacket);
                        MyLog.i("Socket:分包消息"+i+": "+b.length);
                    } catch (IOException e) {
//                        e.printStackTrace();
                        MyLog.e("Socket:分包消息发送失败", e);
                    }
                }

//                byte[] b2 =Arrays.copyOf(data,65507);
//                byte[] b3 = Arrays.copyOfRange(data, 65507, readDataLength);
//
//                DatagramPacket datagramPacket = new DatagramPacket(b2, b2.length, inetAddress, port);
//                DatagramPacket datagramPacket2 = new DatagramPacket(b3, readDataLength - 65507, inetAddress, port);
//                try {
//                    this.mSocket.send(datagramPacket);
//                    this.mSocket.send(datagramPacket2);
//                } catch (IOException e) {
//                    MyLog.e("Socket:消息发送失败", e);
//                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i("Service:onDestroy()");
        EventBus.getDefault().unregister(this);
    }
}
