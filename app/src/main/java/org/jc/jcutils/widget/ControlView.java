package org.jc.jcutils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 控制界面背景
 * Created by a3552 on 2017/5/22.
 */

public class ControlView extends View{
    private Paint mPaint  = new Paint() ;

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    //竖三条线平分
    //横三条线平分
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();
        int with = getMeasuredWidth();

    // 绘制背景（一个矩形框），长度为getMeasuredWidth()，高度为：getMeasuredHeight()
//        canvas.drawRect(1, 1, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        canvas.drawLine(with*0.25f, 0, with*0.25f, height, mPaint);//画直线1
        canvas.drawLine(with*0.5f, 0, with*0.5f, height, mPaint);//画直线2
        canvas.drawLine(with*0.75f, 0, with*0.75f, height, mPaint);//画直线3

        canvas.drawLine(0, height*0.25f, with, height*0.25f, mPaint);//画直线
        canvas.drawLine(0, height*0.5f, with, height*0.5f, mPaint);//画直线
        canvas.drawLine(0, height*0.75f, with, height*0.75f, mPaint);//画直线

    }
}
