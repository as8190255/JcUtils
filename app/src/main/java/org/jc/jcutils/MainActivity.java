package org.jc.jcutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

//    String keyDown = "adb shell input keyevent 3";//home键
//    String down = "adb shell input tap 250 250";//按下
//    String moveTo = "adb shell input swipe 250 250 300 300";//移动

    /** shell命令开始 */
    final String shellStart = "getevent -p";
    /** 移动坐标 x y x y*/
    final String moveAction = "input swipe %s %s %s %s";
    /** 点击坐标 x y*/
    final String clickAction = "input tap %s %s";
    ListView lv_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_data= (ListView) findViewById(R.id.lv_data);
        lv_data.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 50;
            }
            @Override
            public Object getItem(int position) {
                return null;
            }
            @Override
            public long getItemId(int position) {
                return 0;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(MainActivity.this, R.layout.item_data, null);
                return view;
            }
        });



    }

    public void test1(View view){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //execShell(keyDown);
        execShellCmd("getevent -p");
//        execShellCmd("sendevent /dev/input/event0 1 158 1");
//        execShellCmd("sendevent /dev/input/event0 1 158 0");
//        execShellCmd("input keyevent 3");//home
//        execShellCmd("input text  'helloworld!' ");

//        execShellCmd("input tap 200 280");//点击
        execShellCmd("input swipe 200 600 200 400");//滑动

//        execShellCmd("input swipe 100 250 200 280");
//        execShellCmd("input swipe 100 250 220 320");
    }

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

    public void execShell(String cmd){
        try{
            //权限设置
            Process p = Runtime.getRuntime().exec("su");
            //获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            //将命令写入
            dataOutputStream.writeBytes(cmd);
            //提交命令
            dataOutputStream.flush();
            //关闭流操作
            dataOutputStream.close();
            outputStream.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}
