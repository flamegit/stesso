package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.CommonData;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;
/**
 * Created by flame on 2017/8/23.
 */

public class BrandListAdapter extends BaseAdapter<CommonData> {

    private int mCount;
    private int mPadding;
    private int mType;

    public BrandListAdapter(int count,int type){
        mCount=count;
        mPadding= Util.dip2px(8);
        mType=type;
    }

    @Override
    protected int getLayoutId(int viewType) {
        if(mType==0){
            return R.layout.couple_item;
        }else {
            return R.layout.couple_item2;
        }
    }

    @Override
    protected View createView(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View item= LayoutInflater.from(context).inflate(getLayoutId(mType),parent,false);
        int w=parent.getWidth()-(2*(mCount+1))*mPadding;
        int size=w/mCount;
        int h=size;
        if(mType==0){
            h+=Util.dip2px(16);
        }
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(size,h);
        layoutParams.setMargins(mPadding,mPadding,mPadding,mPadding);
        item.setLayoutParams(layoutParams);
        return item;
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, final int position, final CommonData data) {
        final Context context=holder.itemView.getContext();
        ImageView imageView=holder.get(R.id.icon_view);
        holder.<TextView>get(R.id.title_view).setText(data.name);
        Glide.with(context).load(data.url).into(imageView);
        holder.itemView.setOnClickListener(v -> Util.openTarget(context,data.schema,""));
    }
}
