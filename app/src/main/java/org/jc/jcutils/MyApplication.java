package org.jc.jcutils;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.jc.jcutils.config.Config;
import org.jc.jcutils.utils.MyLog;

/**
 * Created by a3552 on 2017/4/20.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();


        //获取屏幕大小
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);//不包括虚拟导航
        //manager.getDefaultDisplay().getRealMetrics(dm);//包括虚拟导航栏
        Config.PHONE_HEIGHT = dm.heightPixels;
        Config.PHONE_WITH = dm.widthPixels;
        MyLog.i(Config.PHONE_HEIGHT + " "+Config.PHONE_WITH);

    }
}
