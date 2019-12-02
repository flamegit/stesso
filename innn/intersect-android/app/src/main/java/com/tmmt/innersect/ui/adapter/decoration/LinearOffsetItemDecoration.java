package com.tmmt.innersect.ui.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tmmt.innersect.utils.Util;

public class LinearOffsetItemDecoration extends RecyclerView.ItemDecoration {

    int mPadding;
    public LinearOffsetItemDecoration(int padding) {
        mPadding=Util.dip2px(padding);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom=mPadding;

    }

}