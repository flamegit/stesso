package com.tmmt.innersect.ui.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tmmt.innersect.utils.Util;

public class OrderDecoration extends RecyclerView.ItemDecoration {

    int padding;
    public OrderDecoration() {
        padding=Util.dip2px(16);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int adapterPosition = parent.getChildAdapterPosition(view);
        if(adapterPosition!=0){
            outRect.left=padding;
            outRect.right=padding;
        }
    }

}