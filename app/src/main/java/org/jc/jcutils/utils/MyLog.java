package org.jc.jcutils.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by a3552 on 2017/4/17.
 */

public class MyLog {
    private static final String TAG = "anan";
    public static void i(String content){
        if (!TextUtils.isEmpty(content))
            Log.i(TAG, content);
    }

    public static void e(String content){
        Log.e(TAG, content);
    }
    public static void e(String content, Throwable t){
        Log.e(TAG, content, t);
    }
}
