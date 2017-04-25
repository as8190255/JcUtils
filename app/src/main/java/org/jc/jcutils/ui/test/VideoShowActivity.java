package org.jc.jcutils.ui.test;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jc.jcutils.R;
import org.jc.jcutils.javabean.H264_Result;
import org.jc.jcutils.utils.MyLog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 显示视频
 */
public class VideoShowActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    VideoThread t;
    SurfaceView surfaceView;
    SurfaceHolder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);
        surfaceView= (SurfaceView) findViewById(R.id.surfaceView2);
        holder = surfaceView.getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        MyLog.i("嘿嘿 surfaceCreated");
        t=new VideoThread(holder);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (t!=null){
            t.isNext=false;
        }
    }


    class VideoThread extends Thread{
        int TIMEOUT_US=10000;
        final int mWidth = 720;
        final int mHeight = 720;
        DatagramSocket mSocket;
        DatagramPacket mPacket;
        private MediaCodec decoder;
        MediaFormat format;
        MediaCodec.BufferInfo mBufferInfo;
        byte[] data = new byte[100000];
        SurfaceHolder holder;
        public VideoThread(SurfaceHolder holder) {
            this.holder=holder;
            try {
                mSocket = new DatagramSocket(35555);
                mPacket = new DatagramPacket(data, data.length);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        public void init(){
            mBufferInfo = new MediaCodec.BufferInfo();
            format = MediaFormat.createVideoFormat("video/avc", mWidth, mHeight);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, mHeight*mWidth);
            format.setInteger(MediaFormat.KEY_MAX_HEIGHT, mHeight);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000);//设置波特率
            format.setInteger(MediaFormat.KEY_MAX_WIDTH,mWidth);
            //[0, 0, 0, 1, 103, 66, -128, 31, -23, 0, -96, 11, 116, 3, 104, 80, -102, -128, 0, 0, 0, 1, 104, -50, 6, -30]
            byte [] spspps = new byte[]{0, 0, 0, 1, 103, 66, -128, 31, -23, 0, -96, 11, 116, 3, 104, 80, -102, -128, 0, 0, 0, 1, 104, -50, 6, -30};
            //找到sps与pps的分隔处
            int pos=0;
            if(!((pos+3<spspps.length)&&(spspps[pos]==0&&spspps[pos+1]==0&&spspps[pos+2]==0&&spspps[pos+3]==1))){
            }else {
                //00 00 00 01开始标志后的一位
                pos=4;
            }
            while((pos+3<spspps.length)&&!(spspps[pos]==0&&spspps[pos+1]==0&&spspps[pos+2]==0&&spspps[pos+3]==1)){
                pos++;
            }
            if(pos+3>=spspps.length){
            }
            byte [] mSps=Arrays.copyOfRange(spspps,0,pos);
            byte [] mPps=Arrays.copyOfRange(spspps,pos,spspps.length);
            format.setByteBuffer("csd-0", ByteBuffer.wrap(mSps));
            format.setByteBuffer("csd-1", ByteBuffer.wrap(mPps));
            try {
                decoder = MediaCodec.createDecoderByType("video/avc");
            } catch (IOException e) {
                e.printStackTrace();
            }
            decoder.configure(format, holder.getSurface(), null, 0);
            decoder.start();
        }
        boolean isNext = true;
        List<byte[]> temp;//分包缓存数据
        @Override
        public void run() {
            super.run();
            init();

            temp = new ArrayList<>();

            while (isNext){
                long t = TIMEOUT_US;
                try {
                    int inIndex = decoder.dequeueInputBuffer(t);
                    if (inIndex >= 0){
                        mSocket.receive(mPacket);
                        byte [] result;
                        result = Arrays.copyOf(mPacket.getData(), mPacket.getLength());
                        MyLog.i("接收到视频数据:"+result.length);
                        if (result.length>=60000){//分包处理
                            byte[] tempByte = Arrays.copyOf(mPacket.getData(), mPacket.getLength());
                            temp.add(tempByte);
                            //缺
//                            decoder.releaseOutputBuffer(inIndex, true);
                        }else {
                            if (temp.size()>0){
                                byte [] resultTemp = new byte[0];
                                for (int i = 0; i < temp.size() - 1; i++) {
                                    resultTemp = byteMerger(temp.get(i), temp.get(i+1));
                                }
                                result = byteMerger(resultTemp, Arrays.copyOf(mPacket.getData(), mPacket.getLength()));
                                temp.clear();
                            }
                        }
                        ByteBuffer buffer = decoder.getInputBuffer(inIndex);
                        if (buffer == null)return;
                        buffer.clear();
                        buffer.put(result, 0, result.length);
                        buffer.clear();
                        buffer.limit(result.length);
                        decoder.queueInputBuffer(inIndex, 0, result.length, 0, MediaCodec.BUFFER_FLAG_SYNC_FRAME);
                    }else {
                        //结束
//                        isNext=false;
//                        MyLog.i("isNext数据:"+isNext);
                    }
                    int outIndex = decoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_US);
                    MyLog.i("outIndex数据:"+outIndex);
                    while (outIndex >= 0){
                        MyLog.i("outIndex数据2:"+outIndex);
                        decoder.releaseOutputBuffer(outIndex, true);
                        outIndex = decoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_US);
                    }
                } catch (IOException e) {
                    MyLog.e("ioe", e);
                }
            }
        }
    }


    //视频帧信息
    @Subscribe()
    public void asdsa(H264_Result result){

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
