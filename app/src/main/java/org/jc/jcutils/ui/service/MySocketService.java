package org.jc.jcutils.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jc.jcutils.ui.javabean.SocketWrite;
import org.jc.jcutils.ui.javabean.TestBean;
import org.jc.jcutils.utils.MyLog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MySocketService extends Service {
    public static final String PORT_KEY = "jc_socket_port";

    public static int port = 35642;
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
        return super.onStartCommand(intent, flags, startId);
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
                    MyLog.i("Socket:接受到数据长度 " + acceptPacket.getLength());
                    if (acceptPacket!=null && acceptPacket.getLength()>0){
                        byte[] result;
                        result = Arrays.copyOf(acceptPacket.getData(), acceptPacket.getLength());
                        //底层协议 byte[]前位数判断请求类型
                        EventBus.getDefault().post(new TestBean(result));
                    }
                } catch (IOException e) {
                    MyLog.e("Socket:接收数据异常", e);
                }
            }
        }

        /** 发送消息*/
        public void write(InetAddress inetAddress, byte[] data){
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
            try {
                this.mSocket.send(datagramPacket);
            } catch (IOException e) {
                MyLog.e("Socket:消息发送失败", e);
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
