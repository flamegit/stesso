package com.tmmt.innersect.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/6/22.
 */

public class WheelView extends LinearLayout {

    private Scroller mScroller;
    private int height= Util.dip2px(100);
    int startY;

    ScrollListener mListener;

    public interface ScrollListener{
        void onScrollFinish();
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(context);
        startY=0;
        setTranslationY(-3*height);
    }

    public void startScroll(int num,int duration){
        int distance=-num *height;
        mScroller.startScroll(0,startY,0,distance,duration);

        invalidate();
    }
    public void setScrollListener(ScrollListener listener){
        mListener=listener;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY()%(3*height));
            postInvalidate();
        }else {
            startY=mScroller.getCurrY()%(3*height);
            if(mListener!=null){
                mListener.onScrollFinish();
            }
        }
    }

    public void setIndex(int index){
        scrollTo(0,-index*height);
    }

    public int getIndex(){
        return Math.abs(getScrollY()/height)%3;
    }
}
