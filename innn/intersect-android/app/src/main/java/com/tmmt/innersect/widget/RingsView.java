package com.tmmt.innersect.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.tmmt.innersect.R;
import com.tmmt.innersect.utils.Util;

/**
 * Created by Administrator on 2016/8/30.
 */
public class RingsView extends View {

    private Paint paint;
    private Paint textPaint;
    private Paint ringPaint;
    private int centerX;
    private int centerY;
    private int originRadius;
    private int inRadius;
    private int outRadius;
    private int inAlpha;
    private int outAlpha;
    private float textHeight;
    private float textWidth;
    private String text;
    private int ascent;

    AnimatorSet mSet;

    Paint.FontMetrics metrics;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        textWidth= textPaint.measureText(text);
        textHeight= textPaint.descent()-textPaint.ascent();
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) textPaint.measureText(text);
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        ascent = (int) textPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-ascent + textPaint.descent());
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }



    public RingsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        originRadius=Util.dip2px(50);
        inRadius=outRadius=originRadius;
        inAlpha=outAlpha=255;

        textPaint=new Paint();
        textPaint.setTextSize(Util.sp2px(14));

        metrics=textPaint.getFontMetrics();

        text=context.getString(R.string.order);

        paint=new Paint();
        paint.setColor(Color.parseColor("#F8E638"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        ringPaint=new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setColor(Color.parseColor("#F8E638"));
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(Util.dip2px(2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(centerX,centerY,originRadius,paint);
        ringPaint.setAlpha(inAlpha);
        canvas.drawCircle(centerX,centerY,inRadius,ringPaint);

        ringPaint.setAlpha(outAlpha);
        canvas.drawCircle(centerX,centerY,outRadius,ringPaint);
        //canvas.drawText("");
        canvas.drawText(text, centerX-textWidth/2, textHeight/2+centerY-metrics.bottom, textPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX=w/2;
        centerY=h/2;
    }

    public void setInAlpha(int inAlpha) {
        this.inAlpha = inAlpha;
        postInvalidate();
    }

    public void setInRadius(int inRadius) {
        this.inRadius = inRadius;
        postInvalidate();
    }

    public void setOutAlpha(int outAlpha) {
        this.outAlpha = outAlpha;
        postInvalidate();
    }

    public void setOutRadius(int outRadius) {
        this.outRadius = outRadius;
        postInvalidate();
    }

    public void spread(){

        ObjectAnimator animator1=createAnimator("inRadius","inAlpha",Util.dip2px(60),50);
        ObjectAnimator animator2=createAnimator("outRadius","outAlpha",Util.dip2px(70),0);
        mSet=new AnimatorSet();
        mSet.playTogether(animator1,animator2);
        mSet.start();

    }

    public void stop(){
        if(mSet!=null){
            mSet.cancel();
        }
    }


    private ObjectAnimator createAnimator(String radius,String alpha,int endRadius,int endAlpha){

        PropertyValuesHolder radiusHolder=PropertyValuesHolder.ofInt(radius,originRadius,endRadius);
        PropertyValuesHolder alphaHolder=PropertyValuesHolder.ofInt(alpha,255,endAlpha);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(this,radiusHolder,alphaHolder).setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                postInvalidate();
            }
        });
        animator.setRepeatCount(1500);
        return animator;
    }
}