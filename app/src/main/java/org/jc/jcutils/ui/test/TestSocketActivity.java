package org.jc.jcutils.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jc.jcutils.R;
import org.jc.jcutils.ui.javabean.SocketWrite;
import org.jc.jcutils.ui.javabean.TestBean;
import org.jc.jcutils.ui.service.MySocketService;
import org.jc.jcutils.utils.MyLog;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestSocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_socket);

        EventBus.getDefault().register(this);

        Intent serviceIntent = new Intent(this, MySocketService.class);
        serviceIntent.putExtra(MySocketService.PORT_KEY,MySocketService.port);
        startService(serviceIntent);
        try {
            inetAddress = InetAddress.getByName("192.168.61.247");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Subscribe()
    public void resultData(TestBean bean){
        MyLog.i("接受到来自其他设备的信息,长度为;" + bean.getAsdasd().length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    InetAddress inetAddress;
    public void send(View view) {
        EventBus.getDefault().post(new SocketWrite("192.168.61.247", "asdsadsad".getBytes()));
    }
}