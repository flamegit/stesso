package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.SettlementInfo;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/7/24.
 */

public class SettlementAdapter extends BaseAdapter<ShopItem> {
    private static final int FOOT=3;
    private static final int CONTENT=1;
    private static final int COUPON=2;
    private SettlementInfo mInfo;
    private int mAvailableCount;
    private int mUseCount;
    private int mAmount;

    private boolean isSelected;
    protected OnItemClickListener<ShopItem> mCallback;

    public void changeSettlementInfo(SettlementInfo info){
        mInfo=info;
        notifyItemChanged(getItemCount()-1);
    }

    public SettlementAdapter(SettlementInfo info){
        mInfo=info;
        mAvailableCount=0;
        mUseCount=0;
        mAmount=0;
        isSelected=false;

    }
    public void setCallback(OnItemClickListener callback ){
        mCallback=callback;
    }

    public void updateCouponInfo(int available,int use,int amount, boolean select){
        mAvailableCount=available;
        mUseCount=use;
        mAmount=amount;
        isSelected=select;
        notifyItemChanged(getItemCount()-2);
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, final int position, final ShopItem data) {
        final Context context=holder.itemView.getContext();
        holder.<TextView>get(R.id.name_view).setText(data.name.replaceAll("\\\\n",""));
        holder.<TextView>get(R.id.quantity_view).setText("x"+data.quantity);
        holder.<TextView>get(R.id.price_view).setText("￥"+data.price);
        if(data.color!=null){
            try {
                holder.<TextView>get(R.id.color_view).setBackgroundColor(Color.parseColor(data.color));
            }catch (IllegalArgumentException e){
                holder.<TextView>get(R.id.color_view).setText(data.color);
            }
        }
        ImageView iconView=holder.get(R.id.icon_view);
        if(data.isPreSale()){
            holder.get(R.id.pre_sale).setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(data.skuThumbnail)
                .placeholder(new ColorDrawable(Color.BLACK))
                .crossFade()
                .into(iconView);
        if(data.size!=null){
            holder.<TextView>get(R.id.size_view).setText(data.size);
        }
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if(getItemViewType(position)==FOOT){
            holder.<TextView>get(R.id.commodity_price).setText("￥"+mInfo.totalPrice);
            holder.<TextView>get(R.id.discount_view).setText("-￥"+mInfo.discount);
            holder.<TextView>get(R.id.transport_price).setText("￥"+mInfo.transport);

            if(mInfo.showHint){
                holder.<TextView>get(R.id.discount_hint).setVisibility(View.VISIBLE);
            }
        }else if(getItemViewType(position)==COUPON){

            TextView saveView=holder.get(R.id.save_view);
            TextView available=holder.get(R.id.available_count);
            TextView countView=holder.get(R.id.count_view);
            View useLayout=holder.get(R.id.use_layout);
            //CheckBox checkBox=holder.get(R.id.check_box);

            if(isSelected && mUseCount>0){
                useLayout.setVisibility(View.VISIBLE);
                saveView.setVisibility(View.VISIBLE);
                saveView.setText(String.format(Util.getString(R.string.save_amount),mAmount));
                available.setVisibility(View.INVISIBLE);
                countView.setText(String.format(Util.getString(R.string.coupon_use_count),mUseCount));
            }else {
                available.setVisibility(View.VISIBLE);
                saveView.setVisibility(View.INVISIBLE);
                useLayout.setVisibility(View.GONE);
                if(mAvailableCount==0){
                    available.setText(Util.getString(R.string.no_coupon));
                    available.setTextColor(Color.GRAY);
                }else {
                    available.setText(String.format(Util.getString(R.string.coupon_available_count),mAvailableCount));
                    available.setTextColor(Color.BLACK);
                }
            }
            holder.itemView.setOnClickListener(v -> {
                if(mCallback!=null){
                    mCallback.onItemClick(position,null);
                }
            });

        }
        else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if(mInfo==null){
            return super.getItemCount();
        }
        return super.getItemCount()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if(mInfo==null){
            return CONTENT;
        }
        if(position+2==getItemCount()){
            return COUPON;
        }
        if(position+1==getItemCount()){
            return FOOT;
        }
        return CONTENT;
    }

    @Override
    protected int getLayoutId(int viewType) {
        if(viewType==FOOT){
            return R.layout.viewholder_order_price;
        }
        if(viewType== COUPON){
            return R.layout.viewholder_coupon_settlement;
        }
      return R.layout.viewholder_settlement;
    }
}
