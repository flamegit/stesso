package com.tmmt.innersect.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by flame on 2017/4/18.
 */

public class RangeSeekBar extends View {

    private final static int IDLE=0;

    private final static int LEFT_MOVE=1;

    private final static int RIGHT_MOVE=2;


    private Paint mBarPaint;

    private Paint mTextPaint;

    private Paint mSelectPaint;

    private RectF mBarRect;

    //need config;

    private int mTextSize;

    private int mDivideWidth;

    private int mCursorHeight;

    private int mCursorWidth;

    private int mBarHeight;

    private int mBarWidth;

    private int mLeftIndex;

    private int mRightIndex;

    private RectF mLeftRect;

    private RectF mRightRect;


    private int mNodeRadius;

    private int mCount;

    private int mStatus;

    private int mOffset;

    private int lmin;
    private int lmax;

    private int rmin;
    private int rmax;


    private float downx;
    private float downy;

    int slop;


    public RangeSeekBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }


    private void init(){

        mBarPaint=new Paint();
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setColor(Color.GRAY);

        mTextPaint=new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setUnderlineText(false);

        mSelectPaint=new Paint();
        mSelectPaint.setColor(Color.GREEN);

        mBarRect=new RectF();
        mLeftRect=new RectF();
        mRightRect=new RectF();

        mTextSize=30;
        mBarHeight=10;
        mCursorHeight=40;
        mCursorWidth=20;
        mCount=4;
        mNodeRadius=10;
        mLeftIndex=0;
        mRightIndex=3;

        mStatus=IDLE;
        slop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int offset=mCursorWidth/2;
        mBarRect.left=getPaddingLeft()+offset;
        mBarRect.top=getPaddingTop()+(mCursorHeight-mBarHeight)/2;
        mBarRect.right=w-getPaddingRight()-offset;
        mBarWidth=w-getPaddingLeft()-getPaddingRight()-mCursorWidth;
        mBarRect.bottom=mBarRect.top+mBarHeight;
        mDivideWidth=mBarWidth/mCount;

        //TODO
        mOffset=mDivideWidth/3;

        mLeftRect.left=getPaddingLeft()+mLeftIndex*mDivideWidth;
        mLeftRect.top = getPaddingTop();
        mLeftRect.right=mLeftRect.left+mCursorWidth;
        mLeftRect.bottom=getPaddingTop()+mCursorHeight;

        mRightRect.left = getPaddingLeft()+(mRightIndex * mDivideWidth);
        mRightRect.top = getPaddingTop();
        mRightRect.right=mRightRect.left+mCursorWidth;
        mRightRect.bottom=getPaddingTop()+mCursorHeight;

        lmin=getPaddingLeft();
        lmax=lmin+(mCount-1)*mDivideWidth;

        rmin=getPaddingLeft()+mDivideWidth;
        rmax=w-getPaddingRight();


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(super.onTouchEvent(event)){
            return true;
        }

        float x=event.getX();
        float y=event.getY();

        int offset=50;

        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:
                downx=x;
                downy=y;

                if(contains(offset,mLeftRect,x,y)){
                    mStatus=LEFT_MOVE;

                }else if(contains(offset,mRightRect,x,y)){
                    mStatus=RIGHT_MOVE;

                }else {
                    mStatus=IDLE;
                    return false;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if(mStatus==LEFT_MOVE){

                    mLeftRect.left=constrain(x,lmin,lmax);
                    mLeftRect.right=mLeftRect.left+mCursorWidth;

                    if(mRightRect.left-mLeftRect.left<mDivideWidth){
                        mRightRect.left=mLeftRect.left+mDivideWidth;
                        mRightRect.right=mRightRect.left+mCursorWidth;
                    }
                }
                if(mStatus==RIGHT_MOVE){

                    mRightRect.left=constrain(x,rmin,rmax);
                    mRightRect.right=mRightRect.left+mCursorWidth;

                    if(mRightRect.left-mLeftRect.left<mDivideWidth){
                        mLeftRect.left=mRightRect.left-mDivideWidth;
                        mLeftRect.right=mLeftRect.left+mCursorWidth;
                    }

                }
                if(Math.abs(x-downx)>slop){
                    postInvalidate();

                }
                break;
            case MotionEvent.ACTION_UP:

                mLeftIndex=calculateIndex(mLeftRect);
                mLeftRect.left=getPaddingLeft()+mLeftIndex*mDivideWidth;
                mLeftRect.right=mLeftRect.left+mCursorWidth;

                mRightIndex=calculateIndex(mRightRect);
                mRightRect.left=getPaddingLeft()+mRightIndex*mDivideWidth;
                mRightRect.right=mRightRect.left+mCursorWidth;

                postInvalidate();

                break;

        }
        return true;

    }

    private float constrain(float x,int min,int max){
        if(x<=min){
            return min;
        }
        if(x>=max){
            return max;
        }
        return x;
    }

    private int calculateIndex(RectF rect){

        return (int) (rect.left-getPaddingLeft()+mOffset)/mDivideWidth;

    }


    private boolean contains(int offset,RectF rectF,float x,float y){
        return x>=rectF.left-offset && x<=rectF.right+offset
                && y>=rectF.top-offset && y<=rectF.bottom+offset;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        int result = 0;
//
//        int specMode = MeasureSpec.getMode(heightMeasureSpec);
//        int specSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        if (specMode == MeasureSpec.EXACTLY) {
//            //result = bmpHeight * 2;
//        } else {
//            //result = bmpHeight + paddingTop;
//
//            if (specMode == MeasureSpec.AT_MOST) {
//                result = Math.min(result, specSize);
//            }
//        }


    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int y =getPaddingTop()+mCursorHeight+mTextSize;


        canvas.drawRect(mBarRect,mBarPaint);

        canvas.drawRect(mLeftRect.left,mBarRect.top,mRightRect.left,mBarRect.bottom,mSelectPaint);

        canvas.drawRoundRect(mLeftRect,5,5,mBarPaint);
        canvas.drawRoundRect(mRightRect,5,5,mBarPaint);

        for(int i=0;i<=mCount;i++){
            canvas.drawCircle(mBarRect.left+i*mDivideWidth,mBarRect.top+mNodeRadius/2,mNodeRadius,mBarPaint);
            //mTextPaint.measureText("",0,0);
            canvas.drawText(""+i*1000,getPaddingLeft()+i*mDivideWidth,y,mTextPaint);

        }
    }
}
