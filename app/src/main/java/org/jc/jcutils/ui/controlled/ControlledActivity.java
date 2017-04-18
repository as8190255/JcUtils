package org.jc.jcutils.ui.controlled;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.jc.jcutils.R;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 被控端
 */
public class ControlledActivity extends AppCompatActivity implements UDPServer.getResultMsg {

    public static final String actionClick="input tap %s %s";
    public static final String actionMove="input swipe %s %s %s %s";

    public static final String actionMenu="input keyevent 1";
    public static final String actionHome="input keyevent 3";
    public static final String actionBack="input keyevent 4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlled);
        // 开启服务器
        ExecutorService exec = Executors.newCachedThreadPool();
        UDPServer server = new UDPServer();
        server.setResultMsg(this);
        exec.execute(server);
    }

    //0x0_0000.00000_0000.00000 点击事件传递协议 x,y
    //0x1_0000.00000_0000.00000_0000.00000_0000.00000 移动事件传递协议 sx,sy -> ex,ey
    //0x2_ 菜单键
    //0x3_ HOME键
    //0x4_ 返回键
    @Override
    public void onResult(byte[] msgs) {
        final String msgStr = new String(msgs);
        if (msgStr.startsWith("0x0_")){//点击协议
            String [] splits = msgStr.substring(4).split("_");
            if (splits.length == 2){
                execShellCmd("getevent -p");
                execShellCmd(String.format(actionClick, splits[0], splits[1]));
            }
        }else if (msgStr.startsWith("0x1_")){//移动协议
            String [] splits = msgStr.substring(4).split("_");
            if (splits.length == 4){
                execShellCmd("getevent -p");
                execShellCmd(String.format(actionMove, splits[0], splits[1], splits[2], splits[3]));
            }
        }else if (msgStr.startsWith("0x2_")){
            execShellCmd(actionMenu);
        }else if (msgStr.startsWith("0x3_")){
            execShellCmd(actionHome);
        }else if (msgStr.startsWith("0x4_")){
            execShellCmd(actionBack);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ControlledActivity.this, msgStr, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     *
     * execShellCmd("getevent -p");
     * execShellCmd("input tap 200 280");//点击
     * execShellCmd("input swipe 200 600 200 400");//滑动
     * @param cmd
     */
    private void execShellCmd(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
