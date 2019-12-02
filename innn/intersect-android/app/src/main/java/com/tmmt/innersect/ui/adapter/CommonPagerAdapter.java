package com.tmmt.innersect.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class CommonPagerAdapter<T> extends android.support.v4.view.PagerAdapter {


    public interface ViewFactory<T> {
        View createView(final ViewGroup container, T content);
    }

    protected List<T> mContent;
    protected int mLoopCount;

    protected ViewFactory<T> mFactory;

    public CommonPagerAdapter() {
        this(true);
    }

    @Override
    public int getCount() {
        if (mContent.size() == 1) {
            return mContent.size();
        }
        return mContent.size() * mLoopCount;
    }

    public void addItems(Collection<T> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        mContent.clear();
        mContent.addAll(lists);
        notifyDataSetChanged();
    }

    public CommonPagerAdapter(boolean loop) {
        mContent = new ArrayList<>();
        if (loop) {
            mLoopCount = 1000;
        } else {
            mLoopCount = 1;
        }
        mFactory = (container, content) -> {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.included_card, container, false);
            ImageView imageView = view.findViewById(R.id.image_view);
            Glide.with(container.getContext()).load(content).into(imageView);
            return view;
        };
    }

    public CommonPagerAdapter(boolean loop, ViewFactory<T> factory) {
        mContent = new ArrayList<>();
        if (loop) {
            mLoopCount = 1000;
        } else {
            mLoopCount = 1;
        }
        mFactory = factory;
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
    public Object instantiateItem(final ViewGroup container, int position) {

        int size = mContent.size();
        final int p = position % size;
        View view = mFactory.createView(container, mContent.get(p));
        container.addView(view);
        return view;
    }
}