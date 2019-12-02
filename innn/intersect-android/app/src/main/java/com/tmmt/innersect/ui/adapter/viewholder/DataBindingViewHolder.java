package com.tmmt.innersect.ui.adapter.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.tmmt.innersect.BR;

/**
 * Created by flame on 2017/9/4.
 */

public class DataBindingViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mDataBinding;

    public DataBindingViewHolder(ViewDataBinding dataBinding){
        super(dataBinding.getRoot());
        mDataBinding=dataBinding;
    }

    public void onBind(Object object){
        mDataBinding.setVariable(BR.data,object);
        mDataBinding.executePendingBindings();
    }

}


