package org.jc.jcutils.ui.test;

import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jc.jcutils.R;
import org.jc.jcutils.service.MySocketService;
import org.jc.jcutils.utils.ScreenRecorder;

import java.io.File;

/**
 * 截屏测试界面
 */
public class ScreenshotTestActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 1;
    MediaProjectionManager mMediaProjectionManager;
    ScreenRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot_test);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        startService(new Intent(this, MySocketService.class));
    }

    public void onScreenshot(View view) {
        if (mRecorder != null){
            mRecorder.quit();
            mRecorder = null;
            Toast.makeText(this, "已关闭", Toast.LENGTH_SHORT).show();
        }else {
            Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection == null)return;
        final int width = 720;
        final int height = 1080;
        File file = new File(Environment.getExternalStorageDirectory(),
                "record-"+width+"x"+height+"-"+System.currentTimeMillis()+".mp4");
        final int bitrate = 6000000;
        mRecorder = new ScreenRecorder(width, height, bitrate, 1, mediaProjection, file.getAbsolutePath());
        mRecorder.start();
        Toast.makeText(this, "开始", Toast.LENGTH_SHORT).show();
        moveTaskToBack(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecorder != null){
            mRecorder.quit();
            mRecorder = null;
        }
    }
}
