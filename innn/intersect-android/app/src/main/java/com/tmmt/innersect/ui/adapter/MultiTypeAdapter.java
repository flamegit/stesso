package com.tmmt.innersect.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/8/23.
 */

public  class MultiTypeAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    public interface BindViewHolder<T>{
        void onBind(CommonViewHolder holder, int position, CommonAdapterItem<T> data);
    }

    public MultiTypeAdapter(SparseArray<Integer> type2Layout, BindViewHolder<T> bind){
        mContent=new ArrayList<>();
        mBind=bind;
        mType2Layout=type2Layout;
    }

    protected List<CommonAdapterItem<T>> mContent;

    protected BindViewHolder<T> mBind;
    private SparseArray<Integer> mType2Layout;


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
        return LayoutInflater.from(parent.getContext()).inflate(mType2Layout.get(viewType),parent,false);
    }


    public void showFooter(){
        CommonAdapterItem<T> footer=new CommonAdapterItem();
        footer.mType= Constants.FOOTER;
        mContent.add(footer);
        notifyItemInserted(mContent.size()-1);
    }

    protected void addItems( List<T> items){
        if(items==null || items.isEmpty()){
            return;
        }
    }

    public static SparseArray<Integer> create(int layout1,int layout2){
        SparseArray<Integer> array=new SparseArray<>();
        array.put(Constants.COMMON_TYPE1,layout1);
        array.put(Constants.COMMON_TYPE1,layout2);
        return array;
    }
}
