package com.tmmt.innersect.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;



public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements IBindViewHolder<T> {

    public BaseViewHolder(View view){
        super(view);
    }


}