package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.moor.imkf.ormlite.stmt.query.In;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.AdapterItem;
import com.tmmt.innersect.mvp.model.BrandItem;
import com.tmmt.innersect.mvp.model.ColorInfo;
import com.tmmt.innersect.mvp.model.CommodityItem;
import com.tmmt.innersect.mvp.model.HotSpotItem;
import com.tmmt.innersect.mvp.model.SearchResult;
import com.tmmt.innersect.mvp.model.StringListItem;
import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
import com.tmmt.innersect.ui.activity.SearchActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;

import java.util.Collections;
import java.util.List;

/**
 * Created by flame on 2017/9/19.
 */

public class SearchAdapter extends BaseAdapter<AdapterItem> {

    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).getType();
    }

    @Override
    protected int getLayoutId(int viewType) {
        switch (viewType){
            case AdapterItem.COMMODITY:
                return R.layout.viewholder_commodity_list2;
            case AdapterItem.HOT_BRAND:
            case AdapterItem.CATEGORY:
                return R.layout.viewholder_search_common;
            case AdapterItem.BRAND:
                return R.layout.viewholder_search_brand;
            case AdapterItem.HISTORY:
            case AdapterItem.HOT_WORD:
                return R.layout.viewholder_search_history;
        }
        return R.layout.viewholder_line;
    }

    public void fillContent(SearchResult result,boolean append){
        if(result==null){
            return;
        }
        if(!append){
            mContent.clear();
        }
        List<BrandItem> brands=result.brands;
        if(brands!=null && !brands.isEmpty()){
           mContent.addAll(brands);
        }
        List<CommodityItem> commodityItems=result.products;
        if(commodityItems!=null && !commodityItems.isEmpty()){
            mContent.addAll(commodityItems);
        }
        notifyDataSetChanged();
    }

    public void fillContent(HotSpotItem hotItem){
        if(hotItem==null){
            return;
        }
        mContent.clear();
        if(hotItem.brands!=null){
            hotItem.brands.setType(AdapterItem.HOT_BRAND);
            mContent.add(hotItem.brands);
        }
        if(hotItem.category!=null){
            hotItem.category.setType(AdapterItem.CATEGORY);
            mContent.add(hotItem.category);
        }
        StringListItem hotWord=new StringListItem();
        hotWord.hot=hotItem.hot;
        hotWord.setType(AdapterItem.HOT_WORD);
        mContent.add(hotWord);
        List<String> historyList=AccountInfo.getInstance().getSearchHistory();
        if(!historyList.isEmpty()){
            StringListItem history=new StringListItem();
            Collections.reverse(historyList);
            history.hot= historyList;
            history.setType(AdapterItem.HISTORY);
            mContent.add(history);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);
        fillViewHolder(holder,position,mContent.get(position));
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, int position, final AdapterItem data) {
        final Context context=holder.itemView.getContext();
        int size= Util.dip2px(8);
        final int margin=Util.dip2px(4);
        switch (data.getType()){

            case AdapterItem.HOT_BRAND:
            case AdapterItem.CATEGORY:

                HotSpotItem.Common hotData=(HotSpotItem.Common)data;
                List<HotSpotItem.SearchDate> brandList=hotData.content;
                LinearLayout linearLayout=holder.get(R.id.container);

                TextView title=holder.get(R.id.title_view);
                String event="search_brand_all";
                final String reportEvent=event;

                if(data.getType()==AdapterItem.HOT_BRAND){
                    title.setText(Util.getString(R.string.hot_brand));holder.get(R.id.show_all).setOnClickListener(v -> {
                        Util.openTarget(context,hotData.more,"");
                        AnalyticsUtil.reportEvent(reportEvent);
                    });
                }else {
                    title.setText(Util.getString(R.string.category));
                    holder.get(R.id.show_more).setVisibility(View.INVISIBLE);
                }

                linearLayout.removeAllViews();
                int width=(Util.getWindowWidth()-10*size)/4;
                int c= Math.min(4,brandList.size());

                for(int i=0;i<c;i++){
                    View view= LayoutInflater.from(context).inflate(R.layout.couple_item2,linearLayout,false);
                    ImageView imageView=view.findViewById(R.id.icon_view);
                    TextView textView=view.findViewById(R.id.title_view);
                    final int index=i;
                    view.setOnClickListener(v ->{
                        if(index>>1==0){
                            AnalyticsUtil.reportEvent("search_category_click");
                        }else {
                            AnalyticsUtil.reportEvent("search_brand_click");
                        }
                        Util.openTarget(context,brandList.get(index).schema,"");

                    });
                    if(data.getType()==AdapterItem.CATEGORY){
                        textView.setText(brandList.get(i).name);
                    }
                    Glide.with(context).load(brandList.get(i).picUrl).into(imageView);
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
                    params.setMargins(size,size,size,size);
                    view.setLayoutParams(params);
                    linearLayout.addView(view);
                }
                break;
            case AdapterItem.COMMODITY:
                final CommodityItem commodityItem=(CommodityItem) data;
                List<ColorInfo> colorInfos=commodityItem.colorValList;
                LinearLayout colors=holder.get(R.id.color_container);
                colors.removeAllViews();
                if(colorInfos!=null){
                    int count=Math.min(5,colorInfos.size());
                    if(count>1){
                        for(int i=0;i<count;i++){
                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(size,size);
                            params.setMargins(margin/2,margin,margin/2,margin);
                            View view=new View(context);
                            if(colorInfos.get(i).displayName!=null){
                                try{
                                    setColor(view, Color.parseColor(colorInfos.get(i).displayName));
                                    colors.addView(view,params);
                                }catch (IllegalArgumentException e){

                                }
                            }
                        }
                    }
                }
                if(commodityItem.isSaleOut()){
                    holder.get(R.id.sale_out).setVisibility(View.VISIBLE);
                }else {
                    holder.get(R.id.sale_out).setVisibility(View.GONE);
                }
                holder.<TextView>get(R.id.title_view).setText(commodityItem.brandName);
                ImageView imageView=holder.get(R.id.icon_view);
                Glide.with(context).load(commodityItem.getThumbnail()).error(R.mipmap.commodity_default)
                        .into(imageView);
                holder.<TextView>get(R.id.desc_view).setText(commodityItem.name);
                holder.<TextView>get(R.id.price_view).setText("￥"+ commodityItem.salePrice);
                if(commodityItem.originalPrice!=commodityItem.salePrice){
                    TextView originPrice=holder.get(R.id.origin_price);
                    originPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    originPrice.setText("￥" +commodityItem.originalPrice);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(commodityItem.isSaleOut()){
                            CommodityDetailActivity.start(context,commodityItem.productId,true);
                        }else {
                            CommodityDetailActivity.start(context,commodityItem.productId);
                        }
                    }
                });

                break;
            case AdapterItem.BRAND:
                final BrandItem brandItem=(BrandItem) data;
                ImageView logo=holder.get(R.id.brand_icon);
                Glide.with(context).load(brandItem.logoUrl).into(logo);
                holder.<TextView>get(R.id.title_view).setText(brandItem.name);
                holder.itemView.setOnClickListener(v->
                        Util.openTarget(context,brandItem.desc,"")
                );
                break;

            case AdapterItem.HISTORY:
            case AdapterItem.HOT_WORD:
                TextView titleView=holder.get(R.id.title_view);
                View delete=holder.get(R.id.delete_view);
                if(data.getType()==AdapterItem.HISTORY){
                    titleView.setText(Util.getString(R.string.search_history));
                    AnalyticsUtil.reportEvent("search_history_click");
                    delete.setOnClickListener(v -> {
                        if(mListener!=null && data.getType()==AdapterItem.HISTORY){
                            AnalyticsUtil.reportEvent("search_history_clear");
                            mListener.onItemClick(position,data);
                        }
                    });
                }else {
                    titleView.setText(Util.getString(R.string.hot_search));
                    delete.setVisibility(View.INVISIBLE);
                }
                FlexboxLayout layout=holder.get(R.id.container);
                layout.removeAllViews();
                final StringListItem history=(StringListItem) data;
                List<String> words=history.hot;
                for(int i=0;i<words.size();i++){
                    View view= LayoutInflater.from(context).inflate(R.layout.include_text,layout,false);
                    TextView text=view.findViewById(R.id.text_view);
                    final int index=i;
                    view.setOnClickListener(v ->
                    {
                        AnalyticsUtil.reportEvent("search_hot_click");
                        Util.startActivity(context, SearchActivity.class, Constants.KEY_WORD,words.get(index));
                    });
                    text.setText(words.get(i));
                    layout.addView(view);
                }
                break;
        }
    }

    public void deleteHistory(int position){
        AccountInfo.getInstance().deleteHistory();
        mContent.remove(mContent.size()-1);
        notifyItemRemoved(position);
    }

    private void setColor(View view,int color){
        view.setBackgroundResource(R.drawable.circle_fill_bg);
        GradientDrawable mm= (GradientDrawable)view.getBackground();
        mm.setColor(color);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return  mContent.get(position).getSpanSize();
                }
            });
        }
    }
}
