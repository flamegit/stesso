package com.tmmt.innersect.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;

/**
 * Created by flame on 2017/10/26.
 */

public class GapColorDrawable extends ColorDrawable {

    private Paint mGapPaint;

    private int mDirection;

    public GapColorDrawable(int direction){
        super();
        mDirection=direction;
        init();
    }

    public GapColorDrawable(@ColorInt int color,int direction){
        super(color);
        mDirection=direction;
        init();
    }

    private void init(){
        mGapPaint=new Paint();
        mGapPaint.setAntiAlias(true); //设置画笔为无锯齿
        //mGapPaint.setStyle(Paint.Style.STROKE);
        mGapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
    }



    @Override
    public void draw(Canvas canvas) {
        Rect rect=getBounds();
        RectF rectf=new RectF(rect);
        int save = canvas.saveLayer(rectf,null,Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        int radius=rect.height()/16;
        if(mDirection==1){
            canvas.drawCircle(rect.left,rect.centerY(),radius,mGapPaint);
        }else {
            canvas.drawCircle(rect.right,rect.centerY(),radius,mGapPaint);
        }
        //canvas.drawArc(new RectF(10f,10f,80f,80f),90,180,false,mGapPaint);
        canvas.restoreToCount(save);
    }
}
