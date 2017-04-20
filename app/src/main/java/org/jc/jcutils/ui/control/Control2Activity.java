package org.jc.jcutils.ui.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.jc.jcutils.R;
import org.jc.jcutils.javabean.Rule65_1Send;
import org.jc.jcutils.javabean.Rule65_2Send;
import org.jc.jcutils.javabean.Rule65_3Send;
import org.jc.jcutils.javabean.Rule65_4Send;
import org.jc.jcutils.javabean.Rule65_5Send;
import org.jc.jcutils.service.MySocketService;
import org.jc.jcutils.utils.MyLog;

/**
 * 控制界面
 * @version 2
 */
public class Control2Activity extends AppCompatActivity implements View.OnTouchListener{
    public static String Address = "192.168.61.164";
    public static int port = 35642;
    float startX, startY, endX, endY;

    View v_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        v_move = findViewById(R.id.v_move);
        v_move.setOnTouchListener(this);

        //启动服务
        Intent service = new Intent(this, MySocketService.class);
        startService(service);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int with = v.getMeasuredWidth();
        int height = v.getMeasuredHeight();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                MyLog.i("ACTION_DOWN: " + startX + "," + startY);
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                if (startX == endX && startY == endY){
                    //点击
                    EventBus.getDefault().post(new Rule65_1Send(Address, (int)startX, (int)startY,
                            with, height));
                }else {
                    //移动
                    EventBus.getDefault().post(new Rule65_2Send(Address, (int)startX, (int)startY,
                            (int)endX, (int)endY, with, height));
                }
                MyLog.i("ACTION_UP: " + endX + "," + endY);
                break;
        }
        return true;
    }

    public void onMenuClick(View view) {
        EventBus.getDefault().post(new Rule65_3Send(Address));
    }

    public void onHomeClick(View view) {
        EventBus.getDefault().post(new Rule65_4Send(Address));
    }

    public void onBackClick(View view) {
        EventBus.getDefault().post(new Rule65_5Send(Address));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
