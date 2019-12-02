package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.AdapterItem;
import com.tmmt.innersect.mvp.model.BrandInfo;
import com.tmmt.innersect.mvp.model.BrandItem;
import com.tmmt.innersect.mvp.model.ColorInfo;
import com.tmmt.innersect.mvp.model.CommodityItem;
import com.tmmt.innersect.mvp.model.EndItem;
import com.tmmt.innersect.mvp.model.PopupInfo;
import com.tmmt.innersect.mvp.model.TitleItem;
import com.tmmt.innersect.mvp.model.UnfoldItem;
import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
import com.tmmt.innersect.ui.activity.CommodityListActivity;
import com.tmmt.innersect.ui.activity.DiscountListActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;

import java.util.List;
/**
 * Created by flame on 2017/9/19.
 */

public class PopupAdapter extends BaseAdapter<AdapterItem> {
    private String mAid;
    public PopupAdapter(String aid){
        mAid=aid;
    }


    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).getType();
    }

    @Override
    protected int getLayoutId(int viewType) {
        switch (viewType){
            case AdapterItem.COMMODITY:
                return R.layout.viewholder_commodity_list2;
            case AdapterItem.LINE:
                return R.layout.viewholder_line;
            case AdapterItem.UNFOLD:
                return R.layout.viewholder_center_button;
            case AdapterItem.TITLE:
                return R.layout.viewholder_popup_title;
            case AdapterItem.BRAND:
                return R.layout.viewholder_brand;
            case AdapterItem.END:
                return R.layout.viewholder_end;
            case AdapterItem.EMPTY:
                return R.layout.include_empty_view;

        }
        return R.layout.viewholder_line;
    }

    public void fillContent(PopupInfo info){
        if(info==null){
            return;
        }
        List<CommodityItem> discounts=info.discounts;
        if(discounts!=null && !discounts.isEmpty()){
            mContent.add(new TitleItem("限时折扣","Discount"));
            discounts.get(0).setSpan(true);
            int end=Math.min(5,discounts.size());
            mContent.addAll(discounts.subList(0,end));
            if(discounts.size()>5){
                mContent.add(new UnfoldItem(-1));
            }else {
                mContent.add(new AdapterItem());
            }
        }
        List<BrandInfo> brandInfos=info.brands;
        if(brandInfos!=null && !brandInfos.isEmpty()){
            mContent.add(new TitleItem("品牌闪购","Brand"));
            for(int i=0;i<brandInfos.size();i++){
                BrandInfo brandInfo=brandInfos.get(i);
                mContent.add(new BrandItem(brandInfo.desc,brandInfo.logoUrl,brandInfo.name,brandInfo.bgUrl,brandInfo.brandId));
                List<CommodityItem> products=brandInfo.products;
                KLog.i(products.size());
                int end=Math.min(2,products.size());
                mContent.addAll(products.subList(0,end));
                if(products.size()>=2){
                    mContent.add(new UnfoldItem(brandInfo.brandId));
                }else {
                    mContent.add(new AdapterItem());
                }
            }
        }
        if(info.recommends!=null && !info.recommends.isEmpty()){
            mContent.add(new TitleItem("为你推荐","For you"));
            info.recommends.get(0).setSpan(true);
            mContent.addAll(info.recommends);
            mContent.add(new EndItem());
        }
        notifyDataSetChanged();
    }

    public void fillContent( List<CommodityItem> list,boolean append){
        if(!append){
            mContent.clear();
        }
        if(list!=null && !list.isEmpty()) {
            mContent.addAll(list);
            notifyDataSetChanged();
        }
//        }else if(!append){
//            AdapterItem item=new AdapterItem();
//            item.setType(AdapterItem.EMPTY);
//            mContent.add(item);
//        }
    }

    public void fillContent(BrandInfo info,boolean append){
        if(!append){
            mContent.clear();
        }
        if(info.products!=null && !info.products.isEmpty()) {
            mContent.addAll(info.products);
        }
//        }else if(!append){
//            AdapterItem item=new AdapterItem();
//            item.setType(AdapterItem.EMPTY);
//            mContent.add(item);
//        }
        notifyDataSetChanged();
    }

    public void fillContent(List<?extends AdapterItem> list){
        mContent.clear();
        if(list!=null && !list.isEmpty()){
            mContent.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, int position, final AdapterItem data) {
        final Context context=holder.itemView.getContext();
        int size= Util.dip2px(8);
        final int margin=Util.dip2px(4);
        switch (data.getType()){
            case AdapterItem.TITLE:
                TitleItem item=(TitleItem)data;
                holder.<TextView>get(R.id.title_view).setText(item.mTitle);
                holder.<TextView>get(R.id.subtitle_view).setText(item.mSubTitle);
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
                                }catch (IllegalArgumentException e){}
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
                holder.<TextView>get(R.id.price_view).setText(String.format("￥%.2f",commodityItem.salePrice));
                TextView originPrice=holder.get(R.id.origin_price);
                if(commodityItem.originalPrice >commodityItem.salePrice){
                    originPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    originPrice.setText(String.format("￥%.2f" ,commodityItem.originalPrice));
                }else {
                    originPrice.setText("");
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(commodityItem.isSaleOut()){
                            CommodityDetailActivity.start(context,commodityItem.productId,true,commodityItem.colorValId);
                        }else {
                            CommodityDetailActivity.start(context,commodityItem.productId,commodityItem.colorValId);
                        }
                    }
                });

                break;
            case AdapterItem.BRAND:
                final BrandItem brandItem=(BrandItem) data;
                ImageView bgImage=holder.get(R.id.bg_view);
                ImageView logo=holder.get(R.id.logo_view);
                Glide.with(context).load(brandItem.logoUrl).into(logo);
                Glide.with(context).load(brandItem.bgUrl).into(bgImage);
                holder.<TextView>get(R.id.name_view).setText(brandItem.name);
                holder.<TextView>get(R.id.desc_view).setText(brandItem.desc);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommodityListActivity.start(context,mAid,brandItem.brandId);
                    }
                });
                break;

            case AdapterItem.UNFOLD:
                final UnfoldItem unfoldItem=(UnfoldItem) data;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(unfoldItem.brandId==-1){
                            DiscountListActivity.start(context,mAid);
                        }else {
                            CommodityListActivity.start(context,mAid,unfoldItem.brandId);
                        }
                    }
                });
                break;
            case AdapterItem.LINE:
                break;
            case AdapterItem.END:
                break;
        }
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
