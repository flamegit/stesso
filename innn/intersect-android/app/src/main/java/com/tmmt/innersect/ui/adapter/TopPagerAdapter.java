package com.tmmt.innersect.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.tmmt.innersect.mvp.model.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class TopPagerAdapter extends android.support.v4.view.PagerAdapter {

    private ArrayList<ImageInfo> mContent;

    private OnItemClick mListener;

    public TopPagerAdapter(Activity activity) {
        mContent = new ArrayList<>();
    }
    public void changeContent(List<ImageInfo> content) {
        if(content==null){
            return;
        }
        mContent.clear();
        mContent.addAll(content);
        notifyDataSetChanged();
    }

    public interface  OnItemClick{
        void onItemClick(int index);
    }

    public void setListener( OnItemClick listener){
        mListener=listener;
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mContent.size();
    }

    public void addItems(List<ImageInfo> lists) {
        mContent.clear();
        mContent.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView contentView = new ImageView(container.getContext());
        contentView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contentView.setPadding(0,0,0,0);
        container.addView(contentView);
        Glide.with(container.getContext())
                .load(mContent.get(position).imgUrl)
                .into(contentView);
        contentView.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.onItemClick(position);
            }
        });
        return contentView;
    }
}