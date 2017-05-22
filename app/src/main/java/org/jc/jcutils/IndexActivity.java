package org.jc.jcutils;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.jc.jcutils.ui.control.Control2Activity;
import org.jc.jcutils.ui.controlled.Controlled2Activity;

public class IndexActivity extends AppCompatActivity {
    public static final String MINOTE = "MI NOTE LTE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //判断手机 若是小米note 则自动跳转到 被控制端
        //Redmi 4
        //MI NOTE LTE
        if (MINOTE.equals(Build.MODEL)){
            startActivity(new Intent(this, Controlled2Activity.class));
        }
    }


    public void onControl(View view) {
        startActivity(new Intent(this, Control2Activity.class));
    }

    public void onControlled(View view) {
        startActivity(new Intent(this, Controlled2Activity.class));
    }
}
