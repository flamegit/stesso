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

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    public interface OnItemClickListener<T>{
        void onItemClick(int position,T data);
    }
    public BaseAdapter(){
        mContent=new ArrayList<>();
    }

    protected List<T> mContent;

    protected OnItemClickListener<T> mListener;

    public void setListener(OnItemClickListener listener){
        mListener=listener;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= createView(parent,viewType);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(position,mContent.get(position));
                }
            }
        });

        fillViewHolder(holder,position,mContent.get(position));
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    protected abstract int getLayoutId(int viewType);

    protected  View createView(ViewGroup parent,int viewType){
        return LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType),parent,false);
    }

    protected abstract void fillViewHolder(CommonViewHolder holder,final int position,T data);

    public void addItems( List<T> items){
        mContent.clear();
        if(items!=null && !items.isEmpty()){
            mContent.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void appendItems(List<T> items){
        if(items==null ||items.isEmpty()){
            return;
        }
        mContent.addAll(items);
        notifyDataSetChanged();
    }
}
