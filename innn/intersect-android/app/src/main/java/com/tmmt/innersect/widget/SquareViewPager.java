package com.tmmt.innersect.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by flame on 2017/8/3.
 */

public class SquareViewPager extends ViewPager {

    public SquareViewPager(Context context, AttributeSet att){
        super(context,att);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

