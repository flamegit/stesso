package com.tmmt.innersect.ui.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.tmmt.innersect.R;
import com.tmmt.innersect.utils.Util;

public class SectionDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "SectionDecoration";

    private DecorationCallback callback;
    private TextPaint bigPaint;
    private TextPaint smallPaint;
    private float leftGap;
    private Paint.FontMetrics fontMetrics;
    Context context;


    public SectionDecoration(Context context, DecorationCallback decorationCallback) {
        this.context=context;
        this.callback = decorationCallback;
        bigPaint = new TextPaint();
        bigPaint.setTypeface(Typeface.DEFAULT_BOLD);
        bigPaint.setAntiAlias(true);
        int big= Util.dip2px(30);
        int small=Util.dip2px(14);
        bigPaint.setTextSize(big);
        bigPaint.setColor(Color.BLACK);
        fontMetrics = new Paint.FontMetrics();
        bigPaint.getFontMetrics(fontMetrics);
        bigPaint.setTextAlign(Paint.Align.LEFT);

        smallPaint = new TextPaint();
        smallPaint.setTypeface(Typeface.DEFAULT);
        smallPaint.setAntiAlias(true);
        smallPaint.setTextSize(small);
        smallPaint.setColor(Color.BLACK);
        smallPaint.setTextAlign(Paint.Align.LEFT);
        leftGap=bigPaint.measureText("00")+smallPaint.measureText("00")+Util.dip2px(20);

    }


    public float getLeftGap(){
        return leftGap;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(leftGap==0){
            int width=parent.getWidth()-parent.getPaddingLeft()-parent.getPaddingRight();
            leftGap=(int)(width*0.25);
        }
        outRect.left=(int)leftGap;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int top=view.getTop();
            float left=view.getPaddingLeft()+view.getLeft()-leftGap;
            int position = parent.getChildAdapterPosition(view);

            String header=callback.getSectionHeader(position);
            String day=header.split("/")[0];
            String month=String.format(context.getString(R.string.month),header.split("/")[1]);
            float offset=bigPaint.measureText(day)+Util.dip2px(5);

            if(callback.isFirstInGroup(position)){
                c.drawText(day,left,top-fontMetrics.ascent,bigPaint);
                c.drawText(month,left+offset,top-fontMetrics.ascent,smallPaint);
            }
        }
    }




    public interface DecorationCallback {

        boolean isFirstInGroup(int pos);

        String getSectionHeader(int pos);


    }
}