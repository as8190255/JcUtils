package org.jc.jcutils.ui.controlled;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.jc.jcutils.R;
import org.jc.jcutils.javabean.CustomStr_Send;
import org.jc.jcutils.service.MySocketService;
import org.jc.jcutils.utils.ShellUtils;

/**
 * 被控端页面
 * @version 2
 */
public class Controlled2Activity extends AppCompatActivity {
    TextView tv_ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlled2);


//        EventBus.getDefault().register(this);
        tv_ip = (TextView) findViewById(R.id.textView3);
        String localIp = getWifiIp();
        if (localIp != null){
            tv_ip.setText("本机地址 " + localIp + ":" + 35642);
        }else {
            tv_ip.setText("网络获取错误");
        }
        //启动服务
        Intent service = new Intent(this, MySocketService.class);
        startService(service);

        tv_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new CustomStr_Send("10.9.254.41","123112"));
            }
        });

//        ShellUtils.execShellCmd("screencap -p "+ Environment.getExternalStorageDirectory() + "/screen.png");
    }

    /**获取本机WIFI的IP地址 */
    public String getWifiIp(){
        String ip;
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled())wifiManager.setWifiEnabled(true);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = (ipAddress&0xFF) + "." + (ipAddress >> 8 & 0xFF) + "." +
                    (ipAddress >> 16 & 0xFF) + "." + (ipAddress >> 24 & 0xFF);
            return  ip;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
