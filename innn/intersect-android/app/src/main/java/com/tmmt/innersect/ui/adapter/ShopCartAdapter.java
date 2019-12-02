package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.widget.QuantityView;

import java.util.List;

/**
 * Created by flame on 2017/7/24.
 */

public class ShopCartAdapter extends BaseAdapter<ShopItem> {
    private Report mReport;
    boolean isEditMode;
    public void setContent(List<ShopItem> itemList){
        mContent=itemList;
    }

    public ShopCartAdapter(boolean isEdit){
        isEditMode=isEdit;
    }

    public void setReport(Report report){
        mReport=report;
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, final int position, final ShopItem data) {
        final CheckBox selectView=holder.get(R.id.check_box);
        final Context context=holder.itemView.getContext();
        QuantityView quantityView=holder.get(R.id.quantity_view);
        quantityView.setQuantity(data.quantity);
        quantityView.setMaxQuantity(data.getLimit());
        holder.<TextView>get(R.id.name_view).setText(data.name.replaceAll("\\\\n",""));
        if(isEditMode){
            selectView.setChecked(data.delete);
            selectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isChecked=selectView.isChecked();
                    ShopCart.getInstance().setDelete(position,isChecked);
                    if(mReport!=null){
                        mReport.checkedChange(isChecked);
                    }
                }
            });
            quantityView.setQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {

                @Override
                public void onQuantityChanged(int newQuantity, boolean programmatically) {
                    //int old=data.getStatus();
                    ShopCart.getInstance().updateQuantity(position,newQuantity);
                    if(mReport!=null){
                        mReport.sizeChange(position,newQuantity);
                    }
                }
                @Override
                public void onLimitReached() {
                    Toast.makeText(context,"不能再多咯",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onMinReached() {}
            });

        }else {
            selectView.setChecked(data.payment);
            selectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isChecked=selectView.isChecked();
                    ShopCart.getInstance().setSelected(position,isChecked);
                    if(mReport!=null){
                        mReport.checkedChange(isChecked);
                    }
                }
            });
            holder.get(R.id.delete_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mReport!=null){
                        mReport.itemDeleted(position,false);
                    }
                }
            });
        }

        if(data.isPreSale()){
            holder.get(R.id.pre_sale).setVisibility(data.isPreSale()?View.VISIBLE:View.INVISIBLE);
        }

        holder.<TextView>get(R.id.color_view).setText(data.color);
        TextView statusView=holder.get(R.id.sale_status);

        quantityView.setQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                int old=data.getStatus();

                ShopCart.getInstance().updateQuantity(position,newQuantity);

                if(old!=data.getStatus()){
                    notifyItemChanged(position);
                }
                //ShopCart.getInstance().updateQuantity(position,newQuantity);
                if(newQuantity==data.pavailInv+1 && newQuantity<= data.availInv){
                    SystemUtil.toast("超过门店可提货数量，将邮寄给您");
                }
                if(mReport!=null){
                    mReport.sizeChange(position,newQuantity);
                }
            }
            @Override
            public void onLimitReached() {
                Toast.makeText(context,"不能再多咯",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onMinReached() {
                if(mReport!=null){
                    mReport.itemDeleted(position,true);
                }
            }
        });

        int status=data.getStatus();
        switch (status){
            case 1:
            case 2:
                statusView.setBackgroundResource(R.drawable.gradient_cart_gray);
                statusView.setVisibility(View.VISIBLE);
                selectView.setEnabled(false);
                statusView.setText(status==1?"已下架":"已售罄");
                quantityView.setVisibility(View.GONE);
                break;
            case 3:
            case 4:
                statusView.setBackgroundResource(R.drawable.gradient_cart_black);
                statusView.setVisibility(View.VISIBLE);
                selectView.setEnabled(true);
                statusView.setText(status==3?"可自提":"仅支持自提");
                quantityView.setVisibility(View.VISIBLE);
                break;
            default:
                statusView.setVisibility(View.INVISIBLE);
                selectView.setEnabled(true);
                quantityView.setVisibility(View.VISIBLE);

        }
        holder.<TextView>get(R.id.price_view).setText("￥"+data.price);
        ImageView iconView=holder.get(R.id.icon_view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.isNotSale()){
                    SystemUtil.toast("商品已下架");
                    return;
                }
                CommodityDetailActivity.start(context,data.productId,data.shop);
            }
        });
        Glide.with(context).load(data.skuThumbnail)
                .into(iconView);
        if(data.size!=null){
            holder.<TextView>get(R.id.size_view).setText(data.size);
        }
    }

//    private void updateStatus(int status, View selectView,TextView statusView,View quantityView){
//        switch (status){
//            case 1:
//            case 2:
//                statusView.setBackgroundResource(R.drawable.gradient_cart_gray);
//                statusView.setVisibility(View.VISIBLE);
//                selectView.setEnabled(false);
//                statusView.setText(status==1?"已下架":"已售罄");
//                quantityView.setEnabled(false);
//                break;
//            case 3:
//            case 4:
//                statusView.setBackgroundResource(R.drawable.gradient_cart_black);
//                statusView.setVisibility(View.VISIBLE);
//                selectView.setEnabled(true);
//                statusView.setText(status==3?"可自提":"仅支持自提");
//                quantityView.setEnabled(true);
//                break;
//            default:
//                statusView.setVisibility(View.INVISIBLE);
//                selectView.setEnabled(true);
//                quantityView.setEnabled(true);
//        }
//
//    }

    @Override
    protected int getLayoutId(int viewType) {
        if(isEditMode){
            return R.layout.viewholder_shop_cart;
        }
        return R.layout.viewholder_swipe_cart;
    }
}
