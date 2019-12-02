package com.tmmt.innersect.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/8/23.
 */

public  class MultiAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    public MultiAdapter(){
        mContent=new ArrayList<>();
    }

    private List<CommonAdapterItem> mContent;

    private SparseArray<ViewTypeDelegateAdapter> mDelegateAdapter;


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return mDelegateAdapter.get(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        int type=mContent.get(position).mType;
        mDelegateAdapter.get(type).onBindViewHolder(holder,mContent.get(position));
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public void addNewsItems( List<Information> items){

        if(items!=null && !items.isEmpty()){
            mContent.clear();
            mContent.addAll(transform(items));
        }
        notifyDataSetChanged();
    }


    private List<CommonAdapterItem> transform(List<?> list){
        List<CommonAdapterItem> adapterList=new ArrayList<>();
        for (Object data:list) {
            CommonAdapterItem item=new CommonAdapterItem();
            item.data=data;
        }
        return adapterList;
    }
}
