package com.tmmt.innersect.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/8/23.
 */

public  class AdvancedAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    public interface BindViewHolder<T>{
        void onBind(CommonViewHolder holder,int position, T data);
    }

    public AdvancedAdapter(int layoutId, BindViewHolder<T> bind){
        mContent=new ArrayList<>();
        mLayoutId=layoutId;
        mBind=bind;
    }

    protected List<T> mContent;

    protected BindViewHolder<T> mBind;

    private int mLayoutId;


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= createView(parent,viewType);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        mBind.onBind(holder,position,mContent.get(position));
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    protected  View createView(ViewGroup parent,int viewType){
        return LayoutInflater.from(parent.getContext()).inflate(mLayoutId,parent,false);
    }

    public void addItems( List<T> items){
        if(items!=null && !items.isEmpty()){
            mContent.clear();
            mContent.addAll(items);
            notifyDataSetChanged();
        }
    }

    public T getItem(int position){
        if(mContent==null){
            return null;
        }
        return mContent.get(position);
    }

    public void appendItems(List<T> items){
        if(items!=null && !items.isEmpty()){
            mContent.addAll(items);
            notifyDataSetChanged();
        }
    }
}
