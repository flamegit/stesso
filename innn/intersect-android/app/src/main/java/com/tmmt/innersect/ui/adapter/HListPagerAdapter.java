package com.tmmt.innersect.ui.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.CommonItem;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class HListPagerAdapter extends android.support.v4.view.PagerAdapter {

    protected List<CommonItem> mContent;


    public HListPagerAdapter(){
        mContent=new ArrayList<>();
    }

    @Override
    public int getCount() {
      return (mContent.size()+2)/3;
    }

    public void addItems(Collection<CommonItem> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
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
    public Object instantiateItem(final ViewGroup container, int position) {
        Context context=container.getContext();
        int w=Util.getWindowWidth()/3;
        LinearLayout view = new LinearLayout(context);
        int begin=position*3;
        int end=Math.min(mContent.size(),begin+3);
        for(int i=begin;i<end;i++){
            View item = LayoutInflater.from(context).inflate(R.layout.include_new_item, container, false);
            ImageView imageView = item.findViewById(R.id.icon_view);
            TextView textView = item.findViewById(R.id.title_view);
            TextView priceView = item.findViewById(R.id.price_view);
            TextView originPrice = item.findViewById(R.id.origin_price);
            View saleOut=item.findViewById(R.id.sale_out);
            saleOut.setVisibility(mContent.get(i).isSaleOut()?View.VISIBLE:View.INVISIBLE);
            Util.fillImage(context,mContent.get(i).picUrl,imageView);

            textView.setText(mContent.get(i).brand);

            priceView.setText("￥"+Util.getPrice(mContent.get(i).salePrice));
            final int index=i;
            item.setOnClickListener(v -> {
                Util.openTarget(context, mContent.get(index).schema, "");
                AnalyticsUtil.reportEvent("home_content_click");
            });
            if (mContent.get(i).originalPrice > mContent.get(i).salePrice) {
                originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                originPrice.setText(String.format("￥%.2f",mContent.get(i).originalPrice));
            }else {
                originPrice.setText("");
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT);
            view.addView(item,params);
        }
        container.addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }
}