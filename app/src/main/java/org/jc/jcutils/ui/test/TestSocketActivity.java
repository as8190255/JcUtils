package org.jc.jcutils.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jc.jcutils.R;
import org.jc.jcutils.javabean.Rule65_3Result;
import org.jc.jcutils.javabean.Rule65_3Send;
import org.jc.jcutils.service.MySocketService;
import org.jc.jcutils.utils.MyLog;

public class TestSocketActivity extends AppCompatActivity {

//    public static final String ip = "10.9.254.41";
    public static final String ip = "192.168.61.247";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_socket);

        EventBus.getDefault().register(this);

        Intent serviceIntent = new Intent(this, MySocketService.class);
        serviceIntent.putExtra(MySocketService.PORT_KEY,MySocketService.port);
        startService(serviceIntent);
//        startActivity(new Intent(this, MainActivity.class));
    }

    @Subscribe()
    @SuppressWarnings("unused")
    public void rule65_3(Rule65_3Result result){
        MyLog.i("菜单点击请求");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void send(View view) {
        EventBus.getDefault().post(new Rule65_3Send(ip));
    }
}
