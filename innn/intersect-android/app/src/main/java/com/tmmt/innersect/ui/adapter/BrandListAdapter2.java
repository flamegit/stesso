package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.BrandList;
import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.mvp.model.CommonData;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by flame on 2017/8/23.
 */

public class BrandListAdapter2 extends RecyclerView.Adapter<CommonViewHolder> {
    List<CommonAdapterItem<?>> mContent;
    List<String> mLetters;

    public BrandListAdapter2(){
        mContent=new ArrayList<>();
        mLetters=new ArrayList<>();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        if(viewType==Constants.COMMON_TYPE1){
            FlexboxLayout layout=new FlexboxLayout(context);
            layout.setFlexWrap(FlexWrap.WRAP);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new CommonViewHolder(layout);
        }else if(viewType==Constants.COMMON_TYPE2){
            View view=LayoutInflater.from(context).inflate(R.layout.viewholder_brand_item,parent,false);
            return new CommonViewHolder(view);

        }else if(viewType==Constants.HEAD){
            int padding=Util.dip2px(10);
            TextView textView=new TextView(context);
            textView.setPadding(padding,padding,padding,padding);
            textView.setBackgroundColor(Color.parseColor("#F8F8F8"));
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new CommonViewHolder(textView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        Context context=holder.itemView.getContext();
        int padding=Util.dip2px(16);
        if(getItemViewType(position)==Constants.COMMON_TYPE1){
            int size= (Util.getWindowWidth()-Util.dip2px(30))/4;
            FlexboxLayout layout=(FlexboxLayout)holder.itemView;

            layout.removeAllViews();
            List<CommonData> list=( List<CommonData>)mContent.get(position).data;
            for(int i=0;i<list.size();i++){
                CircleImageView imageView=new CircleImageView(context);
                imageView.setBorderColor(Color.parseColor("#DBDBDB"));
                imageView.setBorderWidth(2);
                imageView.setPadding(padding,padding,padding,padding);
                layout.addView(imageView,size,size);
                Util.fillImage(context,list.get(i).url,imageView);
                final  int index=i;
                imageView.setOnClickListener(v -> Util.openTarget(context,list.get(index).schema,""));

            }
        }else if(getItemViewType(position)==Constants.COMMON_TYPE2){
            CommonData data=(CommonData)mContent.get(position).data;
            ImageView imageView=holder.get(R.id.icon_view);
            holder.<TextView>get(R.id.title_view).setText(data.name);
            Glide.with(context).load(data.url).into(imageView);
            holder.itemView.setOnClickListener(v -> Util.openTarget(context,data.schema,""));
        }else {

            TextView textView=(TextView)holder.itemView;
            String title=(String) mContent.get(position).data;
            textView.setText(title);
        }

    }

    public String getLetter(int position){
        return mLetters.get(position);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).mType;
    }

    public void addItems(BrandList list){
        if(list==null ){
            return;
        }

        CommonAdapterItem<List<CommonData>> hot=new CommonAdapterItem<>();
        if(list.zinbrands!=null){
            CommonAdapterItem<String> top=new CommonAdapterItem<>();
            top.mType=Constants.HEAD;
            top.data="热门品牌";
            mContent.add(top);
            mLetters.add("?");
        }
        hot.data=list.zinbrands;
        hot.mType= Constants.COMMON_TYPE1;

        mContent.add(hot);
        mLetters.add("?");

        if(list.brands!=null){
            Collections.sort(list.brands, new Comparator<CommonData>() {
                @Override
                public int compare(CommonData o1, CommonData o2) {
                    String str1= o1.getFirstLetter();
                    String str2= o2.getFirstLetter();
                    return str1.compareTo(str2);
                }
            });
            String pre=null,curr;
            for(CommonData data:list.brands){
                CommonAdapterItem<CommonData> item=new CommonAdapterItem<>();
                item.mType=Constants.COMMON_TYPE2;
                item.data=data;
                curr=data.getFirstLetter();
                if(pre==null || !pre.equals(curr)){
                    CommonAdapterItem<String> title=new CommonAdapterItem<>();
                    title.mType=Constants.HEAD;
                    title.data=curr;
                    mContent.add(title);
                    mLetters.add(curr);
                }
                pre=curr;
                mLetters.add(curr);
                mContent.add(item);
            }
        }
        notifyDataSetChanged();
    }
}
