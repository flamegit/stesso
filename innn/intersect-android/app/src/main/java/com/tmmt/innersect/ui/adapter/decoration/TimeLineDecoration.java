package com.tmmt.innersect.ui.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tmmt.innersect.utils.Util;

public class TimeLineDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "SectionDecoration";

    private Paint nodePaint;
    private Paint linePaint;
    private int leftGap;
    private int radio= Util.dip2px(4);
    private int lineWidth=Util.dip2px(2);
    private int offset=Util.dip2px(7);
    private int topOffset=Util.dip2px(12);
    private int leftOffset=2*radio;

    public TimeLineDecoration(Context context) {
        nodePaint=new Paint();
        nodePaint.setAntiAlias(true);
        linePaint=new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#F0F0F0"));
        leftGap=Util.dip2px(16);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left=leftGap;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int top=view.getTop()+view.getPaddingTop()+topOffset;
            int bottom=view.getBottom();
            if(i==0){

                nodePaint.setColor(Color.parseColor("#F8E638"));
                c.drawCircle(leftOffset,top,leftOffset,nodePaint);
                nodePaint.setColor(Color.parseColor("#F8DA38"));
                c.drawCircle(leftOffset,top,radio,nodePaint);
                nodePaint.setColor(Color.parseColor("#9A9B9D"));

            }else {
                c.drawCircle(leftOffset,top,radio,nodePaint);
            }
            if(i<childCount-1){
                c.drawRect(offset,top+(i==0?4:3)*radio,offset+lineWidth,bottom,linePaint);
            }
        }
    }

}