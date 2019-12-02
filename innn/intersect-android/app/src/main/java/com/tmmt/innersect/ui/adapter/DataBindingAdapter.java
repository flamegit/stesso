package com.tmmt.innersect.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tmmt.innersect.ui.adapter.viewholder.DataBindingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/12/13.
 */

public class DataBindingAdapter<T> extends RecyclerView.Adapter<DataBindingViewHolder> {

    public DataBindingAdapter(int layoutId) {
        mContent = new ArrayList<>();
        mLayoutId = layoutId;
    }


    public DataBindingAdapter(int layoutId,OnItemClickListener<T> listener) {
        mContent = new ArrayList<>();
        mLayoutId = layoutId;
        mListener=listener;
    }

    public interface OnItemClickListener<T>{
        void onItemClick(DataBindingViewHolder holder,int position,T data);
    }

    protected OnItemClickListener<T> mListener;
    List<T> mContent;
    int mLayoutId;

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        holder.onBind(mContent.get(position));

        holder.itemView.setOnClickListener(
            v->{
                if(mListener!=null){
                    mListener.onItemClick(holder,position,mContent.get(position));
                }
            }
        );
    }

    @Override
    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);
        DataBindingViewHolder viewHolder = new DataBindingViewHolder(viewBinding);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public void addItems( List<T> items){
        mContent.clear();
        if(items!=null && !items.isEmpty()){
            mContent.addAll(items);
        }
        notifyDataSetChanged();
    }
}
