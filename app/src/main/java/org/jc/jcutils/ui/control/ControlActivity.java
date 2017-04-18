package org.jc.jcutils.ui.control;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.jc.jcutils.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

/**
 * 控制端
 */
public class ControlActivity extends AppCompatActivity implements View.OnTouchListener{

    //private static final String ADD= "192.168.61.247";
    private static final String ADD= "192.168.61.164";
    private static final int SERVER_PORT = 16050;
    //private static final int SERVER_PORT = 8123;
    private DatagramSocket dSocket = null;
    private DecimalFormat decimalFormat=new DecimalFormat(".00000");//构造方法的字符格式这里如果小数不足5位,会以0补足.
    InetAddress address = null;
    //0x0_0000.00000_0000.00000 点击事件传递协议 x,y
    private static final String actionClickRule = "0x0_%s_%s";
    //0x1_0000.00000_0000.00000_0000.00000_0000.00000 移动事件传递协议 sx,sy -> ex,ey
    private static final String actionMoveRule = "0x1_%s_%s_%s_%s";
    float startX, startY, endX, endY;
    View v_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        v_move = findViewById(R.id.v_move);
        v_move.setOnTouchListener(this);

        connectPhone();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int with = v.getMeasuredWidth();
        int height = v.getMeasuredHeight();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                Log.i("anan","ACTION_DOWN: " + startX + "," + startY);
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                if (startX == endX && startY == endY){
                    startX = startX / with * 1080;
                    startY = startY / height * 1920;
                    //点击
                    sendMsg(String.format(actionClickRule, convert(startX), convert(startY)));
                }else {
                    startX = startX / with * 1080;
                    startY = startY / height * 1920;
                    endX = endX / with * 1080;
                    endY = endY / height * 1920;
                    //移动
                    sendMsg(String.format(actionMoveRule, convert(startX), convert(startY),
                            convert(endX), convert(endY)));
                }
                Log.i("anan","ACTION_UP: " + endX + "," + endY);
                break;
        }
        return true;
    }

    private String convert(float f){
        String oo = decimalFormat.format(f);//format 返回的是字符串
        //不足十位首位填0
        for (int i = oo.length(); i < 10; i++) {
            oo = "0" + oo;
        }
        return oo;
    }

    private void sendMsg(final String msg){
        Log.i("anan",msg);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
//                    dSocket = new DatagramSocket();

                    DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(), msg.length() ,address, SERVER_PORT);
                    dSocket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    //dSocket.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(ControlActivity.this, msg,Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void connectPhone() {
        try {
            dSocket = new DatagramSocket();
            address = InetAddress.getByName(ADD);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void onMenuClick(View view) {
        sendMsg("0x2_");
    }

    public void onHomeClick(View view) {
        sendMsg("0x3_");
    }

    public void onBackClick(View view) {
        sendMsg("0x4_");
    }
}
