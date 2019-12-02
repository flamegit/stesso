package com.tmmt.innersect.ui.adapter;

import android.view.ViewGroup;

import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;

interface ViewTypeDelegateAdapter {

    CommonViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(CommonViewHolder holder,CommonAdapterItem data);

}