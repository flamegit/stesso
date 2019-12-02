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

/**
 * Created by flame on 2017/7/24.
 */

public class AwardSettlementAdapter extends BaseAdapter<ShopItem> {

    private static final int FOOT=3;
    private static final int CONTENT=1;
    private SettlementInfo mInfo;

    public void changeSettlementInfo(SettlementInfo info){
        mInfo=info;
        notifyItemChanged(getItemCount()-1);
    }

    public AwardSettlementAdapter(SettlementInfo info){
        mInfo=info;
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
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if(mInfo==null){
            return super.getItemCount();
        }
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mInfo==null){
            return CONTENT;
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
        return R.layout.viewholder_settlement;
    }
}
