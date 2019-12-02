package com.tmmt.innersect.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;


/**
 * Created by flame on 2017/7/24.
 */

public class FooterDelegateAdapter implements ViewTypeDelegateAdapter {


    public FooterDelegateAdapter(int layoutId){
        mLayoutId=layoutId;
    }

    private int mLayoutId;

    @Override
    public void onBindViewHolder(CommonViewHolder holder, CommonAdapterItem item) {}

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(mLayoutId,parent,false);
        return new CommonViewHolder(view);
    }

}
